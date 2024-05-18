package configurator

import (
	"encoding/json"
	"fmt"
	"io"
	"os"
)

type Configuration struct {
	WalSize        int     `json:"wal_size"`
	CacheCapacity  int     `json:"cache_capacity"`
	SampleRate     int     `json:"sample_rate"`
	BloomPrecision float64 `json:"bloom_precision"`
	SegmentNumber  int     `json:"segment_number"`
	FileStructure  string  `json:"file_structure"`
	MemtableSize   uint64  `json:"memtable_size"`
	Threshold      float64 `json:"memtable_threshold"`
	WalByteLength  int     `json:"wal_byte_length"`
	// i should add more fields here later

}

func SetDefaultValues(c *Configuration) {
	c.WalSize = 10
	c.CacheCapacity = 5
	c.SampleRate = 3
	c.BloomPrecision = 0.01
	c.SegmentNumber = 5
	c.FileStructure = "Multiple"
	c.MemtableSize = 10
	c.Threshold = 0.7
	c.WalByteLength = 0
	// here too
}

func ReadConfiguration(c *Configuration) {
	configFile, err := os.Open("configuration/config.json")
	if err != nil || fileIsEmpty(configFile) {
		fmt.Println("Error opening config file, loading default configuration:", err)
		SetDefaultValues(c)
		return
	}
	defer configFile.Close()

	byteValue, err := io.ReadAll(configFile)
	if err != nil {
		fmt.Println("Error reading config file, loading default configuration:", err)
		SetDefaultValues(c)
		return
	}

	err = json.Unmarshal(byteValue, c)
	if err != nil {
		fmt.Println("Error parsing config file, loading default configuration:", err)
		SetDefaultValues(c)
		return
	}
}

func fileIsEmpty(f *os.File) bool {
	fileInfo, err := f.Stat()
	if err != nil {
		return true
	}
	return fileInfo.Size() == 0
}
