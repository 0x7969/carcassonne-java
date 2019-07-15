package model;

import static model.Position.BOTTOM;
import static model.Position.LEFT;
import static model.Position.RIGHT;
import static model.Position.TOP;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import base.Edge;
import base.Graph;
import base.Node;
import view.GameboardObserver;
import view.Observer;

public class Gameboard implements Observable<Gameboard> {
	private Tile[][] board;
	private List<GameboardObserver> observers;
	private final Graph<FeatureType> graph;

	public Gameboard() {
		board = new Tile[1000][1000]; // TODO variabel je nach anzahl an tiles?
		observers = new LinkedList<GameboardObserver>();
		graph = new Graph<FeatureType>();
	}

	// kann nicht im konstrukor erfolgen, weil erst observer gesetzt werden muss
	public void initGameboard(Tile t) {
		newTile(t, 500, 500); // TODO variabel je nach anzahl an tiles?
	}

	public void newTile(Tile t, int x, int y) {
		board[x][y] = t;
		
		connectNodesOfNewTile(x, y);
		
		for (GameboardObserver o : observers)
			o.newTile(t.getType(), t.getRotation(), x, y);
	}
	
	/**
	 * Connects the nodes of all neighbouring tiles facing the tile at coordinates x, y.
	 * It is assumed that the tile is placed according to the rules.
	 * 
	 * @param x coordinate
	 * @param y coordinate
	 */
	private void connectNodesOfNewTile(int x, int y) {
		graph.addAllNodes(board[x][y].getNodes());
		graph.addAllEdges(board[x][y].getEdges());
		
		Tile t = board[x][y];
		
		// Check top tile
		if (board[x][y - 1] != null) {
			graph.addEdge(board[x][y - 1].getNode(BOTTOM), t.getNode(TOP), 1);
		}

		// Check left tile
		if (board[x - 1][y] != null) {
			graph.addEdge(board[x - 1][y].getNode(RIGHT), t.getNode(LEFT), 1);
		}

		// Check right tile
		if (board[x + 1][y] != null) {
			graph.addEdge(board[x + 1][y].getNode(LEFT), t.getNode(RIGHT), 1);
		}

		// Check bottom tile
		if (board[x][y + 1] != null) {
			graph.addEdge(board[x][y + 1].getNode(TOP), t.getNode(BOTTOM), 1);
		}
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
