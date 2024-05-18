#pragma once
#include <iostream>
#include <string>
#include "Sudoku9.h"

using namespace std;

class FileHandler
{
private:
    Sudoku9* sudoku;
public:
    // Loads a Sudoku from a file
    static bool loadSudoku(const string& filename, Sudoku9& sudoku);

    // Saves a Sudoku to a file
    static void saveSudoku(const string& filename, Sudoku9& sudoku);


};

