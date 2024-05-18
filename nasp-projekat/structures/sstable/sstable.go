package sstable

import (
	"encoding/binary"
	"fmt"
	"io"
	bloomFilter "nasp-projekat/structures/BloomFilter"
	log "nasp-projekat/structures/WAL"
	config "nasp-projekat/structures/configurator"
	"os"
	"path/filepath"
	"sort"
	"strconv"
	"strings"
)

const (
	DATA_PATH   = "structures/sstable/sstable-"
	FILE_PREFIX = "SSTable_"
)

type SSTable struct {
	Data    []Segment
	Filter  *bloomFilter.BloomFilter
	Index   []Index
	Summary Summary
	//Metadata *merkle.MerkleTree
}

// Inicijalizacija SSTable-a
func NewSStable(logs []log.Log, current_level uint64) *SSTable {
	var configurator config.Configuration
	config.ReadConfiguration(&configurator)
	segment := Convert(logs)
	//Makes data
	data := make([]Segment, 1, configurator.SegmentNumber)
	data[0] = segment
	//Makes filter using bloomFilter
	filter := bloomFilter.NewBloomFilter(len(logs), configurator.BloomPrecision)
	for _, record := range logs {
		filter.Add(record.Key)
	}
	var indexes []Index
	var summary Summary
	path := FindOrCreateEmptySSTableFolder() + FILE_PREFIX + strconv.FormatUint(current_level, 10) + "_"
	if configurator.FileStructure == "Multiple" {
		//Makes index
		indexes = BuildIndex(data, 0, 0)
		//Makes summary
		summary = CreateSummaryTable(indexes, int(configurator.SampleRate))
		//Adding data to separate data file
		SerializeDataToFile(data, path+"Data.db")
		//Adding filter to separate filter file
		filter.Encode(path + "Filter.db")
		//Adding indexes to separate index file
		err1 := WriteIndexesToFile(indexes, path+"Index.db")
		if err1 != nil {
			panic(err1)
		}
		//Adding summary to separate summary file
		err2 := SerializeSummaryToFile(summary, path+"Summary.db")
		if err2 != nil {
			panic(err2)
		}
	} else {
		filter1 := *filter
		serializedFilter, _ := bloomFilter.SerializeBloomFilter(*filter)

		lengthOfFilter := len(serializedFilter)      //num of bytes serialized filter takes in file
		lengthOfData := lengthOfSerializedData(data) //num of bytes serialized data takes in file
		// index
		indexes = BuildIndex(data, uint64(32+lengthOfFilter+int(lengthOfData)), uint64(32+lengthOfFilter)) //32 - length of header+filter_length(+data_length)
		//Makes summary
		summary = CreateSummaryTable(indexes, int(configurator.SampleRate))
		SerializeToSingleFile(path+"Single.db", filter1, indexes, summary, data)
	}

	return &SSTable{
		Data:    data,
		Filter:  filter,
		Index:   indexes,
		Summary: summary,
		//Metadata: metadata,
	}
}

func Convert(Logs []log.Log) Segment {
	var keyValues = make([]KeyValue, len(Logs))
	var valueStruct ValueStruct
	for i, log_data := range Logs {
		valueStruct.Tombstone = log_data.Tombstone
		valueStruct.Timestamp = log_data.TimeStamp
		valueStruct.Value = log_data.Value
		keyValues[i] = KeyValue{Key: log_data.Key, Value: valueStruct}
	}

	return Segment{KeyValues: keyValues}
}

// SINGLE FILE SER/DESER================================================================================================

func SerializeToSingleFile(fileName string, filter bloomFilter.BloomFilter, indexes []Index, summary Summary, data []Segment) {
	//serialize Index, serialize keyValue
	header := make([]uint64, 4)
	currentOffset := len(header) * 8
	//Serializing filter
	serializedFilter, errBloom := bloomFilter.SerializeBloomFilter(filter)
	if errBloom != nil {
		panic(errBloom)
	}
	bloomOffset := currentOffset
	header[0] = uint64(bloomOffset) // Set the offset of the filter
	currentOffset += len(serializedFilter)
	//Serialize Data
	serializedData := make([]byte, 0)
	for _, segment := range data {
		segmentSize := uint64(len(segment.KeyValues))

		buf := make([]byte, 8)
		binary.BigEndian.PutUint64(buf, segmentSize)

		serializedData = append(serializedData, buf...)

		for _, keyValue := range segment.KeyValues {
			serializedKeyValue, errData := SerializeKeyValue(keyValue)
			if errData != nil {
				panic(errData)
			}
			serializedData = append(serializedData, serializedKeyValue...)
		}
	}
	dataOffset := currentOffset
	header[1] = uint64(dataOffset) // Set the offset of the data
	currentOffset += len(serializedData)
	//Serialize index
	serializedIndexes := make([]byte, 0)
	for _, index := range indexes {
		serializedIndex := index.serializeIndex() // Serialize the index

		// Write the serialized index to the file
		serializedIndexes = append(serializedIndexes, serializedIndex...)
	}
	indexesOffset := currentOffset
	header[2] = uint64(indexesOffset) // Set the offset of the indexes
	currentOffset += len(serializedIndexes)
	//Serialize Summary
	serializedSummary, errSummary := SerializeSummary(summary)
	if errSummary != nil {
		panic(errSummary)
	}
	summaryOffset := currentOffset
	header[3] = uint64(summaryOffset) // Set the offset of the summary

	file, err := os.Create(fileName)
	if err != nil {
		panic(err)
	}
	defer file.Close()

	// Write the header to the file
	headerBytes := make([]byte, 8*len(header))
	for i, value := range header {
		binary.BigEndian.PutUint64(headerBytes[i*8:], value)
	}
	_, err = file.Write(headerBytes)
	if err != nil {
		panic(err)
	}

	// Write the serializedFilter to the file
	_, err = file.Write(serializedFilter)
	if err != nil {
		panic(err)
	}

	// Write the serializedData to the file
	_, err = file.Write(serializedData)
	if err != nil {
		panic(err)
	}

	// Write the serializedIndexes to the file
	_, err = file.Write(serializedIndexes)
	if err != nil {
		panic(err)
	}

	// Write the serializedSummary to the file
	_, err = file.Write(serializedSummary)
	if err != nil {
		panic(err)
	}
}

func DeserializeFromSingleFile(fileName string) (SSTable, error) {
	file, err := os.Open(fileName)
	if err != nil {
		return SSTable{}, err
	}
	defer file.Close()

	fileStat, err := file.Stat()
	if err != nil {
		return SSTable{}, err
	}

	// Read the header from the file
	header := make([]uint64, 4)
	headerBytes := make([]byte, 8*len(header))
	if _, err := file.Read(headerBytes); err != nil {
		return SSTable{}, err
	}
	for i := range header {
		header[i] = binary.BigEndian.Uint64(headerBytes[i*8 : (i+1)*8])
	}

	// Deserialize the Bloom filter
	file.Seek(int64(header[0]), 0)
	filterBytes := make([]byte, header[1]-header[0])
	if _, err := file.Read(filterBytes); err != nil {
		return SSTable{}, err
	}
	filter, err := bloomFilter.DeserializeBloomFilter(filterBytes)
	if err != nil {
		return SSTable{}, err
	}
	// Deserialize the data
	file.Seek(int64(header[1]), 0)
	dataBytes := make([]byte, header[2]-header[1])
	if _, err := file.Read(dataBytes); err != nil {
		return SSTable{}, err
	}
	data, err := DeserializeData(dataBytes)
	if err != nil {
		return SSTable{}, err
	}
	// Deserialize the indexes
	file.Seek(int64(header[2]), 0)
	indexesBytes := make([]byte, header[3]-header[2])
	if _, err := file.Read(indexesBytes); err != nil {
		return SSTable{}, err
	}

	indexes, err := DeserializeIndexes(indexesBytes)
	if err != nil {
		return SSTable{}, err
	}

	// Deserialize the summary
	file.Seek(int64(header[3]), 0)
	summaryBytes := make([]byte, fileStat.Size()-int64(header[3]))
	if _, err := file.Read(summaryBytes); err != nil {
		return SSTable{}, err
	}
	summary, err := DeserializeSummary(summaryBytes)
	if err != nil {
		return SSTable{}, err
	}

	sstable := SSTable{
		Data:    data,
		Filter:  &filter,
		Index:   indexes,
		Summary: summary,
		//Metadata: metadata,
	}

	return sstable, nil
}

func GetHeaderFromSingleFile(filePath string) []uint64 {
	file, err := os.Open(filePath)
	if err != nil {
		return nil
	}
	defer file.Close()

	// Read the header from the file
	header := make([]uint64, 4)
	headerBytes := make([]byte, 8*len(header))
	if _, err := file.Read(headerBytes); err != nil {
		return nil
	}
	for i := range header {
		header[i] = binary.BigEndian.Uint64(headerBytes[i*8 : (i+1)*8])
	}

	return header
}

// ===========================================================================================================================
func ReadRecordWithKey(key string) (KeyValue, error) {
	baseDir := "data"
	var configuration config.Configuration
	config.ReadConfiguration(&configuration)

	// Check if base Directory exists
	if _, err := os.Stat(baseDir); os.IsNotExist(err) {
		return KeyValue{}, fmt.Errorf("base directory %s does not exist", baseDir)
	}

	// Manually iterate through subdirectories of base Directory
	return readRecordInDir(baseDir, key, &configuration, true)
}

// HELPER FUNCTION FOR READ WITH KEY ============================================================
func readRecordInDir(dirPath string, key string, configuration *config.Configuration, isRootDir bool) (KeyValue, error) {
	entries, err := os.ReadDir(dirPath)
	if err != nil {
		return KeyValue{}, err
	}

	for _, entry := range entries {
		path := filepath.Join(dirPath, entry.Name())

		if entry.IsDir() && isRootDir {
			// If it's the root directory and entry is a directory, process the subdirectory
			kv, err := readRecordInDir(path, key, configuration, false)
			if err == nil && kv.Key != "" {
				return kv, nil // Return early if a record is found
			}
		} else if !entry.IsDir() {
			// Process only files in subdirectories

			if strings.HasSuffix(entry.Name(), "Filter.db") {
				filter := bloomFilter.GetBloomFilterFromFile(path)
				if filter.Contains(key) {
					summaryPath := strings.Replace(path, "Filter.db", "Summary.db", 1)
					indexPath := strings.Replace(path, "Filter.db", "Index.db", 1)
					dataPath := strings.Replace(path, "Filter.db", "Data.db", 1)
					//Find which batch index belongs to
					startingIndex := FindBatchForKey(summaryPath, key)

					indexFile, errIndex := os.Open(indexPath)
					if errIndex != nil {
						panic(errIndex)
					}
					defer indexFile.Close()
					index := FindIndex(indexFile, key, startingIndex, int(configuration.SampleRate))
					if index == nil {
						return KeyValue{}, fmt.Errorf("KEY DOES NOT EXIST INSIDE ANY INDEXES")
					}
					final, errData := FindKeyInData(dataPath, int64(index.LogOffset))
					if final.Key == "" {
						continue
					}
					if errData != nil {
						return KeyValue{}, errData
					}
					return final, nil
				}
			}
			if strings.HasSuffix(entry.Name(), "Single.db") {
				filter := bloomFilter.GetBloomFilterFromFile(path)
				if filter.Contains(key) {
					header := GetHeaderFromSingleFile(path)
					startingIndex := FindBatchForKeySingleFile(path, key, header[3]) // header[3] - summary offset

					singleFile, openingErr := os.Open(path)
					if openingErr != nil {
						return KeyValue{}, openingErr
					}
					defer singleFile.Close()
					index := FindIndex(singleFile, key, startingIndex, int(configuration.SampleRate))
					final, dataSearchErr := FindKeyInData(path, int64(index.LogOffset))
					if dataSearchErr != nil {
						return KeyValue{}, dataSearchErr
					}
					return final, nil
				}
			}
		}
	}

	// If no record is found, return an empty KeyValue and nil error
	return KeyValue{}, nil
}

func FindKeyInData(filePath string, offset int64) (KeyValue, error) {
	var kv KeyValue

	file, err := os.Open(filePath)
	if err != nil {
		return kv, fmt.Errorf("error opening file: %v", err)
	}
	defer file.Close()

	_, err = file.Seek(offset, 0)
	if err != nil {
		return kv, fmt.Errorf("error seeking to offset %d in file: %v", offset, err)
	}

	// Read the length of the key
	var keySize uint64
	if err := binary.Read(file, binary.BigEndian, &keySize); err != nil {
		return kv, err
	}

	// Read the key based on its length
	keyBytes := make([]byte, keySize)
	_, err = file.Read(keyBytes)
	if err != nil {
		return kv, fmt.Errorf("error reading key: %v", err)
	}
	kv.Key = string(keyBytes)

	// Read the ValueStruct (Tombstone, Timestamp, and Value)
	err = binary.Read(file, binary.BigEndian, &kv.Value.Tombstone)
	if err != nil {
		return kv, fmt.Errorf("error reading tombstone: %v", err)
	}
	err = binary.Read(file, binary.BigEndian, &kv.Value.Timestamp)
	if err != nil {
		return kv, fmt.Errorf("error reading timestamp: %v", err)
	}
	var valueLen uint64
	err = binary.Read(file, binary.BigEndian, &valueLen)
	if err != nil {
		return kv, fmt.Errorf("error reading value length: %v", err)
	}
	kv.Value.Value = make([]byte, valueLen)
	_, err = file.Read(kv.Value.Value)
	if err != nil {
		return kv, fmt.Errorf("error reading value: %v", err)
	}

	return kv, nil
}

// ==========================================================================================================================
// Adds a new DataBlock to the SSTable, updating relevant structures.
func AddSegmentDevelopment(logs []log.Log, pathToFolder string) bool {
	var configurator config.Configuration
	config.ReadConfiguration(&configurator)
	if configurator.FileStructure == "Single" {
		//Prolazi kroz svaki sstable direktorijum i proverava da li je single, ako jeste proverava da li je "popunjen"
		//ako je popunjen, proverava dalje sstable-e
		sstable, _ := DeserializeFromSingleFile(pathToFolder + "Single.db")

		if len(sstable.Data) < int(configurator.SegmentNumber) {
			//Data segment update
			segment := Convert(logs)
			data := sstable.Data
			data = append(data, segment)
			sstable.Data = data

			// filter update
			lengthOfData := sstable.lengthOfData()
			filter := bloomFilter.NewBloomFilter(lengthOfData, configurator.BloomPrecision)

			for _, segment := range sstable.Data {
				for _, keyValue := range segment.KeyValues {
					filter.Add(keyValue.Key)
				}
			}
			sstable.Filter = filter
			header := GetHeaderFromSingleFile(pathToFolder + "Single.db")

			dataSlice := make([]KeyValue, lengthOfData)
			counter := 0
			for _, segment := range data {
				for j, keyValue := range segment.KeyValues {
					dataSlice[counter+j] = keyValue
				}
				counter += len(segment.KeyValues)
			}

			sort.Slice(dataSlice, func(i, j int) bool {
				return dataSlice[i].Key < dataSlice[j].Key
			})

			sortedData := make([]Segment, len(data))
			sliceIndex := 0
			for i, segment := range data {
				sortedData[i].KeyValues = make([]KeyValue, len(segment.KeyValues))
				for j := range segment.KeyValues {
					sortedData[i].KeyValues[j] = dataSlice[sliceIndex]
					sliceIndex++
				}
			}

			serializedData, _ := SerializeData(sortedData)
			serializedBloom, _ := bloomFilter.SerializeBloomFilter(*filter)
			// index update
			//header[0]+len(serializedBloom)+len(serializedData) -> new indexOffset - header[0] + new serialized bloom size + new serialized data
			//header[0]+len(serializedBloom) -> new dataOffset - header[0] + new serialized bloom size
			indexes := BuildIndex(sortedData, uint64(int(header[0])+len(serializedBloom)+len(serializedData)), uint64(int(header[0])+len(serializedBloom)))
			sstable.Index = indexes

			// summary update
			summary := CreateSummaryTable(indexes, int(configurator.SampleRate))
			sstable.Summary = summary

			//write changed sstable to disk
			SerializeToSingleFile(pathToFolder+"Single.db", *sstable.Filter, sstable.Index, sstable.Summary, sortedData)
			return true
		}
	}
	if configurator.FileStructure == "Multiple" {
		data, _ := DeserializeDataFromFile(pathToFolder + "Data.db")

		if len(data) < int(configurator.SegmentNumber) {
			//Data segment update
			segment := Convert(logs)
			data = append(data, segment)
			lengthOfData := 0
			// calculate len of data
			for _, segment := range data {
				lengthOfData += len(segment.KeyValues)
			}

			dataSlice := make([]KeyValue, lengthOfData)
			counter := 0
			for _, segment := range data {
				for j, keyValue := range segment.KeyValues {
					dataSlice[counter+j] = keyValue
				}
				counter += len(segment.KeyValues)
			}

			sort.Slice(dataSlice, func(i, j int) bool {
				return dataSlice[i].Key < dataSlice[j].Key
			})

			sortedData := make([]Segment, len(data))
			sliceIndex := 0
			for i, segment := range data {
				sortedData[i].KeyValues = make([]KeyValue, len(segment.KeyValues))
				for j := range segment.KeyValues {
					sortedData[i].KeyValues[j] = dataSlice[sliceIndex]
					sliceIndex++
				}
			}
			SerializeDataToFile(sortedData, pathToFolder+"Data.db")
			//Filter update
			filter := bloomFilter.NewBloomFilter(lengthOfData, configurator.BloomPrecision)
			for _, segment := range data {
				for _, keyValue := range segment.KeyValues {
					filter.Add(keyValue.Key)
				}

			}
			// s.Filter = filter
			filter.Encode(pathToFolder + "Filter.db")
			//Index update
			indexes := BuildIndex(sortedData, 0, 0)
			WriteIndexesToFile(indexes, pathToFolder+"Index.db")
			//Summary update
			summary := CreateSummaryTable(indexes, int(configurator.SampleRate))
			// s.Summary = summary
			SerializeSummaryToFile(summary, pathToFolder+"Summary.db")
			return true
		}
	}

	return false
}

func DeleteElement(key, pathToFolder string) bool {
	var configurator config.Configuration
	config.ReadConfiguration(&configurator)
	if configurator.FileStructure == "Single" {
		sstable, _ := DeserializeFromSingleFile(pathToFolder + "Single.db")
		header := GetHeaderFromSingleFile(pathToFolder + "Single.db")
		if sstable.Filter.Contains(key) {
			startingIndex := FindBatchForKeySingleFile(pathToFolder+"Single.db", key, header[3])

			singleFile, _ := os.OpenFile(pathToFolder+"Single.db", os.O_RDWR, 0666)
			defer singleFile.Close()

			index := FindIndex(singleFile, key, startingIndex, int(configurator.SampleRate))

			singleFile.Seek(int64(index.LogOffset), 0)
			var keySize uint64
			binary.Read(singleFile, binary.BigEndian, &keySize)
			singleFile.Seek(int64(keySize), io.SeekCurrent)
			binary.Write(singleFile, binary.BigEndian, byte(1))
		} else {
			return false
		}
		return true
	}
	if configurator.FileStructure == "Multiple" {
		filter := bloomFilter.GetBloomFilterFromFile(pathToFolder + "Filter.db")
		if filter.Contains(key) {
			startingIndex := FindBatchForKey(pathToFolder+"Summary.db", key)
			indexFile, indexFileErr := os.Open(pathToFolder + "Index.db")
			if indexFileErr != nil {
				return false
			}
			defer indexFile.Close()
			index := FindIndex(indexFile, key, startingIndex, configurator.SampleRate)

			dataFile, dataFileErr := os.OpenFile(pathToFolder+"Data.db", os.O_RDWR, 0666)
			if dataFileErr != nil {
				return false
			}
			defer dataFile.Close()
			dataFile.Seek(int64(index.LogOffset), 0)
			var keySize uint64
			binary.Read(dataFile, binary.BigEndian, &keySize)
			dataFile.Seek(int64(keySize), io.SeekCurrent)
			binary.Write(dataFile, binary.BigEndian, byte(1))
			return true
		}
	}

	return false
}

// Function deletes SSTable from Disk
func Delete(pathToFolder string, configurator config.Configuration) {
	os.Remove(pathToFolder)
}

// MERKLE TREE ----------------------------------------------------------------

// BuildMerkleTree constructs the Merkle Tree for the SSTable to ensure data integrity.
// func (s *SSTable) BuildMerkleTree() {
// 	// Convert each DataBlock to a byte slice
// 	var dataBlocks [][]byte
// 	for _, block := range s.Data {
// 		dataBlockBytes, err := serializeDataBlock(block)
// 		if err != nil {
// 			fmt.Println("Error serializing DataBlock:")
// 			continue
// 		}
// 		dataBlocks = append(dataBlocks, dataBlockBytes)
// 	}

// 	// Use the byte slices to build the Merkle Tree
// 	s.Metadata = merkle.NewMerkleTree(dataBlocks)
// }

// serializeDataBlock serializes a DataBlock into a byte slice using Gob encoding.
// func serializeDataBlock(segment Segment) ([]byte, error) {
// 	var buffer bytes.Buffer
// 	encoder := gob.NewEncoder(&buffer)
// 	err := encoder.Encode(segment)
// 	if err != nil {
// 		return nil, err
// 	}
// 	return buffer.Bytes(), nil
// }

// MERKLE TREE ----------------------------------------------------------------

func lengthOfSerializedData(data []Segment) int64 {
	serializedData := make([]byte, 0)
	for _, segment := range data {
		segmentSize := uint64(len(segment.KeyValues))

		buf := make([]byte, 8)
		binary.BigEndian.PutUint64(buf, segmentSize)

		serializedData = append(serializedData, buf...)

		for _, keyValue := range segment.KeyValues {
			serializedKeyValue, errData := SerializeKeyValue(keyValue)
			if errData != nil {
				panic(errData)
			}
			serializedData = append(serializedData, serializedKeyValue...)
		}
	}
	return int64(len(serializedData))
}
