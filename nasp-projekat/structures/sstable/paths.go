package sstable

import (
	"fmt"
	"os"
	"path/filepath"
)

func CountSubdirs(path string) (int, error) {
	contents, err := os.ReadDir(path)
	if err != nil {
		return 0, err
	}
	subfolderCount := 0
	for _, entry := range contents {
		if entry.IsDir() {
			subfolderCount++
		}
	}
	return subfolderCount, nil
}

func CountFilesInFolder(folderPath string) int {
	files, err := os.ReadDir(folderPath)
	if err != nil {
		return 0
	}

	count := 0
	for range files {
		count++
	}

	return count
}

// ========================================
func FindOrCreateEmptySSTableFolder() string {
	baseDir := "data"

	// List all entries in the base directory
	entries, err := os.ReadDir(baseDir)
	if err != nil {
		fmt.Printf("Error reading directory %s: %v\n", baseDir, err)
		return ""
	}

	// Iterate through each entry to find an empty directory
	for _, entry := range entries {
		if entry.IsDir() {
			dirPath := filepath.Join(baseDir, entry.Name())
			dirPath += "/"
			if isDirEmpty(dirPath) {
				return dirPath
			}
		}
	}
	return CreateNextSSTableFolder(baseDir)
}

func isDirEmpty(dirPath string) bool {
	files, err := os.ReadDir(dirPath)
	if err != nil {
		fmt.Printf("Error reading directory %s: %v\n", dirPath, err)
		return false
	}
	return len(files) == 0
}

func CreateNextSSTableFolder(baseDir string) string {
	contents, err := os.ReadDir(baseDir)
	if err != nil {
		fmt.Printf("Error reading directory %s: %v\n", baseDir, err)
		return ""
	}

	subfolderCount := 0
	for _, entry := range contents {
		if entry.IsDir() {
			subfolderCount++
		}
	}

	newSubfolderName := fmt.Sprintf("sstable%d", subfolderCount+1)
	newDirPath := filepath.Join(baseDir, newSubfolderName)
	newDirPath += "/"
	if err := os.Mkdir(newDirPath, 0777); err != nil {
		fmt.Printf("Error creating directory %s: %v\n", newDirPath, err)
		return ""
	}
	return newDirPath
}
