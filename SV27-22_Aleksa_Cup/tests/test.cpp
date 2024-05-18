#include "pch.h"
#include "../Sudoku/Sudoku9.cpp"
#include "../Sudoku/CoreFunctions.cpp"
#include "../Sudoku/FileHandler.cpp"
#include <iostream>

using namespace std;

namespace googletests {

TEST(Sudoku9Test, MatrixInit) {
    Sudoku9 sudoku;
    bool allzeros = true;
    for (int i = 0; i < 9; ++i) {
        for (int j = 0; j < 9; ++j) {
            if (sudoku.getMatrixElement(i, j) != 0) {
                allzeros = false;
                break;
            }
        }
    }
    EXPECT_TRUE(allzeros);
    EXPECT_EQ(sudoku.getCorrectInputs(), 0);
    EXPECT_EQ(sudoku.getIncorrectInputs(), 0);
    EXPECT_EQ(sudoku.getCurrentGameCounter(), 0);
}

TEST(Sudoku9Test, SetAndGetMatrixElement) {
    Sudoku9 sudoku;
    sudoku.setMatrixElement(0, 0, 5);
    EXPECT_EQ(sudoku.getMatrixElement(0, 0), 5);
}

TEST(Sudoku9Test, SetAndGetCorrectInputs) {
    Sudoku9 sudoku;
    sudoku.setCorrectInputs(23);
    EXPECT_EQ(sudoku.getCorrectInputs(), 23);
}

TEST(Sudoku9Test, SetAndGetIncorrectInputs) {
    Sudoku9 sudoku;
    sudoku.setIncorrectInputs(52); 
    EXPECT_EQ(sudoku.getIncorrectInputs(), 52);
}

TEST(Sudoku9Test, SetAndGetCurrentGameCounter) {
    Sudoku9 sudoku;
    sudoku.setCurrentGameCounter(2);
    EXPECT_EQ(sudoku.getCurrentGameCounter(), 2);
}

TEST(Sudoku9Test, IsFull) {
    Sudoku9 sudoku;
    EXPECT_FALSE(sudoku.isFull());

    for (int i = 0; i < 9; ++i) {
        for (int j = 0; j < 9; ++j) {
            sudoku.setMatrixElement(i, j, 1);
        }
    }
    EXPECT_TRUE(sudoku.isFull());
}

TEST(Sudoku9Test, GetMatrix) {
    Sudoku9 sudoku;
    int** matrix = sudoku.getMatrix();

    ASSERT_NE(matrix, nullptr);
    for (int i = 0; i < 9; ++i) {
        ASSERT_NE(matrix[i], nullptr);
        for (int j = 0; j < 9; ++j) {
            EXPECT_EQ(matrix[i][j], 0);
        }
    }
}

TEST(Sudoku9Test, SetMatrix) {
    Sudoku9 sudoku;
    int** newMatrix = new int* [9];
    for (int i = 0; i < 9; ++i) {
        newMatrix[i] = new int[9];
        for (int j = 0; j < 9; ++j) {
            newMatrix[i][j] = i + j;
        }
    }

    sudoku.setMatrix(newMatrix);

    int** matrix = sudoku.getMatrix();
    for (int i = 0; i < 9; ++i) {
        for (int j = 0; j < 9; ++j) {
            EXPECT_EQ(matrix[i][j], i + j);
        }
        delete[] newMatrix[i];
    }
    delete[] newMatrix;
}

TEST(Sudoku9Test, AssignmentOperator) {
    Sudoku9 sudoku1;
    sudoku1.setMatrixElement(0, 0, 1);
    sudoku1.setMatrixElement(1, 5, 2);
    sudoku1.setMatrixElement(8, 2, 7);

    Sudoku9 sudoku2;
    sudoku2 = sudoku1;

    for (int i = 0; i < 9; ++i) {
        for (int j = 0; j < 9; ++j) {
            EXPECT_EQ(sudoku1.getMatrixElement(i, j), sudoku2.getMatrixElement(i, j));
        }
    }
}

// Test for a valid move
TEST(CoreFunctionsTest, IsValidMove_Valid) {
    Sudoku9 sudoku;
    bool solution = false;
    EXPECT_TRUE(CoreFunctions::isValidMove(sudoku, 0, 0, 1, solution));
}

// Test for invalid move due to row conflict
TEST(CoreFunctionsTest, IsValidMove_InvalidRow) {
    Sudoku9 sudoku;
    sudoku.setMatrixElement(0, 0, 1);
    bool solution = false;
    EXPECT_FALSE(CoreFunctions::isValidMove(sudoku, 0, 1, 1, solution));
}

// Test for invalid move due to column conflict
TEST(CoreFunctionsTest, IsValidMove_InvalidColumn) {
    Sudoku9 sudoku;
    sudoku.setMatrixElement(0, 0, 1);
    bool solution = false;
    EXPECT_FALSE(CoreFunctions::isValidMove(sudoku, 1, 0, 1, solution));
}

// Test for invalid move due to subgrid conflict
TEST(CoreFunctionsTest, IsValidMove_InvalidSubgrid) {
    Sudoku9 sudoku;
    sudoku.setMatrixElement(0, 0, 1);
    bool solution = false;
    EXPECT_FALSE(CoreFunctions::isValidMove(sudoku, 1, 1, 1, solution));
}

// Test for invalid move with out of range parameters
TEST(CoreFunctionsTest, IsValidMove_OutOfRange) {
    Sudoku9 sudoku;
    bool solution = false;
    EXPECT_FALSE(CoreFunctions::isValidMove(sudoku, -1, 0, 1, solution));
    EXPECT_FALSE(CoreFunctions::isValidMove(sudoku, 0, -1, 1, solution));
    EXPECT_FALSE(CoreFunctions::isValidMove(sudoku, 0, 0, 0, solution));
}


TEST(CoreFunctionsTest, IsMatrixValid_Valid) {
    Sudoku9 valid_sudoku;
    bool valid = FileHandler::loadSudoku("test_validmatrix.txt", valid_sudoku);
    EXPECT_TRUE(CoreFunctions::isMatrixValid(valid_sudoku));
}

// Test for isMatrixValid with an invalid Sudoku grid
TEST(CoreFunctionsTest, IsMatrixValid_Invalid) {
    Sudoku9 invalid_sudoku;
    invalid_sudoku.setMatrixElement(8, 8, 1);
    invalid_sudoku.setMatrixElement(6, 6, 1);
    /*invalid_sudoku.setMatrixElement(1, 8, 1);
    invalid_sudoku.setMatrixElement(8, 1, 1);*/


    EXPECT_FALSE(CoreFunctions::isMatrixValid(invalid_sudoku));
}

// Test for isSudokuComplete with a complete grid
TEST(CoreFunctionsTest, IsSudokuComplete_Complete) {
    Sudoku9 sudoku;
    CoreFunctions::fillSudoku(sudoku);
    EXPECT_TRUE(CoreFunctions::isSudokuComplete(sudoku));
}

 //Test for isSudokuComplete with a incomplete grid
TEST(CoreFunctionsTest, IsSudokuComplete_Incomplete) {
    Sudoku9 sudoku;
    CoreFunctions::fillSudoku(sudoku);
    sudoku.setMatrixElement(5, 2, 0);
    EXPECT_FALSE(CoreFunctions::isSudokuComplete(sudoku));
}

TEST(CoreFunctionsTest, SolveEmptyMatrix) {
    Sudoku9 sudoku;
    EXPECT_TRUE(CoreFunctions::solve(sudoku));
    EXPECT_TRUE(CoreFunctions::isMatrixValid(sudoku));
    EXPECT_TRUE(CoreFunctions::isSudokuComplete(sudoku));
}

// Test with a randomly generated Sudoku grid
TEST(CoreFunctionsTest, SolveRandomGeneratedMatrix) {
    Sudoku9 sudoku;
    Sudoku9::generateSudoku(sudoku);
    EXPECT_TRUE(CoreFunctions::solve(sudoku));
    EXPECT_TRUE(CoreFunctions::isMatrixValid(sudoku));
}

// Test with a full Sudoku grid
TEST(CoreFunctionsTest, SolveFullMatrix) {
    Sudoku9 sudoku;
    bool valid = FileHandler::loadSudoku("test_validmatrix.txt", sudoku);
    EXPECT_TRUE(CoreFunctions::solve(sudoku));
}

TEST(CoreFunctionsTest, SolveInvalidMatrix) {
    Sudoku9 sudoku;
    sudoku.setMatrixElement(0, 0, 1);
    sudoku.setMatrixElement(0, 1, 1);
    EXPECT_FALSE(CoreFunctions::solve(sudoku));
}

// Test for fillSudoku
TEST(CoreFunctionsTest, FillSudokuEmpty) {
    Sudoku9 sudoku;
    bool filled = CoreFunctions::fillSudoku(sudoku);

    // Verify that the function reports successful filling
    EXPECT_TRUE(filled);
    EXPECT_TRUE(sudoku.isFull());


    bool isValid = true;
    for (int row = 0; row < 9; ++row) {
        for (int col = 0; col < 9; ++col) {
            int num = sudoku.getMatrixElement(row, col);

            if (!CoreFunctions::isValidMove(sudoku, row, col, num, true)) {
                isValid = false;
            }
        }
    }

    EXPECT_TRUE(isValid);
}

TEST(CoreFunctionsTest, FillSudokuWithStartFields) {
    Sudoku9 sudoku;
    FileHandler::loadSudoku("in.txt", sudoku);
    bool filled = CoreFunctions::fillSudoku(sudoku);

    EXPECT_TRUE(filled);
    EXPECT_TRUE(sudoku.isFull());


    bool isValid = true;
    for (int row = 0; row < 9; ++row) {
        for (int col = 0; col < 9; ++col) {
            int num = sudoku.getMatrixElement(row, col);

            if (!CoreFunctions::isValidMove(sudoku, row, col, num, true)) {
                isValid = false;
            }
        }
    }

    EXPECT_TRUE(isValid);
}


TEST(CoreFunctionsTest, FillSudokuFull) {
    Sudoku9 sudoku;
    CoreFunctions::solve(sudoku);
    EXPECT_TRUE(sudoku.isFull());
    bool filled = CoreFunctions::fillSudoku(sudoku);

    EXPECT_TRUE(filled);
    EXPECT_TRUE(sudoku.isFull());

}









// Test for saveSudoku
TEST(FileHandlerTest, SaveSudokuEmpty) {
    Sudoku9 sudoku;
    const string filename = "test_out.txt";
    FileHandler::saveSudoku(filename, sudoku);

    std::ifstream file(filename);
    ASSERT_TRUE(file.good());

    int value, row = 0, col;
    while (file >> value) {
        col = row % 9;
        EXPECT_EQ(value, sudoku.getMatrixElement(row / 9, col));
        row++;
    }
    file.close();
}

TEST(FileHandlerTest, SaveSudokuFull) {
    Sudoku9 sudoku;
    const string filename = "test_out.txt";

    CoreFunctions::solve(sudoku);
    FileHandler::saveSudoku(filename, sudoku);

    std::ifstream file(filename);
    ASSERT_TRUE(file.good());

    int value, row = 0, col;
    while (file >> value) {
        col = row % 9;
        EXPECT_EQ(value, sudoku.getMatrixElement(row / 9, col));
        row++;
    }
    file.close();
}

// Test for loadSudoku
TEST(FileHandlerTest, LoadSudoku) {
    Sudoku9 valid_sudoku;
    bool valid = FileHandler::loadSudoku("test_out.txt", valid_sudoku);
    EXPECT_TRUE(valid);
}

// Test for loadSudoku with non-existent file
TEST(FileHandlerTest, LoadSudoku_NonExistentFile) {
    Sudoku9 sudoku;
    EXPECT_FALSE(FileHandler::loadSudoku("non_existent_file.txt", sudoku));
}


int main(int argc, char** argv) {
    ::testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}


}