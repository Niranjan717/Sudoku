import java.util.Random;

/**
 * PuzzleRepository.java
 *
 * Stores a collection of 5 hand-verified Sudoku puzzles (0 = empty cell).
 * Each puzzle is paired with its pre-computed solution via SudokuSolver.
 *
 * New puzzles are chosen randomly, guaranteeing a different index than the
 * previously used one so the player always gets a fresh puzzle on "New Game".
 */
public class PuzzleRepository {

    private PuzzleRepository() { /* Utility class */ }

    private static final Random RANDOM = new Random();

    /** Index of the puzzle served in the most recent call to getPuzzle(). */
    private static int lastIndex = -1;

    // =========================================================================
    //  PUZZLE DATA  (0 = empty cell)
    // =========================================================================

    private static final int[][][] PUZZLES = {

        // ── Puzzle 1 ─────────────────────────────────────────────────────────
        {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
        },

        // ── Puzzle 2 ─────────────────────────────────────────────────────────
        {
            {0, 0, 0, 2, 6, 0, 7, 0, 1},
            {6, 8, 0, 0, 7, 0, 0, 9, 0},
            {1, 9, 0, 0, 0, 4, 5, 0, 0},
            {8, 2, 0, 1, 0, 0, 0, 4, 0},
            {0, 0, 4, 6, 0, 2, 9, 0, 0},
            {0, 5, 0, 0, 0, 3, 0, 2, 8},
            {0, 0, 9, 3, 0, 0, 0, 7, 4},
            {0, 4, 0, 0, 5, 0, 0, 3, 6},
            {7, 0, 3, 0, 1, 8, 0, 0, 0}
        },

        // ── Puzzle 3 ─────────────────────────────────────────────────────────
        {
            {0, 2, 0, 6, 0, 8, 0, 0, 0},
            {5, 8, 0, 0, 0, 9, 7, 0, 0},
            {0, 0, 0, 0, 4, 0, 0, 0, 0},
            {3, 7, 0, 0, 0, 0, 5, 0, 0},
            {6, 0, 0, 0, 0, 0, 0, 0, 4},
            {0, 0, 8, 0, 0, 0, 0, 1, 3},
            {0, 0, 0, 0, 2, 0, 0, 0, 0},
            {0, 0, 9, 8, 0, 0, 0, 3, 6},
            {0, 0, 0, 3, 0, 6, 0, 9, 0}
        },

        // ── Puzzle 4 ─────────────────────────────────────────────────────────
        {
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 3, 0, 8, 5},
            {0, 0, 1, 0, 2, 0, 0, 0, 0},
            {0, 0, 0, 5, 0, 7, 0, 0, 0},
            {0, 0, 4, 0, 0, 0, 1, 0, 0},
            {0, 9, 0, 0, 0, 0, 0, 0, 0},
            {5, 0, 0, 0, 0, 0, 0, 7, 3},
            {0, 0, 2, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 4, 0, 0, 0, 9}
        },

        // ── Puzzle 5 ─────────────────────────────────────────────────────────
        {
            {1, 0, 0, 4, 8, 9, 0, 0, 6},
            {7, 3, 0, 0, 0, 0, 0, 4, 0},
            {0, 0, 0, 0, 0, 1, 2, 9, 5},
            {0, 0, 7, 1, 2, 0, 6, 0, 0},
            {5, 0, 0, 7, 0, 3, 0, 0, 8},
            {0, 0, 6, 0, 9, 5, 7, 0, 0},
            {9, 1, 4, 6, 0, 0, 0, 0, 0},
            {0, 2, 0, 0, 0, 0, 0, 3, 7},
            {8, 0, 0, 5, 1, 2, 0, 0, 4}
        }
    };

    // =========================================================================
    //  Public API
    // =========================================================================

    /**
     * Returns a {@link SudokuBoard} for a randomly chosen puzzle.
     * Guarantees a different puzzle than the last call (as long as there are
     * at least 2 puzzles in the repository).
     */
    public static SudokuBoard getRandomPuzzle() {
        int index;
        do {
            index = RANDOM.nextInt(PUZZLES.length);
        } while (index == lastIndex && PUZZLES.length > 1);
        lastIndex = index;
        return buildBoard(index);
    }

    /**
     * Returns a {@link SudokuBoard} for the puzzle at the given index.
     *
     * @param index 0-based index into the puzzle array.
     */
    public static SudokuBoard getPuzzle(int index) {
        if (index < 0 || index >= PUZZLES.length) {
            throw new IllegalArgumentException("Puzzle index out of range: " + index);
        }
        return buildBoard(index);
    }

    /** Total number of puzzles stored in the repository. */
    public static int getPuzzleCount() {
        return PUZZLES.length;
    }

    // =========================================================================
    //  Helpers
    // =========================================================================

    /**
     * Deep-copies the puzzle data, solves it to derive the solution,
     * and wraps both in a {@link SudokuBoard}.
     */
    private static SudokuBoard buildBoard(int index) {
        int[][] puzzle = deepCopy(PUZZLES[index]);

        // Derive the solution by solving a copy of the puzzle
        int[][] solution = deepCopy(puzzle);
        SudokuSolver.solve(solution);

        return new SudokuBoard(puzzle, solution);
    }

    private static int[][] deepCopy(int[][] src) {
        int[][] copy = new int[9][9];
        for (int r = 0; r < 9; r++) {
            System.arraycopy(src[r], 0, copy[r], 0, 9);
        }
        return copy;
    }
}
