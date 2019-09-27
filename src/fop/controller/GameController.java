package fop.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import fop.model.Gameboard;
import fop.model.Tile;
import fop.model.TileStack;
import fop.view.GameBoardPanel;
import fop.view.GameView;
import fop.view.GameboardObserver;
import fop.view.MenuView;
import fop.view.Observer;
import fop.view.TileStackPanel;
import fop.view.View;

public class GameController {

	private final static Logger LOG = Logger.getLogger("Carcassonne");

	private JFrame window;
	private GameView view;
	private State state;
	private Gameboard board;
	private TileStack stack;
	private GameBoardPanel boardPanel;
	private TileStackPanel stackPanel;

	public GameController() {
		setupWindow();
		setState(State.GAME_MENU);

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
			stack.push(stack); // pushes stack to observers (= tileStackPanel)
			view.getToolbarPanel().toggleSkipButton();
			System.out.println("Please place a tile.");
			break;
		case PLACING_MEEPLE:
			stackPanel.hideTileStack();
			view.getToolbarPanel().toggleSkipButton();
			System.out.println("Please place a meeple or not.");
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
				setState(State.PLACING_TILE);
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

	private void setupObservers() {
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

	public void addGameBoardObserver(GameboardObserver o) {
		board.addObserver(o);
	}

	public void addTileStackObserver(Observer<TileStack> o) {
		stack.addObserver(o);
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