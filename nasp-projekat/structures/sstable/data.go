package sstable

import (
	"bytes"
	"encoding/binary"
	"io"
	"os"
)

type Segment struct {
	KeyValues []KeyValue
}

type KeyValue struct {
	Key   string
	Value ValueStruct
}

type ValueStruct struct {
	Tombstone bool
	Timestamp uint64
	Value     []byte
}

// ====================================SERIALIZATION OF DATA BLOCK TO FILE====================================
func SerializeDataToFile(data []Segment, filePath string) error {
	file, err := os.OpenFile(filePath, os.O_WRONLY|os.O_CREATE|os.O_TRUNC, 0666)
	if err != nil {
		return err
	}
	defer file.Close()

	// Iterate over each Segment
	for _, segment := range data {
		// Write the number of KeyValues in the Segment
		if err := binary.Write(file, binary.BigEndian, uint64(len(segment.KeyValues))); err != nil {
			return err
		}

		// Iterate over each KeyValue in the Segment
		for _, keyValue := range segment.KeyValues {
			// Serialize and write the Key
			if err := writeStringToFile(file, keyValue.Key); err != nil {
				return err
			}

			// Serialize and write the ValueStruct
			if err := serializeValueStructToFile(file, keyValue.Value); err != nil {
				return err
			}
		}
	}

	return nil
}

func DeserializeDataFromFile(filePath string) ([]Segment, error) {
	file, err := os.Open(filePath)
	if err != nil {
		return nil, err
	}
	defer file.Close()
	var data []Segment

	for {
		// Read the number of KeyValues in the current Segment
		var numKeyValues uint64
		if err := binary.Read(file, binary.BigEndian, &numKeyValues); err != nil {
			if err == io.EOF {
				break
			}
			return nil, err
		}

		segment := Segment{}
		for i := uint64(0); i < numKeyValues; i++ {
			key, err := readStringFromFile(file)
			if err != nil {
				return nil, err
			}

			valueStruct, err := deserializeValueStructFromFile(file)
			if err != nil {
				return nil, err
			}

			segment.KeyValues = append(segment.KeyValues, KeyValue{Key: key, Value: valueStruct})
		}

		data = append(data, segment)
	}

	return data, nil
}

//==========================================DATA SERIALIZATION==========================================

func DeserializeData(dataBytes []byte) ([]Segment, error) {
	var data []Segment
	buffer := bytes.NewReader(dataBytes)

	for buffer.Len() > 0 {
		// Read the number of KeyValues in the current Segment
		var numKeyValues uint64
		if err := binary.Read(buffer, binary.BigEndian, &numKeyValues); err != nil {
			if err == io.EOF {
				break
			}
			return nil, err
		}

		segment := Segment{}
		for i := uint64(0); i < numKeyValues; i++ {
			key, err := readStringFromBuffer(buffer)
			if err != nil {
				return nil, err
			}

			valueStruct, err := deserializeValueStructFromBuffer(buffer)
			if err != nil {
				return nil, err
			}

			segment.KeyValues = append(segment.KeyValues, KeyValue{Key: key, Value: valueStruct})
		}

		data = append(data, segment)
	}

	return data, nil

}

func SerializeData(data []Segment) ([]byte, error) {
	var buffer bytes.Buffer

	for _, segment := range data {
		// Write the number of KeyValues in the current Segment
		numKeyValues := uint64(len(segment.KeyValues))
		if err := binary.Write(&buffer, binary.BigEndian, numKeyValues); err != nil {
			return nil, err
		}

		for _, keyValue := range segment.KeyValues {
			dataBytes, err := SerializeKeyValue(keyValue)
			if err != nil {
				return nil, err
			}

			if _, err := buffer.Write(dataBytes); err != nil { // Write the serialized KeyValue bytes to the buffer
				return nil, err // Handle write error
			}

		}
	}

	return buffer.Bytes(), nil
}

// ===============================================HELPPER FUNCTION================================================

// ========================================SERIALIZATION OF SEGMENT BLOCK========================================
func SerializeKeyValue(kv KeyValue) ([]byte, error) {
	var buffer bytes.Buffer

	// Serialize the Key
	keyBytes := []byte(kv.Key)
	if err := binary.Write(&buffer, binary.BigEndian, uint64(len(keyBytes))); err != nil {
		return nil, err
	}
	if _, err := buffer.Write(keyBytes); err != nil {
		return nil, err
	}

	// Serialize the ValueStruct
	if err := binary.Write(&buffer, binary.BigEndian, kv.Value.Tombstone); err != nil {
		return nil, err
	}
	if err := binary.Write(&buffer, binary.BigEndian, kv.Value.Timestamp); err != nil {
		return nil, err
	}

	// Serialize the length of the Value slice
	if err := binary.Write(&buffer, binary.BigEndian, uint64(len(kv.Value.Value))); err != nil {
		return nil, err
	}

	// Serialize the Value slice
	if _, err := buffer.Write(kv.Value.Value); err != nil {
		return nil, err
	}

	return buffer.Bytes(), nil
}

func DeserializeKeyValue(data []byte) (KeyValue, error) {
	var kv KeyValue
	buffer := bytes.NewReader(data)

	// Deserialize the Key
	var keySize uint64
	if err := binary.Read(buffer, binary.BigEndian, &keySize); err != nil {
		return kv, err
	}
	keyBytes := make([]byte, keySize)
	if _, err := buffer.Read(keyBytes); err != nil {
		return kv, err
	}
	kv.Key = string(keyBytes)

	// Deserialize the ValueStruct
	if err := binary.Read(buffer, binary.BigEndian, &kv.Value.Tombstone); err != nil {
		return kv, err
	}
	if err := binary.Read(buffer, binary.BigEndian, &kv.Value.Timestamp); err != nil {
		return kv, err
	}

	// Deserialize the length of the Value slice
	var valueLen uint64
	if err := binary.Read(buffer, binary.BigEndian, &valueLen); err != nil {
		return kv, err
	}

	// Deserialize the Value slice
	kv.Value.Value = make([]byte, valueLen)
	if _, err := buffer.Read(kv.Value.Value); err != nil {
		return kv, err
	}

	return kv, nil
}

// ===============================================HELPPER FUNCTION================================================
// FUNCTION CALCULATES TOTAL LENGTH OF ALL DATA SEGMENTS
func (sstable *SSTable) lengthOfData() int {
	lengthOfData := 0
	for _, segment := range sstable.Data {
		lengthOfData += len(segment.KeyValues)
	}

	return lengthOfData
}

// SERIALIZATION HELPPER===================================================================
func writeStringToFile(file *os.File, str string) error {
	// Write the length of the string
	if err := binary.Write(file, binary.BigEndian, uint64(len(str))); err != nil {
		return err
	}

	// Write the string bytes
	_, err := file.Write([]byte(str))
	return err
}

func serializeValueStructToFile(file *os.File, valueStruct ValueStruct) error {
	// Write the Tombstone
	if err := binary.Write(file, binary.BigEndian, valueStruct.Tombstone); err != nil {
		return err
	}

	// Write the Timestamp
	if err := binary.Write(file, binary.BigEndian, valueStruct.Timestamp); err != nil {
		return err
	}

	// Write the Value length
	if err := binary.Write(file, binary.BigEndian, uint64(len(valueStruct.Value))); err != nil {
		return err
	}

	// Write the Value bytes
	_, err := file.Write(valueStruct.Value)
	return err
}

// DESERIALIZATION HELPPER===================================================================
// FUNCTION TO READ STRING FROM FILE
func readStringFromFile(file *os.File) (string, error) {
	var length uint64
	if err := binary.Read(file, binary.BigEndian, &length); err != nil {
		return "", err
	}

	keyBytes := make([]byte, length)
	if _, err := file.Read(keyBytes); err != nil {
		return "", err
	}

	return string(keyBytes), nil
}

// FUNCTION TO DESERIALIZE VALUE FROM FILE
func deserializeValueStructFromFile(file *os.File) (ValueStruct, error) {
	var valueStruct ValueStruct

	// Read the Tombstone
	if err := binary.Read(file, binary.BigEndian, &valueStruct.Tombstone); err != nil {
		return ValueStruct{}, err
	}

	// Read the Timestamp
	if err := binary.Read(file, binary.BigEndian, &valueStruct.Timestamp); err != nil {
		return ValueStruct{}, err
	}

	// Read the Value length
	var valueLength uint64
	if err := binary.Read(file, binary.BigEndian, &valueLength); err != nil {
		return ValueStruct{}, err
	}

	// Read the Value bytes
	valueBytes := make([]byte, valueLength)
	if _, err := file.Read(valueBytes); err != nil {
		return ValueStruct{}, err
	}
	valueStruct.Value = valueBytes

	return valueStruct, nil
}

// FUNCTION TO READ STRING FROM BUFFER
func readStringFromBuffer(buffer *bytes.Reader) (string, error) {
	var length uint64
	if err := binary.Read(buffer, binary.BigEndian, &length); err != nil {
		return "", err
	}

	keyBytes := make([]byte, length)
	if _, err := buffer.Read(keyBytes); err != nil {
		return "", err
	}

	return string(keyBytes), nil
}

// FUNCTION TO READ KEYVALUE FROM BUFFER
func deserializeValueStructFromBuffer(buffer *bytes.Reader) (ValueStruct, error) {
	var valueStruct ValueStruct

	// Read the Tombstone
	if err := binary.Read(buffer, binary.BigEndian, &valueStruct.Tombstone); err != nil {
		return ValueStruct{}, err
	}

	// Read the Timestamp
	if err := binary.Read(buffer, binary.BigEndian, &valueStruct.Timestamp); err != nil {
		return ValueStruct{}, err
	}

	// Read the Value length
	var valueLength uint64
	if err := binary.Read(buffer, binary.BigEndian, &valueLength); err != nil {
		return ValueStruct{}, err
	}

	// Read the Value bytes
	valueBytes := make([]byte, valueLength)
	if _, err := buffer.Read(valueBytes); err != nil {
		return ValueStruct{}, err
	}
	valueStruct.Value = valueBytes

	return valueStruct, nil
}
