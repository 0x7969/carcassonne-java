package fop;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import fop.controller.GameController;

public class Carcassonne extends JFrame {

	GameController gc;

	public Carcassonne(String title) {
		super(title);
		this.setSize(800, 800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//		gc = new GameController();
		this.setContentPane(gc.getView());
	}

	public static void main(String[] args) {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Macht man so wegen irgendwas mit threading
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame frame = new Carcassonne("Carcassonne");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
