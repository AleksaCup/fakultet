#pragma once
#include <iostream>
#include <stdexcept>
using namespace std;


class Sudoku9 {
private:
    int** matrix;
    int correctInputs;
    int incorrectInputs;
    int currentGameCounter;

public:
    // Constructor
    Sudoku9();

    // Destructor
    ~Sudoku9();

    int** getMatrix() const;

    void setMatrix(int** matrix);

    // Getter for correctInputs
    int getCorrectInputs() const;

    // Setter for correctInputs
    void setCorrectInputs(int newCorrectInputs);

    // Getter for correctInputs
    int getIncorrectInputs() const;

    // Setter for correctInputs
    void setIncorrectInputs(int newIncorrectInputs);

    // Getter for currentGameCounter
    int getCurrentGameCounter() const;

    // Setter for currentGameCounter
    void setCurrentGameCounter(int newCurrentGameCounter);

    //Returns value from matrix
    int getMatrixElement(int row, int col);

    void setMatrixElement(int x, int y, int value);

    //Print Sudoku board
    void printBoard();

    bool isFull();

    Sudoku9& operator=(const Sudoku9& other);

    static void generateSudoku(Sudoku9& sudoku);



};
