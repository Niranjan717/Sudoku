/**
 * SudokuValidator.java
 *
 * Validates a player's move against the known solution.
 * Also provides a rule-based check (row / column / box) used during solving.
 */
public class SudokuValidator {

    private SudokuValidator() { /* Utility class */ }

    // -------------------------------------------------------------------------
    // Solution-Based Validation  (used during gameplay)
    // -------------------------------------------------------------------------

    /**
     * Returns {@code true} if the digit placed at {@code (row, col)} matches
     * the known correct solution. This is the primary validation used to detect
     * mistakes and trigger the red-highlight feedback.
     *
     * @param board The current SudokuBoard (holds the solution grid).
     * @param row   Row index (0–8).
     * @param col   Column index (0–8).
     * @param value The digit entered by the player (1–9).
     */
    public static boolean isValidMove(SudokuBoard board, int row, int col, int value) {
        if (value == 0) return true; // Erasing is always valid
        return board.getSolutionBoard()[row][col] == value;
    }

    // -------------------------------------------------------------------------
    // Rule-Based Validation  (used by the solver and internally)
    // -------------------------------------------------------------------------

    /**
     * Returns {@code true} when placing {@code value} at {@code (row, col)} on
     * the given board does not violate any Sudoku rule (row, column, 3×3 box).
     * This does NOT check against a solution; it only checks existing numbers.
     *
     * @param board The 9×9 grid (0 = empty).
     * @param row   Row index (0–8).
     * @param col   Column index (0–8).
     * @param value The digit to test (1–9).
     */
    public static boolean isRuleValid(int[][] board, int row, int col, int value) {
        if (value == 0) return true;

        // Row check
        for (int c = 0; c < 9; c++) {
            if (c != col && board[row][c] == value) return false;
        }
        // Column check
        for (int r = 0; r < 9; r++) {
            if (r != row && board[r][col] == value) return false;
        }
        // 3×3 box check
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int r = boxRow; r < boxRow + 3; r++) {
            for (int c = boxCol; c < boxCol + 3; c++) {
                if ((r != row || c != col) && board[r][c] == value) return false;
            }
        }
        return true;
    }
}
