
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.Timer;

/**
 * The CameraGUI class manages the visual representation of the game's
 * surveillance camera system. It showcases the monsters' positions, handles
 * room visuals, and manages camera-related sound effects.
 */
public class CameraGUI {

	/** Timer for controlling monster-related events. */
	public Timer monsterTimer;

	/** The main game sequence logic. */
	private GameSequence game;

	/** The sound engine responsible for managing and playing game sounds. */
	private SoundEngine sound;

	/** Layered pane to manage the depth of components like buttons and labels. */
	private JLayeredPane layeredPane;

	/**
	 * Collection of labels used within the camera GUI, mapped by their identifiers.
	 */
	private Map<String, JLabel> labels;

	/**
	 * Collection of buttons used within the camera GUI, mapped by their
	 * identifiers.
	 */
	private Map<String, JButton> buttons;

	/** Holder for icons used within the game. */
	private IconHolder icons;

	/** Timer specific to kitchen-related sounds and events. */
	private Timer kitchenTimer;

	/** State of the current room being viewed. */
	public int current;

	/** State of the previous room that was viewed. */
	public int previous;

	/**
	 * Constructor for the CameraGUI.
	 * 
	 * @param other The main game sequence logic to be associated with this camera
	 *              GUI.
	 */
	public CameraGUI(GameSequence other) {
		game = other;
	}

	/**
	 * Constructs the camera GUI by associating it with the main components.
	 *
	 * @param other        The main layered pane for GUI elements.
	 * @param labelsOther  The labels to be managed by this camera GUI.
	 * @param buttonsOther The buttons to be managed by this camera GUI.
	 * @param iconsOther   The icons holder to be used for visual representation.
	 * @param otherSound   The sound engine for audio functionality.
	 */
	public void construct(JLayeredPane other, Map<String, JLabel> labelsOther, Map<String, JButton> buttonsOther,
			IconHolder iconsOther, SoundEngine otherSound) {

		layeredPane = other;
		labels = labelsOther;
		buttons = buttonsOther;
		icons = iconsOther;
		previous = 2;
		sound = otherSound;

		for (Map.Entry<String, JButton> entry : buttons.entrySet()) {
			JButton button = entry.getValue();
			layeredPane.add(button, JLayeredPane.MODAL_LAYER);
			button.setVisible(false);
		}
		kitchenTimer = new Timer(9000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch ((int) (Math.random() * 4)) {
				case 0:
					sound.playCameraSound(6, true);
					break;
				case 1:
					sound.playCameraSound(7, true);
					break;
				case 2:
					sound.playCameraSound(8, true);
					break;
				case 3:
					sound.playCameraSound(9, true);
					break;
				}
			}
		});
		kitchenTimer.setRepeats(true);
	}

	/**
	 * Displays the camera, making the buttons and room visuals appear.
	 */
	public void display() {
		JLabel roomLabel = labels.get("roomLabel");
		Integer buttonName = previous;
		JButton button = buttons.get(buttonName.toString());
		button.setIcon(icons.getIcon(ButtonGenerator.setMapIcon(previous, button, true)));
		checkMonster(previous, roomLabel);
		roomLabel.setVisible(true);
		labels.get("mapLabel").setVisible(true);
		for (Map.Entry<String, JButton> entry : buttons.entrySet()) {
			entry.getValue().setVisible(true);
		}
		labels.get("backgroundLabel").setVisible(false);
		current = previous;
	}

	/**
	 * Updates the visual representation based on the given state.
	 *
	 * @param state The state (room number) to display.
	 */
	public void update(int state) {
		Integer buttonName = previous;
		JLabel roomLabel = labels.get("roomLabel");
		for (Map.Entry<String, JButton> entry : buttons.entrySet()) {
			JButton button = entry.getValue();
			int location = 0;
			try {
				location = Integer.parseInt(entry.getKey());
			} catch (NumberFormatException e) {
				System.out.println("Error reading room number from button. Defaulting to 0...");
			}
			entry.getValue().setIcon(icons.getIcon(ButtonGenerator.setMapIcon(location, entry.getValue(), false)));
			resizeButton(button);
		}
		buttonName = state;
		JButton button = buttons.get(buttonName.toString());
		button.setIcon(icons.getIcon(ButtonGenerator.setMapIcon(state, button, true)));
		resizeButton(button);
		checkMonster(state, roomLabel);
		previous = current;
		current = state;
	}

	/**
	 * Cleans up the camera visuals and stops any related timers.
	 */
	public void clean() {
		System.out.println("CAMERA CLOSING...");
		if (kitchenTimer.isRunning()) {
			kitchenTimer.stop();
			sound.stopCameraSound(6);
			sound.stopCameraSound(7);
			sound.stopCameraSound(8);
			sound.stopCameraSound(9);
		}
		labels.get("mapLabel").setVisible(false);
		for (Map.Entry<String, JButton> entry : buttons.entrySet()) {
			entry.getValue().setVisible(false);
			JButton button = entry.getValue();
			int location = 0;
			try {
				location = Integer.parseInt(entry.getKey());
			} catch (NumberFormatException e) {
				System.out.println("Error parsing room number from button. Defaulting to 0...");
				location = 0;
			}
			entry.getValue().setIcon(icons.getIcon(ButtonGenerator.setMapIcon(location, entry.getValue(), false)));
			resizeButton(button);
		}
		previous = current;
	}

	/**
	 * Rescales the icon of a given label to a new width and height.
	 *
	 * @param label         The label containing the icon to be scaled.
	 * @param newWidth      The desired width.
	 * @param newHeight     The desired height.
	 * @param scaleSmoothly Whether or not to use smooth scaling.
	 */
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
	 * Resizes the icon on a button to fit the game's specifications.
	 *
	 * @param button The button containing the icon to be resized.
	 */
	private void resizeButton(JButton button) {
		ImageIcon icon = (ImageIcon) button.getIcon();
		Image theImage = icon.getImage();
		theImage = theImage.getScaledInstance(55, 45, Image.SCALE_SMOOTH);
		ImageIcon theImageIcon = new ImageIcon(theImage);
		button.setIcon(theImageIcon);
	}

	/**
	 * Checks for the presence of a monster in a specific room. Adjusts the room's
	 * visuals and manages related sounds and events.
	 *
	 * @param state The state (room number) to check.
	 * @param label The label used for room visuals.
	 */
	private void checkMonster(int state, JLabel label) {
		if (kitchenTimer.isRunning()) {
			kitchenTimer.stop();
			sound.stopCameraSound(6);
			sound.stopCameraSound(7);
			sound.stopCameraSound(8);
			sound.stopCameraSound(9);
		}
		if (state == 4) {
			Monster monster = game.getMonster(2);
			label.setIcon(icons.getIcon("pirate" + monster.getRoomLocation()));
			scaleIcon(label, 800, 600, true);
			label.repaint();
			return;
		}
		for (int i = 0; i < 2; i++) {
			Monster monster = game.getMonster(i);
			if (state == monster.getRoomLocation()) {
				if (state == 0) {
					if (game.getMonster(0).getRoomLocation() == 0 && game.getMonster(1).getRoomLocation() == 0) {
						label.setIcon(icons.getIcon("monsterroom0_both"));
					} else if (game.getMonster(0).getRoomLocation() == 0) {
						label.setIcon(icons.getIcon("monsterroom0_left"));
					} else if (game.getMonster(1).getRoomLocation() == 0) {
						label.setIcon(icons.getIcon("monsterroom0_right"));
					} else {
						label.setIcon(icons.getIcon("room0"));
					}
					break;
				} else {
					label.setIcon(icons.getIcon("monsterroom" + state));
					if (state == 14) {
						sound.playCameraSound(6, true);
						kitchenTimer.start();
					}
					// System.out.println("Monster found!");
					break;
				}
			} else {
				label.setIcon(icons.getIcon("room" + state));
				// System.out.println("No monsters found.");
			}
			GraysonGiraffe temp = (GraysonGiraffe) game.getMonster(2);
			if (temp.getRoomLocation() > 3 && state == 9 && !temp.animationPlayed) {
				temp.animationPlayed = true;
				sound.playForrestSound(5, true);
				icons.playAnimation("giraffe_run", 90, label, () -> {
					temp.sprintTimer.stop();
					temp.sprintTimer.setInitialDelay(4000);
					temp.sprintTimer.start();
				});
			}

		}
		scaleIcon(label, 800, 600, true);
		label.repaint();
	}
}
