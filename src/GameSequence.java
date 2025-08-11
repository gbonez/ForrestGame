
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.Clip;

/**
 * Represents the main game logic and sequence for a monster-themed game. It
 * manages the interactions of various game entities such as monsters, the
 * graphical user interface, sound, and game settings.
 * 
 * This class keeps track of game attributes such as power, time, night, and
 * various timers that dictate the game's flow.
 */
public class GameSequence {

	/** Instance of the game's graphical user interface. */
	private GUIBuilder gui;

	/** Instance of the game's settings interface. */
	private SettingsGUI settings;

	/** Engine to manage and play game sounds. */
	private SoundEngine sound;

	/** List to manage the monsters in the game. */
	private List<Monster> monsters;

	/** Current power level in the game. */
	private double power = 100.00;

	/** Default delay time in milliseconds for game operations. */
	private int defaultDelay = 9600;

	/** Delay for monster movements. */
	public int movementDelay = 60000;

	/** Delay for initiating scary events. */
	private int scaryDelay = 70000;

	/** Timer for managing the power drain in the game. */
	private PowerTimer powerTimer;
	private long lastUpdateTime;
	private double powerRatio = 1.0;

	/** Timer to handle delays between monster movements. */
	private Timer moveDelayTimer = new Timer();

	/** Timer to keep track of the game's hourly progression. */
	private Timer hourTimer = new Timer();

	/** Task to be executed for hourly game updates. */
	private TimerTask hourTask;

	/** Timer to manage scary events in the game. */
	private Timer scaryTimer = new Timer();

	/** Task to be executed for scary game events. */
	private TimerTask scaryTask;

	/** Task to manage monster movements. */
	private TimerTask movementTask;

	/** Timestamp of the last time power was updated. */
	private long lastUpdated = 0;

	/** Delay threshold for throttling power updates. */
	private static final int THROTTLE_DELAY = 250;

	/** Current time in the game, indicating progression through the night. */
	public int time;

	/** Current night in the game, influencing game difficulty. */
	public int night;

	/**
	 * Initializes a new GameSequence with default configurations.
	 */
	public GameSequence() {
		monsters = new ArrayList<Monster>();
		night = 0;
		lastUpdateTime = System.currentTimeMillis();
		powerTimer = new PowerTimer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long currentTime = System.currentTimeMillis();
				long elapsed = currentTime - lastUpdateTime;
				lastUpdateTime = currentTime;
				double secondsElapsed = elapsed / 1000.0;
				powerCalc();
				double drainPerSecond = 1.0 / 9.6;
				double actualDrain = secondsElapsed * drainPerSecond / powerRatio;
				double rateOfLoss = drainPerSecond / powerRatio;
				double estimatedTimeSec = power / rateOfLoss;
				int minutes = (int) (estimatedTimeSec / 60);
				int seconds = (int) (estimatedTimeSec % 60);
				System.out.printf("[DEBUG] Power Ratio: %.2f | Loss Rate: %.4f%%/s | Est.time to 0%%: %d:%02d\n",
						powerRatio, rateOfLoss, minutes, seconds);
				power -= actualDrain;
				gui.updatePower();

			}
		});
	}

	/**
	 * Initializes the monsters for the game and adds them to the monsters list.
	 */
	public void buildMonsters() {
		monsters.add(new ForrestLeft("ForrestLeft", 0, gui));
		monsters.add(new ForrestRight("ForrestRight", 0, gui));
		monsters.add(new GraysonGiraffe("GraysonGiraffe", 0, gui));
		monsters.add(new Griddy("Griddy", 0, gui));
	}

	// Getter for gui
	public GUIBuilder getGui() {
		return gui;
	}

	// Setter for gui
	public void setGui(GUIBuilder gui) {
		this.gui = gui;
	}

	// Getter for settings
	public SettingsGUI getSettings() {
		return settings;
	}

	// Setter for settings
	public void setSettings(SettingsGUI settings) {
		this.settings = settings;
	}

	// Getter for sound
	public SoundEngine getSound() {
		return sound;
	}

	// Setter for sound
	public void setSound(SoundEngine sound) {
		this.sound = sound;
	}

	// Getter for Monsters
	public Monster getMonster(int value) {
		return monsters.get(value);
	}

	// Getter for power
	public double getPower() {
		return power;
	}

	// Setter for power
	public void setPower(double newPow) {
		power = newPow;
	}

	/**
	 * Schedules various tasks required for game progression, such as updating
	 * hours, playing scary sounds, and initiating monster movements.
	 */
	private void scheduleTasks() {
		hourTask = new TimerTask() {
			@Override
			public void run() {
				if (time == 5) {
					gui.gameOver(true);
				} else {
					if (time != 0 && time != 4) {
						for (int i = 0; i < monsters.size(); i++) {
							Monster monster = monsters.get(i);
							if (monster.getDifficulty() > 0) {
								monster.setDifficulty(monster.getDifficulty() + 1);
							}
						}
					}
					time++;
					gui.updateTime(time);
				}
			}
		};
		scaryTask = new TimerTask() {
			@Override
			public void run() {
				List<Clip> scarySounds = sound.getScarySounds();
				sound.stopAllSounds(scarySounds);
				int random = (int) (Math.random() * scarySounds.size());
				sound.playScarySound(random, true);
			}
		};

		movementTask = new TimerTask() {
			@Override
			public void run() {
				for (int i = 0; i < monsters.size(); i++) {
					monsters.get(i).startTimer();
				}
				this.cancel();
			}

		};

	}

	/**
	 * Sets the difficulty for the game based on the current night.
	 */
	private void nightDifficulty() {
		int left = 6;
		int right = 6;
		int pirate = 6;
		int griddy = 6;
		time = 0;
		power = 100;
		switch (night) {
		case 0:
			left = 0;
			right = 0;
			pirate = -3;
			griddy = 0;
			movementDelay = 120000;
			scaryDelay = 180000;
			break;
		case 1:
			left = 2;
			right = 3;
			pirate = 0;
			griddy = 1;
			movementDelay = 90000;
			scaryDelay = 120000;
			break;
		case 2:
			left = 3;
			right = 5;
			pirate = 2;
			griddy = 2;
			movementDelay = 35000;
			scaryDelay = 450000;
			break;
		case 3:
			left = 4;
			right = 6;
			pirate = 6;
			griddy = 4;
			movementDelay = 20000;
			scaryDelay = 30000;
			break;
		case 4:
			left = 5;
			right = 7;
			pirate = 7;
			griddy = 5;
			movementDelay = 15000;
			scaryDelay = 30000;
			break;
		case 5:
			left = 10;
			right = 12;
			pirate = 14;
			griddy = 8;
			movementDelay = 10000;
			scaryDelay = 30000;
			break;
		case 6:

			movementDelay = 10000;
			scaryDelay = 30000;
			return;
		}
		monsters.get(0).setDifficulty(left);
		monsters.get(1).setDifficulty(right);
		monsters.get(2).setDifficulty(pirate);
		monsters.get(3).setDifficulty(griddy);
	}

	/**
	 * Initiates the start of the game, setting up timers and beginning the game
	 * loop.
	 */
	public void start() {
		time = 0;
		power = 100;
		nightDifficulty();

		powerTimer.start();
		scheduleTasks();
		hourTimer.scheduleAtFixedRate(hourTask, 70000, 70000); // 89170, 89170
		moveDelayTimer.schedule(movementTask, movementDelay);
		scaryTimer.schedule(scaryTask, scaryDelay, 20000);
	}

	/**
	 * Drains the power by a given value.
	 *
	 * @param minus The amount of power to be drained.
	 */
	public void powerDrain(double minus) {
		power = power - minus;
	}

	/**
	 * Calculates and updates the power based on the current state of the game.
	 */
	public void powerCalc() {
		boolean leftDoor = gui.leftDoorShut;
		boolean rightDoor = gui.rightDoorShut;
		boolean camera = gui.cameraOpened;
		boolean light = gui.leftLightHeld || gui.rightLightHeld;

		boolean anyDoor = leftDoor || rightDoor;
		boolean bothDoors = leftDoor && rightDoor;

		if (leftDoor && rightDoor && camera && light) {
			powerRatio = 0.15;
		} else if ((camera || light) && bothDoors) {
			powerRatio = 0.15;
		} else if ((camera || light) && anyDoor) {
			powerRatio = 0.25;
		} else if (bothDoors) {
			powerRatio = 0.25;
		} else if (camera || light || anyDoor) {
			powerRatio = 0.5;
		} else {
			powerRatio = 1.0;
		}
	}

	/**
	 * Handles game-over operations, resets monsters, stops timers, and reverts the
	 * game to its initial state.
	 */
	public void gameOver() {
		hourTask.cancel();
		scaryTask.cancel();
		movementTask.cancel();
		powerTimer.stop();
		for (int i = 0; i < monsters.size(); i++) {
			Monster monster = monsters.get(i);
			monster.setRoomLocation(0);
			monster.stopTimer();
			monster.setDifficulty(0);
		}
	}

}
