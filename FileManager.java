import java.io.*;

/**
 * FileManager.java
 *
 * Provides static helpers to persist a {@link SudokuBoard} to disk and reload
 * it across sessions.  Uses Java serialization via {@link ObjectOutputStream}.
 */
public class FileManager {

    private FileManager() { /* Utility class */ }

    /** Path of the save file (relative to the working directory). */
    private static final String SAVE_FILE = "sudoku_save.dat";

    // -------------------------------------------------------------------------
    // Save
    // -------------------------------------------------------------------------

    /**
     * Serializes the given {@link SudokuBoard} to {@value #SAVE_FILE}.
     * Any existing save is overwritten silently.
     *
     * @param board The board to persist.
     */
    public static void saveGame(SudokuBoard board) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(SAVE_FILE)))) {
            out.writeObject(board);
        } catch (IOException e) {
            System.err.println("[FileManager] Save failed: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Load
    // -------------------------------------------------------------------------

    /**
     * Reads and deserializes a {@link SudokuBoard} from {@value #SAVE_FILE}.
     *
     * @return The saved board, or {@code null} if no save file exists or reading
     *         fails.
     */
    public static SudokuBoard loadGame() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) return null;

        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {
            return (SudokuBoard) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[FileManager] Load failed: " + e.getMessage());
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // Housekeeping
    // -------------------------------------------------------------------------

    /**
     * Deletes the save file, e.g. after a "New Game" where the old save is
     * irrelevant.
     */
    public static void deleteSave() {
        File file = new File(SAVE_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    /** @return {@code true} if a save file is present on disk. */
    public static boolean hasSave() {
        return new File(SAVE_FILE).exists();
    }
}
