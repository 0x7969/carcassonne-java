package fop.controller;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import fop.model.Gameboard;
import fop.model.Tile;
import fop.model.TileStack;
import fop.view.GameboardObserver;
import fop.view.Observer;

public class GameController {

	private final static Logger LOG = Logger.getLogger("Carcassonne");

	private Gameboard board;
	private TileStack tileStack;

	public GameController() {
		board = new Gameboard();
		tileStack = new TileStack();
		
		LOG.setLevel(Level.OFF);
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(LOG.getLevel());
		LOG.addHandler(consoleHandler);
		
	}
	
	public void initGameBoard() {
		board.initGameboard(tileStack.pickUpTile()); // The topmost tile of the tilestack is always the start tile.
	}

	public void newTile(Tile t, int x, int y) {
		board.newTile(t, x, y);
	}

	public Tile peekTile() {
		return tileStack.peekTile();
	}

	public Tile pickUpTile() {
		return tileStack.pickUpTile();
	}

	public void rotateNextTile() {
		tileStack.rotateTopTile();
	}

	public boolean isTileAllowed(Tile t, int x, int y) {
		return board.isTileAllowed(t, x, y);
	}
	
	public boolean[] getMeepleSpots(int x, int y) {
		return board.getMeepleSpots(x, y);
	}

	public void addGameBoardObserver(GameboardObserver o) {
		board.addObserver(o);
	}

	public void addTileStackObserver(Observer<TileStack> o) {
		tileStack.addObserver(o);
	}

}