
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Represents the settings interface of the game where the user can customize
 * game settings like volume, night, and monster difficulty.
 */
public class SettingsGUI {
	// Fields for game mechanics
	private GameSequence game;
	private GUIBuilder gui;
	private SoundEngine sound;
	// UI components for the settings screen
	private JButton backButton;
	private JCheckBox debugCheckBox;
	private JLabel leftieCustomLabel;
	private JLabel rightieCustomLabel;
	private JLabel graysonCustomLabel;
	private JLabel griddyCustomLabel;
	private JButton leftieIncreaseButton;
	private JButton leftieDecreaseButton;
	private JButton rightieIncreaseButton;
	private JButton rightieDecreaseButton;
	private JButton graysonIncreaseButton;
	private JButton graysonDecreaseButton;
	private JButton griddyIncreaseButton;
	private JButton griddyDecreaseButton;
	private JButton customButton;

	private JLabel volumeCustomLabel;
	private JButton volumeIncreaseButton;
	private JButton volumeDecreaseButton;

	private JLabel nightCustomLabel;
	private JButton nightIncreaseButton;
	private JButton nightDecreaseButton;

	/**
	 * Initializes the settings screen with basic UI components.
	 */
	public SettingsGUI() {
		backButton = new JButton("Back");
		backButton.setBounds(10, 10, 100, 50);

		debugCheckBox = new JCheckBox("Unlock All? (Check then uncheck once to enable)");
		debugCheckBox.setForeground(Color.WHITE);
		debugCheckBox.setBounds(10, 100, 500, 25);
		debugCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					System.out.println("DEBUG MODE ENABLED");
					gui.displayStoryStar = true;
					gui.displayHardStar = true;
					for (int i = 0; i < 3; i++) {
						Monster monster = game.getMonster(i);
						monster.setDifficulty(20);
						monster.debug = true;
						game.movementDelay = 5000;
					}
				} else {
					System.out.println("DEBUG MODE DISABLED");
					for (int i = 0; i < 3; i++) {
						Monster monster = game.getMonster(i);
						monster.setDifficulty(6);
						monster.debug = false;
						game.movementDelay = 60000;
					}
				}
			}
		});
	}

	/**
	 * Sets up volume-related UI components.
	 */
	public void volumeSetup() {
		volumeCustomLabel = new JLabel("Volume Level: " + sound.getVolume());
		volumeCustomLabel.setOpaque(true);
		volumeCustomLabel.setBackground(Color.LIGHT_GRAY);
		volumeCustomLabel.setForeground(Color.BLACK);
		volumeCustomLabel.setBounds(10, 150, 200, 50);
		volumeCustomLabel.setHorizontalAlignment(SwingConstants.CENTER);

		volumeDecreaseButton = new JButton("<");
		volumeDecreaseButton.addActionListener(e -> {
			int volume = sound.getVolume();
			if (volume > 0) {
				volume = volume - 5;
				sound.setVolume(volume);
				volumeCustomLabel.setText("Volume Level: " + volume);
			}
		});

		volumeIncreaseButton = new JButton(">");
		volumeIncreaseButton.addActionListener(e -> {
			int volume = sound.getVolume();
			if (volume < 100) {
				volume = volume + 5;
				sound.setVolume(volume);
				volumeCustomLabel.setText("Volume Level: " + volume);
			}
		});

		volumeDecreaseButton.setBounds(215, 150, 50, 50);
		volumeIncreaseButton.setBounds(270, 150, 50, 50);
	}

	/**
	 * Sets up night-related UI components.
	 */
	public void nightSetup() {
		nightCustomLabel = new JLabel("Night: " + (game.night + 1));
		nightCustomLabel.setOpaque(true);
		nightCustomLabel.setBackground(Color.LIGHT_GRAY);
		nightCustomLabel.setForeground(Color.BLACK);
		nightCustomLabel.setBounds(10, 250, 200, 50);
		nightCustomLabel.setHorizontalAlignment(SwingConstants.CENTER);

		nightDecreaseButton = new JButton("<");
		nightDecreaseButton.addActionListener(e -> {
			int night = game.night;
			if (night > 0) {
				night = night - 1;
				game.night = night;
				nightCustomLabel.setText("Night: " + (night + 1));
			}
		});

		nightIncreaseButton = new JButton(">");
		nightIncreaseButton.addActionListener(e -> {
			int night = game.night;
			if (night < 4) {
				night = night + 1;
				game.night = night;
				nightCustomLabel.setText("Night: " + (night + 1));
			}
		});

		nightDecreaseButton.setBounds(215, 250, 50, 50);
		nightIncreaseButton.setBounds(270, 250, 50, 50);

	}

	/**
	 * Sets up custom difficulty menu settings for the monsters.
	 */
	public void customSetup() {
		leftieCustomLabel = new JLabel("Leftie Difficulty: " + game.getMonster(0).getDifficulty());
		leftieCustomLabel.setOpaque(true);
		leftieCustomLabel.setBackground(Color.GRAY);
		leftieCustomLabel.setForeground(Color.decode("#6D1189"));
		leftieCustomLabel.setBounds(10, 100, 200, 50);
		leftieCustomLabel.setHorizontalAlignment(SwingConstants.CENTER);

		rightieCustomLabel = new JLabel("Rightie Difficulty: " + game.getMonster(1).getDifficulty());
		rightieCustomLabel.setOpaque(true);
		rightieCustomLabel.setBackground(Color.GRAY);
		rightieCustomLabel.setForeground(Color.decode("#0C6202"));
		rightieCustomLabel.setBounds(10, 200, 200, 50);
		rightieCustomLabel.setHorizontalAlignment(SwingConstants.CENTER);

		graysonCustomLabel = new JLabel("Grayson Giraffe Difficulty: " + game.getMonster(2).getDifficulty());
		graysonCustomLabel.setOpaque(true);
		graysonCustomLabel.setBackground(Color.GRAY);
		graysonCustomLabel.setForeground(Color.decode("#E6EB5D"));
		graysonCustomLabel.setBounds(10, 300, 200, 50);
		graysonCustomLabel.setHorizontalAlignment(SwingConstants.CENTER);

		griddyCustomLabel = new JLabel("Griddy Difficulty: " + game.getMonster(3).getDifficulty());
		griddyCustomLabel.setOpaque(true);
		griddyCustomLabel.setBackground(Color.GRAY);
		griddyCustomLabel.setForeground(Color.ORANGE);
		griddyCustomLabel.setBounds(10, 400, 200, 50);
		griddyCustomLabel.setHorizontalAlignment(SwingConstants.CENTER);

		leftieDecreaseButton = new JButton("<");
		leftieDecreaseButton.addActionListener(e -> {
			int difficulty = game.getMonster(0).getDifficulty();
			if (difficulty > 0) {
				difficulty--;
				game.getMonster(0).setDifficulty(difficulty);
				leftieCustomLabel.setText("Leftie Difficulty: " + difficulty);
			}
		});

		leftieIncreaseButton = new JButton(">");
		leftieIncreaseButton.addActionListener(e -> {
			int difficulty = game.getMonster(0).getDifficulty();
			if (difficulty < 20) {
				difficulty++;
				game.getMonster(0).setDifficulty(difficulty);
				leftieCustomLabel.setText("Leftie Difficulty: " + difficulty);
			}
		});

		rightieDecreaseButton = new JButton("<");
		rightieDecreaseButton.addActionListener(e -> {
			int difficulty = game.getMonster(1).getDifficulty();
			if (difficulty > 0) {
				difficulty--;
				game.getMonster(1).setDifficulty(difficulty);
				rightieCustomLabel.setText("Rightie Difficulty: " + difficulty);
			}
		});

		rightieIncreaseButton = new JButton(">");
		rightieIncreaseButton.addActionListener(e -> {
			int difficulty = game.getMonster(1).getDifficulty();
			if (difficulty < 20) {
				difficulty++;
				game.getMonster(1).setDifficulty(difficulty);
				rightieCustomLabel.setText("Rightie Difficulty: " + difficulty);
			}
		});

		graysonDecreaseButton = new JButton("<");
		graysonDecreaseButton.addActionListener(e -> {
			int difficulty = game.getMonster(2).getDifficulty();
			if (difficulty > 0) {
				difficulty--;
				game.getMonster(2).setDifficulty(difficulty);
				graysonCustomLabel.setText("Grayson Giraffe Difficulty: " + difficulty);
			}
		});

		graysonIncreaseButton = new JButton(">");
		graysonIncreaseButton.addActionListener(e -> {
			int difficulty = game.getMonster(2).getDifficulty();
			if (difficulty < 20) {
				difficulty++;
				game.getMonster(2).setDifficulty(difficulty);
				graysonCustomLabel.setText("Grayson Giraffe Difficulty: " + difficulty);
			}
		});

		griddyDecreaseButton = new JButton("<");
		griddyDecreaseButton.addActionListener(e -> {
			int difficulty = game.getMonster(3).getDifficulty();
			if (difficulty > 0) {
				difficulty--;
				game.getMonster(3).setDifficulty(difficulty);
				griddyCustomLabel.setText("Griddy Difficulty: " + difficulty);
			}
		});

		griddyIncreaseButton = new JButton(">");
		griddyIncreaseButton.addActionListener(e -> {
			int difficulty = game.getMonster(3).getDifficulty();
			if (difficulty < 20) {
				difficulty++;
				game.getMonster(3).setDifficulty(difficulty);
				griddyCustomLabel.setText("Griddy Difficulty: " + difficulty);
			}
		});

		leftieDecreaseButton.setBounds(215, 100, 50, 50);
		leftieIncreaseButton.setBounds(270, 100, 50, 50);
		rightieDecreaseButton.setBounds(215, 200, 50, 50);
		rightieIncreaseButton.setBounds(270, 200, 50, 50);
		graysonDecreaseButton.setBounds(215, 300, 50, 50);
		graysonIncreaseButton.setBounds(270, 300, 50, 50);
		griddyDecreaseButton.setBounds(215, 400, 50, 50);
		griddyIncreaseButton.setBounds(270, 400, 50, 50);

		customButton = new JButton("Start Custom Night");
		customButton.setBounds(600, 500, 150, 50);
		customButton.setOpaque(true);
		customButton.setForeground(Color.BLUE);
	}

	// Getter for game
	public GameSequence getGame() {
		return game;
	}

	// Setter for game
	public void setGame(GameSequence game) {
		this.game = game;
	}

	// Getter for gui
	public GUIBuilder getGui() {
		return gui;
	}

	// Setter for gui
	public void setGui(GUIBuilder gui) {
		this.gui = gui;
	}

	// Getter for sound
	public SoundEngine getSound() {
		return sound;
	}

	// Setter for sound
	public void setSound(SoundEngine sound) {
		this.sound = sound;
	}

	/**
	 * Constructs the main settings screen on the provided frame.
	 * 
	 * @param frame The JFrame to display the settings on.
	 */
	public void construct(JFrame frame) {
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.remove(backButton);
				frame.remove(debugCheckBox);
				frame.remove(volumeCustomLabel);
				frame.remove(volumeDecreaseButton);
				frame.remove(volumeIncreaseButton);
				frame.remove(nightCustomLabel);
				frame.remove(nightDecreaseButton);
				frame.remove(nightIncreaseButton);
				backButton.removeActionListener(this);
				sound.playCameraSound(0, true);
				gui.rebuild();
				frame.repaint();
			}
		});
		frame.getContentPane().add(debugCheckBox);
		frame.getContentPane().add(backButton);
		frame.getContentPane().add(volumeCustomLabel);
		frame.getContentPane().add(volumeIncreaseButton);
		frame.getContentPane().add(volumeDecreaseButton);
		frame.getContentPane().add(nightCustomLabel);
		frame.getContentPane().add(nightIncreaseButton);
		frame.getContentPane().add(nightDecreaseButton);
		frame.repaint();
	}

	/**
	 * Constructs the custom night settings screen on the provided frame.
	 * 
	 * @param frame The JFrame to display the custom settings on.
	 */
	public void custom(JFrame frame) {
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.remove(backButton);
				frame.remove(customButton);
				frame.remove(leftieCustomLabel);
				frame.remove(leftieDecreaseButton);
				frame.remove(leftieIncreaseButton);
				frame.remove(rightieCustomLabel);
				frame.remove(rightieDecreaseButton);
				frame.remove(rightieIncreaseButton);
				frame.remove(graysonCustomLabel);
				frame.remove(graysonDecreaseButton);
				frame.remove(graysonIncreaseButton);
				frame.remove(griddyCustomLabel);
				frame.remove(griddyDecreaseButton);
				frame.remove(griddyIncreaseButton);
				backButton.removeActionListener(this);
				sound.playCameraSound(0, true);
				gui.rebuild();
				frame.repaint();
			}
		});

		customButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.remove(backButton);
				frame.remove(customButton);
				frame.remove(leftieCustomLabel);
				frame.remove(leftieDecreaseButton);
				frame.remove(leftieIncreaseButton);
				frame.remove(rightieCustomLabel);
				frame.remove(rightieDecreaseButton);
				frame.remove(rightieIncreaseButton);
				frame.remove(graysonCustomLabel);
				frame.remove(graysonDecreaseButton);
				frame.remove(graysonIncreaseButton);
				frame.remove(griddyCustomLabel);
				frame.remove(griddyDecreaseButton);
				frame.remove(griddyIncreaseButton);

				backButton.removeActionListener(this);
				customButton.removeActionListener(this);
				sound.stopAmbientSound(0);
				sound.playCameraSound(0, true);
				game.night = 6;
				gui.construct();
				game.start();
			}
		});

		frame.getContentPane().add(backButton);
		frame.getContentPane().add(customButton);
		frame.getContentPane().add(leftieCustomLabel);
		frame.getContentPane().add(leftieDecreaseButton);
		frame.getContentPane().add(leftieIncreaseButton);
		frame.getContentPane().add(rightieCustomLabel);
		frame.getContentPane().add(rightieDecreaseButton);
		frame.getContentPane().add(rightieIncreaseButton);
		frame.getContentPane().add(graysonCustomLabel);
		frame.getContentPane().add(graysonDecreaseButton);
		frame.getContentPane().add(graysonIncreaseButton);
		frame.getContentPane().add(griddyCustomLabel);
		frame.getContentPane().add(griddyDecreaseButton);
		frame.getContentPane().add(griddyIncreaseButton);
		frame.repaint();

	}
}
