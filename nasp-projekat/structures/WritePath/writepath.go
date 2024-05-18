package WritePath

import (
	"fmt"
	"nasp-projekat/structures/Memtable"
	"nasp-projekat/structures/cache"
	"nasp-projekat/structures/configurator"
	"nasp-projekat/structures/sstable"
	"os"
	"strconv"
	"strings"
)

type WritePath struct {
	Memtable *Memtable.Memtable
	Cache    *cache.LRUCache
}

func NewWritePath(memtable *Memtable.Memtable, cache *cache.LRUCache) *WritePath {
	return &WritePath{
		Memtable: memtable,
		Cache:    cache,
	}
}

func (writePath *WritePath) Write(key string, value []byte, tombstone bool) {
	writeToMemTable := writePath.Memtable.Insert(key, value, tombstone)
	writePath.Cache.Put(key, value)
	if !writeToMemTable {
		fmt.Println("Failed to write into WAL and MemTable")
	} else {
		fmt.Println("Successful writing to system!")
	}
}

func (writePath *WritePath) Delete(key string) bool {
	var config configurator.Configuration
	configurator.ReadConfiguration(&config)

	if writePath.Cache.Get(key) != nil {
		writePath.Cache.Remove(key)
	}

	log := writePath.Memtable.Find(key)
	if log.Key != "" {
		writePath.Memtable.Delete(log.Key, log.Value, true)
		return true
	} else {
		numOfDirectorys, _ := sstable.CountSubdirs("data")
		for i := 0; i < numOfDirectorys; i++ {
			a := strconv.Itoa(i + 1)
			folder, err := os.ReadDir("data/sstable" + a)
			if err != nil {
				return false
			}
			numOfFiles := sstable.CountFilesInFolder("data/sstable" + a)
			if config.FileStructure == "Multiple" {
				if numOfFiles <= 1 {
					continue
				} else {
					for _, entry := range folder {
						if strings.HasSuffix(entry.Name(), "Data.db") {
							if sstable.DeleteElement(key, "data/sstable"+a+"/"+strings.TrimSuffix(entry.Name(), "Data.db")) {
								return true
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
							if sstable.DeleteElement(key, "data/sstable"+a+"/"+strings.TrimSuffix(entry.Name(), "Single.db")) {
								return true
							} else {
								break
							}
						}
					}
				}
			}
		}
	}

	return false
}
