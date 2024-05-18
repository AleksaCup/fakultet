package sstable

import (
	"bytes"
	"encoding/binary"
	"io"
	"os"
)

type Summary struct {
	Indexes  []Index
	StartKey string
	EndKey   string
}

// Create a new summary from the given indexes.
func CreateSummaryTable(indexes []Index, sampleRate int) Summary {
	summary := Summary{
		StartKey: indexes[0].Key,
		EndKey:   indexes[len(indexes)-1].Key,
	}

	// Sample entries based on the sampleRate
	for i, index := range indexes {
		if i%sampleRate == 0 {
			summary.Indexes = append(summary.Indexes, index)
		}
	}

	return summary
}

func SerializeSummaryToFile(summary Summary, filename string) error {
	file, err := os.OpenFile(filename, os.O_WRONLY|os.O_CREATE|os.O_TRUNC, 0666)
	if err != nil {
		return err
	}
	defer file.Close()

	// Write StartKey
	if err := writeString(file, summary.StartKey); err != nil {
		return err
	}

	// Write EndKey
	if err := writeString(file, summary.EndKey); err != nil {
		return err
	}

	// Write number of indexes
	if err := binary.Write(file, binary.BigEndian, uint64(len(summary.Indexes))); err != nil {
		return err
	}

	// Write each index
	for _, index := range summary.Indexes {
		if err := writeIndex(file, index); err != nil {
			return err
		}
	}

	return nil
}

// =========================================HELPER FUNCTIONS FOR SERIALIZATION=========================================
func writeString(file *os.File, str string) error {
	// Write string length
	if err := binary.Write(file, binary.BigEndian, uint64(len(str))); err != nil {
		return err
	}

	// Write string bytes
	_, err := file.Write([]byte(str))
	return err
}

func writeIndex(file *os.File, index Index) error {
	// Write Key
	if err := writeString(file, index.Key); err != nil {
		return err
	}
	//Write Offset
	binary.Write(file, binary.BigEndian, index.Offset)

	// Write LogOffset
	return binary.Write(file, binary.BigEndian, index.LogOffset)
}

// ====================================================================================================================

func DeserializeSummaryFromFile(filename string) (Summary, error) {
	file, err := os.Open(filename)
	if err != nil {
		return Summary{}, err
	}
	defer file.Close()

	var summary Summary

	// Read StartKey
	summary.StartKey, err = readString(file)
	if err != nil {
		return Summary{}, err
	}

	// Read EndKey
	summary.EndKey, err = readString(file)
	if err != nil {
		return Summary{}, err
	}

	// Read number of indexes
	var numIndexes uint64
	if err := binary.Read(file, binary.BigEndian, &numIndexes); err != nil {
		return Summary{}, err
	}

	// Read each index
	for i := uint64(0); i < numIndexes; i++ {
		index, err := readIndex(file)
		if err != nil {
			return Summary{}, err
		}
		summary.Indexes = append(summary.Indexes, index)
	}

	return summary, nil
}

// ========================================HELPER FUNCTIONS FOR DESERIALIZATION========================================
func readString(file *os.File) (string, error) {
	var length uint64
	if err := binary.Read(file, binary.BigEndian, &length); err != nil {
		return "", err
	}

	bytes := make([]byte, length)
	if _, err := io.ReadFull(file, bytes); err != nil {
		return "", err
	}

	return string(bytes), nil
}

func readIndex(file *os.File) (Index, error) {
	var index Index

	// Read Key
	key, err := readString(file)
	if err != nil {
		return Index{}, err
	}
	index.Key = key

	// Read Offset
	if err := binary.Read(file, binary.BigEndian, &index.Offset); err != nil {
		return Index{}, err
	}
	//Read LogOffset
	if err := binary.Read(file, binary.BigEndian, &index.LogOffset); err != nil {
		return Index{}, err
	}

	return index, nil
}

// ====================================================================================================================

func SerializeSummary(s Summary) ([]byte, error) {
	var buffer bytes.Buffer

	// Serialize the number of indexes
	indexCount := uint64(len(s.Indexes))
	if err := binary.Write(&buffer, binary.BigEndian, indexCount); err != nil {
		return nil, err
	}

	// Serialize each index
	for _, index := range s.Indexes {
		// Serialize the Index structure using the serializeIndex method
		serializedIndex := index.serializeIndex()

		// Serialize the length of the serializedIndex
		serializedIndexSize := uint64(len(serializedIndex))
		if err := binary.Write(&buffer, binary.BigEndian, serializedIndexSize); err != nil {
			return nil, err
		}

		// Serialize the serializedIndex
		if _, err := buffer.Write(serializedIndex); err != nil {
			return nil, err
		}
	}

	// Serialize the StartKey string
	if err := binary.Write(&buffer, binary.BigEndian, uint64(len(s.StartKey))); err != nil {
		return nil, err
	}
	if err := binary.Write(&buffer, binary.BigEndian, []byte(s.StartKey)); err != nil {
		return nil, err
	}

	// Serialize the EndKey string
	if err := binary.Write(&buffer, binary.BigEndian, uint64(len(s.EndKey))); err != nil {
		return nil, err
	}
	if err := binary.Write(&buffer, binary.BigEndian, []byte(s.EndKey)); err != nil {
		return nil, err
	}

	// Return the serialized data as a byte slice
	return buffer.Bytes(), nil
}

func DeserializeSummary(data []byte) (Summary, error) {
	var s Summary
	buffer := bytes.NewReader(data)

	// Deserialize the number of indexes
	var indexCount uint64
	if err := binary.Read(buffer, binary.BigEndian, &indexCount); err != nil {
		return s, err
	}

	// Deserialize each index
	for i := uint64(0); i < indexCount; i++ {
		// Deserialize the length of the serializedIndex
		var serializedIndexSize uint64
		if err := binary.Read(buffer, binary.BigEndian, &serializedIndexSize); err != nil {
			return s, err
		}

		// Read the serializedIndex bytes
		serializedIndex := make([]byte, serializedIndexSize)
		if _, err := buffer.Read(serializedIndex); err != nil {
			return s, err
		}

		// Deserialize the Index structure from the serializedIndex
		index, err := DeserializeIndex(serializedIndex)
		if err != nil {
			return s, err
		}

		s.Indexes = append(s.Indexes, index)
	}

	// Deserialize the StartKey string
	var startKeyLen uint64
	if err := binary.Read(buffer, binary.BigEndian, &startKeyLen); err != nil {
		return s, err
	}
	startKeyBytes := make([]byte, startKeyLen)
	if err := binary.Read(buffer, binary.BigEndian, startKeyBytes); err != nil {
		return s, err
	}
	s.StartKey = string(startKeyBytes)

	// Deserialize the EndKey string
	var endKeyLen uint64
	if err := binary.Read(buffer, binary.BigEndian, &endKeyLen); err != nil {
		return s, err
	}
	endKeyBytes := make([]byte, endKeyLen)
	if err := binary.Read(buffer, binary.BigEndian, endKeyBytes); err != nil {
		return s, err
	}
	s.EndKey = string(endKeyBytes)

	return s, nil
}

func FindBatchForKey(summaryFile string, searchKey string) Index {
	summary, err := DeserializeSummaryFromFile(summaryFile)
	if err != nil {
		return Index{}
	}

	var lastSeenIndex Index

	// Iterate through the indexes in the summary
	for _, index := range summary.Indexes {
		if searchKey == index.Key {
			return index
		} else if searchKey < index.Key {
			if lastSeenIndex.Key != "" {
				return lastSeenIndex
			}
			break
		}
		lastSeenIndex = index
	}

	if searchKey > lastSeenIndex.Key {
		return lastSeenIndex
	}

	return Index{}
}

func FindBatchForKeySingleFile(singleFile, searchKey string, summaryOffset uint64) Index {
	file, err := os.Open(singleFile)
	if err != nil {
		return Index{}
	}
	defer file.Close()

	_, err = file.Seek(int64(summaryOffset), 0)
	if err != nil {
		return Index{}
	}

	var indexesLength uint64
	err = binary.Read(file, binary.BigEndian, &indexesLength)
	if err != nil {
		return Index{}
	}

	var lastValidIndex Index
	var found bool

	for i := uint64(0); i < indexesLength; i++ {
		var serializedIndexSize uint64
		if err := binary.Read(file, binary.BigEndian, &serializedIndexSize); err != nil {
			return Index{}
		}

		// Read the serializedIndex bytes
		serializedIndex := make([]byte, serializedIndexSize)
		if _, err := file.Read(serializedIndex); err != nil {
			return Index{}
		}

		// Deserialize the Index structure from the serializedIndex
		index, err := DeserializeIndex(serializedIndex)
		if err != nil {
			return Index{}
		}

		// Compare the key with the search key
		if index.Key == searchKey {
			return index
		} else if index.Key > searchKey {
			if found {
				return lastValidIndex
			}
			return Index{}
		}

		// Update last valid index and found flag
		lastValidIndex = index
		found = true
	}

	// If reached the end without finding a greater key, return the last valid index if any valid index was found
	if found {
		return lastValidIndex
	}

	return Index{}
}
