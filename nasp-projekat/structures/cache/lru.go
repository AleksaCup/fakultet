package cache

import (
	"container/list"
	"fmt"
)

type LRUCache struct {
	capacity       uint
	ListOfElements *list.List
	MapOfElements  map[string]*list.Element
}

type cacheItem struct {
	Key   string
	Value []byte
}

func NewLRUCache(capacity uint) (*LRUCache, error) {
	if capacity <= 0 {
		return nil, fmt.Errorf("capacity must be greater than 0")
		// panic("Capacity must be greater than 0")
	}

	mapOfElements := make(map[string]*list.Element, capacity)
	listOfElements := list.New()

	return &LRUCache{
		capacity:       capacity,
		ListOfElements: listOfElements,
		MapOfElements:  mapOfElements,
	}, nil
}

func (cache *LRUCache) Get(key string) []byte {
	element, found := cache.MapOfElements[key]
	if found {
		cache.ListOfElements.MoveToFront(element)
		return element.Value.(*cacheItem).Value

	}
	return nil
}

func (cache *LRUCache) Put(key string, value []byte) {
	if elem, found := cache.MapOfElements[key]; found {
		elem.Value.(*cacheItem).Value = value

		cache.ListOfElements.MoveToFront(elem)
		return
	}

	newItem := &cacheItem{Key: key, Value: value}
	newElem := cache.ListOfElements.PushFront(newItem)
	cache.MapOfElements[key] = newElem

	if cache.ListOfElements.Len() > int(cache.capacity) {
		oldest := cache.ListOfElements.Back()
		if oldest != nil {
			cache.ListOfElements.Remove(oldest)
			oldestItem := oldest.Value.(*cacheItem)
			delete(cache.MapOfElements, oldestItem.Key)
		}
	}
}

func (cache *LRUCache) Remove(key string) {

	result, present := cache.MapOfElements[key]
	if present {
		cache.ListOfElements.Remove(result)
		delete(cache.MapOfElements, key)
	}
}

func (cache *LRUCache) PrintList() {

	fmt.Println("List of elements")
	for e := cache.ListOfElements.Front(); e != nil; e = e.Next() {
		fmt.Println(e.Value)
	}
}
