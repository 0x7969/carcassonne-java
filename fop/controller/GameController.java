package fop.controller;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import fop.model.Gameboard;
import fop.model.Tile;
import fop.model.TileStack;
import fop.view.GameView;
import fop.view.GameboardObserver;
import fop.view.MainWindow;
import fop.view.Observer;
import fop.view.View;

public class GameController {

	private final static Logger LOG = Logger.getLogger("Carcassonne");

	private View view;
	private Gameboard board;
	private TileStack stack;

	public GameController() {
		board = new Gameboard();
		stack = new TileStack();

		initView();

		LOG.setLevel(Level.OFF);
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(LOG.getLevel());
		LOG.addHandler(consoleHandler);

	}

	public void initView() {
		try {
			// set look and feel of swing components
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		view = new GameView(this);
		
		JFrame frame = new JFrame("Carcassonne");
		frame.setSize(800, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(view);
		frame.setVisible(true);
		
		// this is the recommended way to start swing applications but i didnt quite understand it
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					view = new GameView(gc);
//					JFrame mainWindow = new MainWindow("Carcassonne");
//					mainWindow.setVisible(true);
//					mainWindow.setContentPane(view);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}

	public void initGameBoard() {
		board.initGameboard(stack.pickUpTile()); // The topmost tile of the tilestack is always the start tile.
	}

	public void newTile(Tile t, int x, int y) {
		board.newTile(t, x, y);
	}

	public Tile peekTile() {
		return stack.peekTile();
	}

	public Tile pickUpTile() {
		return stack.pickUpTile();
	}

	public void rotateNextTile() {
		stack.rotateTopTile();
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
		stack.addObserver(o);
	}

	public static void main(String[] args) {
		new GameController();
	}

}