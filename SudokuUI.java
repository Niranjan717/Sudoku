import javax.swing.*;
import java.awt.*;

/**
 * SudokuUI.java (View — main window)
 *
 * The top-level {@link JFrame} that hosts:
 *   • {@link ControlPanel}  — header bar with timer, mistakes, and action buttons
 *   • {@link GridPanel}     — the 9×9 Sudoku grid
 *   • A thin status bar showing short game messages (win/lose hints)
 *
 * The window uses an all-dark theme that matches the CellField and ControlPanel
 * palette.  It is non-resizable; all size decisions are made here once and
 * passed to child components via pack().
 */
public class SudokuUI extends JFrame {

    // ── Sub-panels ───────────────────────────────────────────────────────────
    private final GridPanel    gridPanel;
    private final ControlPanel controlPanel;

    // ── Status bar ───────────────────────────────────────────────────────────
    private final JLabel statusBar;

    private static final Color BG_MAIN   = new Color(0x12141F);
    private static final Color BG_STATUS = new Color(0x0D0F18);
    private static final Font  STATUS_FG = new Font("Segoe UI", Font.PLAIN, 13);

    // ─────────────────────────────────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────────────────────────────────

    public SudokuUI(GameController controller) {
        super("Sudoku");

        /* ── Build child panels before wiring ── */
        controlPanel = new ControlPanel(controller);
        controller.setUI(this);              // Give controller access to UI
        gridPanel    = new GridPanel(controller);

        /* ── Status bar ── */
        statusBar = new JLabel(" Ready  —  Start a new game or fill a cell.", JLabel.LEFT);
        statusBar.setFont(STATUS_FG);
        statusBar.setForeground(new Color(0x6A7090));
        statusBar.setBackground(BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0x2A2D42)),
            BorderFactory.createEmptyBorder(5, 14, 5, 14)
        ));

        /* ── Layout ── */
        getContentPane().setBackground(BG_MAIN);
        setLayout(new BorderLayout(0, 0));
        add(controlPanel, BorderLayout.NORTH);
        add(gridPanel,    BorderLayout.CENTER);
        add(statusBar,    BorderLayout.SOUTH);

        /* ── Preferred sizing — 9 cells × 58px each ── */
        gridPanel.setPreferredSize(new Dimension(522, 522));

        /* ── Frame settings ── */
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);   // Centre on screen
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Accessors (used by GameController)
    // ─────────────────────────────────────────────────────────────────────────

    public GridPanel    getGridPanel()    { return gridPanel; }
    public ControlPanel getControlPanel() { return controlPanel; }

    // ─────────────────────────────────────────────────────────────────────────
    // Status bar
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Updates the status-bar message.
     *
     * @param message Short text to display.
     * @param color   Text colour (use null for the default muted colour).
     */
    public void setStatus(String message, Color color) {
        statusBar.setText("  " + message);
        statusBar.setForeground(color != null ? color : new Color(0x6A7090));
    }
}
