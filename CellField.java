import javax.swing.*;
import java.awt.*;

/**
 * CellField.java (View — individual cell)
 *
 * A custom {@link JTextField} that represents one cell in the 9×9 Sudoku grid.
 *
 * Visual states:
 *   • Initial  — a clue digit from the puzzle (non-editable, dark background)
 *   • Empty    — an empty cell waiting for user input
 *   • User     — a digit entered by the player (blue text)
 *   • Valid    — last entry was correct (white background)
 *   • Invalid  — last entry was wrong (red background, text stays)
 *   • Solved   — cell was filled by the "Solve" button (teal text)
 *   • Selected — the cell that just received focus (subtle highlight)
 */
public class CellField extends JTextField {

    // ── Identity ──────────────────────────────────────────────────────────────
    private final int row;
    private final int col;

    // ── Color Palette ─────────────────────────────────────────────────────────
    public static final Color CLR_BG_INITIAL     = new Color(0x2A2D3E); // dark slate
    public static final Color CLR_BG_EMPTY       = new Color(0x1E2130); // near-black
    public static final Color CLR_BG_VALID       = new Color(0x1E2130);
    public static final Color CLR_BG_INVALID     = new Color(0x7B2D35); // deep red
    public static final Color CLR_BG_SOLVED      = new Color(0x1A3040); // deep teal tint
    public static final Color CLR_BG_HOVER       = new Color(0x252840); // subtle hover

    public static final Color CLR_FG_INITIAL     = new Color(0xE0E6F0); // off-white
    public static final Color CLR_FG_USER        = new Color(0x7EB8F7); // soft blue
    public static final Color CLR_FG_INVALID     = new Color(0xFF6B6B); // bright red text
    public static final Color CLR_FG_SOLVED      = new Color(0x4FC3A1); // teal

    // Border colours for the 3×3 box lines
    private static final Color CLR_LINE_THICK    = new Color(0x8888BB);
    private static final Color CLR_LINE_THIN     = new Color(0x3A3D55);

    // Font used across all cells (falls back gracefully)
    public static final Font  CELL_FONT          = new Font("Segoe UI", Font.BOLD, 22);

    // ── Constructor ───────────────────────────────────────────────────────────

    public CellField(int row, int col) {
        this.row = row;
        this.col = col;

        setHorizontalAlignment(JTextField.CENTER);
        setFont(CELL_FONT);
        setCaretColor(CLR_FG_USER);
        setOpaque(true);

        // Border: thick lines at 3×3 box boundaries, thin lines elsewhere
        int top    = (row % 3 == 0) ? 3 : 1;
        int left   = (col % 3 == 0) ? 3 : 1;
        int bottom = (row == 8)      ? 3 : 1;
        int right  = (col == 8)      ? 3 : 1;

        Color topClr    = (row % 3 == 0 || row == 0) ? CLR_LINE_THICK : CLR_LINE_THIN;
        Color leftClr   = (col % 3 == 0 || col == 0) ? CLR_LINE_THICK : CLR_LINE_THIN;
        Color bottomClr = (row == 8) ? CLR_LINE_THICK : CLR_LINE_THIN;
        Color rightClr  = (col == 8) ? CLR_LINE_THICK : CLR_LINE_THIN;

        // Uniform matte border using the dominant colour for simplicity
        Color borderClr = (row % 3 == 0 || col % 3 == 0 || row == 8 || col == 8)
                ? CLR_LINE_THICK : CLR_LINE_THIN;
        setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, borderClr));

        // Default background
        applyBackground(CLR_BG_EMPTY);
        setForeground(CLR_FG_USER);
    }

    // ── Public State Setters ──────────────────────────────────────────────────

    /** Sets the cell to display a fixed clue digit (non-editable). */
    public void setInitialState(int val) {
        setText(String.valueOf(val));
        setEditable(false);
        setFocusable(false);
        applyBackground(CLR_BG_INITIAL);
        setForeground(CLR_FG_INITIAL);
        setFont(CELL_FONT.deriveFont(Font.BOLD));
    }

    /** Clears the cell and makes it ready for user input. */
    public void setEmptyState() {
        setText("");
        setEditable(true);
        setFocusable(true);
        applyBackground(CLR_BG_EMPTY);
        setForeground(CLR_FG_USER);
    }

    /** Displays a valid user-entered digit with blue text. */
    public void setUserState(int val) {
        setText(val == 0 ? "" : String.valueOf(val));
        setEditable(true);
        setFocusable(true);
        applyBackground(CLR_BG_VALID);
        setForeground(CLR_FG_USER);
    }

    /** Highlights the cell green/white to confirm a correct entry. */
    public void setValidState() {
        applyBackground(CLR_BG_VALID);
        setForeground(CLR_FG_USER);
    }

    /** Highlights the cell red to signal an incorrect entry. */
    public void setInvalidState() {
        applyBackground(CLR_BG_INVALID);
        setForeground(CLR_FG_INVALID);
    }

    /** Displays a digit filled by the auto-solve feature (teal text). */
    public void setSolvedState(int val) {
        setText(String.valueOf(val));
        setEditable(false);
        setFocusable(false);
        applyBackground(CLR_BG_SOLVED);
        setForeground(CLR_FG_SOLVED);
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    public int getRow() { return row; }
    public int getCol() { return col; }

    // ── Internals ─────────────────────────────────────────────────────────────

    private void applyBackground(Color c) {
        setBackground(c);
        // Repaint immediately to avoid Swing's lazy repaint on some L&Fs
        repaint();
    }
}
