#pragma once
#include <iostream>
#include "Sudoku9.h"
#include "FileHandler.h"
#include "CoreFunctions.h"

using namespace std;

int main(int argc, char *argv[]) {
     Sudoku9 sudoku, sudoku1;

     int choice;
     bool playgame = true;
     while (playgame) {
         do {
             CoreFunctions::menu();

             cout << "Enter your choice (1-3): ";
             cin >> choice;


             switch (choice) {
             case 1:
                 cout << "Loading Sudoku from file..." << endl;
                 cout << "Checking if matrix is valid..." << endl;
                 if (FileHandler::loadSudoku(argv[1], sudoku)) {
                     if (CoreFunctions::isMatrixValid(sudoku)) {
                         cout << "Matrix is valid!" << endl;
                     }
                     else
                     {
                         cout << "Matrix is invalid!" << endl;
                         break;
                     }

                     //Setting counters
                     sudoku.setCorrectInputs(0);
                     sudoku.setIncorrectInputs(0);
                     sudoku.setCurrentGameCounter(sudoku.getCurrentGameCounter() + 1);

                     if (sudoku.isFull()) {
                         cout << "File already has a solution." << endl;
                         sudoku.printBoard();
                     }

                     sudoku.printBoard();

                     CoreFunctions::solutionMenu();

                     cin >> choice;
                     bool solved;

                     switch (choice) {
                     case 1:
                         if (FileHandler::loadSudoku(argv[3], sudoku1)) {
                             int incorrectMoves, correctMoves;
                             bool isSolutionValid = CoreFunctions::checkSolutionFromFile(sudoku, sudoku1, incorrectMoves, correctMoves);

                             if (isSolutionValid) {
                                 cout << "The solution is valid." << endl;
                                 cout << "Correct moves: " << correctMoves << endl;
                                 cout << "Incorrect moves: " << incorrectMoves << endl;
                             }
                             else {
                                 cout << "The solution is not valid." << endl;
                             }
                         }
                         else {
                             cout << "Error loading a file" << endl;
                         }

                         break;
                     case 2:
                         solved = CoreFunctions::solve(sudoku);
                         if (solved) {
                             cout << "Sudoku solved successfully!" << endl;
                             sudoku.printBoard();
                         }
                         else {
                             cout << "Failed to solve Sudoku." << endl;
                         }
                         break;
                     default:
                         cout << "Invalid choice: " << choice << endl;
                         break;
                     }




                 }

                 break;
             case 2:
                 // Load Sudoku randomly
                 cout << "Loading Sudoku randomly..." << endl;
                 Sudoku9::generateSudoku(sudoku);
                 if (CoreFunctions::isMatrixValid(sudoku)) {
                     cout << "Matrix is valid!" << endl;
                 }
                 else
                 {
                     cout << "Matrix is invalid!" << endl;
                     break;
                 }

                 FileHandler::saveSudoku(argv[2], sudoku);

                 //Setting counters
                 sudoku.setCorrectInputs(0);
                 sudoku.setIncorrectInputs(0);
                 sudoku.setCurrentGameCounter(sudoku.getCurrentGameCounter() + 1);

                 sudoku.printBoard();

                 CoreFunctions::solutionMenu();
                 
                 cin >> choice;
                 bool solved;

                 switch (choice) {
                 case 1:
                     if (FileHandler::loadSudoku(argv[1], sudoku1)) {
                         int incorrectMoves, correctMoves;
                         bool isSolutionValid = CoreFunctions::checkSolutionFromFile(sudoku, sudoku1, incorrectMoves, correctMoves);

                         if (isSolutionValid) {
                             sudoku1.setCorrectInputs(correctMoves);
                             sudoku1.setIncorrectInputs(incorrectMoves);
                             sudoku1.setCurrentGameCounter(sudoku1.getCurrentGameCounter() + 1);
                             cout << "The solution is valid." << endl;
                             sudoku1.printBoard();
                         }
                         else {
                             cout << "The solution is not valid." << endl;
                         }
                     }
                     else {
                         cout << "Error loading a file" << endl;
                     }

                     break;
                 case 2:
                     solved = CoreFunctions::solve(sudoku);
                     if (solved) {
                         cout << "Sudoku solved successfully!" << endl;
                         sudoku.printBoard();
                     }
                     else {
                         cout << "Failed to solve Sudoku." << endl;
                     }
                     break;
                 default:
                     cout << "Invalid choice: " << choice << endl;
                     break;
                 }

                 break;
             case 3:
                 cout << "Exiting the program." << endl;
                 break;
             default:
                 cout << "Invalid choice, please try again." << endl;
                 break;
             }

         } while (choice != 1 && choice != 2 && choice != 3);

         while (true) {
             cout << "Do you want to play another game? (1 for Yes, 2 for No): ";
             cin >> choice;

             if (choice == 1 || choice == 2) {
                 break;
             }
             else {
                 cout << "Invalid input. Please enter 1 for Yes or 2 for No." << endl;
             }
         }

         if (choice == 2) {
             playgame = false;
         }
     }

     return 0;

}
