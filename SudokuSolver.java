/**
 * SudokuSolver.java
 *
 * Implements a classic backtracking algorithm to solve any valid Sudoku puzzle.
 * Used internally by PuzzleRepository to compute the solution for each puzzle.
 */
public class SudokuSolver {

    private SudokuSolver() { /* Utility class — no instantiation */ }

    /**
     * Attempts to solve the given 9×9 board in place using backtracking.
     *
     * @param board The 9×9 grid (0 = empty cell).
     * @return {@code true} if the puzzle was solvable; {@code false} otherwise.
     */
    public static boolean solve(int[][] board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) {
                    // Try every digit 1–9
                    for (int n = 1; n <= 9; n++) {
                        if (isValid(board, r, c, n)) {
                            board[r][c] = n;
                            if (solve(board)) {
                                return true;
                            }
                            board[r][c] = 0; // Backtrack
                        }
                    }
                    return false; // No digit worked — backtrack further
                }
            }
        }
        return true; // All cells filled
    }

    /**
     * Checks whether placing {@code value} at {@code (row, col)} is valid
     * according to standard Sudoku rules (row, column, 3×3 box).
     */
    private static boolean isValid(int[][] board, int row, int col, int value) {
        // Row check
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == value) return false;
        }
        // Column check
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == value) return false;
        }
        // 3×3 box check
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int r = boxRow; r < boxRow + 3; r++) {
            for (int c = boxCol; c < boxCol + 3; c++) {
                if (board[r][c] == value) return false;
            }
        }
        return true;
    }
}
