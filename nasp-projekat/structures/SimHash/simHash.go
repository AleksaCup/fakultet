package SimHash

import (
	"crypto/md5"
	"encoding/binary"
	"fmt"
	hash "nasp-projekat/structures/BloomFilter"
	"strings"
)

func GetHashAsString(data []byte) string {
	hash := md5.Sum(data)
	res := ""
	for _, b := range hash {
		res = fmt.Sprintf("%s%b", res, b)
	}
	return res
}

type SimHash struct {
	bits uint
}

func NewSimHash(bits uint) *SimHash {
	return &SimHash{
		bits: bits,
	}
}

func getWeightMap(tockens []string) map[string]int {
	weightMap := make(map[string]int)

	for _, tocken := range tockens {
		_, check := weightMap[tocken]

		if !check {
			weightMap[tocken] = 1
		} else {
			weightMap[tocken]++
		}
	}

	return weightMap
}

func (sh *SimHash) getFingerprint(tockens []string, weights map[string]int) string {
	sum := make([]int, sh.bits)

	for _, tocken := range tockens {
		tockenBin := hash.ToBinary(hash.GetMD5Hash(tocken))

		for i := 0; i < int(sh.bits); i++ {
			if tockenBin[i] == '1' {
				sum[i] += weights[tocken]
			} else {
				sum[i] -= weights[tocken]
			}
		}
	}

	fingerprint := ""
	for i := 0; i < int(sh.bits); i++ {
		if sum[i] > 0 {
			fingerprint += "1"
		} else {
			fingerprint += "0"
		}
	}

	return fingerprint
}

func (sh *SimHash) hammingDistance(bits1 string, bits2 string) uint {
	ans := 0
	for i := 0; i < int(sh.bits); i++ {
		if bits1[i] != bits2[i] {
			ans++
		}
	}

	return uint(ans)
}

func (sh *SimHash) Compare(data1 string, data2 string) uint {
	data1 = strings.ToLower(data1)
	data2 = strings.ToLower(data2)

	replacer := strings.NewReplacer(",", ".", "?", "!")
	data1 = replacer.Replace(data1)
	data2 = replacer.Replace(data2)

	tockens1 := strings.Split(data1, " ")
	tockens2 := strings.Split(data2, " ")

	weights1 := getWeightMap(tockens1)
	weights2 := getWeightMap(tockens2)

	fingerprint1 := sh.getFingerprint(tockens1, weights1)
	fingerprint2 := sh.getFingerprint(tockens2, weights2)

	return sh.hammingDistance(fingerprint1, fingerprint2)
}

func (sh *SimHash) Serialize() []byte {
	ret := make([]byte, 0)
	return binary.BigEndian.AppendUint32(ret, uint32(sh.bits))
}

func Deserialize(byteArray []byte) *SimHash {
	return &SimHash{
		bits: uint(binary.BigEndian.Uint32(byteArray)),
	}
}
