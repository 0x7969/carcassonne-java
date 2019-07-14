package model;

import static model.Position.BOTTOM;
import static model.Position.BOTTOMLEFT;
import static model.Position.BOTTOMRIGHT;
import static model.Position.LEFT;
import static model.Position.RIGHT;
import static model.Position.TOP;
import static model.Position.TOPLEFT;
import static model.Position.TOPRIGHT;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import base.Edge;

public class Tile {
	private String type;
	private Map<Position, FeatureNode> nodes;
	private final List<Edge<FeatureType>> edges;
	private final boolean coatOfArms;
	private int rotation;

	public Tile(String type) {
		this.type = type;
		nodes = new HashMap<Position, FeatureNode>();
		edges = new LinkedList<Edge<FeatureType>>();
		coatOfArms = false;
	}

	public Tile(String id, boolean coatOfArms) {
		this.type = id;
		nodes = new HashMap<Position, FeatureNode>();
		edges = new LinkedList<Edge<FeatureType>>();
		this.coatOfArms = coatOfArms;
	}

	public String getType() {
		return type;
	}

	public void addNode(Position position, FeatureNode node) {
		nodes.put(position, node);
	}

	public boolean addEdge(Edge<FeatureType> edge) {
		return edges.add(edge);
	}


	public FeatureType featureAtPosition(Position p) {
		return nodes.get(p).getType();
	}

	public FeatureType featureAtPositionComingFromDirection(Position p, Direction d) {
		FeatureNode n = nodes.get(p);
		if(n != null && n.getDirection() == d)
			return n.getType();
		else
			return null;
	}

	public Collection<FeatureNode> getNodes() {
		return nodes.values();
	}

	public void rotateRight() {
		Map<Position, FeatureNode> rotatedNodes = new HashMap<Position, FeatureNode>();
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
		
		for (FeatureNode n : nodes.values()) {
			n.switchDirection();
//			n.rotatePositionRight(); // Wird jetzt über die Map gemacht. TODO entfernen.
		}
		
		if (rotation == 270)
			rotation = 0;
		else
			rotation += 90;
	}

	public int getRotation() {
		return rotation;
	}

	public String toString() {
		return "Type: " + type +
				", top feature: " + featureAtPosition(TOP) + 
				", left feature: " + featureAtPosition(LEFT) +
				", right feature: " + featureAtPosition(RIGHT) +
				", bottom feature: " + featureAtPosition(BOTTOM) +   
				", rotation: " + rotation;
	}

}
