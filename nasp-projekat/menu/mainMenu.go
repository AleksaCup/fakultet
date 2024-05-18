package menu

import (
	"fmt"
	readPath "nasp-projekat/structures/ReadPath"
	"nasp-projekat/structures/configurator"
	writePath "nasp-projekat/structures/writePath"
)

func App(config configurator.Configuration, read *readPath.ReadPath, write *writePath.WritePath) {

	fmt.Println("-----MENU-----")
	for {
		fmt.Println("1. PUT")
		fmt.Println("2. GET")
		fmt.Println("3. DELETE")
		fmt.Println("4. EXIT")
		fmt.Println("Choose your option:")
		var answer, key, value string
		_, _ = fmt.Scanln(&answer)

		//...PUT
		if answer == "1" {
			fmt.Printf("Key: ")
			_, _ = fmt.Scanln(&key)
			fmt.Printf("Value: ")
			_, _ = fmt.Scanln(&value)
			write.Write(key, []byte(value), false)
			//...GET
		} else if answer == "2" {
			fmt.Printf("Key: ")
			_, _ = fmt.Scanln(&key)
			found := read.Read(key)
			if found == nil {
				fmt.Println("Element not found")
			} else {
				var deleted string
				if found.Tombstone {
					deleted += "deleted"
				} else {
					deleted += "not deleted"
				}
				fmt.Println("key: " + found.Key + " value: " + string(found.Value) + " tombstone: " + deleted)
			}
			//...DELETE
		} else if answer == "3" {
			fmt.Printf("Key: ")
			_, _ = fmt.Scanln(&key)
			done := write.Delete(key)
			if done {
				fmt.Println("SUCCESSFUL DELETION")
			} else {
				fmt.Println("DELETION FAILED")
			}
			// ...EXIT
		} else if answer == "4" {
			fmt.Println("Sayonara")
			break
		} else {
			fmt.Println("Wrong input")
			continue
		}

	}

	// found := read.Read("aleksa")
	// var deleted string
	// if found.Tombstone {
	// 	deleted += "deleted"
	// } else {
	// 	deleted += "not deleted"
	// }
	// fmt.Println("key: " + found.Key + " value: " + string(found.Value) + " tombstone: " + deleted)
	// write.Write("zdravkoo1", []byte("value"), false)
	// write.Write("zdravkoo2", []byte("value"), false)
	// write.Write("zdravkoo3", []byte("value"), false)
	// write.Write("zdravkoo4", []byte("value"), false)
	// write.Write("zdravkoo5", []byte("value"), false)
	// write.Write("zdravkoo6", []byte("value"), false)
	// write.Write("zdravkoo7", []byte("value"), false)
}
