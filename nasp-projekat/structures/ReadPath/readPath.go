package ReadPath

import (
	"fmt"
	"nasp-projekat/structures/Memtable"
	"nasp-projekat/structures/cache"
	sstable "nasp-projekat/structures/sstable"
)

type ReturnValue struct {
	Key       string
	Value     []byte
	Tombstone bool
}

type ReadPath struct {
	Memtable *Memtable.Memtable
	Cache    *cache.LRUCache
}

func NewReadPath(memtable *Memtable.Memtable, cache *cache.LRUCache) *ReadPath {
	return &ReadPath{
		Memtable: memtable,
		Cache:    cache,
	}
}

func (readPath *ReadPath) Read(key string) *ReturnValue {
	fmt.Println("Reading memtable")
	foundInMemTable := readPath.Memtable.Find(key)
	if foundInMemTable.Key != "" {
		readPath.Cache.Put(key, foundInMemTable.Value)
		return &ReturnValue{Key: foundInMemTable.Key, Value: foundInMemTable.Value, Tombstone: foundInMemTable.Tombstone}
	}
	fmt.Println("Not found in memtable")
	fmt.Println("Reading cache")
	foundInCache := readPath.Cache.Get(key)
	if foundInCache != nil {
		readPath.Cache.Put(key, foundInCache)
		return &ReturnValue{Key: key, Value: foundInCache, Tombstone: false}
	}
	fmt.Println("Not found in Cache")
	fmt.Println("Reading sstable")
	foundInSSTable, err := sstable.ReadRecordWithKey(key)
	if err != nil {
		return nil
	}
	if foundInSSTable.Key != "" {
		readPath.Cache.Put(key, foundInSSTable.Value.Value)
		return &ReturnValue{Key: foundInSSTable.Key, Value: foundInSSTable.Value.Value, Tombstone: foundInSSTable.Value.Tombstone}
	}
	fmt.Println("Not found in SSTable")
	return nil
}
