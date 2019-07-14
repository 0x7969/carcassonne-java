package model;

import java.util.LinkedList;
import java.util.List;

import view.GameboardObserver;
import view.Observer;

import static model.Position.*;

public class Gameboard implements Observable<Gameboard> {
	private Tile[][] board;
	private List<GameboardObserver> observers;

	public Gameboard() {
		board = new Tile[1000][1000]; // TODO variabel je nach anzahl an tiles?
		observers = new LinkedList<GameboardObserver>();
	}

	// kann nicht im konstrukor erfolgen, weil erst observer gesetzt werden muss
	public void initGameboard(Tile t) {
		newTile(t, 500, 500); // TODO variabel je nach anzahl an tiles?
	}

	public void newTile(Tile t, int x, int y) {
		board[x][y] = t;
		for (GameboardObserver o : observers)
			o.newTile(t.getType(), t.getRotation(), x, y);
	}

	public boolean isAllowed(Tile t, int x, int y) {
		// Check top tile
		if (board[x][y - 1] != null) {
			System.out.println("Their bottom: " + board[x][y - 1].featureAtPosition(BOTTOM) + ", my top: "
					+ t.featureAtPosition(TOP));
			if (board[x][y - 1].featureAtPosition(BOTTOM) != t.featureAtPosition(TOP)) {
				return false;
			}
		}

		// Check left tile
		if (board[x - 1][y] != null) {
			System.out.println("Their right: " + board[x - 1][y].featureAtPosition(RIGHT) + ", my left: "
					+ t.featureAtPosition(LEFT));
			if (board[x - 1][y].featureAtPosition(RIGHT) != t.featureAtPosition(LEFT)) {
				return false;
			}
		}

		// Check right tile
		if (board[x + 1][y] != null) {
			System.out.println("Their left: " + board[x + 1][y].featureAtPosition(LEFT) + ", my right: "
					+ t.featureAtPosition(RIGHT));
			if (board[x + 1][y].featureAtPosition(LEFT) != t.featureAtPosition(RIGHT)) {
				return false;
			}
		}

		// Check bottom tile
		if (board[x][y + 1] != null) {
			System.out.println("Their top: " + board[x][y + 1].featureAtPosition(TOP) + ", my bottom: "
					+ t.featureAtPosition(BOTTOM));
			if (board[x][y + 1].featureAtPosition(TOP) != t.featureAtPosition(BOTTOM)) {
				return false;
			}
		}

		System.out.println(t.getType() + ": Has a " + t.featureAtPosition(TOP) + " on its top, a "
				+ t.featureAtPosition(LEFT) + " on its left, a " + t.featureAtPosition(RIGHT) + " on its right and a "
				+ t.featureAtPosition(BOTTOM) + " on its bottom.");

		return true;
	}

	public Tile[][] getBoard() {
		return board;
	}

	@Override
	public boolean addObserver(Observer<Gameboard> o) {
		return observers.add((GameboardObserver) o);
	}

	@Override
	public boolean removeObserver(Observer<Gameboard> o) {
		return observers.remove((GameboardObserver) o);
	}

}
