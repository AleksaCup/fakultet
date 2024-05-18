#pragma once
#include "FileHandler.h"

#include <fstream>
#include <sstream>
#include <iostream>

using namespace std;

bool FileHandler::loadSudoku(const string& filename, Sudoku9& sudoku) {
    int** matrix = sudoku.getMatrix();

	ifstream file(filename);

    if (!file) {
        cerr << "Error opening file." << endl;
        return false;
    }

    string line;
    int row = 0;
    
    while (getline(file, line) && row < 9) {
        istringstream iss(line);
        int num;
        int col = 0;

        while (iss >> num && col < 9) {
            matrix[row][col] = num;
            col++;
        }

        row++;
        
    }

    file.close();


    return true;
}

void FileHandler::saveSudoku(const string& filename, Sudoku9& sudoku) {
    int** matrix = sudoku.getMatrix();

    ofstream file(filename);

    if (!file) {
        cerr << "Error opening file." << endl;
        return;
    }

    for (int row = 0; row < 9; ++row) {
        for (int col = 0; col < 9; ++col) {
            if (col != 8) {
                file << matrix[row][col] << " ";
            }
            else {
                file << matrix[row][col];
            }
            
            if (col == 8 && row !=8) {
                file << "\n";
            }
        }
    }

    file.close();

}
