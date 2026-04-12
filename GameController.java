import javax.swing.*;
import java.awt.*;

/**
 * GameController.java (Controller — MVC)
 *
 * Central controller that wires the Model ({@link SudokuBoard}) to the View
 * ({@link SudokuUI}).  It handles:
 *   • Starting a new game          — loads a fresh puzzle from the repository
 *   • Resetting the current game   — restores the initial puzzle state
 *   • Auto-solving                 — fills the solution grid
 *   • Processing user input        — validates, updates, checks win/lose
 *   • Mistake counting             — up to 3 mistakes → LOST
 *   • Timer lifecycle              — start / stop / reset
 *   • Status-bar messages
 */
public class GameController {

    // ── Model ─────────────────────────────────────────────────────────────────
    private SudokuBoard board;
    private GameState   state;
    private int         mistakes;
    private GameTimer   gameTimer;

    // ── View ──────────────────────────────────────────────────────────────────
    private SudokuUI ui;

    // ── Constants ─────────────────────────────────────────────────────────────
    private static final int MAX_MISTAKES = 3;

    // ── Colours for status bar messages ───────────────────────────────────────
    private static final Color CLR_WIN    = new Color(0x4FC3A1);  // teal
    private static final Color CLR_LOSE   = new Color(0xFF6B6B);  // red
    private static final Color CLR_NORMAL = new Color(0x8899BB);  // muted blue

    // ─────────────────────────────────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────────────────────────────────

    public GameController() {
        this.state    = GameState.START;
        this.mistakes = 0;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // View wiring  (called by SudokuUI after it builds the ControlPanel)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Injects the view and creates the {@link GameTimer} bound to the timer
     * label exposed by the {@link ControlPanel}.
     *
     * @param ui The fully constructed SudokuUI.
     */
    public void setUI(SudokuUI ui) {
        this.ui        = ui;
        this.gameTimer = new GameTimer(ui.getControlPanel().getTimerLabel());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Game-flow Actions
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Loads a new random puzzle, resets mistake count and timer, and redraws
     * the grid.  Safe to call at any point in the game lifecycle.
     */
    public void startNewGame() {
        board     = PuzzleRepository.getRandomPuzzle();
        state     = GameState.PLAYING;
        mistakes  = 0;

        ui.getControlPanel().updateMistakes(mistakes, MAX_MISTAKES);
        gameTimer.reset();
        gameTimer.start();

        ui.getGridPanel().updateBoard(board);
        FileManager.deleteSave();   // Previous save is no longer relevant
        ui.setStatus("New game started — Good luck!", CLR_NORMAL);
    }

    /**
     * Resets the current board to the original initial-puzzle state.
     * Only user-entered cells are cleared; the puzzle clues are restored.
     * Timer and mistake count are also reset.
     */
    public void resetGame() {
        if (board == null) return;

        board.resetBoard();
        state    = GameState.PLAYING;
        mistakes = 0;

        ui.getControlPanel().updateMistakes(mistakes, MAX_MISTAKES);
        gameTimer.reset();
        gameTimer.start();

        ui.getGridPanel().updateBoard(board);
        ui.setStatus("Board reset — your entries have been cleared.", CLR_NORMAL);
    }

    /**
     * Auto-fills the puzzle with the correct solution.
     * Only allowed while the game is in the PLAYING state.
     */
    public void solveGame() {
        if (board == null || state != GameState.PLAYING) return;

        board.fillSolution();
        state = GameState.WON;
        gameTimer.stop();

        // Show solved state with distinct teal coloring
        ui.getGridPanel().showSolution(board);
        ui.setStatus("Puzzle auto-solved! 🎉", CLR_WIN);

        showDialog("Auto-Solved!",
            "The puzzle has been filled with the correct solution.\n" +
            "Try the next one!",
            JOptionPane.INFORMATION_MESSAGE);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Input Processing  (called from GridPanel key listeners)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Validates and applies a user's digit entry.
     *
     * @param row   Grid row (0–8).
     * @param col   Grid column (0–8).
     * @param value The entered digit (1–9) or 0 (erased).
     * @param cell  The {@link CellField} that triggered the input event.
     */
    public void processInput(int row, int col, int value, CellField cell) {
        // Ignore input when the game is not active
        if (state != GameState.PLAYING) return;

        // Erasing a cell is always valid
        if (value == 0) {
            board.setCell(row, col, 0);
            cell.setValidState();
            ui.setStatus("Cell cleared.", CLR_NORMAL);
            return;
        }

        if (SudokuValidator.isValidMove(board, row, col, value)) {
            // ── Correct entry ──
            board.setCell(row, col, value);
            cell.setValidState();

            if (board.isSolved()) {
                state = GameState.WON;
                gameTimer.stop();
                ui.setStatus("Congratulations — Puzzle solved! 🎉", CLR_WIN);
                showDialog("You Won! 🎉",
                    "Excellent work!\nTime: " + formatTime(gameTimer.getSecondsElapsed()) +
                    "\nMistakes: " + mistakes + " / " + MAX_MISTAKES,
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                ui.setStatus("Correct! Keep going.", CLR_NORMAL);
            }

        } else {
            // ── Wrong entry ──
            mistakes++;
            ui.getControlPanel().updateMistakes(mistakes, MAX_MISTAKES);
            cell.setInvalidState();

            if (mistakes >= MAX_MISTAKES) {
                state = GameState.LOST;
                gameTimer.stop();
                ui.setStatus("Game Over — too many mistakes!", CLR_LOSE);
                showDialog("Game Over 💀",
                    "You made " + MAX_MISTAKES + " mistakes.\n" +
                    "Better luck next time!",
                    JOptionPane.ERROR_MESSAGE);
            } else {
                int remaining = MAX_MISTAKES - mistakes;
                ui.setStatus("Wrong! " + remaining + " mistake(s) remaining.", CLR_LOSE);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    /** Formats seconds as MM:SS. */
    private static String formatTime(int totalSeconds) {
        return String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60);
    }

    /**
     * Displays a styled JOptionPane dialog centered on the main window.
     *
     * @param title   Dialog title.
     * @param message Body text (newlines accepted).
     * @param type    One of the JOptionPane message-type constants.
     */
    private void showDialog(String title, String message, int type) {
        // Slight delay so Swing can repaint grid before blocking
        SwingUtilities.invokeLater(() ->
            JOptionPane.showMessageDialog(ui, message, title, type)
        );
    }
}
