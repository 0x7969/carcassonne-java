package fop.controller;

import static fop.model.FeatureType.CASTLE;
import static fop.model.FeatureType.ROAD;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import fop.model.Gameboard;
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
		LOG.setLevel(Level.OFF);
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(LOG.getLevel());
		LOG.addHandler(consoleHandler);
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
			// TODO soll dann vom menu aus gesetzt werden
			players = new Player[]{new Player("P1"), new Player("P2")};
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
			push(players); // push players to observers (= ToolbarPanel)
			boardPanel.hideMeepleOverlay();
			stack.push(stack); // pushes tile stack to observers (= TileStackPanel)
			view.getToolbarPanel().toggleSkipButton();
			System.out.println("Please place a tile.");
			break;
		case PLACING_MEEPLE:
			Tile newestTile = board.getNewestTile();
			boardPanel.showMeepleOverlay(newestTile.getMeepleSpots(), newestTile.x, newestTile.y);
			stackPanel.hideTileStack();
			view.getToolbarPanel().toggleSkipButton();
			System.out.println("Please place a meeple or skip.");
			break;
		case GAME_SCORE:
			// score anzeigen
			break;
		default:
			break;
		}
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
				nextRound();
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
		board.calculatePoints(ROAD); // TODO soll eigentlich durch state change ausgelöst werden
		board.calculatePoints(CASTLE);
		// TODO eigentlich müsste man das gar nicht trennen. wir wollen nur wiesen noch
		// nicht behandeln.
		// sobald die verbindung/berechnung der wiesen funktioniert, können alle punkte
		// in einem rutsch berechnet werden
		// (für die wiese muss dann trotzdem noch zusätzlich die anzahl der berührten
		// fertigen castles gesammelt werden).
		currentRound++;
		setState(State.PLACING_TILE);
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

	public boolean[] getMeepleSpots(int x, int y) {
		return board.getMeepleSpots(x, y);
	}

	public void placeMeeple(Position position) {
		board.placeMeeple(position, players[currentRound % players.length]); // the current player is determinded by the
																				// current round modulo amount of
																				// players
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

	private class GameMenuActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			// hier kann dann noch genauer der button abgefragt werden usw.
			setState(State.GAME_START);
		}
	}

}