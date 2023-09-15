import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * The GUIBuilder class constructs GUI components for a game application.
 */
public class GUIBuilder {
	private GameSequence game;
	private CameraGUI camera;
	private SettingsGUI settings;
	private SoundEngine sound;
	private IconHolder icons;
	private JFrame frame;
	private JLayeredPane layeredPane;
	private Map<String, JLabel> labels;
	private Map<String, JButton> mainButtons;
	private Map<String, JButton> cameraButtons;
	private JButton startButton;
	private JButton settingsButton;
	private JButton hardButton;
	private JButton customButton;
	private JLabel storyStar;
	private JLabel hardStar;
	private int movementDelta;

	private Timer spookTimer;
	private Timer jumpscareTimer;
	private Timer nightShiftTimer;
	private Timer moveAroundTimer;
	private Timer griddyTimer;
	private Timer transitionTimer;

	public boolean leftDoorShut;
	public boolean rightDoorShut;
	public boolean cameraOpened;
	public boolean leftLightHeld;
	public boolean rightLightHeld;
	public boolean jumpscareIncoming;
	public boolean displayStoryStar;
	public boolean displayHardStar;
	public boolean hardModeEnabled;
	public boolean griddyInOffice;

	/**
	 * Constructs a GUIBuilder object with an initial GameSequence.
	 *
	 * @param other The initial GameSequence to be associated with this GUIBuilder.
	 */
	public GUIBuilder(GameSequence other) {

		game = other;
		icons = new IconHolder();
		labels = new HashMap<String, JLabel>();
		mainButtons = new HashMap<String, JButton>();
		frame = new JFrame("Fun Time with Forrest");
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		layeredPane = new JLayeredPane();
		camera = new CameraGUI(game);
		spookTimer = new Timer(45000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (game.getMonster(0).getRoomLocation() < 9 && game.getMonster(1).getRoomLocation() < 10) {
					spookTimer.stop();
				}
			}
		});
		jumpscareTimer = new Timer(0, null);

		nightShiftTimer = new javax.swing.Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getContentPane().removeAll();
				construct();
				game.start();
				nightShiftTimer.stop();
			}
		});

		transitionTimer = new javax.swing.Timer(3500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sound.playCameraSound(0, true);
				construct();
				game.start();
			}
		});
		transitionTimer.setRepeats(false);

		// OFFICE MOVEMENT
		moveAroundTimer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Point mousePosition = MouseInfo.getPointerInfo().getLocation();
				SwingUtilities.convertPointFromScreen(mousePosition, frame.getContentPane());
				moveBackground(mousePosition);
			}
		});

		// GRIDDY
		griddyTimer = new Timer(10000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				labels.get("griddyLabel").setVisible(false);
				if (!cameraOpened) {
					labels.get("backgroundLabel").repaint();
				}
				sound.stopForrestSound(7);
				game.getMonster(3).setRoomLocation(0);
				griddyInOffice = false;
				griddyTimer.stop();
			}
		});
		movementDelta = 8;
		displayStoryStar = false;
		displayHardStar = false;
		hardModeEnabled = false;
		griddyInOffice = false;
	}

	// Getters and Setters for private member variables

	/**
	 * Get the current GameSequence associated with this GUIBuilder.
	 *
	 * @return The current GameSequence.
	 */
	public GameSequence getGame() {
		return game;
	}

	/**
	 * Set the GameSequence for this GUIBuilder.
	 *
	 * @param game The GameSequence to set.
	 */
	public void setGame(GameSequence game) {
		this.game = game;
	}

	/**
	 * Get the current SettingsGUI associated with this GUIBuilder.
	 *
	 * @return The current SettingsGUI.
	 */
	public SettingsGUI getSettings() {
		return settings;
	}

	/**
	 * Set the SettingsGUI for this GUIBuilder.
	 *
	 * @param settings The SettingsGUI to set.
	 */
	public void setSettings(SettingsGUI settings) {
		this.settings = settings;
	}

	/**
	 * Get the current SoundEngine associated with this GUIBuilder.
	 *
	 * @return The current SoundEngine.
	 */
	public SoundEngine getSound() {
		return sound;
	}

	/**
	 * Set the SoundEngine for this GUIBuilder.
	 *
	 * @param sound The SoundEngine to set.
	 */
	public void setSound(SoundEngine sound) {
		this.sound = sound;
	}

	// Getter for a specific label
	public JLabel getLabel(String labelKey) {
		return labels.get(labelKey);
	}

	// Setter for a specific label
	public void setLabel(String labelKey, JLabel newLabel) {
		labels.put(labelKey, newLabel);
	}

	// Getter for a specific button
	public JButton getButton(String buttonKey) {
		return mainButtons.get(buttonKey);
	}

	// Setter for a specific button
	public void setButton(String buttonKey, JButton newButton) {
		mainButtons.put(buttonKey, newButton);
	}

	/**
	 * Initialize the GUI by creating and displaying the main frame. The frame size
	 * is set to 800x600 pixels.
	 */
	public void initialize() {
		game.setPower(100);
		icons.construct();
		startButton = new JButton("Start");
		startButton.setBounds(350, 250, 100, 50);
		settingsButton = new JButton("Settings");
		settingsButton.setBounds(350, 350, 100, 50);

		hardButton = new JButton("Hell Night");
		hardButton.setBounds(350, 150, 100, 50);
		hardButton.setVisible(false);

		customButton = new JButton("Custom");
		customButton.setBounds(350, 450, 100, 50);
		customButton.setVisible(false);

		storyStar = new JLabel(icons.getIcon("star"));
		storyStar.setBounds(10, 490, 57, 55);
		storyStar.setVisible(false);

		hardStar = new JLabel(icons.getIcon("star"));
		hardStar.setBounds(70, 490, 57, 55);
		hardStar.setVisible(false);

		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hardModeEnabled = false;
				sound.stopAmbientSound(0);
				sound.playCameraSound(0, true);
				frame.remove(startButton);
				frame.remove(settingsButton);
				frame.remove(storyStar);
				frame.remove(hardStar);
				frame.remove(hardButton);
				frame.remove(customButton);
				JLabel label = new JLabel("Night " + (game.night + 1));
				label.setFont(new Font("Arial", Font.BOLD, 24));
				label.setForeground(new Color(255, 255, 255, 0));
				label.setBounds(350, 250, 100, 100);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVisible(true);
				frame.getContentPane().add(label);
				label.setForeground(Color.WHITE);
				frame.repaint();
				transitionTimer.start();
			}
		});
		startButton.setFocusPainted(false);
		frame.getContentPane().add(startButton);

		settingsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.remove(startButton);
				frame.remove(settingsButton);
				frame.remove(storyStar);
				frame.remove(hardStar);
				frame.remove(hardButton);
				frame.remove(customButton);
				sound.playCameraSound(0, true);
				settings.construct(frame);
			}
		});
		settingsButton.setFocusPainted(false);
		frame.getContentPane().add(settingsButton);
		hardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.night = 5;
				hardModeEnabled = true;
				sound.stopAmbientSound(0);
				sound.playScarySound(16, true);
				frame.remove(startButton);
				frame.remove(settingsButton);
				frame.remove(storyStar);
				frame.remove(hardStar);
				frame.remove(hardButton);
				frame.remove(customButton);
				construct();
				game.start();
			}
		});

		customButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hardModeEnabled = false;
				frame.remove(startButton);
				frame.remove(settingsButton);
				frame.remove(storyStar);
				frame.remove(hardStar);
				frame.remove(hardButton);
				frame.remove(customButton);
				sound.playCameraSound(0, true);
				settings.custom(frame);
			}
		});

		frame.setLayout(null);

		frame.getContentPane().setBackground(Color.BLACK);
		startButton.setForeground(Color.BLACK);
		settingsButton.setForeground(Color.BLACK);
		hardButton.setForeground(Color.RED);
		customButton.setForeground(Color.BLUE);

		frame.setVisible(true);

		sound.playAmbientSound(0, false);
	}

	public void rebuild() {
		if (nightShiftTimer.isRunning()) {
			nightShiftTimer.stop();
		}
		if (moveAroundTimer.isRunning()) {
			moveAroundTimer.stop();
		}
		frame.getContentPane().add(startButton);
		frame.getContentPane().add(settingsButton);
		if (displayStoryStar) {
			frame.getContentPane().add(storyStar);
			frame.getContentPane().add(hardButton);
			frame.getContentPane().add(customButton);
			storyStar.setVisible(true);
			hardButton.setVisible(true);
			customButton.setVisible(true);
			if (displayHardStar) {
				frame.getContentPane().add(hardStar);
				hardStar.setVisible(true);
			}
		}
		frame.setLayout(null);
		frame.repaint();
	}

	public void construct() {
		if (moveAroundTimer.isRunning()) {
			moveAroundTimer.stop();
		}

		labels = null;
		labels = new HashMap<String, JLabel>();
		mainButtons = null;
		mainButtons = new HashMap<String, JButton>();

		ImageIcon office = icons.getIcon("office");
		Image temp = office.getImage();
		int windowWidth = frame.getWidth();
		int windowHeight = frame.getHeight();
		int imageWidth = office.getIconWidth();
		int imageHeight = office.getIconHeight();

		int newImageWidth = (int) (((double) windowHeight / imageHeight * imageWidth));
		Image resizedImage = temp.getScaledInstance(newImageWidth, windowHeight, Image.SCALE_SMOOTH);
		office.setImage(resizedImage);

		labels.put("backgroundLabel", new JLabel(office));

		int imageX = (windowWidth - newImageWidth) / 2;
		labels.get("backgroundLabel").setBounds(imageX, 0, newImageWidth, windowHeight);

		frame.setContentPane(layeredPane);
		layeredPane.setLayout(null);
		layeredPane.setBackground(Color.BLACK);
		layeredPane.add(labels.get("backgroundLabel"), JLayeredPane.DEFAULT_LAYER);

		labels.put("roomLabel", new JLabel(icons.getIcon("room2")));
		scaleIcon(labels.get("roomLabel"), 800, 600, false);
		labels.get("roomLabel").setBackground(Color.BLACK);
		labels.get("roomLabel").setOpaque(true);
		layeredPane.add(labels.get("roomLabel"), JLayeredPane.DEFAULT_LAYER);

		mainButtons.put("leftLightButton",
				ButtonGenerator.createButton(icons.getIcon("lightneutral"), 10, 298, 50, 50));
		mainButtons.put("rightLightButton",
				ButtonGenerator.createButton(icons.getIcon("lightneutral"), 1275, 298, 50, 50));
		mainButtons.put("leftDoorButton", ButtonGenerator.createButton(icons.getIcon("dooropen"), 10, 238, 50, 50));
		mainButtons.put("rightDoorButton", ButtonGenerator.createButton(icons.getIcon("dooropen"), 1275, 238, 50, 50));
		mainButtons.put("cameraButton", ButtonGenerator.createButton(icons.getIcon("camerabutton"), 318, 503, 150, 50));

		for (Map.Entry<String, JButton> entry : mainButtons.entrySet()) {
			if (entry.getKey().equals("cameraButton")) {
				layeredPane.add(entry.getValue(), JLayeredPane.PALETTE_LAYER);
			} else {
				labels.get("backgroundLabel").add(entry.getValue());
			}
		}

		List<JLabel> tempicons = DoorLightGUI.createLight(true, mainButtons.get("leftLightButton"),
				icons.getIcons("left"));
		labels.put("leftHallway", tempicons.get(0));
		labels.put("leftWall", tempicons.get(1));
		labels.put("leftWindow", tempicons.get(2));
		tempicons = DoorLightGUI.createLight(false, mainButtons.get("rightLightButton"), icons.getIcons("right"));
		labels.put("rightHallway", tempicons.get(0));
		labels.put("rightWall", tempicons.get(1));
		labels.put("rightWindow", tempicons.get(2));

		labels.put("leftDoor", DoorLightGUI.createDoor(true, icons.getIcon("left_door")));
		labels.put("rightDoor", DoorLightGUI.createDoor(false, icons.getIcon("right_door")));

		cameraButtons = ButtonGenerator.mapButtonCreation(icons);

		ImageIcon mapIcon = icons.getIcon("map");
		Image mapImage = mapIcon.getImage();
		windowWidth = layeredPane.getWidth();
		windowHeight = layeredPane.getHeight();

		mapImage = mapImage.getScaledInstance(375, 275, Image.SCALE_SMOOTH);
		ImageIcon mapImageIcon = new ImageIcon(mapImage);
		JLabel mapLabel = new JLabel(mapImageIcon);
		mapLabel.setBounds(390, 285, 375, 275);
		layeredPane.add(mapLabel, JLayeredPane.PALETTE_LAYER);
		mapLabel.setVisible(false);
		labels.put("mapLabel", mapLabel);

		camera.construct(layeredPane, labels, cameraButtons, icons, sound);

		JLabel powerLabel = new JLabel("Power: " + (int) game.getPower() + "%");
		powerLabel.setFont(powerLabel.getFont().deriveFont(16f));
		powerLabel.setForeground(Color.YELLOW);
		if (hardModeEnabled) {
			powerLabel.setForeground(Color.RED);
		}
		powerLabel.setVisible(true);
		layeredPane.add(powerLabel, JLayeredPane.MODAL_LAYER);
		powerLabel.setBounds(15, 490, 200, 100);
		labels.put("powerLabel", powerLabel);

		JLabel timeLabel = new JLabel("12 AM");
		timeLabel.setFont(powerLabel.getFont().deriveFont(28f));
		timeLabel.setForeground(Color.WHITE);
		if (hardModeEnabled) {
			timeLabel.setForeground(Color.RED);
		}
		timeLabel.setVisible(true);
		timeLabel.setBounds(690, 10, 100, 25);
		layeredPane.add(timeLabel, JLayeredPane.MODAL_LAYER);
		labels.put("timeLabel", timeLabel);

		JLabel jumpscareLabel = new JLabel(icons.getIcon("jump1"));
		jumpscareLabel.setVisible(false);
		jumpscareLabel.setBounds(0, 0, 800, 600);
		layeredPane.add(jumpscareLabel, JLayeredPane.DRAG_LAYER);
		labels.put("jumpscareLabel", jumpscareLabel);

		JLabel fartLabel = new JLabel(icons.getIcon("fartgun"));
		fartLabel.setVisible(true);
		fartLabel.setBounds(600, 315, 84, 88);
		fartLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sound.stopForrestSound(6);
				sound.playForrestSound(6, true);
			}
		});
		labels.get("backgroundLabel").add(fartLabel);
		labels.put("fartLabel", fartLabel);

		JLabel griddyLabel = new JLabel(icons.getIcon("griddy"));
		griddyLabel.setVisible(false);
		griddyLabel.setBounds(700, 200, 600, 400);
		labels.get("backgroundLabel").add(griddyLabel);
		labels.put("griddyLabel", griddyLabel);

		game.setPower(100);
		updatePower();
		frame.revalidate();
		frame.repaint();

		sound.playAmbientSound(1, false);

		leftDoorShut = false;
		rightDoorShut = false;
		cameraOpened = false;
		leftLightHeld = false;
		rightLightHeld = false;
		jumpscareIncoming = false;
		griddyInOffice = false;
		if (nightShiftTimer.isRunning()) {
			nightShiftTimer.stop();
		}
		action();
	}

	/**
	 * Generates various action and mouse listeners to allow the user to interact
	 * with the GUI
	 */
	public void action() {

		switch (game.night) {
		case 0:
			sound.playAmbientSound(3, true);
			break;
		case 1:
			sound.playAmbientSound(4, true);
			break;
		case 2:
			sound.playAmbientSound(5, true);
			break;
		case 3:
			sound.playAmbientSound(6, true);
			break;
		}

		moveAroundTimer.start();

		// MAP BUTTONS
		ActionListener mapClickListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton clickedButton = (JButton) e.getSource();
				int state = 0;
				powerDrop(.01);
				enableCameraButtons(false);
				for (Map.Entry<String, JButton> entry : cameraButtons.entrySet()) {
					if (entry.getValue().equals(clickedButton)) {
						try {
							state = Integer.parseInt(entry.getKey());
						} catch (NumberFormatException error) {
							System.err.println("Button pressed does not have a readable number key");
							System.err.println("Defaulting to Stage...");
							state = 0;
						}
						break;
					}
				}
				IconHolder staticAnimation = icons.getIcons("static");
				final int temp = state;
				staticAnimation.playAnimation(50, labels.get("roomLabel"), () -> {
					enableCameraButtons(true);
					camera.update(temp);
					layeredPane.repaint();
				});
				sound.playCameraSound(0, true);

			}
		};
		for (Entry<String, JButton> entry : cameraButtons.entrySet()) {
			JButton button = entry.getValue();
			button.addActionListener(mapClickListener);
		}

		// CAMERA
		mainButtons.get("cameraButton").addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IconHolder cameraAnimation = icons.getIcons("camani");
				JLabel backgroundLabel = labels.get("backgroundLabel");
				JLabel roomLabel = labels.get("roomLabel");
				JButton cameraButton = mainButtons.get("cameraButton");
				if (cameraOpened) {
					camera.clean();
					cameraButton.setVisible(false);
					cameraAnimation.reverse();
					sound.stopCameraSound(4);
					sound.stopCameraSound(3);
					sound.playCameraSound(5, true);
					cameraAnimation.playAnimation(20, roomLabel, () -> {
						labels.get("roomLabel").setVisible(false);
						cameraButton.setBounds(318, 503, 150, 50);
						labels.get("backgroundLabel").setVisible(true);
						backgroundLabel.setIcon(icons.getIcon("office"));
						cameraButton.setVisible(true);
						moveAroundTimer.start();
						cameraOpened = false;
						game.powerCalc();

					});
				} else {
					game.powerDrain(.035);
					backgroundLabel.setVisible(false);
					cameraButton.setVisible(false);
					moveAroundTimer.stop();
					sound.playCameraSound(4, true);
					cameraAnimation.playAnimation(20, roomLabel, () -> {
						cameraButton.setBounds(278, 503, 150, 50);
						camera.display();
						cameraButton.setVisible(true);
						cameraOpened = true;
						game.powerCalc();
						sound.playCameraSound(3, false);
					});
				}
				layeredPane.repaint();
			}
		});

		// LEFT LIGHT
		mainButtons.get("leftLightButton").addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JLabel temp = labels.get("backgroundLabel");
				if (jumpscareIncoming || griddyInOffice) {
					sound.playCameraSound(10, true);
				} else {
					sound.playCameraSound(2, false);
					if (game.getMonster(0).getRoomLocation() == 9) {
						temp.add(DoorLightGUI.labelRefresh(true, labels.get("leftWindow"), "leftwindow", icons));
						temp.add(DoorLightGUI.labelRefresh(false, labels.get("leftHallway"), "lefthall", icons));
						lightSpook();
					} else if (game.getMonster(0).getRoomLocation() == 11) {
						temp.add(DoorLightGUI.labelRefresh(false, labels.get("leftWindow"), "leftwindow", icons));
						temp.add(DoorLightGUI.labelRefresh(true, labels.get("leftHallway"), "lefthall", icons));
						lightSpook();
					} else {
						temp.add(DoorLightGUI.labelRefresh(false, labels.get("leftWindow"), "leftwindow", icons));
						temp.add(DoorLightGUI.labelRefresh(false, labels.get("leftHallway"), "lefthall", icons));
					}
					temp.add(labels.get("leftWall"));
					leftLightHeld = true;
					mainButtons.get("leftLightButton").setIcon(icons.getIcon("lightactive"));
					game.powerDrain(.02);
					temp.repaint();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				JLabel temp = labels.get("backgroundLabel");
				temp.remove(labels.get("leftHallway"));
				temp.remove(labels.get("leftWall"));
				temp.remove(labels.get("leftWindow"));
				mainButtons.get("leftLightButton").setIcon(icons.getIcon("lightneutral"));
				leftLightHeld = false;
				sound.stopCameraSound(2);
				temp.repaint();
			}
		});

		// RIGHT LIGHT
		mainButtons.get("rightLightButton").addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JLabel temp = labels.get("backgroundLabel");
				if (jumpscareIncoming || griddyInOffice) {
					sound.playCameraSound(10, true);
				} else {
					sound.playCameraSound(2, false);
					// game.rescheduleTask(powerCalc(), true);
					if (game.getMonster(1).getRoomLocation() == 10) {
						temp.add(DoorLightGUI.labelRefresh(true, labels.get("rightWindow"), "rightwindow", icons));
						temp.add(DoorLightGUI.labelRefresh(false, labels.get("rightHallway"), "righthall", icons));
						lightSpook();
					} else if (game.getMonster(1).getRoomLocation() == 12) {
						temp.add(DoorLightGUI.labelRefresh(false, labels.get("rightWindow"), "rightwindow", icons));
						temp.add(DoorLightGUI.labelRefresh(true, labels.get("rightHallway"), "righthall", icons));
						lightSpook();
					} else {
						temp.add(DoorLightGUI.labelRefresh(false, labels.get("rightWindow"), "rightwindow", icons));
						temp.add(DoorLightGUI.labelRefresh(false, labels.get("rightHallway"), "righthall", icons));
					}
					temp.add(labels.get("rightWall"));
					rightLightHeld = true;
					mainButtons.get("rightLightButton").setIcon(icons.getIcon("lightactive"));
					game.powerDrain(.02);
					temp.repaint();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				JLabel temp = labels.get("backgroundLabel");
				temp.remove(labels.get("rightHallway"));
				temp.remove(labels.get("rightWall"));
				temp.remove(labels.get("rightWindow"));
				mainButtons.get("rightLightButton").setIcon(icons.getIcon("lightneutral"));
				rightLightHeld = false;
				sound.stopCameraSound(2);
				temp.repaint();
			}
		});

		// LEFT DOOR
		mainButtons.get("leftDoorButton").addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jumpscareIncoming) {
					sound.playCameraSound(10, true);
				} else {
					JLabel temp = labels.get("leftDoor");
					IconHolder door = icons.getIcons("leftdoor");
					door.sort();
					if (!leftDoorShut) {
						game.powerDrain(.06);
						leftDoorShut = true;
						labels.get("backgroundLabel").add(temp);
						sound.playCameraSound(1, true);
						DoorLightGUI.animateDoor(true, door, temp);
						game.powerCalc();
					} else {
						game.powerDrain(.01);
						leftDoorShut = false;
						sound.playCameraSound(1, true);
						DoorLightGUI.animateDoor(false, door, temp);
						game.powerCalc();
					}
					mainButtons.get("leftDoorButton").setEnabled(false);
					Timer enableButtonTimer = new Timer(1025, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							mainButtons.get("leftDoorButton").setEnabled(true);
							if (leftDoorShut) {
								temp.setIcon(icons.getIcon("left_door"));
								mainButtons.get("leftDoorButton").setIcon(icons.getIcon("doorclosed"));
							} else {
								mainButtons.get("leftDoorButton").setIcon(icons.getIcon("dooropen"));
								labels.get("backgroundLabel").remove(temp);
							}
							labels.get("backgroundLabel").repaint();
						}
					});
					enableButtonTimer.setRepeats(false);
					enableButtonTimer.start();
				}
			}
		});

		// RIGHT DOOR
		mainButtons.get("rightDoorButton").addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jumpscareIncoming) {
					sound.playCameraSound(10, true);
				} else {
					JLabel temp = labels.get("rightDoor");
					IconHolder door = icons.getIcons("rightdoor");
					door.sort();
					if (!rightDoorShut) {
						game.powerDrain(.06);
						rightDoorShut = true;
						labels.get("backgroundLabel").add(temp);
						sound.playCameraSound(1, true);
						DoorLightGUI.animateDoor(true, door, temp);
						game.powerCalc();
					} else {
						game.powerDrain(.01);
						rightDoorShut = false;
						sound.playCameraSound(1, true);
						DoorLightGUI.animateDoor(false, door, temp);
						game.powerCalc();
					}
					mainButtons.get("rightDoorButton").setEnabled(false);
					Timer enableButtonTimer = new Timer(1025, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							mainButtons.get("rightDoorButton").setEnabled(true);
							if (rightDoorShut) {
								temp.setIcon(icons.getIcon("right_door"));
								mainButtons.get("rightDoorButton").setIcon(icons.getIcon("doorclosed"));
							} else {
								labels.get("backgroundLabel").remove(temp);
								mainButtons.get("rightDoorButton").setIcon(icons.getIcon("dooropen"));
							}
							labels.get("backgroundLabel").repaint();
						}
					});
					enableButtonTimer.setRepeats(false);
					enableButtonTimer.start();
				}
			}
		});
	}

	/**
	 * Updates the camera based on where the passed Monster is currently located.
	 * 
	 * @param current The current camera location.
	 * @param prev    The previous camera location.
	 * @param monster The monster whos location we wish to check.
	 */
	public void mapRefresh(int current, int prev, Monster monster) {
		if (cameraOpened && (camera.current == current)) {
			IconHolder staticAnimation = icons.getIcons("static");
			final int temp = current;
			enableCameraButtons(false);
			staticAnimation.playAnimation(50, labels.get("roomLabel"), () -> {
				enableCameraButtons(true);
				camera.update(temp);
				layeredPane.repaint();
			});
			sound.playCameraSound(0, true);
		} else if (cameraOpened && (camera.current == prev)) {
			IconHolder staticAnimation = icons.getIcons("static");
			final int temp = prev;
			enableCameraButtons(false);
			staticAnimation.playAnimation(50, labels.get("roomLabel"), () -> {
				enableCameraButtons(true);
				camera.update(temp);
				layeredPane.repaint();
			});
			sound.playCameraSound(0, true);
		}
		if (monster.getName() == ("ForrestLeft")) {
			sound.playForrestSound(2, true);
		} else if (monster.getName() == ("ForrestRight")) {
			sound.playForrestSound(3, true);
		}
		layeredPane.repaint();

	}

	/**
	 * Determines the direction to move the background image label based on the
	 * current mouse position and moves it if necessary.
	 *
	 * @param currentMousePos Current mouse position relative to the frame.
	 */
	private void moveBackground(Point currentMousePos) {
		JLabel move = labels.get("backgroundLabel");
		int thirdOfWindow = frame.getWidth() / 3;
		int deltaX = 0;

		if (currentMousePos.x < thirdOfWindow && move.getLocation().x < 0) {
			deltaX = movementDelta; // LEFT MOVEMENT
		} else if (currentMousePos.x > 2 * thirdOfWindow && move.getLocation().x > frame.getWidth() - move.getWidth()) {
			deltaX = -movementDelta; // RIGHT MOVEMENT
		}
		int newX = move.getLocation().x + deltaX;
		int newY = move.getLocation().y;

		int imageWidth = move.getWidth();
		int windowWidth = frame.getContentPane().getWidth();

		if (newX > 0) {
			newX = 0;
		} else if (newX < windowWidth - imageWidth) {
			newX = windowWidth - imageWidth;
		}
		move.setLocation(newX, newY);

	}

	private void scaleIcon(JLabel label, int newWidth, int newHeight, boolean scaleSmoothly) {
		ImageIcon originalIcon = (ImageIcon) label.getIcon();
		if (originalIcon != null) {
			Image originalImage = originalIcon.getImage();
			Image scaledImage;
			if (scaleSmoothly) {
				scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
			} else {
				scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
			}
			ImageIcon scaledIcon = new ImageIcon(scaledImage);
			label.setIcon(scaledIcon);
			label.setBounds(0, 0, newWidth, newHeight);
		}
	}

	/**
	 * Updates the GUI and game logic as the game is ending. Eventually constructs a
	 * victory or game over message then resets all game states.
	 *
	 * @param gameWon true = win, false = loss
	 */
	public void gameOver(boolean gameWon) {
		leftDoorShut = true;
		rightDoorShut = true;
		if (spookTimer.isRunning()) {
			spookTimer.stop();
		}
		game.gameOver();
		if (gameWon) {
			frame.getContentPane().removeAll();
			sound.stopAllSounds();
			frame.setLayout(new GridBagLayout());
			frame.setBackground(Color.BLACK);
			sound.playAmbientSound(2, true);
			gameOverHelper(true);
		} else {
			jumpscareIncoming = true;
			jumpscareTimer = new Timer(10000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (cameraOpened) {
						IconHolder cameraAnimation = icons.getIcons("camani");
						JLabel backgroundLabel = labels.get("backgroundLabel");
						JLabel roomLabel = labels.get("roomLabel");
						JButton cameraButton = mainButtons.get("cameraButton");
						camera.clean();
						cameraButton.setVisible(false);
						cameraAnimation.reverse();
						sound.stopCameraSound(4);
						sound.stopCameraSound(3);
						sound.playCameraSound(5, true);
						cameraAnimation.playAnimation(20, roomLabel, () -> {
							labels.get("roomLabel").setVisible(false);
							cameraButton.setBounds(318, 503, 150, 50);
							labels.get("backgroundLabel").setVisible(true);
							backgroundLabel.setIcon(icons.getIcon("office"));
							cameraOpened = false;
							layeredPane.remove(labels.get("powerLabel"));
							layeredPane.remove(labels.get("timeLabel"));
							sound.stopAllSounds();
							sound.playForrestSound(4, true);
							labels.get("jumpscareLabel").setVisible(true);

							icons.playAnimation("jump", 100, labels.get("jumpscareLabel"), () -> {

								frame.getContentPane().removeAll();
								frame.setLayout(new GridBagLayout());
								frame.setBackground(Color.BLACK);
								gameOverHelper(false);
							});
						});
					} else {
						layeredPane.remove(labels.get("powerLabel"));
						layeredPane.remove(labels.get("timeLabel"));
						sound.stopAllSounds();
						sound.playForrestSound(4, true);
						labels.get("jumpscareLabel").setVisible(true);
						icons.playAnimation("jump", 100, labels.get("jumpscareLabel"), () -> {
							frame.getContentPane().removeAll();
							frame.setLayout(new GridBagLayout());
							frame.setBackground(Color.BLACK);
							gameOverHelper(false);
						});
					}
				}
			});
			jumpscareTimer.start();
		}
	}

	private void gameOverHelper(boolean gameWon) {
		jumpscareTimer.stop();
		JLabel label;
		if (gameWon) {
			label = new JLabel("You survived the night!");
			game.night++;
			if (game.night >= 5) {
				label.setText("Congratulations! You survived the week!");
			}
		} else {
			jumpscareIncoming = false;
			label = new JLabel("Game Over");
		}
		label.setFont(new Font("Arial", Font.BOLD, 24));
		label.setForeground(new Color(255, 255, 255, 0));
		label.setHorizontalAlignment(JLabel.CENTER);
		frame.add(label);
		frame.revalidate();
		frame.repaint();
		new javax.swing.Timer(40, new ActionListener() {
			int alpha = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				alpha += 5;
				if (alpha >= 255) {
					alpha = 255;
					((Timer) e.getSource()).stop();
					frame.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							if (gameWon && game.night < 5) {
								int night = game.night + 1;
								label.setText("Night " + night);
								nightShiftTimer.setRepeats(false);
								nightShiftTimer.start();
								frame.removeMouseListener(this);
							} else if (gameWon && game.night >= 5) {
								displayStoryStar = true;
								if (game.night > 5) {
									displayHardStar = true;
								}
								frame.getContentPane().removeAll();
								sound.stopAllSounds();
								rebuild();
								sound.playAmbientSound(0, false);
								frame.repaint();
								frame.removeMouseListener(this);
							} else {
								frame.getContentPane().removeAll();
								sound.stopAllSounds();
								rebuild();
								sound.playAmbientSound(0, false);
								frame.repaint();
								frame.removeMouseListener(this);
							}
						}
					});
				}
				label.setForeground(new Color(255, 255, 255, alpha));
			}
		}).start();
	}

	/**
	 * Enables/Disables the buttons within the camera based on the passed boolean.
	 *
	 * @param flag true = enabled, false = disabled
	 */
	private void enableCameraButtons(boolean flag) {
		for (Map.Entry<String, JButton> entry : cameraButtons.entrySet()) {
			entry.getValue().setEnabled(flag);
		}
		mainButtons.get("cameraButton").setEnabled(flag);
	}

	/**
	 * Updates the GUI text regarding the power variable.
	 */
	public void updatePower() {
		double power = game.getPower();
		int rounded = (int) Math.round(power);
		labels.get("powerLabel").setText("Power: " + rounded + "%");
		labels.get("powerLabel").repaint();
	}

	/**
	 * Updates the GUI text regarding the time variable.
	 * 
	 * @param time Game's time variable
	 */
	public void updateTime(int time) {
		JLabel timeLabel = labels.get("timeLabel");
		timeLabel.setBounds(700, 10, 100, 25);
		timeLabel.setText(time + " AM");
		timeLabel.repaint();
	}

	/**
	 * Plays a scary sound to the player if a certain amount of time has passed.
	 */
	public void lightSpook() {
		if (!spookTimer.isRunning()) {
			sound.playForrestSound(1, true);
			spookTimer.setRepeats(true);
			spookTimer.start();
		}
	}

	/**
	 * Updates the game power variable
	 * 
	 * @param minus the amount to subtract the game's power variable by
	 */
	public void powerDrop(double minus) {
		game.powerDrain(minus);
	}

	/**
	 * Updates the GUI for when Griddy has progressed to a certain point.
	 */
	public void updateGriddy() {
		sound.playForrestSound(7, true);
		labels.get("griddyLabel").setVisible(true);
		labels.get("backgroundLabel").repaint();
		griddyInOffice = true;
		griddyTimer.start();

	}

}