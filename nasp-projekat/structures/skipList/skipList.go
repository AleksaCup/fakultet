package skiplist

import (
	"math/rand"
	"nasp-projekat/structures/WAL"
	"time"
)

type SkipList struct {
	maxHeight int
	height    int
	size      int
	head      *SkipListNode
}
type SkipListNode struct {
	Log  WAL.Log
	Next []*SkipListNode
}

func (sl *SkipList) Empty() {
	// Reset the size and height of the SkipList
	sl.size = 0
	sl.height = 0

	// Reinitialize the head node
	sl.head = &SkipListNode{
		Log:  WAL.Log{}, // Assuming WAL.Log can be zero-initialized or provide a method to get an empty Log
		Next: make([]*SkipListNode, sl.maxHeight),
	}
}

func (sl *SkipList) GetMaxHeight() int {
	return sl.maxHeight
}
func (sl *SkipList) GetHeight() int {
	return sl.height
}
func (sl *SkipList) GetSize() int {
	return sl.size
}

func (sl *SkipList) GetHeader() *SkipListNode {
	return sl.head
}

func NewSkipList(maxHeight int, height int, size int, head *SkipListNode) *SkipList {
	skipList := SkipList{maxHeight, height, size, head}
	skipList.CreateSkipList()
	return &skipList

}
func (sl *SkipList) CreateSkipList() {
	bytes := []byte("")
	chcks := WAL.CRC32(bytes)

	t := time.Now().Unix()
	timeStamp := uint64(t)
	tombStone := false
	keySize := uint64(len(""))
	value := make([]byte, 10)
	valueSize := uint64(len(value))

	var log WAL.Log
	log.Crc = chcks
	log.TimeStamp = uint64(timeStamp)
	log.Tombstone = tombStone
	log.KeySize = keySize
	log.ValueSize = valueSize
	log.Value = value
	log.Key = ""

	headNode := SkipListNode{log, make([]*SkipListNode, sl.maxHeight)}
	sl.head = &headNode
	//PROCITAJ JOS JEDNOM
}

func (sl *SkipList) FindElementByKey(key string) *SkipListNode {
	node := sl.head
	for {

		for _, subNode := range (node).Next {
			if node.Log.Key == key {
				return node
			} else if subNode == nil {
				node = subNode
				break
			} else if subNode.Log.Key < key {
				node = subNode
				break
			} else if subNode.Log.Key >= key {
				node = subNode
				break
			}
		}
		if node == nil {
			return nil
		}
	}
}

func (sl *SkipList) Insert(key string, value []byte, tombstone bool) {
	update := make([]*SkipListNode, sl.maxHeight)
	headHead := sl.head
	for i := sl.height; i >= 0; i-- {
		for headHead.Next[i] != nil && headHead.Next[i].Log.Key < key {
			headHead = headHead.Next[i]
		}
		update[i] = headHead
	}
	headHead = headHead.Next[0]
	if headHead == nil || key != headHead.Log.Key {
		sl.size++
		level := sl.flip()
		if level > sl.height {
			for i := sl.height + 1; i <= level; i++ {
				update[i] = sl.head
			}
			sl.height = level
		}
		bytes := []byte(key)
		chcks := WAL.CRC32(bytes)

		t := time.Now().Unix()
		timeStamp := uint64(t)
		keySize := uint64(len(key))
		valueSize := uint64(len(value))

		var log WAL.Log
		log.Crc = chcks
		log.TimeStamp = uint64(timeStamp)
		log.Tombstone = tombstone
		log.KeySize = keySize
		log.ValueSize = valueSize
		log.Value = value
		log.Key = key

		newNode := SkipListNode{log, make([]*SkipListNode, sl.maxHeight)}
		for i := 0; i <= level; i++ {
			newNode.Next[i] = update[i].Next[i]
			update[i].Next[i] = &newNode
		}
		return
	}
	if headHead.Log.Key == key {
		bytes := []byte(key)
		chcks := WAL.CRC32(bytes)

		t := time.Now().Unix()
		timeStamp := uint64(t)
		keySize := uint64(len(key))
		valueSize := uint64(len(value))

		var log WAL.Log
		log.Crc = chcks
		log.TimeStamp = uint64(timeStamp)
		log.Tombstone = tombstone
		log.KeySize = keySize
		log.ValueSize = valueSize
		log.Value = value
		log.Key = key

		headHead.Log = log
		return
	}
}

func (sl *SkipList) Delete(key string) {
	update := make([]*SkipListNode, sl.maxHeight)
	headHead := sl.head
	for i := sl.height; i >= 0; i-- {
		for headHead.Next[i] != nil && headHead.Next[i].Log.Key < key {
			headHead = headHead.Next[i]
		}
		update[i] = headHead
	}

	headHead = headHead.Next[0]
	elementToDelete := sl.FindElementByKey(key)
	if headHead == elementToDelete && headHead != nil {
		headHead.Log.Tombstone = true
	} else {
		return
	}
}
func (sl *SkipList) flip() int {
	level := 0
	for ; rand.Int31n(2) == 1; level++ {
		if level > sl.maxHeight {
			if level > sl.height {
				sl.height = level
			}
		}
	}
	return level
}
