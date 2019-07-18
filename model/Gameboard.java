package model;

import static model.FeatureType.CASTLE;
import static model.FeatureType.ROAD;
import static model.Position.BOTTOM;
import static model.Position.LEFT;
import static model.Position.RIGHT;
import static model.Position.TOP;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import base.Edge;
import base.Node;
import view.GameboardObserver;
import view.Observer;

public class Gameboard implements Observable<Gameboard> {

	private final static Logger LOG = Logger.getLogger("Carcassonne");

	private Tile[][] board;
	private List<GameboardObserver> observers;
	private final FeatureGraph graph;

	public Gameboard() {
		board = new Tile[1000][1000]; // TODO variabel je nach anzahl an tiles?
		observers = new LinkedList<GameboardObserver>();
		graph = new FeatureGraph();
	}

	// kann nicht im konstrukor erfolgen, weil erst observer gesetzt werden muss
	public void initGameboard(Tile t) {
		newTile(t, 500, 500); // TODO variabel je nach anzahl an tiles?
	}

	public void newTile(Tile t, int x, int y) {
		board[x][y] = t;

		connectNodes(x, y);
		for (GameboardObserver o : observers)
			o.newTile(t.getType(), t.getRotation(), x, y);

		calculatePoints(ROAD);
		calculatePoints(CASTLE); // TODO soll eigentlich durch state change ausgelöst werden
		// TODO eigentlich müsste man das gar nicht trennen. wir wollen nur wiesen noch
		// nicht behandeln.
		// sobald die verbindung/berechnung der wiesen funktioniert, können alle punkte
		// in einem rutsch berechnet werden
		// (für die wiese muss dann trotzdem noch zusätzlich die anzahl der berührten
		// fertigen castles gesammelt werden).
	}

	/**
	 * Connects the nodes of all neighbouring tiles facing the tile at given
	 * coordinates x, y. It is assumed that the tile is placed according to the
	 * rules.
	 * 
	 * @param x coordinate
	 * @param y coordinate
	 */
	private void connectNodes(int x, int y) {
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

	public boolean isTileAllowed(Tile t, int x, int y) {
		LOG.finest("Checking if tile would be allowed at cursors position.");

		// Check top tile
		if (board[x][y - 1] != null) {
			LOG.finest("Checking top tile. Its bottom: " + board[x][y - 1].featureAtPosition(BOTTOM) + ", my top: "
					+ t.featureAtPosition(TOP));
			if (board[x][y - 1].featureAtPosition(BOTTOM) != t.featureAtPosition(TOP)) {
				return false;
			}
		}

		// Check left tile
		if (board[x - 1][y] != null) {
			LOG.finest("Check left tile. Its right: " + board[x - 1][y].featureAtPosition(RIGHT) + ", my left: "
					+ t.featureAtPosition(LEFT));
			if (board[x - 1][y].featureAtPosition(RIGHT) != t.featureAtPosition(LEFT)) {
				return false;
			}
		}

		// Check right tile
		if (board[x + 1][y] != null) {
			LOG.finest("Checking right tile. Its left: " + board[x + 1][y].featureAtPosition(LEFT) + ", my right: "
					+ t.featureAtPosition(RIGHT));
			if (board[x + 1][y].featureAtPosition(LEFT) != t.featureAtPosition(RIGHT)) {
				return false;
			}
		}

		// Check bottom tile
		if (board[x][y + 1] != null) {
			LOG.finest("Checking bottom tile. Its top: " + board[x][y + 1].featureAtPosition(TOP) + ", my bottom: "
					+ t.featureAtPosition(BOTTOM));
			if (board[x][y + 1].featureAtPosition(TOP) != t.featureAtPosition(BOTTOM)) {
				return false;
			}
		}
		return true;
	}

	private List<Integer> calculatePoints(FeatureType type) {
		List<Integer> scores = new LinkedList<Integer>();
		List<Node<FeatureType>> nodeList = new ArrayList<>(graph.getNodes(type));
		ArrayDeque<Node<FeatureType>> queue = new ArrayDeque<>(); // TODO hat deque hier einen vorteil? habs übernommen

		int score = 1; // As we only get points for transitioning between tiles, we start with 1.
		boolean connects = true; // Is set to false if a node is visited that does not connect to any other tile
		queue.push(nodeList.remove(0));
		while (!queue.isEmpty()) {
			FeatureNode node = (FeatureNode) queue.pop();
			if (Arrays.stream(Position.getStraightPositions()).anyMatch(node.getPosition()::equals)) { // TODO etwas
																										// unschön...
				if (!node.isConnectingTiles()) {
					connects = false;
				}

			}

			List<Edge<FeatureType>> edges = graph.getEdges(node);
			for (Edge<FeatureType> edge : edges) {
				Node<FeatureType> nextNode = edge.getOtherNode(node);
				if (nodeList.contains(nextNode)) {
					LOG.info("Adding points of edge connecting a " + node.getValue() + " and a "
							+ edge.getOtherNode(node).getValue() + ", weight " + edge.getWeight());
					score += edge.getWeight();
					queue.push(nextNode);
					nodeList.remove(nextNode);
				}
			}
			// If the queue is empty, all nodes of a subgraph were visited
			if (queue.isEmpty()) {
				if (type == CASTLE)
					score *= 2;
				scores.add(score);
				System.out.println(type.toString() + " of " + score + " points. Connects? " + connects);
				score = 1;
				connects = true;
				// If there are nonetheless nodes left, there must be another subgraph
				if (!nodeList.isEmpty())
					queue.push(nodeList.remove(0));
			}
		}
		return scores;

//		System.out.println("+++++++");
//		return points;
//        List<Node<FeatureType>> nodeList = new ArrayList<>(graph.getNodes());
//        int points = 0;
//        
//        ArrayDeque<Node<FeatureType>> queue = new ArrayDeque<>();
//        queue.push(nodeList.remove(0));
//
//        while(!queue.isEmpty()) {
//            Node<FeatureType> node = queue.pop();
//            List<Edge<FeatureType>> edges = graph.getEdges(node);
//            for(Edge<FeatureType> edge : edges) {
//                Node<FeatureType> nextNode = edge.getOtherNode(node);
//                if(nodeList.contains(nextNode)) {
//                    queue.push(nextNode);
//                    nodeList.remove(nextNode);
//                }
//            }
//        }
//
//        return nodeList.isEmpty();
	}

	public Tile[][] getBoard() {
		return board;
	}

	public boolean addObserver(Observer<Gameboard> o) {
		return observers.add((GameboardObserver) o);
	}

	public boolean removeObserver(Observer<Gameboard> o) {
		return observers.remove((GameboardObserver) o);
	}

}
