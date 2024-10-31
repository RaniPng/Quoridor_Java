package Main;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		// screen size
		Dimension DimMax = Toolkit.getDefaultToolkit().getScreenSize();

		// jframe settings
		JFrame window = new JFrame("Quoridor");
		window.setUndecorated(true);
		window.setResizable(false);

		// set full screen
		window.setMinimumSize(DimMax);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setLocationRelativeTo(null);

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(new GamePanel(DimMax.width, DimMax.height));

		window.pack();
		window.setVisible(true);
	}
}
