package sstable

import (
	"encoding/binary"
	"io"
	"os"
)

type Index struct {
	LogOffset uint64
	Key       string
	Offset    uint64
}

// BuildIndex constructs the Index for the SSTable based on DataBlock entries.
func BuildIndex(data []Segment, startIndexOffset, startLogOffset uint64) []Index {
	lengthOfData := 0
	for _, segment := range data {
		lengthOfData += len(segment.KeyValues)
	}
	var indexes []Index = make([]Index, lengthOfData)
	var offset uint64 = startIndexOffset
	var logOffset uint64 = startLogOffset
	currentPosition := 0
	for _, segment := range data {
		logOffset += 8
		for i := 0; i < len(segment.KeyValues); i++ {
			index := Index{
				LogOffset: logOffset,
				Key:       segment.KeyValues[i].Key,
				Offset:    offset,
			}
			indexes[currentPosition+i] = index
			serializesKeyValue, err := SerializeKeyValue(segment.KeyValues[i])
			if err != nil {
				return nil
			}
			logOffset += uint64(len(serializesKeyValue))
			offset += uint64(len(index.serializeIndex()))
		}
		currentPosition += len(segment.KeyValues)
	}

	return indexes
}

func (index Index) serializeIndex() []byte {
	keyBytes := []byte(index.Key)
	keySize := uint64(len(keyBytes)) // Ensure KeySize is updated

	// 10 pingvin 140

	// Create byte slices for KeySize and Offset
	keySizeBytes := make([]byte, 8) // KeySize is uint64, which is 8 bytes
	offsetBytes := make([]byte, 8)  // Offset is uint64, which is 8 bytes

	logOffsetBytes := make([]byte, 8)

	// Convert KeySize and Offset to byte slices using BigEndian
	binary.BigEndian.PutUint64(keySizeBytes, keySize)
	binary.BigEndian.PutUint64(offsetBytes, index.Offset)
	binary.BigEndian.PutUint64(logOffsetBytes, index.LogOffset)

	// Concatenate KeySize, Key, and Offset bytes to form the serialized index
	serializedIndex := append(keySizeBytes, keyBytes...)
	serializedIndex = append(serializedIndex, offsetBytes...)
	serializedIndex = append(serializedIndex, logOffsetBytes...)

	return serializedIndex
}

func DeserializeIndex(data []byte) (Index, error) {
	var index Index

	// Read KeySize from the byte slice
	keySize := binary.BigEndian.Uint64(data[:8])
	data = data[8:]

	// Read Key from the byte slice based on KeySize
	keyBytes := data[:keySize]
	data = data[keySize:]

	// Read Offset from the byte slice
	offset := binary.BigEndian.Uint64(data[:8])
	data = data[8:]

	// Read LogOffset from the byte slice
	logOffset := binary.BigEndian.Uint64(data[:8])

	// Populate the Index structure
	index.LogOffset = logOffset
	index.Offset = offset
	index.Key = string(keyBytes)

	return index, nil
}

func DeserializeIndexes(data []byte) ([]Index, error) {
	var indexes []Index

	for len(data) > 0 {
		// Deserialize a single Index from the current position in data
		index, err := DeserializeIndex(data)
		if err != nil {
			return nil, err // Return error if deserialization fails
		}
		indexes = append(indexes, index)

		// Calculate the total size of the current Index to find the start of the next Index
		currentIndexSize := 8 + uint64(len(index.Key)) + 8 + 8 // KeySize (8 bytes) + Key length + Offset (8 bytes) + LogOffset (8 bytes)

		// Check if there's enough data left for another Index; if not, break the loop
		if currentIndexSize >= uint64(len(data)) {
			break
		}
		data = data[currentIndexSize:] // Move to the start of the next Index
	}

	return indexes, nil
}

func WriteIndexesToFile(indexes []Index, filePath string) error {
	// Open the file for writing, create it if it does not exist, and truncate it if it does
	file, err := os.OpenFile(filePath, os.O_WRONLY|os.O_CREATE|os.O_TRUNC, 0644)
	if err != nil {
		return err // Return the error if file opening fails
	}
	defer file.Close()

	// Iterate through each index and write its serialized form to the file
	for _, index := range indexes {
		serializedIndex := index.serializeIndex() // Serialize the index

		// Write the serialized index to the file
		_, err := file.Write(serializedIndex)
		if err != nil {
			return err // Return the error if writing fails
		}
	}

	return nil // Return nil on success
}

func ReadIndexesFromFile(filePath string) ([]Index, error) {
	file, err := os.Open(filePath)
	if err != nil {
		return nil, err
	}
	defer file.Close()

	var indexes []Index

	// Iterate through the file until EOF
	for {
		// Read the KeySize
		var keySize uint64
		err := binary.Read(file, binary.BigEndian, &keySize)
		if err == io.EOF {
			break // End of file reached, stop reading
		}
		if err != nil {
			return nil, err // Handle other errors
		}

		// Read the Key based on KeySize
		keyBytes := make([]byte, keySize)
		_, err = file.Read(keyBytes)
		if err != nil {
			return nil, err // Handle read error
		}

		// Read the Offset
		var offset uint64
		err = binary.Read(file, binary.BigEndian, &offset)
		if err != nil {
			return nil, err // Handle read error
		}

		var logOffset uint64
		err = binary.Read(file, binary.BigEndian, &logOffset)
		if err != nil {
			return nil, err
		}

		// Construct the Index and add it to the slice
		index := Index{
			Key:       string(keyBytes),
			Offset:    offset,
			LogOffset: logOffset,
		}
		indexes = append(indexes, index)
	}

	return indexes, nil
}

func FindIndex(file *os.File, searchKey string, startIndex Index, sampleRate int) *Index {
	currentOffset := int64(startIndex.Offset)

	buffer := make([]byte, 8) // Buffer for reading KeySize and Offset

	for i := 0; i < sampleRate; i++ {
		// Seek to the current offset in the file
		if _, err := file.Seek(currentOffset, io.SeekStart); err != nil {
			break // Break on any seek error, including EOF
		}

		// Read the KeySize
		if _, err := io.ReadFull(file, buffer); err != nil {
			break // Break on read error or EOF
		}
		keySize := binary.BigEndian.Uint64(buffer)

		// Read the Key based on KeySize
		keyBytes := make([]byte, keySize)
		if _, err := io.ReadFull(file, keyBytes); err != nil {
			break // Break on read error or EOF
		}

		// Read the Offset
		if _, err := io.ReadFull(file, buffer); err != nil {
			break // Break on read error or EOF
		}
		offsetRead := binary.BigEndian.Uint64(buffer)

		//Read the LogOffset
		if _, err := io.ReadFull(file, buffer); err != nil {
			break
		}
		logOffsetRead := binary.BigEndian.Uint64(buffer)

		// Check if the current index's Key matches the searchKey
		if string(keyBytes) == searchKey {
			return &Index{Key: string(keyBytes), Offset: offsetRead, LogOffset: logOffsetRead} // Match found
		}

		// Update currentOffset to the next index position
		currentOffset += 8 + int64(keySize) + 8 + 8 //(key_size - 8bytes, key - unknown num of bytes, offset - 8 bytes, logOffset - 8 bytes)
	}

	// If no match is found, return nil
	return nil
}
