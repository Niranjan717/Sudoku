import java.io.Serializable;

/**
 * SudokuBoard.java (Model)
 *
 * Holds three 9x9 grids:
 *   - initialBoard  : the original puzzle (read-only reference)
 *   - currentBoard  : the player's in-progress state
 *   - solutionBoard : the fully solved grid
 *
 * All boards use 0 to represent empty cells.
 */
public class SudokuBoard implements Serializable {

    private static final long serialVersionUID = 1L;
    public  static final int SIZE = 9;

    private final int[][] initialBoard;
    private final int[][] currentBoard;
    private final int[][] solutionBoard;

    /**
     * Constructs a new SudokuBoard.
     *
     * @param initialBoard  The original unsolved puzzle.
     * @param solutionBoard The fully solved grid.
     */
    public SudokuBoard(int[][] initialBoard, int[][] solutionBoard) {
        this.initialBoard  = deepCopy(initialBoard);
        this.currentBoard  = deepCopy(initialBoard); // Start from the initial state
        this.solutionBoard = deepCopy(solutionBoard);
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    /** Returns the original puzzle grid. */
    public int[][] getInitialBoard()  { return initialBoard; }

    /** Returns the player's current progress grid. */
    public int[][] getCurrentBoard()  { return currentBoard; }

    /** Returns the fully solved grid. */
    public int[][] getSolutionBoard() { return solutionBoard; }

    // -------------------------------------------------------------------------
    // Cell Operations
    // -------------------------------------------------------------------------

    /**
     * Returns the value of the current board at the given position.
     */
    public int getCell(int row, int col) {
        return currentBoard[row][col];
    }

    /**
     * Sets a cell value on the current board.
     */
    public void setCell(int row, int col, int value) {
        currentBoard[row][col] = value;
    }

    /**
     * Returns true if the cell was part of the original puzzle.
     */
    public boolean isInitialCell(int row, int col) {
        return initialBoard[row][col] != 0;
    }

    // -------------------------------------------------------------------------
    // Board-Level Operations
    // -------------------------------------------------------------------------

    /**
     * Resets the current board to the initial puzzle state,
     * clearing all user-entered values.
     */
    public void resetBoard() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                currentBoard[r][c] = initialBoard[r][c];
            }
        }
    }

    /**
     * Fills the current board with the complete solution.
     */
    public void fillSolution() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                currentBoard[r][c] = solutionBoard[r][c];
            }
        }
    }

    /**
     * Returns true when every cell matches the solution.
     */
    public boolean isSolved() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (currentBoard[r][c] == 0 || currentBoard[r][c] != solutionBoard[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    // -------------------------------------------------------------------------
    // Utility
    // -------------------------------------------------------------------------

    /** Deep-copies a 9×9 int array. */
    private static int[][] deepCopy(int[][] src) {
        int[][] copy = new int[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            System.arraycopy(src[r], 0, copy[r], 0, SIZE);
        }
        return copy;
    }
}
