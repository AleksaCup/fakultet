package WAL

import (
	"bytes"
	"encoding/binary"
	"fmt"
	"hash/crc32"
	"log"
	"nasp-projekat/structures/configurator"
	"os"
	"strconv"
	"time"
)

/*
   +---------------+-----------------+---------------+---------------+-----------------+-...-+--...--+
   |    CRC (4B)   | Timestamp (8B) | Tombstone(1B) | Key Size (8B) | Value Size (8B) | Key | Value |
   +---------------+-----------------+---------------+---------------+-----------------+-...-+--...--+
   CRC = 32bit hash computed over the payload using CRC
   Key Size = Length of the Key data
   Tombstone = If this record was deleted and has a value
   Value Size = Length of the Value data
   Key = Key data
   Value = Value data
   Timestamp = Timestamp of the operation in seconds
*/

const (
	CRC_SIZE        = 4
	TIMESTAMP_SIZE  = 8
	TOMBSTONE_SIZE  = 1
	KEY_SIZE_SIZE   = 8
	VALUE_SIZE_SIZE = 8

	CRC_START        = 0
	TIMESTAMP_START  = CRC_START + CRC_SIZE
	TOMBSTONE_START  = TIMESTAMP_START + TIMESTAMP_SIZE
	KEY_SIZE_START   = TOMBSTONE_START + TOMBSTONE_SIZE
	VALUE_SIZE_START = KEY_SIZE_START + KEY_SIZE_SIZE
	KEY_START        = VALUE_SIZE_START + VALUE_SIZE_SIZE
)

type Log struct {
	Crc       uint32
	TimeStamp uint64
	Tombstone bool
	KeySize   uint64
	ValueSize uint64
	Key       string //[]byte
	Value     []byte
}

type CurrentWalSegment struct {
	WalCounter     int
	SegmentCounter int
}

var currentWalSegment CurrentWalSegment

// Racunanje crc-a
func CRC32(data []byte) uint32 {
	return crc32.ChecksumIEEE(data)
}

// Kreiranje Log zapisa
func CreateLog(key string, value []byte, tombstone bool) Log {
	return Log{
		Crc:       crc32.ChecksumIEEE(value),
		TimeStamp: uint64(time.Now().Unix()),
		Tombstone: tombstone,
		KeySize:   uint64(len(key)),
		ValueSize: uint64(len(value)),
		Key:       key,
		Value:     value,
	}
}

// Racunamo koliko zapisa ima u WAL-u
func GetWallCounter() {
	currentWalSegment.SegmentCounter, _ = GetCurrentSegment()
	a := strconv.Itoa(currentWalSegment.SegmentCounter)
	file, _ := os.OpenFile("logs/output"+a+".bin", os.O_RDWR, 0644)
	defer file.Close()
	log, err := ReadLog(file, 0)

	if err != nil {
		return
	}

	currentWalSegment.WalCounter++
	var sum int64 = 0

	for err == nil {
		sum += int64(log.logSize())
		log, err = ReadLog(file, sum)
		if err == nil {
			currentWalSegment.WalCounter++
		}

	}
}

// Racuna u kom segmentu (fajlu) se trenutno nalazimo
func GetCurrentSegment() (int, error) {
	dirPath := "logs/"
	files, err := os.ReadDir(dirPath)
	if err != nil {
		log.Fatal(err)
	}

	var fileNames []string
	for _, file := range files {
		if file.IsDir() {
			// Skip directories
			continue
		}
		fileNames = append(fileNames, file.Name())
	}
	//currentWalSegment.SegmentCounter = len(fileNames)
	if len(fileNames) == 0 {
		return 1, nil
	}
	return len(fileNames), nil
}

// Pomocna funkcija za zapisivanje Loga u fajl
func (record *Log) Write(file *os.File) error {

	buf := record.Serialize()

	if _, err := file.Write(buf); err != nil {
		log.Fatal(err)
	}

	currentWalSegment.WalCounter++
	return nil
}

// Zapisivanje JEDNOG Log-a u fajl
func WriteLog(loglog *Log) bool {
	var config configurator.Configuration
	configurator.ReadConfiguration(&config)
	c, _ := GetCurrentSegment()
	GetWallCounter()
	a := strconv.Itoa(c)
	sumOfBytes := 0
	if config.WalByteLength == 0 {
		if currentWalSegment.WalCounter >= config.WalSize {
			currentWalSegment.SegmentCounter++
			a = strconv.Itoa(currentWalSegment.SegmentCounter)
		}
	} else {
		file, _ := os.OpenFile("logs/output"+a+".bin", os.O_APPEND|os.O_CREATE, 0644)
		defer file.Close()
		currentWalSegment.WalCounter = 0
		log1, err := ReadLog(file, 0)
		if err == nil {
			currentWalSegment.WalCounter++
		}
		for err == nil {
			sumOfBytes += int(log1.logSize())
			log1, err = ReadLog(file, int64(sumOfBytes))
		}
		if sumOfBytes >= config.WalByteLength-int(loglog.logSize()) {
			currentWalSegment.SegmentCounter++
			a = strconv.Itoa(currentWalSegment.SegmentCounter)
		}
	}

	file, _ := os.OpenFile("logs/output"+a+".bin", os.O_APPEND|os.O_CREATE, 0644)
	defer file.Close()
	loglog.Write(file)

	return true

}

// Funkcija koja cita sve zapise jednog loga
func ReadAllLogs(file *os.File) ([]Log, error) {
	var logs []Log
	currentWalSegment.WalCounter = 0
	log1, err := ReadLog(file, 0)
	logs = append(logs, log1)
	if err == nil {
		currentWalSegment.WalCounter++
	}

	var sum int64 = 0

	for err == nil {
		sum += int64(log1.logSize())
		log1, err = ReadLog(file, sum)
		if err == nil {
			logs = append(logs, log1)
		}

	}
	return logs, nil
}

// Citanje JEDNOG loga iz fajla
func ReadLog(file *os.File, offset int64) (Log, error) {
	_, _ = file.Seek(offset, 0)
	var b = make([]byte, CRC_SIZE)
	file.Read(b)
	var record Log
	record.Crc = binary.BigEndian.Uint32(b)
	b = make([]byte, TIMESTAMP_SIZE)
	file.Read(b)
	record.TimeStamp = binary.BigEndian.Uint64(b)
	b = make([]byte, TOMBSTONE_SIZE)
	file.Read(b)
	if b[0] == 1 {
		record.Tombstone = true
	} else {
		record.Tombstone = false
	}
	b = make([]byte, KEY_SIZE_SIZE)
	file.Read(b)
	record.KeySize = binary.BigEndian.Uint64(b)
	b = make([]byte, VALUE_SIZE_SIZE)
	file.Read(b)
	record.ValueSize = binary.BigEndian.Uint64(b)
	b = make([]byte, record.KeySize)
	file.Read(b)
	record.Key = string(b)
	b = make([]byte, record.ValueSize)
	file.Read(b)
	record.Value = b
	if CRC32(b) != record.Crc || (record.Crc == uint32(0) && CRC32(b) == uint32(0)) {
		return Log{}, fmt.Errorf("error")
	}
	return record, nil
}

// Prevodjenje jednog zapisa u niz bajtova
func (record *Log) Serialize() []byte {
	var buf bytes.Buffer
	// Write the record to the buffer
	binary.Write(&buf, binary.BigEndian, record.Crc)
	binary.Write(&buf, binary.BigEndian, record.TimeStamp)
	binary.Write(&buf, binary.BigEndian, record.Tombstone)
	binary.Write(&buf, binary.BigEndian, record.KeySize)
	binary.Write(&buf, binary.BigEndian, record.ValueSize)
	binary.Write(&buf, binary.BigEndian, []byte(record.Key))
	binary.Write(&buf, binary.BigEndian, record.Value)
	return buf.Bytes()
}

func Deserialize(data []byte) *Log {
	buf := bytes.NewReader(data)

	var log Log

	if err := binary.Read(buf, binary.BigEndian, &log.Crc); err != nil {
		return nil
	}

	if err := binary.Read(buf, binary.BigEndian, &log.TimeStamp); err != nil {
		return nil
	}

	if err := binary.Read(buf, binary.BigEndian, &log.Tombstone); err != nil {
		return nil
	}

	if err := binary.Read(buf, binary.BigEndian, &log.KeySize); err != nil {
		return nil
	}

	if err := binary.Read(buf, binary.BigEndian, &log.ValueSize); err != nil {
		return nil
	}

	key := make([]byte, log.KeySize)
	if err := binary.Read(buf, binary.BigEndian, key); err != nil {
		return nil
	}
	log.Key = string(key)

	value := make([]byte, log.ValueSize)
	if err := binary.Read(buf, binary.BigEndian, value); err != nil {
		return nil
	}
	log.Value = value

	return &log
}

// Racuna velicinu jednoig Log zapisa
func (record *Log) logSize() uint64 {
	var sum uint64
	sum += 4                // CRC
	sum += 8                // Timestamp
	sum += 1                // Tombstone
	sum += 8 + 8            // KeySize + ValueSize
	sum += record.KeySize   // Key
	sum += record.ValueSize // Value
	return sum
}

func DeleteWAL() {
	dirPath := "logs/"
	files, err := os.ReadDir(dirPath)
	if err != nil {
		return
	}
	for _, file := range files {
		if file.IsDir() {
			// Skip directories
			continue
		}
		os.Remove(dirPath + file.Name())
	}
	currentWalSegment.SegmentCounter = 0
	currentWalSegment.WalCounter = 0
}
