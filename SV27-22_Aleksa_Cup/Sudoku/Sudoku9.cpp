#pragma once
#include "Sudoku9.h"
#include "CoreFunctions.h"


Sudoku9::Sudoku9() : correctInputs(0), currentGameCounter(0), incorrectInputs(0){
    matrix = new int* [9];
    for (int i = 0; i < 9; ++i) {
        matrix[i] = new int[9];
        for (int j = 0; j < 9; ++j) {
            matrix[i][j] = 0;
        }
    }
}

Sudoku9::~Sudoku9() {
    for (int i = 0; i < 9; ++i) {
        delete[] matrix[i];
    }
    delete[] matrix;
}


int** Sudoku9::getMatrix() const {
    return matrix;
}

void Sudoku9::setMatrix(int** newMatrix) {
    // Deallocate existing matrix
    for (int i = 0; i < 9; ++i) {
        delete[] matrix[i];
    }
    delete[] matrix;

    // Allocate memory for the new matrix
    matrix = new int* [9];
    for (int i = 0; i < 9; ++i) {
        matrix[i] = new int[9];

        // Copy elements from the new matrix
        for (int j = 0; j < 9; ++j) {
            matrix[i][j] = newMatrix[i][j];
        }
    }
}



int Sudoku9::getCorrectInputs() const {
    return correctInputs;
}

// Setter for correctInputs
void Sudoku9::setCorrectInputs(int newCorrectInputs) {
    this->correctInputs = newCorrectInputs;
}

int Sudoku9::getIncorrectInputs() const {
    return incorrectInputs;
}

// Setter for correctInputs
void Sudoku9::setIncorrectInputs(int newIncorrectInputs) {
    this->incorrectInputs = newIncorrectInputs;
}

// Getter for currentGameCounter
int Sudoku9::getCurrentGameCounter() const {
    return currentGameCounter;
}

// Setter for currentGameCounter
void Sudoku9::setCurrentGameCounter(int newCurrentGameCounter) {
    this->currentGameCounter = newCurrentGameCounter;
}

int Sudoku9::getMatrixElement(int row, int col) {
    return matrix[row][col];

}

void Sudoku9::setMatrixElement(int x, int y, int value) {
    int** matrix = this->matrix;
    matrix[x][y] = value;
}

bool Sudoku9::isFull() {
    for (int i = 0; i < 9; ++i) {
        for (int j = 0; j < 9; ++j) {
            if (matrix[i][j] == 0) {
                return false;
            }
        }
    }
    return true;
}


Sudoku9& Sudoku9::operator=(const Sudoku9& other) {
    if (this != &other) {
        for (int i = 0; i < 9; ++i) {
            delete[] matrix[i];
        }
        delete[] matrix;

        matrix = new int* [9];
        for (int i = 0; i < 9; ++i) {
            matrix[i] = new int[9];
        }

        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                matrix[i][j] = other.matrix[i][j];
            }
        }

        correctInputs = other.correctInputs;
        currentGameCounter = other.currentGameCounter;
    }

    return *this;
}



void Sudoku9::printBoard() {
    const std::string horizontalBorder = "   +-----------+-----------+-----------+";
    const std::string horizontalSubBorder = "   |-----------|-----------|-----------|";

    // Print column headers
    cout << "     ";
    for (int col = 0; col < 9; ++col) {
        cout << col << "   ";
    }
    cout << endl;

    // Print Sudoku grid with row numbers
    for (int i = 0; i < 9; i++) {
        if (i % 3 == 0) {
            cout << horizontalBorder << endl;
        }
        else {
            cout << horizontalSubBorder << endl;
        }

        cout << i << "  ";  // Display row number (0 to 8)

        for (int j = 0; j < 9; j++) {
            cout << "| ";

            int cellValue = getMatrixElement(i, j);
            if (cellValue != 0) {
                cout << cellValue << " ";
            }
            else {
                cout << "  ";  // Empty cells
            }
        }
        cout << "|" << endl;  // End of row
    }

    cout << horizontalBorder << endl;  // Bottom border
    cout << endl;
    cout << "Correct inputs counter: " << getCorrectInputs() << endl;
    cout << "Incorrect inputs counter: " << getIncorrectInputs() << endl;
    cout << "Game number counter: " << getCurrentGameCounter() << endl << endl;

}



void Sudoku9::generateSudoku(Sudoku9& sudoku) {
    CoreFunctions::fillSudoku(sudoku);
    CoreFunctions::removeRandomElements(sudoku);
}