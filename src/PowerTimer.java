import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * PowerTimer is an extension of the Swing Timer class that allows for pausing,
 * resuming, and adjusting the timer's delay even after the timer has started.
 */
public class PowerTimer extends Timer {

	private int originalDelay;
	private int elapsedDelay;
	private boolean isPaused;

	/**
	 * Constructs a PowerTimer.
	 *
	 * @param delay    The initial delay (in milliseconds) for the timer.
	 * @param listener The ActionListener that gets notified when the timer fires.
	 */
	public PowerTimer(int delay, ActionListener listener) {
		super(delay, listener);
		this.originalDelay = delay;
		this.elapsedDelay = 0;
		this.isPaused = false;
	}

	/**
	 * Restarts the timer, resetting the elapsed delay.
	 */
	@Override
	public void restart() {
		this.elapsedDelay = 0;
		super.setInitialDelay(this.originalDelay);
		super.restart();
	}

	/**
	 * Starts or resumes the timer.
	 */
	@Override
	public void start() {
		if (isPaused) {
			super.setInitialDelay(this.originalDelay - this.elapsedDelay);
		} else {
			this.elapsedDelay = 0;
			super.setInitialDelay(this.originalDelay);
		}
		super.start();
	}

	/**
	 * Pauses the timer, preserving the elapsed delay.
	 */
	public void pause() {
		if (!isRunning()) {
			return;
		}
		isPaused = true;
		super.stop();
	}

	/**
	 * Resumes the timer from its paused state.
	 */
	public void resume() {
		this.isPaused = false;
		this.start();
	}

	/**
	 * Updates the timer's delay. If the new delay is less than the elapsed time,
	 * the action associated with the timer is called immediately, and the timer's
	 * elapsed time is reset to zero. Otherwise, if the elapsed time is less than
	 * the new delay, the timer's elapsed time is set to the remainder of the
	 * elapsed time divided by the new delay.
	 *
	 * @param newDelay The new delay (in milliseconds) for the timer.
	 */
	public void updateDelay(int newDelay) {
		if (this.elapsedDelay >= newDelay) {
			for (ActionListener listener : getActionListeners()) {
				listener.actionPerformed(null);
			}
			this.elapsedDelay = 0;
		} else {
			this.elapsedDelay = this.elapsedDelay % newDelay;
		}

		this.originalDelay = newDelay;
		if (isPaused) {
			super.setInitialDelay(this.originalDelay - this.elapsedDelay);
		}
	}

}
