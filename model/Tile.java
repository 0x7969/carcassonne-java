package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import base.Edge;

import static model.Direction.*;
import static model.Position.*;

public class Tile {
	private String type;
	private final List<FeatureNode> nodes;
	private final List<Edge<FeatureType>> edges;
	private final boolean coatOfArms;
	private int rotation;

	public Tile(String type) {
		this.type = type;
		nodes = new ArrayList<FeatureNode>();
		edges = new LinkedList<Edge<FeatureType>>();
		coatOfArms = false;
	}

	public Tile(String id, boolean coatOfArms) {
		this.type = id;
		nodes = new ArrayList<FeatureNode>();
		edges = new LinkedList<Edge<FeatureType>>();
		this.coatOfArms = coatOfArms;
	}

	public String getType() {
		return type;
	}

	public boolean addNode(FeatureNode node) {
		return nodes.add(node);
	}

	public boolean addEdge(Edge<FeatureType> edge) {
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
		return nodes.stream().filter(n -> n.getPosition() == p && n.getDirection() == d).findFirst().orElse(null)
				.getType();
	}

	public List<FeatureNode> getNodes() {
		return nodes;
	}

	public void rotateRight() {
		System.out.println("IS SPINNIN BISH");
		for (FeatureNode n : nodes) {
			n.switchDirection();
			n.rotatePositionRight();
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
