#pragma once
#include "Sudoku9.h"
#include <vector>
class CoreFunctions
{
private:
    Sudoku9* sudoku;
public:
    static bool isValidMove(Sudoku9& sudoku, int row, int col, int value, bool solution);

    static bool isMatrixValid(Sudoku9& sudoku);

    static bool solve(Sudoku9& sudoku);

    static bool isSudokuComplete(Sudoku9& sudoku);

    static bool fillSudoku(Sudoku9& sudoku);

    static void removeRandomElements(Sudoku9& sudoku);

    static bool checkSolutionFromFile(Sudoku9& startingSudoku, Sudoku9& solutionSudoku, int& mistakes, int& validMoves);

    static void clearConsole();

    static void menu();

    static void solutionMenu();


};

