package fop.model;

import static fop.model.FeatureType.CASTLE;
import static fop.model.FeatureType.ROAD;
import static fop.model.FeatureType.MONASTERY;
import static fop.model.FeatureType.FIELDS;
import static fop.model.Position.BOTTOM;
import static fop.model.Position.LEFT;
import static fop.model.Position.RIGHT;
import static fop.model.Position.TOP;
import static fop.model.Position.TOPRIGHT;
import static fop.model.Position.TOPLEFT;
import static fop.model.Position.BOTTOMLEFT;
import static fop.model.Position.BOTTOMRIGHT;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import fop.base.Node;
import fop.base.Edge;
import fop.controller.State;

public class Gameboard extends Observable<Gameboard> {

	private final static Logger LOG = Logger.getLogger("Carcassonne");

	private Tile[][] board;
	private List<Tile> tiles;
	private final FeatureGraph graph;
	private Tile newestTile;

	public Gameboard() {
		board = new Tile[144][144];
		tiles = new LinkedList<Tile>();
		graph = new FeatureGraph();
	}

	// kann nicht im konstrukor erfolgen, weil erst observer gesetzt werden muss
	public void initGameboard(Tile t) {
		newTile(t, 72, 72);
	}

	public void newTile(Tile t, int x, int y) {
		t.x = x;
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

			// As we already ensured that the tile on top exists and fits the tile at x, y,
			// we know that if the feature of its top is a ROAD, the feature at the bottom
			// of the tile on top is a ROAD aswell. As every ROAD has FIELD nodes as
			// neighbours on both sides, we can connect those nodes of the two tiles. The
			// same logic applies to the next three routines.
			if (t.getNode(TOP).getType() == ROAD) {
				graph.addEdge(board[x][y - 1].getNode(BOTTOMLEFT), t.getNode(TOPLEFT), 1);
				graph.addEdge(board[x][y - 1].getNode(BOTTOMRIGHT), t.getNode(TOPRIGHT), 1);
			}
		}

		// Check left tile
		if (board[x - 1][y] != null) {
			graph.addEdge(board[x - 1][y].getNode(RIGHT), t.getNode(LEFT), 1);

			if (t.getNode(LEFT).getType() == ROAD) {
				graph.addEdge(board[x - 1][y].getNode(TOPRIGHT), t.getNode(TOPLEFT), 1);
				graph.addEdge(board[x - 1][y].getNode(BOTTOMRIGHT), t.getNode(BOTTOMLEFT), 1);
			}
		}

		// Check right tile
		if (board[x + 1][y] != null) {
			graph.addEdge(board[x + 1][y].getNode(LEFT), t.getNode(RIGHT), 1);

			if (t.getNode(RIGHT).getType() == ROAD) {
				graph.addEdge(board[x + 1][y].getNode(TOPLEFT), t.getNode(TOPRIGHT), 1);
				graph.addEdge(board[x + 1][y].getNode(BOTTOMLEFT), t.getNode(BOTTOMRIGHT), 1);
			}
		}

		// Check bottom tile
		if (board[x][y + 1] != null) {
			graph.addEdge(board[x][y + 1].getNode(TOP), t.getNode(BOTTOM), 1);

			if (t.getNode(BOTTOM).getType() == ROAD) {
				graph.addEdge(board[x][y + 1].getNode(TOPLEFT), t.getNode(BOTTOMLEFT), 1);
				graph.addEdge(board[x][y + 1].getNode(TOPRIGHT), t.getNode(BOTTOMRIGHT), 1);
			}
		}
	}

	/**
	 * Checks if the given tile could be placed at position x, y on the board
	 * according to the rules.
	 * 
	 * @param t The tile
	 * @param x The x position on the board
	 * @param y The y position on the board
	 * @return True if it would be allowed, false if not.
	 */
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

	/**
	 * Checks if the given tile would be allowed anywhere on the board adjacent to
	 * other tiles and according to the rules.
	 * 
	 * @param newTile The tile.
	 * @return True if it is allowed to place the tile somewhere on the board, false
	 *         if not.
	 */
	public boolean isTileAllowedAnywhere(Tile newTile) {
		for (Tile tile : tiles) {
			// check top
			if (board[tile.x][tile.y - 1] == null)
				for (int i = 0; i < 4; i++) {
					if (isTileAllowed(newTile, tile.x, tile.y - 1))
						return true;
					newTile.rotateRight();
				}

			// check left
			if (board[tile.x - 1][tile.y] == null)
				for (int i = 0; i < 4; i++) {
					if (isTileAllowed(newTile, tile.x - 1, tile.y))
						return true;
					newTile.rotateRight();
				}

			// check right
			if (board[tile.x + 1][tile.y] == null)
				for (int i = 0; i < 4; i++) {
					if (isTileAllowed(newTile, tile.x + 1, tile.y))
						return true;
					newTile.rotateRight();
				}

			// check bottom
			if (board[tile.x][tile.y + 1] == null)
				for (int i = 0; i < 4; i++) {
					if (isTileAllowed(newTile, tile.x, tile.y + 1))
						return true;
					newTile.rotateRight();
				}
		}
		return false;
	}

	/**
	 * Calculates points for monasteries (one point for each adjacent tile).
	 */
	private void calculateMonasteries(State s) {
		int score = 1;
		for (Tile t : tiles) {
			FeatureNode node = t.getNode(Position.CENTER);
			if (node != null && node.getType() == MONASTERY && node.hasMeeple()) {
				int x = t.x;
				int y = t.y;

				// Check top left tile
				if (board[x - 1][y - 1] != null) {
					score++;
				}

				// Check top tile
				if (board[x][y - 1] != null) {
					score++;
				}

				// Check top right tile
				if (board[x + 1][y - 1] != null) {
					score++;
				}

				// Check left tile
				if (board[x - 1][y] != null) {
					score++;
				}

				// Check right tile
				if (board[x + 1][y] != null) {
					score++;
				}

				// Check bottom left tile
				if (board[x - 1][y + 1] != null) {
					score++;
				}

				// Check bottom tile
				if (board[x][y + 1] != null) {
					score++;
				}

				// Check bottom right tile
				if (board[x + 1][y + 1] != null) {
					score++;
				}

				if (score == 9 || s == State.GAME_OVER) {
					node.getPlayer().addScore(score);
					node.getPlayer().returnMeeple();
					LOG.info("Player " + node.getPlayer().getName() + " scored " + score
							+ " points completing a monastery.");
					node.setPlayer(null);
				}
			}
			score = 1;
		}
	}

	/**
	 * Calculates points and adds them to the players score, if a feature was
	 * completed. FIELDS are only calculated when the game is over.
	 * 
	 * @param state The current game state.
	 */
	public void calculatePoints(State state) {
		// Fields are only calculated on final scoring.
		if (state == State.GAME_OVER)
			calculatePoints(FIELDS, state);

		calculatePoints(CASTLE, state);
		calculatePoints(ROAD, state);
		calculateMonasteries(state);
	}

	/**
	 * Calculates and adds points to the players that scored a feature. If the given
	 * state is GAME_OVER, points are added to the player with the most meeple on a
	 * subgraph, even if it is not completed.
	 * 
	 * @param type  The FeatureType that is supposed to be calculated.
	 * @param state The current game state.
	 */
	private void calculatePoints(FeatureType type, State state) {
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
			if (!node.isConnectingTiles()) {
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

			List<Edge<FeatureType>> edges = graph.getEdges(node);
			for (Edge<FeatureType> edge : edges) {
				Node<FeatureType> nextNode = edge.getOtherNode(node);
				if (((FeatureNode) nextNode).getType() != type)
					System.out.println("FATAL ERROR LAWL");
				if (nodeList.contains(nextNode)) {
					queue.push(nextNode);
					nodeList.remove(nextNode);
				}
			}

			// If the queue is empty, all nodes of a subgraph were visited
			if (queue.isEmpty()) {
				// On final scoring, the castles and pennants only give one point.
				if (type == CASTLE && state != State.GAME_OVER)
					score *= 2;
				
				// In our simple variant of counting every tile with fields, scoring fields
				// gets rather imbalanced, so we just divide the points by four.
				if (type == FIELDS)
					score /= 4;

				// If there are meeple placed on this subgraph and we are either calculating the
				// final score or the subgraph is completed.
				if (meeplesPerPlayer.size() > 0 && (state == State.GAME_OVER || completed)) {
					int maxMeepleCount = Collections.max(meeplesPerPlayer.values());
					List<Player> playersWithMostMeeples = meeplesPerPlayer.entrySet().stream()
							.filter(e -> e.getValue().equals(maxMeepleCount)).map(e -> e.getKey())
							.collect(Collectors.toList());

					// The player with the most meeples on this feature gets the points
					// in case of a tie: every player gets the points
					for (Player p : playersWithMostMeeples) {
						p.addScore(score);
						LOG.info("Player " + p.getName() + " scored " + score + " points completing a "
								+ node.getType().toString().toLowerCase() + ".");
					}

					// Now that the score is added, the meeple are returned to the players
					// inventories and removed from the tile.
					for (FeatureNode n : nodesWithMeeple) {
						n.getPlayer().returnMeeple();
						n.setPlayer(null);
					}
				}

				LOG.finer(type.toString() + " of " + score + " points. Connects? " + completed + ".");

				// reset all variables that correspond to a single subgraph
				nodesWithMeeple = new LinkedList<FeatureNode>(); // reset nodes with meeple
				meeplesPerPlayer = new HashMap<Player, Integer>(); // reset collected meeples
				score = 0; // reset score
				completed = true; // reset connects
				visitedTiles = new LinkedList<Tile>(); // rest visited tiles

				// If there are still nodes left, there must be another subgraph
				if (!nodeList.isEmpty())
					queue.push(nodeList.remove(0));
			}
		}
	}

	/**
	 * Returns all Tiles on the Gameboard.
	 * 
	 * @return all Tiles on the Gameboard.
	 */
	public List<Tile> getTiles() {
		return tiles;
	}

	/**
	 * Returns the Tile containing the given FeatureNode.
	 * 
	 * @param node A FeatureNode.
	 * @return the Tile containing the given FeatureNode.
	 */
	public Tile getTileContainingNode(FeatureNode node) {
		for (Tile t : tiles) {
			if (t.containsNode(node))
				return t;
		}
		return null;
	}

	/**
	 * Returns the spots on the most recently placed tile on which it is allowed to
	 * place a meeple .
	 * 
	 * @return The spots on which it is allowed to place a meeple as a boolean array
	 *         representing the tile split in nine cells from top left, to right, to
	 *         bottom right. If there is no spot available at all, returns null.
	 */
	public boolean[] getMeepleSpots() {
		boolean[] positions = new boolean[9];
		boolean anySpot = false; // if there is not a single spot, this remains false

		for (Position p : Position.values()) {
			FeatureNode n = newestTile.getNodeAtPosition(p);
			if (n != null)
				if (n.hasMeepleSpot() && !hasMeepleOnSubGraph(n))
					positions[p.ordinal()] = anySpot = true;
		}

		if (anySpot)
			return positions;
		else
			return null;
	}

	/**
	 * Checks if there are any meeple on the subgraph that FeatureNode n is a part
	 * of.
	 * 
	 * @param n The FeatureNode to be checked.
	 * @return True if the given FeatureNode has any meeple on its subgraph, false
	 *         if not.
	 */
	private boolean hasMeepleOnSubGraph(FeatureNode n) {
		List<Node<FeatureType>> visitedNodes = new ArrayList<>();
		ArrayDeque<Node<FeatureType>> queue = new ArrayDeque<>();

		queue.push(n);
		while (!queue.isEmpty()) {
			FeatureNode node = (FeatureNode) queue.pop();
			if (node.hasMeeple())
				return true;

			List<Edge<FeatureType>> edges = graph.getEdges(node);
			for (Edge<FeatureType> edge : edges) {
				Node<FeatureType> nextNode = edge.getOtherNode(node);
				if (!visitedNodes.contains(nextNode)) {
					queue.push(nextNode);
					visitedNodes.add(nextNode);
				}
			}
		}
		return false;
	}

	/**
	 * Returns the newest tile.
	 * 
	 * @return the newest tile.
	 */
	public Tile getNewestTile() {
		return newestTile;
	}

	/**
	 * Places a meeple of given player at given position on the most recently placed
	 * tile (it is only allowed to place meeple on the most recent tile).
	 * 
	 * @param position The position the meeple is supposed to be placed on on the
	 *                 tile (separated in a 3x3 grid).
	 * @param player   The owner of the meeple.
	 */
	public void placeMeeple(Position position, Player player) {
		board[newestTile.x][newestTile.y].getNode(position).setPlayer(player);
		player.removeMeeple();
	}

}