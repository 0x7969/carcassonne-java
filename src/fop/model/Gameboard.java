package fop.model;

import static fop.model.FeatureType.CASTLE;
import static fop.model.FeatureType.ROAD;
import static fop.model.Position.BOTTOM;
import static fop.model.Position.LEFT;
import static fop.model.Position.RIGHT;
import static fop.model.Position.TOP;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import fop.base.Node;
import fop.base.WeightedEdge;

public class Gameboard extends Observable<Gameboard> {

	private final static Logger LOG = Logger.getLogger("Carcassonne");

	private Tile[][] board;
	private final FeatureGraph graph;
	private Tile newestTile;

	public Gameboard() {
		board = new Tile[1000][1000]; // TODO variabel je nach anzahl an tiles?
		graph = new FeatureGraph();
	}

	// kann nicht im konstrukor erfolgen, weil erst observer gesetzt werden muss
	public void initGameboard(Tile t) {
		newTile(t, 500, 500); // TODO variabel je nach anzahl an tiles?
	}

	public void newTile(Tile t, int x, int y) {
		t.x = x; // TODO unschön
		t.y = y;
		board[x][y] = newestTile = t;

		connectNodes(x, y);
		push(this);
		
		calculatePoints(ROAD); // TODO soll eigentlich durch state change ausgelöst werden
		calculatePoints(CASTLE);
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

			List<WeightedEdge<FeatureType>> edges = graph.getEdges(node);
			for (WeightedEdge<FeatureType> edge : edges) {
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

	}
	
	public boolean[] getMeepleSpots(int x, int y) {
		return board[x][y].getMeepleSpots();
	}
	
	public Tile getNewestTile() {
		return newestTile;
	}

}
