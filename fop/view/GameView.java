package fop.view;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import fop.Carcassonne;
import fop.controller.GameController;
import fop.model.TileStack;

public class GameView extends JPanel {

	GameBoardPanel gameboardPanel;
	JPanel gameboardPanelWrapper;
	ToolbarPanel toolbarPanel;
	TileStackPanel tileStackPanel;
	GameController gc;

	public GameView(String title) {
//		super(title);
//		this.setSize(800, 800);
//		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		gc = new GameController(this);

		// top toolbar
		toolbarPanel = new ToolbarPanel();
		this.add(toolbarPanel, BorderLayout.NORTH);

		// tile stack
		tileStackPanel = new TileStackPanel();
		this.add(tileStackPanel, BorderLayout.EAST);

		// game board (the wrapper is needed to be able to drag the board around. it can
		// be understood as a window that you look through onto the gameboard, only
		// seeing part of it)
		gameboardPanelWrapper = new JPanel();
		gameboardPanelWrapper.setLayout(null);
		gameboardPanel = new GameBoardPanel(gc);
		gameboardPanel.setBounds(-50000 + getWidth() / 2, -50000 + getHeight() / 2, 100000, 100000);
		gameboardPanelWrapper.add(gameboardPanel);
		this.add(gameboardPanelWrapper);
	}
	
	public Observer<TileStack> getTileStackObserver() {
		return tileStackPanel;
	}
	
	public GameboardObserver getGameBoardObserver() {
		return gameboardPanel;
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
					JFrame frame = new JFrame("Carcassonne");
					frame.setSize(800, 800);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setContentPane(new GameView("Carcassonne"));
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}