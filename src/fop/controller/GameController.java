package fop.controller;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import fop.model.Gameboard;
import fop.model.MeepleColor;
import fop.model.Observable;
import fop.model.Player;
import fop.model.Position;
import fop.model.State;
import fop.model.Tile;
import fop.model.TileStack;
import fop.view.GameBoardPanel;
import fop.view.GameView;
import fop.view.MenuView;
import fop.view.TileStackPanel;

public class GameController extends Observable<List<Player>> {

	private final static Logger LOG = Logger.getLogger("Carcassonne");

	// model
	private State state;
	private Gameboard board;
	private TileStack stack;
	private List<Player> players;
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
		LOG.setLevel(Level.FINER);

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
			LOG.finer("Entered GAME_START");

			// TODO soll dann vom menu aus gesetzt werden
			players = new LinkedList<Player>();
			players.add(new Player("P1", MeepleColor.RED));
			players.add(new Player("P2", MeepleColor.BLUE));
			players.add(new Player("P3", MeepleColor.GREEN));

			board = new Gameboard();
			stack = new TileStack();
			view = new GameView(this);
			setView(view);
			boardPanel = view.getGameBoardPanel();
			stackPanel = view.getTileStackPanel();
			setupListeners();
			setupObservers();
			initGameboard();
			setState(State.PLACING_TILE);
			break;
		case PLACING_TILE:
			LOG.finer("Entered PLACING_TILE");
			push(players); // push players to observers (= ToolbarPanel)

			// According to the rules, a tile that does not fit anywhere is not mixed into
			// the stack again, but simply discarded.
			if (!board.isTileAllowedAnywhere(stack.peekTile())) {
				LOG.info("A tile of type " + stack.peekTile().getType()
						+ " did not have any legal placement options and was discarded.");
				stack.discardTopTile();
			}

			stack.push(stack); // pushes tile stack to observers (= TileStackPanel)
			view.getToolbarPanel().hideSkipButton();
			view.setStatusbar("Player " + currentPlayer().getName() + ", please place a tile.",
					currentPlayer().getMeepleColor().getColor());
			// Now waiting for user input
			break;
		case PLACING_MEEPLE:
			LOG.finer("Entered PLACING_MEEPLE");

			// When the current player does not have any meeple left, go to next round
			// immediately.
			if (currentPlayer().getMeepleAmount() == 0) {
				nextRound();
				break;
			}

			Tile newestTile = board.getNewestTile();
			boolean[] meepleSpots = board.getMeepleSpots();
			if (meepleSpots != null) {
				boardPanel.showTemporaryMeepleOverlay(meepleSpots, newestTile.x, newestTile.y, currentPlayer());
				stackPanel.hideTopTile();
				view.getToolbarPanel().showSkipButton();
				view.setStatusbar(
						"Player " + currentPlayer().getName() + ", please place a meeple or skip (right mouse button).",
						currentPlayer().getMeepleColor().getColor());
				// Now waiting for user input
			} else {
				// If there are no possibilities of placing a meeple, skip to the next round
				// right away.
				LOG.info("There was no option for " + currentPlayer().getName()
						+ " to place a meeple. Going to next round.");
				nextRound();
			}
			break;
		case GAME_OVER:
			board.calculatePoints(getState());
			board.push(board);
			push(players);
			stack.push(stack);
			view.getToolbarPanel().hideSkipButton();

			// display winners
			String winnersMessage = getWinnersMessage();
			view.setStatusbar(winnersMessage);
			JOptionPane.showMessageDialog(null, winnersMessage);

			break;
		default:
			break;
		}
	}

	/**
	 * Returns a list of all players having the highest score.
	 * 
	 * @return a list of all players having the highest score.
	 */
	private List<Player> getWinners() {
		List<Player> winners = new LinkedList<Player>();
		int highestScore = 0;
		for (Player p : players)
			if (p.getScore() > highestScore) {
				winners = new LinkedList<Player>();
				winners.add(p);
				highestScore = p.getScore();
			} else if (p.getScore() == highestScore) {
				winners.add(p);
			}
		return winners;
	}

	/**
	 * Generates a string listing all winners and their score.
	 * 
	 * @return A string listing all winners and their score.
	 */
	private String getWinnersMessage() {
		String message = "Game over! ";
		Iterator<Player> i = getWinners().iterator();
		while (i.hasNext()) {
			Player p = i.next();
			message += p.getName();
			if (i.hasNext())
				message += " and ";
			else
				message += " won, scoring " + p.getScore() + " points.";
		}
		return message;
	}

	/**
	 * Returns the player whose turn it is.
	 * 
	 * @return the player whose turn it is.
	 */
	private Player currentPlayer() {
		return players.get(currentRound % players.size());
	}

	/**
	 * Returns the current game state.
	 * 
	 * @return the current game state.
	 */
	public State getState() {
		return state;
	}

	/**
	 * Set up the listeners listening to user input
	 */
	private void setupListeners() {
		view.getToolbarPanel().addToolbarActionListener((event) -> {
			switch (event.getActionCommand()) {
			case "Main menu":
				setState(State.GAME_MENU);
				window.dispose();
				break;
			case "Skip":
				nextRound();
				break;
			}
		});
	}

	/**
	 * Go to next round or set state to GAME_OVER (initiates final scoring), when
	 * there are no remaining tiles.
	 */
	public void nextRound() {
		boardPanel.removeTempMeepleOverlay();
		if (stack.remainingTiles() == 0)
			setState(State.GAME_OVER);
		else {
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

	private void initGameboard() {
		board.initGameboard(stack.pickUpTile()); // The topmost tile of the tilestack is always the start tile.
	}

	public void newTile(Tile t, int x, int y) {
		board.newTile(t, x, y);
	}

	private Tile peekTile() {
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

	public List<Player> getPlayers() {
		return players;
	}
}