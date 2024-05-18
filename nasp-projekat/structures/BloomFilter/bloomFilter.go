package BloomFilter

import (
	"bytes"
	"encoding/binary"
	"encoding/gob"
	"fmt"
	"os"
	"strings"
)

type BloomFilter struct {
	BitSet []uint8
	Funcs  []HashWithSeed
}

func NewBloomFilter(expectedElements int, falsePositiveRate float64) *BloomFilter {
	m := CalculateM(expectedElements, falsePositiveRate)
	k := CalculateK(expectedElements, m)
	return &BloomFilter{BitSet: MakeBitSet(m), Funcs: CreateHashFunctions(k)}
}

func (bf *BloomFilter) Position(index uint64) (uint64, uint8) {
	index = index % (uint64(len(bf.BitSet)) * 8)
	bucket := index / 8
	mask := uint8(1) << ((index - 1) % 8)
	return uint64(len(bf.BitSet)) - 1 - bucket, mask
}

func (bf *BloomFilter) Check(index uint64) bool {
	bucket, mask := bf.Position(index)
	return bf.BitSet[bucket]&mask>>((index-1)%8) == 1
}

func (bf *BloomFilter) Set(index uint64) {
	if !bf.Check(index) {
		bucket, mask := bf.Position(index)
		bf.BitSet[bucket] |= mask
	}
}

func (bf *BloomFilter) Add(key string) {
	var index uint64
	for _, f := range bf.Funcs {
		index = f.Hash([]byte(key))
		bf.Set(index)
	}
}

func (bf *BloomFilter) Contains(key string) bool {
	var index uint64
	for _, f := range bf.Funcs {
		index = f.Hash([]byte(key))
		if !bf.Check(index) {
			return false
		}
	}
	return true
}

func (bf *BloomFilter) Encode(fname string) {
	file, err := os.OpenFile(fname, os.O_RDWR|os.O_CREATE, 0666)
	if err != nil {
		panic(err)
	}
	encoder := gob.NewEncoder(file)
	err2 := encoder.Encode(bf)
	if err2 != nil {
		panic(err2)
	}
	file.Close()
}

func (bf *BloomFilter) Decode(fname string) {
	file, err := os.OpenFile(fname, os.O_RDWR, 0666)
	if err != nil {
		panic(err)
	}
	decoder := gob.NewDecoder(file)
	err2 := decoder.Decode(&bf)
	if err2 != nil {
		panic(err2)
	}
	file.Close()

}

func SerializeBloomFilter(bf BloomFilter) ([]byte, error) {
	var buffer bytes.Buffer

	// Serialize BitSet length
	if err := binary.Write(&buffer, binary.BigEndian, uint32(len(bf.BitSet))); err != nil {
		return nil, err
	}

	// Serialize BitSet
	if _, err := buffer.Write(bf.BitSet); err != nil {
		return nil, err
	}

	// Serialize Funcs length
	if err := binary.Write(&buffer, binary.BigEndian, uint32(len(bf.Funcs))); err != nil {
		return nil, err
	}

	// Serialize Funcs
	for _, h := range bf.Funcs {
		// Serialize Seed length
		if err := binary.Write(&buffer, binary.BigEndian, uint32(len(h.Seed))); err != nil {
			return nil, err
		}

		// Serialize Seed
		if _, err := buffer.Write(h.Seed); err != nil {
			return nil, err
		}
	}

	return buffer.Bytes(), nil
}

func DeserializeBloomFilter(data []byte) (BloomFilter, error) {
	var bf BloomFilter
	buffer := bytes.NewReader(data)

	// Deserialize BitSet length
	var bitSetLength uint32
	if err := binary.Read(buffer, binary.BigEndian, &bitSetLength); err != nil {
		return bf, err
	}

	// Deserialize BitSet
	bf.BitSet = make([]uint8, int(bitSetLength))
	if _, err := buffer.Read(bf.BitSet); err != nil {
		return bf, err
	}

	// Deserialize Funcs length
	var funcsLength uint32
	if err := binary.Read(buffer, binary.BigEndian, &funcsLength); err != nil {
		return bf, err
	}

	// Deserialize Funcs
	bf.Funcs = make([]HashWithSeed, int(funcsLength))
	for i := 0; i < int(funcsLength); i++ {
		// Deserialize Seed length
		var seedLength uint32
		if err := binary.Read(buffer, binary.BigEndian, &seedLength); err != nil {
			return bf, err
		}

		// Deserialize Seed
		bf.Funcs[i].Seed = make([]byte, int(seedLength))
		if _, err := buffer.Read(bf.Funcs[i].Seed); err != nil {
			return bf, err
		}
	}

	return bf, nil
}

func GetBloomFilterFromFile(file string) *BloomFilter {
	f, err := os.Open(file)
	if err != nil {
		fmt.Printf("Error opening file: %v\n", err)
		return nil
	}
	defer f.Close()

	if strings.HasSuffix(file, "Single.db") {
		header := make([]uint64, 4)
		headerBytes := make([]byte, 8*len(header))
		if _, err := f.Read(headerBytes); err != nil {
			return &BloomFilter{}
		}
		for i := range header {
			header[i] = binary.BigEndian.Uint64(headerBytes[i*8 : (i+1)*8])
		}

		// Deserialize the Bloom filter
		f.Seek(int64(header[0]), 0)
		filterBytes := make([]byte, header[1]-header[0])
		if _, err := f.Read(filterBytes); err != nil {
			return &BloomFilter{}
		}
		filter, err := DeserializeBloomFilter(filterBytes)
		if err != nil {
			return &BloomFilter{}
		}
		return &filter
	} else {
		filter := BloomFilter{}
		filter.Decode(file)
		return &filter
	}
}
