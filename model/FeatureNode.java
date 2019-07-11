package model;

import base.Node;

public class FeatureNode extends Node<FeatureType> {
	
	private Position position;
	private Direction direction;

	public FeatureNode(FeatureType type, Position position, Direction direction) {
		super(type);
		this.position = position;
		this.direction = direction;
	}
	
	public FeatureNode(FeatureType type, Position position) {
		super(type);
		this.position = position;
		this.direction = null;
	}
	
	public FeatureType getType() {
		return getValue();
	}
	
	public Position getPosition() {
		return position;
	}
	
	public Direction getDirection() {
		return direction;
	}

}
