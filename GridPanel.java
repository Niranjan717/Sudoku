import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.*;

/**
 * GridPanel.java (View — 9×9 grid)
 *
 * Renders the 9×9 Sudoku grid as a table of {@link CellField} components.
 * Handles:
 *   • Keyboard input filtering (only digits 1–9 accepted, Delete/Backspace clears)
 *   • Forwarding validated values to the {@link GameController}
 *   • Highlighting the currently selected cell's row and column
 *   • Reflecting board state (initial, user, solved) during updateBoard()
 */
public class GridPanel extends JPanel {

    private final CellField[][]    cells;
    private final GameController   controller;

    // Padding around the grid panel
    private static final int PADDING = 12;

    public GridPanel(GameController controller) {
        this.controller = controller;
        this.cells      = new CellField[9][9];

        // GridLayout with 0 gap — borders on cells handle visual separation
        setLayout(new GridLayout(9, 9, 0, 0));
        setBackground(CellField.CLR_BG_EMPTY);
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        // Shared DocumentFilter — allows at most one character, digits 1–9 only
        DocumentFilter digitFilter = new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length,
                                String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) { super.replace(fb, offset, length, text, attrs); return; }
                // Accept empty replacement (delete) or a single digit 1–9
                if (text.isEmpty() || (text.length() == 1 && text.matches("[1-9]"))) {
                    // Ensure the total document length stays ≤ 1
                    if (fb.getDocument().getLength() - length + text.length() <= 1) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }
            }
        };

        // Build the 9×9 grid
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                CellField cell = new CellField(r, c);
                cells[r][c] = cell;

                // Attach the digit filter
                ((AbstractDocument) cell.getDocument()).setDocumentFilter(digitFilter);

                final int row = r;
                final int col = c;

                // ── Key listener: notify controller after every key-up ─────────
                cell.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        if (!cell.isEditable()) return;

                        String text = cell.getText();
                        int value = 0;
                        if (!text.isEmpty()) {
                            try { value = Integer.parseInt(text); }
                            catch (NumberFormatException ignored) { }
                        }
                        controller.processInput(row, col, value, cell);
                    }
                });

                // ── Focus listener: highlight row/column of selected cell ──────
                cell.addFocusListener(new FocusAdapter() {
                    @Override public void focusGained(FocusEvent e) { highlightRelated(row, col); }
                    @Override public void focusLost(FocusEvent e)   { clearHighlight(); }
                });

                add(cell);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Public API called by the Controller
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Redraws the entire grid to match the given {@link SudokuBoard}.
     * Called after New Game, Reset, and Solve.
     */
    public void updateBoard(SudokuBoard board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int initial = board.getInitialBoard()[r][c];
                int current = board.getCurrentBoard()[r][c];

                if (initial != 0) {
                    // Pre-filled puzzle clue
                    cells[r][c].setInitialState(initial);
                } else if (current != 0) {
                    // Either a user entry or a solve-filled value
                    int solution = board.getSolutionBoard()[r][c];
                    if (current == solution) {
                        // Correct user entry — display in user style unless it was auto-solved
                        // We distinguish by checking editability; after fillSolution() the
                        // GameController calls updateBoard and sets solved style
                        cells[r][c].setUserState(current);
                    } else {
                        cells[r][c].setUserState(current);
                    }
                } else {
                    cells[r][c].setEmptyState();
                }
            }
        }
    }

    /**
     * Redraws the grid after the Solve button fills the solution.
     * Cells that were initially empty are displayed in "solved" (teal) style.
     */
    public void showSolution(SudokuBoard board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int initial = board.getInitialBoard()[r][c];
                int solved  = board.getCurrentBoard()[r][c];
                if (initial != 0) {
                    cells[r][c].setInitialState(initial);
                } else {
                    cells[r][c].setSolvedState(solved);
                }
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Highlight Helper
    // ─────────────────────────────────────────────────────────────────────────

    /** Dims all cells, then brightens every cell in the same row and column
     *  as the focused cell for a subtle directional highlight. */
    private void highlightRelated(int focusRow, int focusCol) {
        Color highlight = new Color(0x2B2F4A);
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                CellField cell = cells[r][c];
                if (!cell.isEditable()) continue; // Skip clue cells
                if (r == focusRow || c == focusCol) {
                    cell.setBackground(highlight);
                }
            }
        }
    }

    /** Restores all editable cells to their default empty/valid background. */
    private void clearHighlight() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                CellField cell = cells[r][c];
                if (!cell.isEditable()) continue;
                // Only reset cells that don't have an invalid red background
                Color bg = cell.getBackground();
                if (!bg.equals(CellField.CLR_BG_INVALID)) {
                    cell.setBackground(CellField.CLR_BG_EMPTY);
                }
            }
        }
    }
}
