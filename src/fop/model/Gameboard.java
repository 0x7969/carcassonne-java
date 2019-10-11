package fop.model;

import static fop.model.FeatureType.CASTLE;
import static fop.model.FeatureType.ROAD;
import static fop.model.FeatureType.MONASTERY;
import static fop.model.Position.BOTTOM;
import static fop.model.Position.LEFT;
import static fop.model.Position.RIGHT;
import static fop.model.Position.TOP;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import fop.base.Node;
import fop.base.WeightedEdge;

public class Gameboard extends Observable<Gameboard> {

	private final static Logger LOG = Logger.getLogger("Carcassonne");

	private Tile[][] board;
	private List<Tile> tiles; // secondary structure as a helper, should be replaced
	private final FeatureGraph graph;
	private Tile newestTile;

	public Gameboard() {
		board = new Tile[1000][1000]; // TODO variabel je nach anzahl an tiles?
		tiles = new LinkedList<Tile>();
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
		tiles.add(t);

		connectNodes(x, y);
		push(this); // pushes the new gameboard state to its observers (= GameBoardPanel)
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

	public void calculatePoints() {
		calculatePoints(ROAD);
		calculatePoints(CASTLE);
		calculateMonasteries();
		// TODO eigentlich müsste man das gar nicht trennen. wir wollen nur wiesen noch
		// nicht behandeln.
		// sobald die verbindung/berechnung der wiesen funktioniert, können alle punkte
		// in einem rutsch berechnet werden
		// (für die wiese muss dann trotzdem noch zusätzlich die anzahl der berührten
		// fertigen castles gesammelt werden).
	}

	/**
	 * Calculates points for monasteries (one point for each adjacent tile).
	 */
	private void calculateMonasteries() {
		int score = 1;
		for (Tile t : tiles) {
			FeatureNode node = t.getNode(Position.CENTER);
			if (node != null && node.getType() == MONASTERY && node.hasMeeple()) {
				int x = t.x;
				int y = t.y;

				// Check top left tile
				if (board[x - 1][y - 1] != null) {
					score++;
					System.out.print(score + " ");
					System.out.println(board[x - 1][y - 1].getType());
				}

				// Check top tile
				if (board[x][y - 1] != null) {
					score++;
					System.out.print(score + " ");
					System.out.println(board[x][y - 1].getType());
				}

				// Check top right tile
				if (board[x + 1][y - 1] != null) {
					score++;
					System.out.print(score + " ");
					System.out.println(board[x + 1][y - 1].getType());
				}

				// Check left tile
				if (board[x - 1][y] != null) {
					score++;
					System.out.print(score + " ");
					System.out.println(board[x - 1][y].getType());
				}

				// Check right tile
				if (board[x + 1][y] != null) {
					score++;
					System.out.print(score + " ");
					System.out.println(board[x + 1][y].getType());
				}

				// Check bottom left tile
				if (board[x - 1][y + 1] != null) {
					score++;
					System.out.print(score + " ");
					System.out.println(board[x - 1][y + 1].getType());
				}

				// Check bottom tile
				if (board[x][y + 1] != null) {
					score++;
					System.out.print(score + " ");
					System.out.println(board[x][y + 1].getType());
				}

				// Check bottom right tile
				if (board[x + 1][y + 1] != null) {
					score++;
					System.out.print(score + " ");
					System.out.println(board[x + 1][y + 1].getType());
				}

				if (score == 9) {
					node.getPlayer().addScore(score);
					node.getPlayer().returnMeeple();
					LOG.info("Player " + node.getPlayer().getName() + " scored " + score + " points completing a monastery.");
					node.setPlayer(null);
				}
			}
			score = 1;
		}
	}

	private void calculatePoints(FeatureType type) {
		List<Node<FeatureType>> nodeList = new ArrayList<>(graph.getNodes(type));
		ArrayDeque<Node<FeatureType>> queue = new ArrayDeque<>();

		HashMap<Player, Integer> meeplesPerPlayer = new HashMap<Player, Integer>(); // Counts the meeples met while
																					// traversing
		List<FeatureNode> nodesWithMeeple = new LinkedList<FeatureNode>(); // Collects all nodes with meeple so we can
																			// remove them later, in case the feature is
																			// completed.
		List<Tile> visitedTiles = new LinkedList<Tile>(); // Collects all visited tiles

		int score = 0;
		boolean completed = true; // Is the feature completed? Is set to false if a node is visited that does not
									// connect to any other tile

		queue.push(nodeList.remove(0));
		while (!queue.isEmpty()) {
			FeatureNode node = (FeatureNode) queue.pop(); 
			Tile tile = getTileContainingNode(node);

			if (!visitedTiles.contains(tile)) {
				score++;
				if (type == CASTLE && tile.hasPennant())
					score += 1;
				visitedTiles.add(tile);
			}

			// If there is one straight positioned node that does not connect to another
			// tile, the feature cannot be completed.
			if (tile.isNodeOnStraightPosition(node) && !node.isConnectingTiles()) {
				completed = false;
			}

			// Collect the meeples encountered on traversal, so we know who has the most
			// meeples on a connected feature subgraph.
			Player meeple = node.getPlayer();
			if (meeple != null) {
				nodesWithMeeple.add(node);
				int previousMeeplesFromPlayer = meeplesPerPlayer.getOrDefault(meeple, 0);
				meeplesPerPlayer.put(meeple, previousMeeplesFromPlayer + 1);
			}

			List<WeightedEdge<FeatureType>> edges = graph.getEdges(node);
			for (WeightedEdge<FeatureType> edge : edges) {
				Node<FeatureType> nextNode = edge.getOtherNode(node);
				if (nodeList.contains(nextNode)) {
					LOG.finer("Adding points of edge connecting a " + node.getValue() + " on " + tile.x + "/" + tile.y + "and a "
							+ edge.getOtherNode(node).getValue() + ", weight " + edge.getWeight());
//					score += edge.getWeight();

					queue.push(nextNode);
					nodeList.remove(nextNode);
				}
			}

			// If the queue is empty, all nodes of a subgraph were visited
			if (queue.isEmpty()) {
				if (type == CASTLE)
					score *= 2;

				if (completed && meeplesPerPlayer.size() > 0) {
					int maxMeepleCount = Collections.max(meeplesPerPlayer.values());
					List<Player> playersWithMostMeeples = meeplesPerPlayer.entrySet().stream()
							.filter(e -> e.getValue().equals(maxMeepleCount)).map(e -> e.getKey())
							.collect(Collectors.toList());

					// The player with the most meeples on this feature gets the points
					// in case of a tie: every player gets the points
					for (Player p : playersWithMostMeeples) {
						p.addScore(score);
						LOG.info("Player " + p.getName() + " scored " + score + " points completing a " + node.getType().toString().toLowerCase() + ".");
					}

					// Now that the score is added, the meeple are returned to the players
					// inventories and removed from the tile.
					for (FeatureNode n : nodesWithMeeple) {
						n.getPlayer().returnMeeple();
						n.setPlayer(null);
					}
				}

				System.out.println(type.toString() + " of " + score + " points. Connects? " + completed + ".");

				nodesWithMeeple = new LinkedList<FeatureNode>();
				meeplesPerPlayer = new HashMap<Player, Integer>(); // reset collected meeples
				score = 0; // reset score
				completed = true; // reset connects
				visitedTiles = new LinkedList<Tile>(); // rest visited tiles
				// If there are nonetheless nodes left, there must be another subgraph
				if (!nodeList.isEmpty())
					queue.push(nodeList.remove(0));
			}
		}
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	public Tile getTileContainingNode(FeatureNode node) {
		for (Tile t : tiles) {
			if (t.containsNode(node))
				return t;
		}
		return null;
	}

	/**
	 * Returns the spots on which it is allowed to place a meeple on the most
	 * recently placed tile.
	 * 
	 * @return The spots on which it is allowed to place a meeple as a boolean array
	 *         representing the tile split in nine cells from top left, to right, to
	 *         bottom right.
	 */
	public boolean[] getMeepleSpots() {
		boolean[] positions = new boolean[9];

		for (Position p : Position.values()) {
			FeatureNode n = newestTile.getNodeAtPosition(p);
			if (n != null)
				if (n.hasMeepleSpot() && !hasMeepleOnSubGraph(n))
					positions[p.ordinal()] = true;
		}

		return positions;
	}

	private boolean hasMeepleOnSubGraph(FeatureNode n) {
		List<Node<FeatureType>> visitedNodes = new ArrayList<>();
		ArrayDeque<Node<FeatureType>> queue = new ArrayDeque<>();

		queue.push(n);
		while (!queue.isEmpty()) {
			FeatureNode node = (FeatureNode) queue.pop();
			if (node.hasMeeple())
				return true;

			List<WeightedEdge<FeatureType>> edges = graph.getEdges(node);
			for (WeightedEdge<FeatureType> edge : edges) {
				Node<FeatureType> nextNode = edge.getOtherNode(node);
				if (!visitedNodes.contains(nextNode)) {
					queue.push(nextNode);
					visitedNodes.add(nextNode);
				}
			}
		}
		return false;
	}

	public Tile getNewestTile() {
		return newestTile;
	}

	public void placeMeeple(Position position, Player player) {
		board[newestTile.x][newestTile.y].getNode(position).setPlayer(player);
		player.removeMeeple();
	}

}