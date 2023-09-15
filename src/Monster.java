/**
 * Represents an abstract Monster class that outlines the common properties and
 * behaviors of different types of monsters in the game. Each monster has a
 * name, a room location, a difficulty level, and a jump scare associated with
 * it. The Monster class also provides mechanisms to interact with the GUI and
 * debugging.
 * 
 * Specific monster types are expected to extend this class and implement their
 * own movement and other specific behaviors.
 */
public abstract class Monster {
	/** The name of the monster. */
	public String name;

	/** The room location of the monster represented as an integer. */
	public int roomLocation;

	/** The difficulty level of the monster. */
	public int difficulty;

	/** Represents the jump scare visuals of the monster. */
	public IconHolder jumpscare;

	/**
	 * The GUIBuilder instance to interact with the game's graphical user interface.
	 */
	public GUIBuilder gui;

	/** Debug flag for development/testing purposes. */
	public boolean debug;

	/**
	 * Constructs a new Monster with a specified name, starting room, and
	 * GUIBuilder.
	 *
	 * @param name  The name of the monster.
	 * @param start The starting room location of the monster.
	 * @param other The GUIBuilder instance.
	 */
	public Monster(String name, int start, GUIBuilder other) {
		this.name = name;
		this.roomLocation = start;
		this.difficulty = 6;
		IconHolder jumpscare = new IconHolder();
		this.gui = other;
		this.debug = false;
	}

	/**
	 * Returns the name of the monster.
	 *
	 * @return The name of the monster.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the monster.
	 *
	 * @param name The new name for the monster.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the room location of the monster.
	 *
	 * @return The room location of the monster.
	 */
	public int getRoomLocation() {
		return roomLocation;
	}

	/**
	 * Sets the room location of the monster.
	 *
	 * @param roomLocation The new room location for the monster.
	 */
	public void setRoomLocation(int roomLocation) {
		this.roomLocation = roomLocation;
	}

	/**
	 * Returns the difficulty level of the monster.
	 *
	 * @return The difficulty level of the monster.
	 */
	public int getDifficulty() {
		return difficulty;
	}

	/**
	 * Sets the difficulty level of the monster.
	 *
	 * @param difficulty The new difficulty level for the monster.
	 */
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	/**
	 * Placeholder method for starting a timer associated with the monster. Specific
	 * monster types should provide their own implementation.
	 */
	public void startTimer() {

	}

	/**
	 * Placeholder method for stopping a timer associated with the monster. Specific
	 * monster types should provide their own implementation.
	 */
	public void stopTimer() {

	}

	/**
	 * Placeholder method for the movement mechanism for the monster. Specific
	 * monster types should provide their own implementation.
	 */
	public void movementRoll() {
	}

}
