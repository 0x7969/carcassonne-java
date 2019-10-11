package fop.controller;

import java.awt.Dimension;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import fop.model.Gameboard;
import fop.model.MeepleColour;
import fop.model.Observable;
import fop.model.Player;
import fop.model.Position;
import fop.model.Tile;
import fop.model.TileStack;
import fop.view.GameBoardPanel;
import fop.view.GameView;
import fop.view.GameboardObserver;
import fop.view.MenuView;
import fop.view.Observer;
import fop.view.TileStackPanel;

public class GameController extends Observable<Player[]> {

	private final static Logger LOG = Logger.getLogger("Carcassonne");

	// model
	private State state;
	private Gameboard board;
	private TileStack stack;
	private Player[] players;
	private int currentRound;

	// view
	private JFrame window;
	private GameView view;
	private GameBoardPanel boardPanel;
	private TileStackPanel stackPanel;

	public GameController() {
		setupWindow();
		setState(State.GAME_MENU);

		currentRound = 0;

		// set up logging
		LOG.setLevel(Level.FINEST);
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(LOG.getLevel());
//		LOG.addHandler(consoleHandler);
		try {
			FileHandler fileHandler = new FileHandler("Game.log");
			fileHandler.setLevel(LOG.getLevel());
			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);
			LOG.addHandler(fileHandler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Makes the window look more like your OS, sets its title and enables closing
	 * it.
	 */
	private void setupWindow() {
		try {
			// set look and feel of swing components
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		window = new JFrame("Carcassonne");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setMinimumSize(new Dimension(400, 300));
	}

	/**
	 * Replaces the windows content with the given view.
	 */
	private void setView(JPanel view) {
		window.setContentPane(view);
		window.pack();
		window.revalidate();
		window.repaint();
		window.setVisible(true);
	}

	public void setState(State state) {
		this.state = state;
		switch (state) {
		case GAME_MENU:
			setView(new MenuView(this));
			break;
		case GAME_START:
			System.out.println("Entered GAME_START");

			// TODO soll dann vom menu aus gesetzt werden
			players = new Player[] { new Player("P1", MeepleColour.RED), new Player("P2", MeepleColour.BLUE) };
			board = new Gameboard();
			stack = new TileStack();
			view = new GameView(this);
			setView(view);
			boardPanel = view.getGameBoardPanel();
			stackPanel = view.getTileStackPanel();
			setupListeners();
			setupObservers();
			initGameBoard();

			setState(State.PLACING_TILE);
			break;
		case PLACING_TILE:
			System.out.println("Entered PLACING_TILE");
			push(players); // push players to observers (= ToolbarPanel)
			stack.push(stack); // pushes tile stack to observers (= TileStackPanel)
			view.getToolbarPanel().hideSkipButton();
			view.setStatusbar("Player " + currentPlayer().getName() + ", please place a tile.");
			// Now waiting for user input
			break;
		case PLACING_MEEPLE:
			System.out.println("Entered PLACING_MEEPLE");

			// When the current player does not have any meeple left, go to next round
			// immediately.
			if (currentPlayer().getMeeples() == 0) {
				nextRound();
				break;
			}

			Tile newestTile = board.getNewestTile();
			boardPanel.showTemporaryMeepleOverlay(board.getMeepleSpots(), newestTile.x, newestTile.y, currentPlayer());
			stackPanel.hideTileStack();
			view.getToolbarPanel().showSkipButton();
			view.setStatusbar(
					"Player " + currentPlayer().getName() + ", please place a meeple or skip (right mouse button).");
			// Now waiting for user input
			break;
		case GAME_OVER:
			board.calculatePoints(getState());
			// Show final score popup or w/e
			break;
		case GAME_SCORE:
			// score anzeigen
			break;
		}
	}

	private Player currentPlayer() {
		return players[currentRound % players.length];
	}

	public State getState() {
		return state;
	}

	private void setupListeners() {
		view.getToolbarPanel().addToolbarActionListeners((event) -> {
			switch (event.getActionCommand()) {
			case "Quit":
				window.dispose();
				break;
			case "Skip":
				boardPanel.removeTempMeepleOverlay();
				nextRound();
				break;
			}
		});

//			view.addMouseListener(new MouseAdapter() {
//				public void mouseClicked(MouseEvent event) {
//					if (state == State.PLACING_TILE && view.hasOverlay()) {
//						if (SwingUtilities.isLeftMouseButton(event)) {
//							Point p = view.getOverlayedTileGridPosition();
//							newTile(pickUpTile(), p.x, p.y);
//							view.repaint(); // !
//						} else if (SwingUtilities.isRightMouseButton(event)) {
//							rotateUntilAllowed();
//						}
//					} else if (state == State.PLACING_MEEPLE) {
//						if (SwingUtilities.isLeftMouseButton(event)) {
//							// place meeple
//						}
//					}
//				}
//
//				private void rotateUntilAllowed() {
//					rotateTopTile();
//					Point p = view.getOverlayedTileGridPosition();
//					
//					for (int i = 0; i < 3; i++) {
//						if (isTileAllowed(peekTile(), p.x, p.y)) {
//							view.getOverlayedTile().setRotation(peekTile().getRotation());
//							view.repaint();
//							return;
//						} else {
//							rotateTopTile();
//							view.repaint();
//						}
//					}
//				}
//			});
	}

	public void nextRound() {
		System.out.println(stack.remainingTiles());
		if (stack.remainingTiles() == 0)
			setState(State.GAME_OVER);
		else {
			boardPanel.removeTempMeepleOverlay();
			board.calculatePoints(getState());
			board.push(board);
			currentRound++;
			setState(State.PLACING_TILE);
		}
	}

	private void setupObservers() {
		this.addObserver(view.getToolbarPanel());
		stack.addObserver(stackPanel);
		stack.addObserver(boardPanel.getTileOverlay());
		board.addObserver(boardPanel);
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

	public void rotateTopTile() {
		stack.rotateTopTile();
	}

	public boolean isTopTileAllowed(int x, int y) {
		return board.isTileAllowed(peekTile(), x, y);
	}

	public void placeMeeple(Position position) {
		board.placeMeeple(position, currentPlayer());
		nextRound();
	}

	public void addGameBoardObserver(GameboardObserver o) {
		board.addObserver(o);
	}

	public void addTileStackObserver(Observer<TileStack> o) {
		stack.addObserver(o);
	}

	public Player[] getPlayers() {
		return players;
	}

	public static void main(String[] args) {
		new GameController();
	}

}