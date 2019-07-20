package fop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
import fop.view.Observer;
import fop.view.View;

public class GameController {

	private final static Logger LOG = Logger.getLogger("Carcassonne");

	private JFrame window;
	private View view;
	private GameState state;
	private Gameboard board;
	private TileStack stack;

	public GameController() {
		board = new Gameboard();
		stack = new TileStack();
		state = new MenuState(this);
		initView();

		view.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				state.mouseClicked(event);
			}
		});

		view.addMouseMotionListener(new MouseAdapter() {

			@Override
			public void mouseMoved(MouseEvent event) {
				state.mouseMoved(event);
			}

			@Override
			public void mouseDragged(MouseEvent event) {
				state.mouseDragged(event);
			}
		});

		view.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				state.mouseWheelMoved(event);
			}
		});

		view.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				state.actionPerformed(event);
			}
		});

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

		view = state.initView();

		window = new JFrame("Carcassonne");
		window.setSize(400, 300);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane(view);
		window.setVisible(true);
	}
	
	public void setState(GameState state) {
		this.state = state;
		window.setContentPane(state.initView());
		window.setSize(1200, 900);
		window.revalidate();
		window.repaint();
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