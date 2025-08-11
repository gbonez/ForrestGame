
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * Represents a specific type of monster named ForrestRight. This monster has
 * its own unique movement behavior and interacts with various game components,
 * such as doors and lights.
 * 
 * ForrestRight extends the generic Monster class, inheriting its properties and
 * behaviors, and then further specifies its own unique movement behavior and
 * interaction with the game's GUI.
 * 
 * @see Monster
 */
public class ForrestRight extends Monster {

	/** The previous room location of the monster before its last movement. */
	private int prevLocation;

	/** A constant representing the movement duration in milliseconds. */
	private static final int MOVEMENT = 4100;

	/** Timer responsible for controlling the monster's movement intervals. */
	private Timer movementTimer;

	/**
	 * Constructs a new ForrestRight monster with a specified name, starting room,
	 * and GUIBuilder. Initializes the movementTimer to dictate the monster's
	 * movement behavior.
	 *
	 * @param name  The name of the ForrestRight monster.
	 * @param start The starting room location of the monster.
	 * @param other The GUIBuilder instance to interact with the game's graphical
	 *              user interface.
	 */
	public ForrestRight(String name, int start, GUIBuilder other) {
		super(name, start, other);
		prevLocation = 1;
		movementTimer = new Timer(MOVEMENT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (debug) {
					debugRoom();
				} else if (gui.rightDoorShut && roomLocation == 12) {
					roomLocation = 3;
					prevLocation = 12;
					gui.mapRefresh(roomLocation, prevLocation, ForrestRight.this);
				} else if (gui.rightLightHeld && roomLocation == 10) {
					gui.powerDrop(.1);
					roomLocation = 3;
					prevLocation = 10;
					gui.mapRefresh(roomLocation, prevLocation, ForrestRight.this);
				} else {
					movementRoll();
				}
			}
		});
		movementTimer.setRepeats(true);
	}

	/**
	 * Starts the movement timer for the ForrestRight monster.
	 */
	@Override
	public void startTimer() {
		movementTimer.start();
	}

	/**
	 * Stops the movement timer for the ForrestRight monster.
	 */
	@Override
	public void stopTimer() {
		movementTimer.stop();
	}

	/**
	 * Sets the room location of the ForrestRight monster and resets the previous
	 * location.
	 *
	 * @param roomLocation The new room location for the ForrestRight monster.
	 */
	@Override
	public void setRoomLocation(int roomLocation) {
		this.roomLocation = roomLocation;
		this.prevLocation = 0;
	}

	/**
	 * Dictates the movement behavior of the ForrestRight monster based on random
	 * chance and the monster's difficulty level. Determines if the movement is
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
	 * Handles the successful movement of the ForrestRight monster. Depending on its
	 * current room, the monster can move to different rooms. Additionally,
	 * interacts with the game's doors and lights, influencing the game state.
	 */
	private void movementSuccess() {
		int check = (int) (Math.random() * 4);
		switch (roomLocation) {
		case 0:
			roomLocation = 1;
			prevLocation = 0;
			gui.mapRefresh(roomLocation, prevLocation, this);
			break;
		case 1:
			roomLocation = 3;
			prevLocation = 1;
			gui.mapRefresh(roomLocation, prevLocation, this);
			break;
		case 3:
			roomLocation = 5;
			prevLocation = 3;
			gui.mapRefresh(roomLocation, prevLocation, this);
			break;
		case 5:
			if (check >= 2) {
				roomLocation = 10;
			} else if (check == 1) {
				roomLocation = 14;
			} else {
				roomLocation = 3;
			}
			prevLocation = 5;
			gui.mapRefresh(roomLocation, prevLocation, this);
			break;
		case 7:
			if (check >= 1) {
				roomLocation = 10;
			} else {
				roomLocation = 14;
			}
			prevLocation = 7;
			gui.mapRefresh(roomLocation, prevLocation, this);
			break;
		case 8:
			if (check == 1) {
				roomLocation = 3;
			} else {
				roomLocation = 10;
			}
			prevLocation = 8;
			gui.mapRefresh(roomLocation, prevLocation, this);
			break;

		case 10:
			if (check >= 2) {
				roomLocation = 12;
			} else if (check == 1) {
				roomLocation = 7;
			} else {
				roomLocation = 8;
			}
			prevLocation = 10;
			gui.mapRefresh(roomLocation, prevLocation, this);
			break;
		case 12:

			if (gui.rightDoorShut) {
				roomLocation = 3;
			} else {
				gui.gameOver(false);
			}
			prevLocation = 12;
			gui.mapRefresh(roomLocation, prevLocation, this);
			break;
		case 14:
			if (check >= 1) {
				roomLocation = 7;
			} else {
				roomLocation = 5;
			}
			prevLocation = 10;
			gui.mapRefresh(roomLocation, prevLocation, this);
			break;
		}
	}

	/**
	 * Represents a debug mode for the ForrestRight monster's movement. This mode
	 * simplifies the monster's movement for debugging and development purposes.
	 */
	public void debugRoom() {
		int check = (int) (Math.random() * 4);
		switch (roomLocation) {
		case 0:
			roomLocation = 1;
			prevLocation = 0;
			gui.mapRefresh(roomLocation, prevLocation, ForrestRight.this);
			break;
		case 1:
			roomLocation = 0;
			prevLocation = 1;
			gui.mapRefresh(roomLocation, prevLocation, ForrestRight.this);
			break;
		}
	}
}
