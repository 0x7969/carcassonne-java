package fop.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import fop.controller.GameController;
import fop.model.TileStack;

public class GameView extends View {

	GameBoardPanel gameBoardPanel;
	JPanel gameBoardPanelWrapper;
	ToolbarPanel toolbarPanel;
	TileStackPanel tileStackPanel;

	public GameView(GameController gc) {
		super(gc);
		setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(1200, 900));
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
		gameBoardPanelWrapper = new JPanel();
		gameBoardPanelWrapper.setLayout(null);
		gameBoardPanel = new GameBoardPanel(gc);
		gameBoardPanel.setBounds(-49950 + gameBoardPanelWrapper.getWidth() / 2,
				-50000 + gameBoardPanelWrapper.getHeight() / 2, 100000, 100000);
		gameBoardPanelWrapper.add(gameBoardPanel);
		this.add(gameBoardPanelWrapper);
		gc.addGameBoardObserver(gameBoardPanel);
		gc.initGameBoard();

		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				gameBoardPanel.setBounds(-49950 + gameBoardPanelWrapper.getWidth() / 2,
						-50000 + gameBoardPanelWrapper.getHeight() / 2, 100000, 100000);

			}
		});
	}
	
	public Point getOverlayedTileGridPosition() {
		return gameBoardPanel.getOverlayedTileGridPosition();
	}

	@Override
	public void addActionListener(ActionListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasOverlay() {
		return gameBoardPanel.hasOverlay();
	}

	@Override
	protected Object getOverlayedTile() {
		return gameBoardPanel.getOverlayedTile();
	}

}