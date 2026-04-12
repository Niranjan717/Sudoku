import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GameTimer.java
 *
 * Tracks elapsed time since the start of a game and updates a Swing JLabel
 * every second in a thread-safe manner via javax.swing.Timer.
 */
public class GameTimer {

    private final Timer  swingTimer;
    private       int    secondsElapsed;
    private final JLabel timeLabel;

    /**
     * Creates a new GameTimer that will keep the given label updated.
     *
     * @param timeLabel The label to display the elapsed time.
     */
    public GameTimer(JLabel timeLabel) {
        this.timeLabel      = timeLabel;
        this.secondsElapsed = 0;

        swingTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsElapsed++;
                refresh();
            }
        });
    }

    // -------------------------------------------------------------------------
    // Control
    // -------------------------------------------------------------------------

    /** Starts (or resumes) the timer. */
    public void start() {
        swingTimer.start();
    }

    /** Pauses the timer without resetting the counter. */
    public void stop() {
        swingTimer.stop();
    }

    /**
     * Stops the timer and resets the counter to zero.
     * Does <strong>not</strong> restart it — call {@link #start()} afterwards.
     */
    public void reset() {
        swingTimer.stop();
        secondsElapsed = 0;
        refresh();
    }

    /** @return The number of seconds elapsed so far. */
    public int getSecondsElapsed() {
        return secondsElapsed;
    }

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------

    /** Updates the label text with the current MM:SS value. */
    private void refresh() {
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }
}
