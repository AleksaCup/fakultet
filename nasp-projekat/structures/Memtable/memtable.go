package Memtable

import (
	WAL "nasp-projekat/structures/WAL"
	configurator "nasp-projekat/structures/configurator"
	skipList "nasp-projekat/structures/skipList"
	sstable "nasp-projekat/structures/sstable"
	"os"
	"sort"
	"strconv"
	"strings"
)

type Memtable struct {
	Size     uint64
	SkipList skipList.SkipList
}

func CreateMemtable() *Memtable {
	var config configurator.Configuration
	configurator.ReadConfiguration(&config)
	emptySkipList := skipList.NewSkipList(5, 0, 0, nil)
	memTable := Memtable{Size: 0, SkipList: *emptySkipList}
	memTable.Init()
	return &memTable

}
func (memtable *Memtable) EmptyMemtable() {
	memtable.Size = 0
	memtable.SkipList.Empty()
}

func (memtable *Memtable) Init() {
	var config configurator.Configuration
	configurator.ReadConfiguration(&config)
	dirPath := "logs/"
	files, err := os.ReadDir(dirPath)
	if err != nil {
		panic(err)
	}
	for _, file := range files {
		if file.IsDir() {
			// Skip directories
			continue
		}
		fileF, err := os.OpenFile("logs/"+file.Name(), os.O_RDWR, 0644)
		if err != nil {
			return
		}
		defer fileF.Close()
		data, _ := WAL.ReadAllLogs(fileF)
		if len(data) > int(config.MemtableSize) {
			return
		}
		for line := range data {
			memtable.SkipList.Insert(data[line].Key, data[line].Value, data[line].Tombstone)
			memtable.Size++
		}

	}

}

func (mem *Memtable) Find(key string) WAL.Log {
	node := mem.SkipList.FindElementByKey(key)
	if node != nil {
		return node.Log
	}
	return WAL.Log{}
}

func (memtable *Memtable) Insert(key string, value []byte, tombstone bool) bool {
	var config configurator.Configuration
	configurator.ReadConfiguration(&config)
	log := WAL.CreateLog(key, value, tombstone)
	if !WAL.WriteLog(&log) {
		return false
	}
	memtable.SkipList.Insert(key, value, false)
	memtable.Size++
	if memtable.Size >= uint64(float64(config.MemtableSize)*config.Threshold) {
		memtable.Flush()

	}
	return true
}

// Pitaj za razliku izmedju tudje i tvoje fje
func (memtable *Memtable) Delete(key string, value []byte, tombstone bool) {
	log := WAL.CreateLog(key, value, tombstone)
	if !WAL.WriteLog(&log) {
		return
	}
	memtable.SkipList.Delete(key)

}

func (memtable *Memtable) Flush() {
	var config configurator.Configuration
	configurator.ReadConfiguration(&config)
	node := memtable.SkipList.GetHeader()
	var data []WAL.Log
	for {
		for _, subNode := range (node).Next {
			if subNode == nil {
				node = subNode
				continue
			}
			data = append(data, subNode.Log)
		}
		if node == nil {
			break
		}
	}
	sort.Slice(data, func(i, j int) bool {
		return data[i].Key < data[j].Key
	})

	numOfDirectorys, _ := sstable.CountSubdirs("data")
	// if numOfDirectorys == 0 {
	// 	sstable.NewSStable(data, 1)
	// 	memtable.EmptyMemtable()
	// 	WAL.DeleteWAL()
	// 	return
	// }
	for i := 0; i < numOfDirectorys; i++ {
		a := strconv.Itoa(i + 1)
		folder, err := os.ReadDir("data/sstable" + a)
		if err != nil {
			return
		}
		numOfFiles := sstable.CountFilesInFolder("data/sstable" + a)
		if config.FileStructure == "Multiple" {
			if numOfFiles <= 1 {
				continue
			} else {
				for _, entry := range folder {
					if strings.HasSuffix(entry.Name(), "Data.db") {
						if sstable.AddSegmentDevelopment(data, "data/sstable"+a+"/"+strings.TrimSuffix(entry.Name(), "Data.db")) {
							memtable.EmptyMemtable()
							WAL.DeleteWAL()
							return

						} else {
							break
						}
					}
				}
			}
		} else if config.FileStructure == "Single" {
			if numOfFiles > 1 || numOfFiles == 0 {
				continue
			} else {
				for _, entry := range folder {
					if strings.HasSuffix(entry.Name(), "Single.db") {
						if sstable.AddSegmentDevelopment(data, "data/sstable"+a+"/"+strings.TrimSuffix(entry.Name(), "Single.db")) {
							memtable.EmptyMemtable()
							WAL.DeleteWAL()
							return
						} else {
							break
						}
					}
				}
			}
		}
	}
	sstable.NewSStable(data, 1)
	memtable.EmptyMemtable()
	WAL.DeleteWAL()
}
