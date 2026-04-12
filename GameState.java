/**
 * GameState.java
 * Enum representing the possible states of the Sudoku game.
 */
public enum GameState {
    START,    // Game has not started yet
    PLAYING,  // Game is actively in progress
    WON,      // Player has successfully completed the puzzle
    LOST      // Player exceeded the maximum allowed mistakes
}
