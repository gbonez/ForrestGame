/**
 * The Runner class serves as the main entry point for the game application. It
 * initializes and configures various components of the game such as the GUI,
 * sound engine, game sequence, and settings.
 */
public class Runner {

	/** The core game sequence handler for the game. */
	static GameSequence game = new GameSequence();

	/** The GUI builder to set up and manage the game's graphical interface. */
	static GUIBuilder gui = new GUIBuilder(game);

	/** The sound engine responsible for managing and playing game sounds. */
	static SoundEngine sound = new SoundEngine();

	/** The interface for modifying and retrieving game settings. */
	static SettingsGUI settings = new SettingsGUI();

	/**
	 * The main method serves as the starting point for the game application. It
	 * initializes all core components and ensures they're configured to work
	 * together.
	 *
	 * @param args Command-line arguments (currently not used).
	 */

	public static void main(String[] args) {
		game.setSound(sound);
		game.setGui(gui);
		game.setSettings(settings);
		gui.setSound(sound);
		gui.setSettings(settings);
		settings.setGame(game);
		settings.setGui(gui);
		settings.setSound(sound);
		sound.constructSounds();
		game.buildMonsters();
		settings.customSetup();
		settings.volumeSetup();
		settings.nightSetup();
		gui.initialize();
	}

}
