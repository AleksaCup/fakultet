package main

import (
	// "fmt"
	menu "nasp-projekat/menu"
	memtable "nasp-projekat/structures/Memtable"
	readPath "nasp-projekat/structures/ReadPath"

	// wal "nasp-projekat/structures/WAL"
	cache "nasp-projekat/structures/cache"
	configurator "nasp-projekat/structures/configurator"
	writePath "nasp-projekat/structures/writePath"
	// "strconv"
)

// func generateLogs(numLogs int) []wal.Log {
// 	var logs []wal.Log

// 	for i := 1; i <= numLogs; i++ {
// 		key := fmt.Sprintf("%05d_kljuc", i)
// 		data := []byte(strconv.Itoa(i))
// 		log := wal.CreateLog(key, data, false)
// 		logs = append(logs, log)
// 	}

// 	return logs
// }

func main() {
	var config configurator.Configuration
	configurator.ReadConfiguration(&config)
	Memtable := memtable.CreateMemtable()
	Lru, _ := cache.NewLRUCache(uint(config.CacheCapacity))
	read := readPath.NewReadPath(Memtable, Lru)
	write := writePath.NewWritePath(Memtable, Lru)

	menu.App(config, read, write)
}
