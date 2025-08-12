
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class DoorLightGUI {

	/**
	 * Creates a list of JLabels representing lights for a specific side (left or
	 * right) of a room.
	 *
	 * @param side   Specifies the side of the room. Use true for the left side and
	 *               false for the right side.
	 * @param button The JButton associated with the lights.
	 * @param icons  An IconHolder object containing icons for the different light
	 *               states.
	 * @return A list of JLabels representing the lights for the specified side of
	 *         the room.
	 */
	public static List<JLabel> createLight(boolean side, JButton button, IconHolder icons) {
		List<JLabel> labels = new ArrayList<JLabel>();
		if (side) {
			JLabel leftHallLabel = new JLabel(icons.getIcon("lefthall_lit"));
			leftHallLabel.setBounds(150, 160, leftHallLabel.getIcon().getIconWidth(),
					leftHallLabel.getIcon().getIconHeight());

			JLabel leftWallLabel = new JLabel(icons.getIcon("leftwall_lit"));
			leftWallLabel.setBounds(15, 0, leftWallLabel.getIcon().getIconWidth(),
					leftWallLabel.getIcon().getIconHeight());

			JLabel leftWindowLabel = new JLabel(icons.getIcon("leftwindow_lit"));
			leftWindowLabel.setBounds(300, 150, leftWindowLabel.getIcon().getIconWidth(),
					leftWindowLabel.getIcon().getIconHeight());

			labels.add(leftHallLabel);
			labels.add(leftWallLabel);
			labels.add(leftWindowLabel);
		} else {
			JLabel rightHallLabel = new JLabel(icons.getIcon("righthall_lit"));
			rightHallLabel.setBounds(1115, 190, rightHallLabel.getIcon().getIconWidth(),
					rightHallLabel.getIcon().getIconHeight());

			JLabel rightWallLabel = new JLabel(icons.getIcon("rightwall_lit"));
			rightWallLabel.setBounds(1140, 0, rightWallLabel.getIcon().getIconWidth(),
					rightWallLabel.getIcon().getIconHeight());

			JLabel rightWindowLabel = new JLabel(icons.getIcon("rightwindow_lit"));
			rightWindowLabel.setBounds(960, 135, rightWindowLabel.getIcon().getIconWidth(),
					rightWindowLabel.getIcon().getIconHeight());

			labels.add(rightHallLabel);
			labels.add(rightWallLabel);
			labels.add(rightWindowLabel);
		}
		return labels;
	}

	/**
	 * Updates the icon of the provided label based on the presence of a monster at
	 * the specified location.
	 *
	 * @param isMonsterThere Indicates if a monster is present at the given
	 *                       location.
	 * @param label          The JLabel which needs to be updated.
	 * @param location       Specifies the location or room associated with the
	 *                       label.
	 * @param icons          An IconHolder object containing icons for the different
	 *                       states.
	 * @return The updated JLabel with the relevant icon set.
	 */
	public static JLabel labelRefresh(boolean isMonsterThere, JLabel label, String location, IconHolder icons) {
		if (isMonsterThere) {
			label.setIcon(icons.getIcon(location + "_danger"));
		} else {
			label.setIcon(icons.getIcon(location + "_lit"));
		}
		return label;
	}

	/**
	 * Creates a JLabel representing a door with the given icon, placing it on the
	 * left or right side.
	 * 
	 * @param side A boolean value where true represents the left door, and false
	 *             represents the right door.
	 * @param icon The ImageIcon to be used for the door's visual representation.
	 * @return A JLabel with the specified icon and bounds set according to the
	 *         specified side.
	 */
	public static JLabel createDoor(boolean side, ImageIcon icon) {
		JLabel temp = new JLabel();
		if (side) {
			temp.setBounds(100, 0, icon.getIconWidth(), icon.getIconHeight());
		} else {
			temp.setBounds(1050, 0, icon.getIconWidth(), icon.getIconHeight());
		}
		return temp;
	}

	/**
	 * Animates a door represented by a JLabel, either opening or closing it based
	 * on the specified parameters.
	 *
	 * @param isClosing A boolean value where true triggers the door closing
	 *                  animation, and false triggers the door opening animation.
	 * @param icons     An instance of IconHolder containing the animation frames
	 *                  for the door.
	 * @param label     A JLabel representing the door, which will be animated.
	 */
	public static void animateDoor(boolean isClosing, IconHolder icons, JLabel label) {
		if (isClosing) {
			icons.playAnimation(20, label);
		} else {
			icons.reverse();
			icons.playAnimation(20, label);
		}
	}

}
