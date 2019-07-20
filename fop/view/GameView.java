package fop.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import fop.controller.GameController;
import fop.model.TileStack;

public class GameView extends View {

	GameBoardPanel gameboardPanel;
	JPanel gameboardPanelWrapper;
	ToolbarPanel toolbarPanel;
	TileStackPanel tileStackPanel;

	public GameView(GameController gc) {
		super(gc);
		setLayout(new BorderLayout());
		// top toolbar
		toolbarPanel = new ToolbarPanel();
		this.add(toolbarPanel, BorderLayout.NORTH);

		// tile stack
		tileStackPanel = new TileStackPanel();
		this.add(tileStackPanel, BorderLayout.EAST);
		gc.addTileStackObserver(tileStackPanel);

		// game board (the wrapper is needed to be able to drag the board around. it can
		// be understood as a window that you look through onto the gameboard, only
		// seeing part of it)
		gameboardPanelWrapper = new JPanel();
		gameboardPanelWrapper.setLayout(null);
		gameboardPanel = new GameBoardPanel(gc);
		gameboardPanel.setBounds(-50000 + getWidth() / 2, -50000 + getHeight() / 2, 100000, 100000);
		gameboardPanelWrapper.add(gameboardPanel);
		this.add(gameboardPanelWrapper);
		gc.addGameBoardObserver(gameboardPanel);
		gc.initGameBoard();
	}

	public Observer<TileStack> getTileStackObserver() {
		return tileStackPanel;
	}

	public GameboardObserver getGameBoardObserver() {
		return gameboardPanel;
	}

}