package HyperLogLog

import (
	"math"
	hash "nasp-projekat/structures/BloomFilter"
)

const (
	HLL_MIN_PRECISION = 4
	HLL_MAX_PRECISION = 16
)

type HyperLogLog struct {
	p   uint8
	m   uint64
	reg []uint8
}

func trailingZeros(binNum string) int {
	numOfZeros := 0

	for i := len(binNum) - 1; i >= 0; i-- {
		if binNum[i] == '0' {
			numOfZeros++
		} else {
			return numOfZeros
		}
	}

	return numOfZeros
}

func toInt(num string) uint {
	intNum := 0
	mul := 1

	for i := 0; i < len(num); i++ {
		if num[len(num)-i-1] == '1' {
			intNum += mul
		}

		mul *= 2
	}
	return uint(intNum)
}

func (hll *HyperLogLog) Estimate() float64 {
	sum := 0.0
	for _, val := range hll.reg {
		sum += math.Pow(math.Pow(2.0, float64(val)), -1)
	}

	alpha := 0.7213 / (1.0 + 1.079/float64(hll.m))
	estimation := alpha * math.Pow(float64(hll.m), 2.0) / sum
	emptyRegs := hll.emptyCount()
	if estimation <= 2.5*float64(hll.m) { // do small range correction
		if emptyRegs > 0 {
			estimation = float64(hll.m) * math.Log(float64(hll.m)/float64(emptyRegs))
		}
	} else if estimation > 1/30.0*math.Pow(2.0, 32.0) { // do large range correction
		estimation = -math.Pow(2.0, 32.0) * math.Log(1.0-estimation/math.Pow(2.0, 32.0))
	}
	return estimation
}

func (hll *HyperLogLog) emptyCount() int {
	sum := 0
	for _, val := range hll.reg {
		if val == 0 {
			sum++
		}
	}
	return sum
}

func NewHLL(p uint8) *HyperLogLog {
	if p < HLL_MIN_PRECISION || p > HLL_MAX_PRECISION {
		return nil
	}

	m := uint64(math.Exp2(float64(p)))
	reg := make([]uint8, m)

	return &HyperLogLog{
		p:   p,
		m:   m,
		reg: reg,
	}
}

func (hll *HyperLogLog) Add(key string) {
	bin := hash.ToBinary(key)

	bucket := toInt(bin[:hll.p])
	value := uint8(trailingZeros(bin))

	if value > hll.reg[bucket] {
		hll.reg[bucket] = value
	}
}

func (hll *HyperLogLog) Serialize() []byte {
	retArray := make([]byte, 1)

	retArray[0] = hll.p
	retArray = append(retArray, hll.reg...)

	return retArray
}

func Deserialize(byteArray []byte) *HyperLogLog {
	p := byteArray[0]
	m := uint64(math.Exp2(float64(p)))

	reg := make([]byte, m)
	copy(reg, byteArray[1:])

	return &HyperLogLog{
		p:   p,
		m:   m,
		reg: reg,
	}
}
