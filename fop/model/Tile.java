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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import fop.base.WeightedEdge;

public class Tile {
	private String type;
	private SortedMap<Position, FeatureNode> nodes; // by using a sorted map, the key and value pairs will be sorted
													// from left to right, top to bottom (following the Position enums
													// order).
	private final List<WeightedEdge<FeatureType>> edges;
	private final boolean coatOfArms;
	private int rotation;

	public Tile(String type) {
		this.type = type;
		nodes = new TreeMap<Position, FeatureNode>();
		edges = new LinkedList<WeightedEdge<FeatureType>>();
		coatOfArms = false;
	}

	public Tile(String type, boolean coatOfArms) {
		this.type = type;
		nodes = new TreeMap<Position, FeatureNode>();
		edges = new LinkedList<WeightedEdge<FeatureType>>();
		this.coatOfArms = coatOfArms;
	}

	public String getType() {
		return type;
	}

	public void addNode(Position position, FeatureNode node) {
		nodes.put(position, node);
	}

	public FeatureNode getNode(Position position) {
		return nodes.get(position);
	}

	public boolean addEdge(WeightedEdge<FeatureType> edge) {
		return edges.add(edge);
	}

	public FeatureType featureAtPosition(Position p) {
		return nodes.get(p).getType();
	}

	public FeatureType featureAtPositionComingFromDirection(Position p, Direction d) {
		FeatureNode n = nodes.get(p);
		if (n != null && n.getDirection() == d)
			return n.getType();
		else
			return null;
	}

	public Collection<FeatureNode> getNodes() {
		return nodes.values();
	}

	public List<WeightedEdge<FeatureType>> getEdges() {
		return edges;
	}

	public void rotateRight() {
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
		
		for (FeatureNode n : rotatedNodes.values()) {
			n.switchDirection();
//			n.rotatePositionRight(); // Wird jetzt über die Map gemacht. TODO entfernen.
		}
		
		nodes = rotatedNodes;

		if (rotation == 270)
			rotation = 0;
		else
			rotation += 90;
	}

	public int getRotation() {
		return rotation;
	}

	public String toString() {
		return "Type: " + type + ", top feature: " + featureAtPosition(TOP) + ", left feature: "
				+ featureAtPosition(LEFT) + ", right feature: " + featureAtPosition(RIGHT) + ", bottom feature: "
				+ featureAtPosition(BOTTOM) + ", rotation: " + rotation;
	}

	public boolean hasCoatOfArms() {
		return coatOfArms;
	}

	public boolean[] getMeepleSpots() {
		boolean[] positions = new boolean[9];
		for (FeatureNode n : nodes.values()) {
			if(n.hasMeepleSpot())
				positions[n.getPosition().ordinal()] = true;
		}
		return positions;
	}

}
