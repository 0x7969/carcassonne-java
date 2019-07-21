package fop.controller;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import fop.view.MenuView;
import fop.view.Observer;
import fop.view.View;

public class GameController {

	private final static Logger LOG = Logger.getLogger("Carcassonne");

	private JFrame window;
	private View view;
	private State state;
	private Gameboard board;
	private TileStack stack;

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
	private void setView(View view) {
		this.view = view;
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
			setupListeners();
			break;
		case GAME_START:
			board = new Gameboard();
			stack = new TileStack();
			setView(new GameView(this));
			setupListeners();
			setState(State.PLACING_TILE);
			break;
		case GAME_SCORE:
			// score anzeigen
			break;
		default:
			break;
		}
	}

	private void setupListeners() {
		switch (state) {
		case GAME_MENU:
			view.addActionListener(new GameMenuActionListener());
			break;
		case GAME_START:
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

			view.addMouseMotionListener(new MouseAdapter() {
				@Override
				public void mouseMoved(MouseEvent event) {
					// mooooooove
				}
			});
		default:
			break;
		}

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

	private class GameMenuActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			// hier kann dann noch genauer der button abgefragt werden usw.
			setState(State.GAME_START);
		}
	}

}