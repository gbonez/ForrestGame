
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * Represents the GraysonGiraffe type of monster, which is a subtype of the
 * Monster class. GraysonGiraffe exhibits unique movement behavior, including
 * the sprint, and interacts differently with the game environment compared to
 * other monster types.
 * 
 * @see Monster
 */
public class GraysonGiraffe extends Monster {
	/**
	 * A constant indicating the movement duration for GraysonGiraffe in
	 * milliseconds.
	 */
	private static final int MOVEMENT = 5100;

	/** A variable to determine the amount of power drop during sprints. */
	private int minus;

	/**
	 * Timer responsible for managing the standard movement intervals of
	 * GraysonGiraffe.
	 */
	private Timer movementTimer;

	/** Timer responsible for managing the sprinting behavior of GraysonGiraffe. */
	public Timer sprintTimer;

	/** Flag to determine if the sprint animation has been played. */
	public boolean animationPlayed;

	/**
	 * Constructs a new GraysonGiraffe monster with a given name, starting room, and
	 * GUIBuilder. Initializes both movement and sprint timers to manage the
	 * monster's behavior.
	 *
	 * @param name  The name of the GraysonGiraffe monster.
	 * @param start The starting room location for the monster.
	 * @param other The GUIBuilder instance used to interact with the game's
	 *              graphical interface.
	 */
	public GraysonGiraffe(String name, int start, GUIBuilder other) {
		super(name, start, other);
		minus = 2;
		animationPlayed = false;
		movementTimer = new Timer(MOVEMENT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (debug) {
					// debugRoom();
				} else {
					movementRoll();
				}

			}
		});
		movementTimer.setRepeats(true);

		sprintTimer = new Timer(25000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gui.leftDoorShut) {
					animationPlayed = false;
					gui.powerDrop(minus);
					gui.getSound().playForrestSound(0, true);
					if (minus <= 2) {
						minus = minus + 2;
					}
					roomLocation = 0;
					sprintTimer.stop();
					sprintTimer.setInitialDelay(25000);
					movementTimer.start();
				} else {
					gui.gameOver(false);
					sprintTimer.setInitialDelay(25000);
				}
			}
		});
	}

	/**
	 * Starts the movement timer for GraysonGiraffe.
	 */
	@Override
	public void startTimer() {
		movementTimer.start();
	}

	/**
	 * Stops both the movement timer and the sprint timer for GraysonGiraffe.
	 */
	@Override
	public void stopTimer() {
		movementTimer.stop();
		if (sprintTimer.isRunning()) {
			sprintTimer.stop();
		}
	}

	/**
	 * Dictates the movement behavior of the GraysonGiraffe. It decides based on a
	 * random number and the monster's current difficulty whether the movement will
	 * be successful.
	 */
	@Override
	public void movementRoll() {
		int check = (int) (Math.random() * 20);
		check++;
		if (check <= difficulty) {
			movementSuccess();
		}
	}

	/**
	 * Handles successful movement for the GraysonGiraffe. Depending on its current
	 * room location and the state of its sprint timer, the monster might move to a
	 * new room, initiate a sprint, or interact with other game components.
	 */
	private void movementSuccess() {
		if (!sprintTimer.isRunning()) {
			if (roomLocation < 3) {
				roomLocation++;
				gui.mapRefresh(5, 5, this);
			} else {
				roomLocation++;
				movementTimer.stop();
				gui.mapRefresh(5, 5, this);
				sprintTimer.start();
			}
		}
	}

	/**
	 * Represents a debug mode for the GraysonGiraffe's movement. Simplifies the
	 * movement patterns for debugging and development purposes.
	 */
	private void debugRoom() {
		if (!sprintTimer.isRunning()) {
			if (roomLocation < 3) {
				roomLocation++;
				gui.mapRefresh(5, 5, this);
			} else {
				roomLocation = 0;
				gui.mapRefresh(5, 5, this);
			}
		}

	}
}
