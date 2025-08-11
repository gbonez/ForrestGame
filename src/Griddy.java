
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * Represents the Griddy type of monster, which is a subtype of the Monster
 * class. Griddy exhibits unique movement behavior and interacts with the game
 * environment in specific ways, particularly affecting the ablity to use the
 * lights.
 * 
 * @see Monster
 */
public class Griddy extends Monster {
	/** A constant indicating the movement duration for Griddy in milliseconds. */
	private static final int MOVEMENT = 6000;

	/** Timer responsible for managing the movement intervals of Griddy. */
	private Timer movementTimer;

	/**
	 * Constructs a new Griddy monster with a given name, starting room, and
	 * GUIBuilder. Initializes the movement timer to manage the monster's behavior.
	 *
	 * @param name  The name of the Griddy monster.
	 * @param start The starting room location for the monster.
	 * @param other The GUIBuilder instance used to interact with the game's
	 *              graphical interface.
	 */
	public Griddy(String name, int start, GUIBuilder other) {
		super(name, start, other);
		movementTimer = new Timer(MOVEMENT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (debug) {
					debugRoom();
				} else {
					movementRoll();
				}

			}
		});
	}

	/**
	 * Starts the movement timer for Griddy.
	 */
	@Override
	public void startTimer() {
		movementTimer.start();
	}

	/**
	 * Stops the movement timer for Griddy.
	 */
	@Override
	public void stopTimer() {
		movementTimer.stop();
	}

	/**
	 * Dictates the movement behavior of the Griddy. It decides based on a random
	 * number and the monster's current difficulty whether the movement will be
	 * successful.
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
	 * Handles successful movement for Griddy. If Griddy has reached a specific
	 * count (6), the lights are disbaled and a power drop is initiated. Otherwise,
	 * Griddy progresses forward.
	 */
	private void movementSuccess() {
		if (roomLocation == 6) {
			gui.updateGriddy();
			gui.powerDrop(.25);
		} else {
			roomLocation++;
		}
	}

	/**
	 * Represents a debug mode for the Griddy's movement. Allows for testing and
	 * development with simplified movement patterns.
	 */
	private void debugRoom() {
		if (roomLocation == 6) {
			gui.updateGriddy();
		} else {
			roomLocation++;
		}
	}

}
