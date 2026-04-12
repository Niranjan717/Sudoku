import javax.swing.*;

/**
 * Main.java — Application entry point.
 *
 * Bootstraps the MVC components in the correct order on the Swing
 * Event Dispatch Thread (EDT):
 *
 *   1. Apply a cross-platform look-and-feel so the dark-themed components
 *      render consistently on Windows, macOS and Linux.
 *   2. Create the {@link GameController} (handles all game logic).
 *   3. Create the {@link SudokuUI} (top-level JFrame); its constructor
 *      calls {@code controller.setUI()} to complete the two-way binding.
 *   4. Make the window visible.
 *   5. Start the first game automatically.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            applyLookAndFeel();

            GameController controller = new GameController();
            SudokuUI ui = new SudokuUI(controller);
            ui.setVisible(true);

            // Auto-start the first game so the user sees a puzzle immediately
            controller.startNewGame();
        });
    }

    /**
     * Tries to apply the Nimbus look-and-feel for a modern appearance.
     * Falls back to the system L&amp;F, then to the cross-platform default.
     */
    private static void applyLookAndFeel() {
        // First, try Nimbus — it renders our custom-painted components well
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());

                    // Override Nimbus' default bright backgrounds so they
                    // don't leak through on our opaque=false components
                    UIManager.put("control",      new java.awt.Color(0x12141F));
                    UIManager.put("info",         new java.awt.Color(0x12141F));
                    UIManager.put("nimbusBase",   new java.awt.Color(0x1E2130));
                    UIManager.put("nimbusBlueGrey", new java.awt.Color(0x2A2D42));
                    UIManager.put("text",         new java.awt.Color(0xD0D6F0));
                    return;
                } catch (Exception ignored) { }
            }
        }

        // Fallback: system L&F
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }
    }
}
