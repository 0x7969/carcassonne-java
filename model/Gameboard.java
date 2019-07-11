package model;

import java.util.LinkedList;
import java.util.List;

import view.GameboardObserver;
import view.Observer;

public class Gameboard implements Observable<Gameboard> {
	private Tile[][] board;
	private List<GameboardObserver> observers;

	public Gameboard() {
		board = new Tile[1000][1000];
		observers = new LinkedList<GameboardObserver>();
	}

	// kann nicht im konstrukor erfolgen, weil erst observer gesetzt werden muss
	public void initGameboard() {
		placeTile("D", 500, 500);
	}

	public void placeTile(String id, int x, int y) {
		board[x][y] = new Tile(id);
		for (GameboardObserver o : observers)
			o.setTileID(id, x, y);
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
