# Sudoku Game (Java Swing)

## Overview

A desktop Sudoku game built using Java Swing.
The application allows users to play Sudoku with real-time validation, limited mistakes, and utility features like reset, solve, and timer.

## Features

* 9×9 interactive Sudoku grid
* Real-time input validation
* Incorrect inputs highlighted
* Maximum 3 mistakes → game over
* Reset (clears user input, same puzzle)
* New Game (loads a new puzzle)
* Solve option (auto-completes puzzle)
* Timer to track gameplay
* Save/Load functionality


## Tech Stack

* Java (JDK 17/21)
* Java Swing (GUI)
* Git & GitHub


## Project Structure

Main.java              → Entry point
SudokuBoard.java      → Stores board data
PuzzleRepository.java → Provides puzzles

SudokuValidator.java  → Validates moves
SudokuSolver.java     → Solves puzzle

GameController.java   → Handles game logic
GameState.java        → Game states
GameTimer.java        → Timer

SudokuUI.java         → Main window
GridPanel.java        → Grid layout
CellField.java        → Individual cell
ControlPanel.java     → Buttons

FileManager.java      → Save/Load

## How to Run

```bash
cd Sudoku
rm *.class
javac *.java
java Main
```