package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import controller.GameController;

public class MainWindow extends JFrame {

	GameController gc;
	GameboardPanel gameboardPanel;
	JPanel gameboardPanelWrapper;
	ToolbarPanel toolbarPanel;
	TileStackPanel tileStackPanel;

	public MainWindow(String title) {
		super(title);
		this.setSize(800, 800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		gc = new GameController();

		// top toolbar
		toolbarPanel = new ToolbarPanel();
		this.add(toolbarPanel, BorderLayout.NORTH);

		// tile stack
		tileStackPanel = new TileStackPanel();
		this.add(tileStackPanel, BorderLayout.EAST);
		gc.addTileStackObserver(tileStackPanel);

		// game board (the wrapper is needed to be able to drag the board around. it can
		// be understood as a window that you look through onto the gameboard, only
		// seeing a part of it)
		gameboardPanelWrapper = new JPanel();
		gameboardPanelWrapper.setLayout(null);
		gameboardPanel = new GameboardPanel(gc);
		gameboardPanel.setBounds(-50000 + getWidth() / 2, -50000 + getHeight() / 2, 100000, 100000);
		gameboardPanelWrapper.add(gameboardPanel);
		this.add(gameboardPanelWrapper);
		gc.addGameBoardObserver(gameboardPanel);
		gc.initGameBoard();
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
					JFrame frame = new MainWindow("Carcassonne");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}