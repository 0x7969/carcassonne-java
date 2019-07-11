package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import base.Edge;

import static model.Direction.*;

public class Tile {
	private String type;
	private final List<FeatureNode> nodes;
	private final List<Edge> edges;
	private final boolean coatOfArms;
	private int rotation;

	public Tile(String id) {
		this.type = id;
		nodes = new ArrayList<FeatureNode>();
        edges = new LinkedList<Edge>();
        coatOfArms = false;
	}
	
	public Tile(String id, boolean coatOfArms) {
		this.type = id;
		nodes = new ArrayList<FeatureNode>();
        edges = new LinkedList<Edge>();
        this.coatOfArms = coatOfArms;
	}
	
	public String getType() {
		return type;
	}
	
	public boolean addNode(FeatureNode node) {
		return nodes.add(node);
	}
	
	public boolean addEdge(Edge edge) {
		return edges.add(edge);
	}

	public FeatureNode top(Position p) {
		return nodes.stream().filter(n -> n.getPosition() == p).findFirst().orElse(null);
	}

	public FeatureType featureAtPosition(Position p) {
		return nodes.stream().filter(n -> n.getPosition() == p && n.getDirection() == BOTH).findFirst().orElse(null)
				.getType();
	}

	public FeatureType featureAtPositionComingFromDirection(Position p, Direction d) {
		return nodes.stream().filter(n -> n.getPosition() == p && n.getDirection() == d).findFirst()
				.orElse(null).getType();
	}
	
	public void rotate() {
		rotation += 90;
	}

	public int getRotation() {
		return rotation;
	}

}
