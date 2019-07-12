package model;

import base.Node;

import static model.Direction.*;

public class FeatureNode extends Node<FeatureType> {
	
	private Position position;
	private Direction direction;

	/**
	 * Creates a new FeatureNode. It is only present when coming from the given direction.
	 * @param type The features type.
	 * @param position Its position on a tile.
	 * @param direction The direction in which it is present.
	 */
	public FeatureNode(FeatureType type, Position position, Direction direction) {
		super(type);
		this.position = position;
		this.direction = direction;
	}
	
	/**
	 * Creates a new FeatureNode. It is assumed that the feature is present in both directions.
	 * @param type The features type.
	 * @param position Its position on a tile.
	 */
	public FeatureNode(FeatureType type, Position position) {
		super(type);
		this.position = position;
		this.direction = BOTH;
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
