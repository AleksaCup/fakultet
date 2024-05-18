#include "CoreFunctions.h"
#include <vector>
#include <random>
#include <cstdlib>


void CoreFunctions::clearConsole() {
#ifdef _WIN32
    // Windows
    system("cls");
#else
    // Unix/Linux/macOS
    system("clear");
#endif
}

bool CoreFunctions::isValidMove(Sudoku9& sudoku, int row, int col, int value, bool solution) {
    if (solution) {
        if (row < 0 || row >8 || col < 0 || col >8 || value == 0) {
            return false;
        }
    }
    else
    {
        if (row < 0 || row >8 || col < 0 || col >8 || value == 0 || sudoku.getMatrixElement(row, col) != 0) {
            return false;
        }
    }


    for (int i = 0; i < 9; i++) {
        if ((i != col && sudoku.getMatrixElement(row, i) == value) ||
            (i != row && sudoku.getMatrixElement(i, col) == value)) {
            return false;
        }
    }

    int subgridStartRow = 3 * (row / 3);
    int subgridStartCol = 3 * (col / 3);
    for (int i = 0; i < 3; ++i) {
        for (int j = 0; j < 3; ++j) {
            int currentRow = subgridStartRow + i;
            int currentCol = subgridStartCol + j;
            if (currentRow == row && currentCol == col) {
                continue;
            }
            if (sudoku.getMatrixElement(currentRow, currentCol) == value) {
                return false;
            }
        }
    }

    return true;
}


bool CoreFunctions::isMatrixValid(Sudoku9& sudoku) {
    for (int row = 0; row < 9; ++row) {
        for (int col = 0; col < 9; ++col) {
            int el = sudoku.getMatrixElement(row, col);
            if (el != 0) {
                sudoku.setMatrixElement(row, col, 0);
                bool valid = CoreFunctions::isValidMove(sudoku, row, col, el, false);
                sudoku.setMatrixElement(row, col, el);

                if (!valid) {
                    return false;
                }
            }
        }
    }
    return true;
}



bool CoreFunctions::isSudokuComplete(Sudoku9& sudoku) {
    for (int row = 0; row < 9; ++row) {
        for (int col = 0; col < 9; ++col) {
            if (sudoku.getMatrixElement(row, col) == 0) {
                return false;
            }
        }
    }
    return true;
}

void CoreFunctions::menu() {
    cout << "SUDOKU" << endl;
    cout << "----------------------------------------------" << endl;
    cout << "1. Load Sudoku from file." << endl;
    cout << "2. Load Sudoku by randomly." << endl;
    cout << "3. Exit." << endl;

}

void CoreFunctions::solutionMenu() {
    cout << "1. Solve from file." << endl;
    cout << "2. Solve it automatically." << endl;
    cout << "Enter your choice (1-2): ";
}

bool CoreFunctions::solve(Sudoku9& sudoku) {
    if (!CoreFunctions::isMatrixValid(sudoku)) {
        return false;
    }

    int correct, incorrect;
    for (int row = 0; row < 9; row++) {
        for (int col = 0; col < 9; col++) {
            if (sudoku.getMatrixElement(row, col) == 0) { 
                for (int num = 1; num <= 9; num++) {  
                    if (CoreFunctions::isValidMove(sudoku, row, col, num, false)) {
                        sudoku.setMatrixElement(row, col, num);
                        correct = sudoku.getCorrectInputs();
                        sudoku.setCorrectInputs(correct + 1);

                        if (solve(sudoku)) {
                            return true;
                        }

                        sudoku.setMatrixElement(row, col, 0);  // Backtrack
                        incorrect = sudoku.getIncorrectInputs();
                        sudoku.setIncorrectInputs(incorrect + 1);
                    }
                }
                return false;  //  backtracking
            }
        }
    }
    return true;
}

bool CoreFunctions::fillSudoku(Sudoku9& sudoku) {
    for (int row = 0; row < 9; ++row) {
        for (int col = 0; col < 9; ++col) {
            if (sudoku.getMatrixElement(row, col) == 0) {
                std::vector<int> numbers = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
                std::random_device rd;
                std::mt19937 g(rd());
                std::shuffle(numbers.begin(), numbers.end(), g);

                for (int num : numbers) {
                    if (CoreFunctions::isValidMove(sudoku, row, col, num, false)) {
                        sudoku.setMatrixElement(row, col, num);
                        if (CoreFunctions::fillSudoku(sudoku)) {
                            return true;
                        }
                        sudoku.setMatrixElement(row, col, 0); // Backtrack
                    }
                }
                return false;
            }
        }
    }
    return true;
}

void CoreFunctions::removeRandomElements(Sudoku9& sudoku) {
    srand(static_cast<unsigned int>(time(nullptr)));
    // Seed the random number generator

    for (int row = 0; row < 9; ++row) {
        vector<int> indices = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
        random_shuffle(indices.begin(), indices.end());

        int elementsToRemove = rand() % 2 + 5; // will be 5 or 6

        for (int i = 0; i < elementsToRemove; ++i) {
            sudoku.setMatrixElement(row, indices[i], 0);
        }
    }
}


bool CoreFunctions::checkSolutionFromFile(Sudoku9& startingSudoku, Sudoku9& solutionSudoku, int& mistakes, int& validMoves) {
    mistakes = 0;
    validMoves = 0;

    if (!solutionSudoku.isFull()) {
        return false;
    }

    for (int row = 0; row < 9; ++row) {
        for (int col = 0; col < 9; ++col) {
            int startingValue = startingSudoku.getMatrixElement(row, col);
            int solutionValue = solutionSudoku.getMatrixElement(row, col);

            if (startingValue != 0 && startingValue != solutionValue) {
                mistakes = 0;
                validMoves = 0;
                return false;
            }

            if (isValidMove(solutionSudoku, row, col, solutionValue, true)) {
                validMoves++;
            }
            else {
                mistakes++;
            }
        }
    }

    return true;
}


