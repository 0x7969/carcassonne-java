package fop.model;

import static fop.model.Position.BOTTOM;
import static fop.model.Position.BOTTOMLEFT;
import static fop.model.Position.BOTTOMRIGHT;
import static fop.model.Position.CENTER;
import static fop.model.Position.LEFT;
import static fop.model.Position.RIGHT;
import static fop.model.Position.TOP;
import static fop.model.Position.TOPLEFT;
import static fop.model.Position.TOPRIGHT;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import fop.base.Edge;

public class Tile {
	private TileType type;
	private SortedMap<Position, FeatureNode> nodes; // by using a sorted map, the key and value pairs will be sorted
													// from left to right, top to bottom (following the Position enums
													// order).
	private final List<Edge<FeatureType>> edges;
	private final boolean pennant;
	private int rotation;
	public int x;
	public int y;

	Tile(TileType type) {
		this.type = type;
		nodes = new TreeMap<Position, FeatureNode>();
		edges = new LinkedList<Edge<FeatureType>>();
		pennant = false;
	}

	Tile(TileType type, boolean coatOfArms) {
		this.type = type;
		nodes = new TreeMap<Position, FeatureNode>();
		edges = new LinkedList<Edge<FeatureType>>();
		this.pennant = coatOfArms;
	}

	public TileType getType() {
		return type;
	}

	void addNode(Position position, FeatureNode node) {
		nodes.put(position, node);
	}

	FeatureNode getNode(Position position) {
		return nodes.get(position);
	}

	boolean containsNode(FeatureNode node) {
		for (FeatureNode n : nodes.values()) {
			if (n == node)
				return true;
		}
		return false;
	}

	boolean addEdge(Edge<FeatureType> edge) {
		return edges.add(edge);
	}

	FeatureType featureAtPosition(Position p) {
		return nodes.get(p).getType();
	}

	public Collection<FeatureNode> getNodes() {
		return nodes.values();
	}

	public List<Edge<FeatureType>> getEdges() {
		return edges;
	}

	/**
	 * Returns the node at given position.
	 * 
	 * @param p A Position
	 * @return The node at given position or null.
	 */
	FeatureNode getNodeAtPosition(Position p) {
		return nodes.get(p);
	}

	/**
	 * Rotates this tile 90 degree to the right and updates the position and
	 * direction of its nodes accordingly.
	 */
	void rotateRight() {
		SortedMap<Position, FeatureNode> rotatedNodes = new TreeMap<Position, FeatureNode>();

		for (Position p : nodes.keySet()) {
			switch (p) {
			case TOPLEFT:
				rotatedNodes.put(TOPRIGHT, nodes.get(p));
				break;
			case TOP:
				rotatedNodes.put(RIGHT, nodes.get(p));
				break;
			case TOPRIGHT:
				rotatedNodes.put(BOTTOMRIGHT, nodes.get(p));
				break;
			case LEFT:
				rotatedNodes.put(TOP, nodes.get(p));
				break;
			case CENTER:
				rotatedNodes.put(CENTER, nodes.get(p));
				break;
			case RIGHT:
				rotatedNodes.put(BOTTOM, nodes.get(p));
				break;
			case BOTTOMLEFT:
				rotatedNodes.put(TOPLEFT, nodes.get(p));
				break;
			case BOTTOM:
				rotatedNodes.put(LEFT, nodes.get(p));
				break;
			case BOTTOMRIGHT:
				rotatedNodes.put(BOTTOMLEFT, nodes.get(p));
				break;
			}
		}

		nodes = rotatedNodes;

		if (rotation == 270)
			rotation = 0;
		else
			rotation += 90;
	}

	/**
	 * Returns this tiles current rotation in degree.
	 * 
	 * @return this tiles current rotation in degree.
	 */
	public int getRotation() {
		return rotation;
	}

	public String toString() {
		return "Type: " + type + ", top feature: " + featureAtPosition(TOP) + ", left feature: "
				+ featureAtPosition(LEFT) + ", right feature: " + featureAtPosition(RIGHT) + ", bottom feature: "
				+ featureAtPosition(BOTTOM) + ", rotation: " + rotation;
	}

	/**
	 * Returns if this tile has a pennant.
	 * 
	 * @return true if this tile has a pennant, false if not.
	 */
	boolean hasPennant() {
		return pennant;
	}

	/**
	 * 
	 * @return The position on which a meeple was placed or null if no meeple was
	 *         placed.
	 */
	public Position getMeeplePosition() {
		for (Position p : Position.values()) {
			FeatureNode n = nodes.get(p);
			if (n != null && n.hasMeeple())
				return p;
		}

		return null;
	}

	/**
	 * Returns true if this tile has a meeple placed on it.
	 * 
	 * @return true if it does, false if it doesn't
	 */
	public boolean hasMeeple() {
		for (FeatureNode n : nodes.values())
			if (n.hasMeeple())
				return true;
		return false;
	}

	/**
	 * Returns the Player that has placed a meeple on this tile.
	 * 
	 * @return The Player that has placed a meeple or null if there is no meeple
	 *         placed here.
	 */
	public Player getMeeple() {
		for (FeatureNode n : nodes.values())
			if (n.hasMeeple())
				return n.getPlayer();
		return null;
	}

}
