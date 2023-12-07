package assignment2;

import javax.swing.SwingUtilities;

public class Driver {
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(() -> AdminView.getInstance().setVisible(true));
		//TODO add button for Valid User Group Button
		//TODO add functionality to get most recently updated user
	}
}
