import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * ControlPanel.java (View — header bar)
 *
 * A dark top bar that contains:
 *   • Timer display  — live clock (MM:SS) updated by GameTimer
 *   • Mistakes badge — e.g. "❌ 1 / 3"
 *   • New Game, Reset, Solve buttons with hover animations
 *
 * All user actions are delegated to the {@link GameController}.
 */
public class ControlPanel extends JPanel {

    // ── Colors ────────────────────────────────────────────────────────────────
    private static final Color BG        = new Color(0x12141F);
    private static final Color BTN_BG    = new Color(0x2A2D42);
    private static final Color BTN_HOVER = new Color(0x3C3F5C);
    private static final Color BTN_TEXT  = new Color(0xD0D6F0);
    private static final Color ACCENT    = new Color(0x7EB8F7); // soft blue
    private static final Color DANGER    = new Color(0xFF6B6B); // red for mistakes

    // ── Font ──────────────────────────────────────────────────────────────────
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font BTN_FONT   = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font TIMER_FONT = new Font("Segoe UI Semibold", Font.BOLD, 20);

    // ── Widgets ───────────────────────────────────────────────────────────────
    private final JLabel timerLabel;
    private final JLabel mistakesLabel;
    private final JButton newGameBtn;
    private final JButton resetBtn;
    private final JButton solveBtn;

    public ControlPanel(GameController controller) {
        setBackground(BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(12, 18, 12, 18));

        // ── Left section: Timer icon + clock ──────────────────────────────────
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftPanel.setOpaque(false);

        JLabel clockIcon = new JLabel("⏱");
        clockIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        clockIcon.setForeground(ACCENT);

        timerLabel = new JLabel("00:00");
        timerLabel.setFont(TIMER_FONT);
        timerLabel.setForeground(ACCENT);

        leftPanel.add(clockIcon);
        leftPanel.add(timerLabel);

        // ── Centre section: Buttons ────────────────────────────────────────────
        JPanel centrePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        centrePanel.setOpaque(false);

        newGameBtn = createButton("New Game", new Color(0x3C5975), new Color(0x4E7499));
        resetBtn   = createButton("Reset",    BTN_BG,              BTN_HOVER);
        solveBtn   = createButton("Solve",    new Color(0x2E4A38), new Color(0x3D6349));

        newGameBtn.addActionListener(e -> controller.startNewGame());
        resetBtn.addActionListener(e   -> controller.resetGame());
        solveBtn.addActionListener(e   -> controller.solveGame());

        centrePanel.add(newGameBtn);
        centrePanel.add(resetBtn);
        centrePanel.add(solveBtn);

        // ── Right section: Mistakes badge ─────────────────────────────────────
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightPanel.setOpaque(false);

        JLabel heartIcon = new JLabel("❌");
        heartIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        mistakesLabel = new JLabel("0 / 3");
        mistakesLabel.setFont(LABEL_FONT);
        mistakesLabel.setForeground(new Color(0xB0B8D8));

        rightPanel.add(heartIcon);
        rightPanel.add(mistakesLabel);

        add(leftPanel,   BorderLayout.WEST);
        add(centrePanel, BorderLayout.CENTER);
        add(rightPanel,  BorderLayout.EAST);

        // Separator below the bar
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0x2A2D42)),
            new EmptyBorder(12, 18, 12, 18)
        ));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Public API  (called from GameController)
    // ─────────────────────────────────────────────────────────────────────────

    /** Returns the JLabel used by {@link GameTimer} to display elapsed time. */
    public JLabel getTimerLabel() { return timerLabel; }

    /**
     * Updates the mistakes counter badge.
     *
     * @param count Current number of mistakes.
     * @param max   Maximum allowed mistakes.
     */
    public void updateMistakes(int count, int max) {
        mistakesLabel.setText(count + " / " + max);
        // Turn label red when mistakes are critical
        mistakesLabel.setForeground(count > 0 ? DANGER : new Color(0xB0B8D8));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helper
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Creates a styled, hover-animated button.
     *
     * @param text      Button caption.
     * @param normalBg  Background when idle.
     * @param hoverBg   Background when the cursor enters.
     */
    private JButton createButton(String text, Color normalBg, Color hoverBg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setFont(BTN_FONT);
        btn.setForeground(BTN_TEXT);
        btn.setBackground(normalBg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 38));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(hoverBg); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(normalBg); }
        });

        return btn;
    }
}
