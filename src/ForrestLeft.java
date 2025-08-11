
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * Represents a ForrestLeft monster which appears to have specific movement
 * patterns on the left side of the game interface.
 * 
 * This monster can interact with game elements such as doors and lights. It has
 * a specific movement pattern defined by its current location.
 * 
 * @see Monster
 */
public class ForrestLeft extends Monster {

	/** Holds the previous room location of the monster before its last movement. */
	private int prevLocation;

	/** A constant representing the movement duration in milliseconds. */
	private static final int MOVEMENT = 4500;

	/** Timer responsible for managing the monster's movement intervals. */
	private Timer movementTimer;

	/**
	 * Constructs a new ForrestLeft monster with a specified name, starting room,
	 * and GUIBuilder. Initializes its movementTimer to manage the monster's
	 * movement behavior.
	 *
	 * @param name  The name of the ForrestLeft monster.
	 * @param start The starting room location for the monster.
	 * @param other The GUIBuilder instance used to interact with the game's
	 *              graphical interface.
	 */
	public ForrestLeft(String name, int start, GUIBuilder other) {
		super(name, start, other);
		prevLocation = 1;
		movementTimer = new Timer(MOVEMENT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (debug) {
					debugRoom();
				} else if (gui.leftDoorShut && roomLocation == 11) {
					roomLocation = 2;
					prevLocation = 11;
					gui.mapRefresh(roomLocation, prevLocation, ForrestLeft.this);
				} else if (gui.leftLightHeld && roomLocation == 9) {
					gui.powerDrop(.1);
					roomLocation = 2;
					prevLocation = 9;
					gui.mapRefresh(roomLocation, prevLocation, ForrestLeft.this);
				} else {
					movementRoll();
				}
			}
		});
		movementTimer.setRepeats(true);
	}

	/**
	 * Starts the movement timer for the ForrestLeft monster.
	 */
	@Override
	public void startTimer() {
		movementTimer.start();
	}

	/**
	 * Stops the movement timer for the ForrestLeft monster.
	 */
	@Override
	public void stopTimer() {
		movementTimer.stop();
	}

	/**
	 * Overrides the setRoomLocation method to also reset the previous location of
	 * the ForrestLeft monster.
	 *
	 * @param roomLocation The new room location for the ForrestLeft monster.
	 */
	@Override
	public void setRoomLocation(int roomLocation) {
		this.roomLocation = roomLocation;
		this.prevLocation = 0;
	}

	/**
	 * Dictates the movement behavior of the ForrestLeft monster. It decides based
	 * on a random number and the monster's current difficulty whether the movement
	 * will be successful.
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
	 * Handles successful movement for the ForrestLeft monster. Depending on its
	 * current room and a random number, the monster might move to a new room or
	 * interact with the game's components like doors.
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
			roomLocation = 2;
			prevLocation = 1;
			gui.mapRefresh(roomLocation, prevLocation, this);
			break;
		case 2:
			roomLocation = 6;
			prevLocation = 2;
			gui.mapRefresh(roomLocation, prevLocation, this);
			break;
		case 4:
			if (check >= 2) {
				roomLocation = 6;
			} else if (check == 1) {
				roomLocation = 9;
			} else {
				roomLocation = 2;
			}
			prevLocation = 4;
			gui.mapRefresh(roomLocation, prevLocation, this);
			break;
		case 6:
			if (check >= 1) {
				roomLocation = 9;
			} else {
				roomLocation = 2;
			}
			prevLocation = 6;
			gui.mapRefresh(roomLocation, prevLocation, this);
			break;
		case 8:
			if (check == 1) {
				roomLocation = 2;
			} else {
				roomLocation = 9;
			}
			prevLocation = 8;
			gui.mapRefresh(roomLocation, prevLocation, this);
			break;
		case 9:
			if (check >= 2) {
				roomLocation = 11;
			} else if (check == 1) {
				roomLocation = 8;
			} else {
				roomLocation = 2;
			}
			prevLocation = 9;
			gui.mapRefresh(roomLocation, prevLocation, this);
			break;
		case 11:
			if (gui.leftDoorShut) {
				roomLocation = 2;
			} else {
				gui.gameOver(false);
			}
			prevLocation = 11;
			gui.mapRefresh(roomLocation, prevLocation, this);
			break;
		}
	}

	/**
	 * Represents a debug mode for the ForrestLeft monster's movement. Simplifies
	 * the movement patterns for debugging and development purposes.
	 */
	public void debugRoom() {
		int check = (int) (Math.random() * 4);
		switch (roomLocation) {
		case 0:
			roomLocation = 11;
			prevLocation = 0;
			gui.mapRefresh(roomLocation, prevLocation, ForrestLeft.this);
			break;
		case 11:
			if (gui.leftDoorShut) {
				roomLocation = 2;
			} else {
				gui.gameOver(false);
				break;
			}
			prevLocation = 11;
			gui.mapRefresh(roomLocation, prevLocation, this);
			break;
		}
	}
}
