package merkle

import (
	"crypto/sha1"
	// "fmt"
	"encoding/hex"
	"os"
)

type MerkleTree struct {
	root *MerkleNode
}

type MerkleNode struct {
	left  *MerkleNode
	right *MerkleNode
	data  [20]byte
}

func (n *MerkleNode) String() string {
	return hex.EncodeToString(n.data[:])
}

func (t *MerkleTree) Root() *MerkleNode {
	return t.root
}

func NewMerkleTree(dataBlocks [][]byte) *MerkleTree {
	if len(dataBlocks) == 0 {
		return &MerkleTree{}
	}

	leafNodes := make([]*MerkleNode, len(dataBlocks))
	for i, data := range dataBlocks {
		leafNodes[i] = &MerkleNode{data: Hash(data)}
	}

	// build merkle tree
	root := buildMerkleTree(leafNodes)

	return &MerkleTree{root: root}
}

func buildMerkleTree(nodes []*MerkleNode) *MerkleNode {
	// only root node
	if len(nodes) == 1 {
		return nodes[0]
	}

	newLevel := make([]*MerkleNode, 0, (len(nodes)+1)/2)
	for i := 0; i < len(nodes); i += 2 {
		var left, right *MerkleNode
		left = nodes[i]

		if i+1 < len(nodes) {
			right = nodes[i+1]
		} else {
			right = &MerkleNode{data: Hash([]byte{})}
		}

		parent := &MerkleNode{
			left:  left,
			right: right,
			data:  Hash(append(left.data[:], right.data[:]...)),
		}

		newLevel = append(newLevel, parent)
	}

	return buildMerkleTree(newLevel)
}

func Hash(data []byte) [20]byte {
	return sha1.Sum(data)
}

func (t *MerkleTree) Serialize(filename string) error {
	file, err := os.Create(filename)
	if err != nil {
		return err
	}
	defer file.Close()

	var queue []*MerkleNode
	queue = append(queue, t.root)

	for len(queue) > 0 {
		node := queue[0]
		queue = queue[1:]

		if node != nil {
			_, err := file.WriteString(hex.EncodeToString(node.data[:]) + "\n")
			if err != nil {
				return err
			}
			queue = append(queue, node.left, node.right)
		}
	}

	return nil
}
