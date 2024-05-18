package CountMinSketch

import (
	"encoding/binary"
	hash "nasp-projekat/structures/BloomFilter"
)

type CountMinSketch struct {
	m             uint64
	k             uint64
	valueMatrix   [][]uint64
	hashFunctions []hash.HashWithSeed
}

func NewCountMinSketch(epsilon float64, delta float64) *CountMinSketch {
	m := uint64(CalculateM(epsilon))
	k := uint64(CalculateK(delta))

	matrix := make([][]uint64, k)
	for i := uint64(0); i < k; i++ {
		matrix[i] = make([]uint64, m)
	}

	return &CountMinSketch{
		m:             m,
		k:             k,
		valueMatrix:   matrix,
		hashFunctions: hash.CreateHashFunctions(uint(k)),
	}
}

func (cms *CountMinSketch) Add(key string) {
	for i, hashFunc := range cms.hashFunctions {
		j := hashFunc.Hash([]byte(key)) % uint64(cms.m)

		cms.valueMatrix[i][j] += 1
	}
}

func (cms *CountMinSketch) CountMin(key string) uint64 {
	min := uint64(0)

	for i, hashFunc := range cms.hashFunctions {
		j := hashFunc.Hash([]byte(key)) % uint64(cms.m)

		if i == 0 {
			min = cms.valueMatrix[i][j]
		} else if min > cms.valueMatrix[i][j] {
			min = cms.valueMatrix[i][j]
		}
	}

	return min
}

func (cms *CountMinSketch) Serialize() []byte {
	retArray := make([]byte, 0)

	retArray = binary.BigEndian.AppendUint64(retArray, cms.m)
	retArray = binary.BigEndian.AppendUint64(retArray, cms.k)

	for i := uint64(0); i < cms.k; i++ {
		for j := uint64(0); j < cms.m; j++ {
			retArray = binary.BigEndian.AppendUint64(retArray, cms.valueMatrix[i][j])
		}
	}

	for _, hashFunc := range cms.hashFunctions {
		retArray = append(retArray, hashFunc.Seed...)
	}

	return retArray
}

func Deserialize(byteArray []byte) *CountMinSketch {
	m := binary.BigEndian.Uint64(byteArray[0:8])
	k := binary.BigEndian.Uint64(byteArray[8:16])

	byteArray = byteArray[16:]
	matrix := make([][]uint64, k)
	for i := uint64(0); i < k; i++ {
		matrix[i] = make([]uint64, m)
		for j := uint64(0); j < m; j++ {
			matrix[i][j] = binary.BigEndian.Uint64(byteArray[0:8])
			byteArray = byteArray[8:]
		}
	}

	hashFuncs := make([]hash.HashWithSeed, k)
	for i := uint64(0); i < k; i++ {
		seed := byteArray[0:32]
		hashFuncs[i].Seed = seed
		byteArray = byteArray[32:]
	}

	return &CountMinSketch{
		m:             m,
		k:             k,
		valueMatrix:   matrix,
		hashFunctions: hashFuncs,
	}
}
