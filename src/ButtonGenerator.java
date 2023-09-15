import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * This class provides utility methods to generate and manage game buttons. It
 * allows for the creation of buttons with different states and positions on the
 * camera interface.
 */
public class ButtonGenerator {
	/**
	 * Creates a new JButton with specified properties.
	 *
	 * @param icon   The ImageIcon to be set on the JButton.
	 * @param x      The x-coordinate of the JButton's top-left corner.
	 * @param y      The y-coordinate of the JButton's top-left corner.
	 * @param width  The width of the JButton.
	 * @param height The height of the JButton.
	 * @return A JButton object with the specified properties.
	 */
	public static JButton createButton(ImageIcon icon, double x, double y, double width, double height) {
		JButton button = new JButton(icon);
		button.setBounds((int) x, (int) y, (int) width, (int) height);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		return button;
	}

	/**
	 * Creates a button specifically for a map interface, scaling the icon to a
	 * predetermined size.
	 *
	 * @param icon The ImageIcon to be used for the button.
	 * @param x    The x-coordinate of the JButton's top-left corner.
	 * @param y    The y-coordinate of the JButton's top-left corner.
	 * @return A JButton object with the specified icon and position.
	 */
	public static JButton createMapButton(ImageIcon icon, double x, double y) {

		Image theImage = icon.getImage();

		theImage = theImage.getScaledInstance(55, 45, Image.SCALE_SMOOTH);
		ImageIcon theImageIcon = new ImageIcon(theImage);
		JButton button = createButton(theImageIcon, x, y, 55, 40);

		return button;
	}

	/**
	 * Generates a collection of buttons with icons from the provided IconHolder.
	 * The button identifiers and their positions are predefined.
	 *
	 * @param icons The IconHolder containing the necessary icons for button
	 *              creation.
	 * @return A map with string keys and JButton values representing the created
	 *         buttons.
	 */
	public static Map<String, JButton> mapButtonCreation(IconHolder icons) {
		Map<String, JButton> buttons = new HashMap<String, JButton>();
		buttons.put("0", createMapButton(icons.getIcon("neutral1a"), 520, 283));
		buttons.put("2", createMapButton(icons.getIcon("neutral1b"), 505, 330));
		buttons.put("3", createMapButton(icons.getIcon("neutral1c"), 594, 330));
		buttons.put("4", createMapButton(icons.getIcon("neutral5"), 445, 300));
		buttons.put("5", createMapButton(icons.getIcon("neutral7"), 653, 300));
		buttons.put("6", createMapButton(icons.getIcon("neutral6"), 385, 370));
		buttons.put("7", createMapButton(icons.getIcon("neutral8"), 715, 425));
		buttons.put("8", createMapButton(icons.getIcon("neutral3"), 593, 440));
		buttons.put("9", createMapButton(icons.getIcon("neutral2a"), 445, 400));
		buttons.put("11", createMapButton(icons.getIcon("neutral2b"), 445, 500));
		buttons.put("10", createMapButton(icons.getIcon("neutral4a"), 653, 400));
		buttons.put("12", createMapButton(icons.getIcon("neutral4b"), 653, 500));
		buttons.put("14", createMapButton(icons.getIcon("neutral9"), 713, 300));
		return buttons;
	}

	/**
	 * Sets the icon on a button based on the game's state and whether the button is
	 * active.
	 *
	 * @param state    An integer representing the game state. Each state
	 *                 corresponds to a specific icon.
	 * @param button   The JButton whose icon needs to be updated.
	 * @param isActive A boolean indicating if the button is currently active or
	 *                 not.
	 * @return A string representation of the updated state for the button.
	 */
	public static String setMapIcon(int state, JButton button, boolean isActive) {
		StringBuilder update = new StringBuilder();
		if (isActive) {
			update.append("active");
		} else {
			update.append("neutral");
		}
		switch (state) {
		case 0:
			update.append("1a");
			break;
		case 2:
			update.append("1b");
			break;
		case 3:
			update.append("1c");
			break;
		case 4:
			update.append("5");
			break;
		case 5:
			update.append("7");
			break;
		case 6:
			update.append("6");
			break;
		case 7:
			update.append("8");
			break;
		case 8:
			update.append("3");
			break;
		case 9:
			update.append("2a");
			break;
		case 10:
			update.append("4a");
			break;
		case 11:
			update.append("2b");
			break;
		case 12:
			update.append("4b");
			break;
		case 14:
			update.append("9");
			break;
		}
		return update.toString();
	}
}
