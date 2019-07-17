package model;

import base.Node;

import static model.Direction.*;
import static model.Position.*;

public class FeatureNode extends Node<FeatureType> {

	private Position position;
	private Direction direction;
	private boolean connectsTiles;

	/**
	 * Creates a new FeatureNode. It is only present when coming from the given
	 * direction.
	 * 
	 * @param type      The features type.
	 * @param position  Its position on a tile.
	 * @param direction The direction in which it is present.
	 */
	public FeatureNode(FeatureType type, Position position, Direction direction) {
		super(type);
		this.position = position;
		this.direction = direction;
		this.connectsTiles = false;
	}

	/**
	 * Creates a new FeatureNode. It is assumed that the feature is present in both
	 * directions.
	 * 
	 * @param type     The features type.
	 * @param position Its position on a tile.
	 */
	public FeatureNode(FeatureType type, Position position) {
		super(type);
		this.position = position;
		this.direction = BOTH;
		this.connectsTiles = false;
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

	public void switchDirection() {
		if (direction == BOTH)
			return;
		else
			direction = direction.getOpposite();
	}

	public void rotatePositionRight() {
		switch (position) {
		case TOPLEFT:
			position = TOPRIGHT;
			break;
		case TOP:
			position = RIGHT;
			break;
		case TOPRIGHT:
			position = BOTTOMRIGHT;
			break;
		case LEFT:
			position = TOP;
			break;
		case CENTER:
			break;
		case RIGHT:
			position = BOTTOM;
			break;
		case BOTTOMLEFT:
			position = TOPLEFT;
			break;
		case BOTTOM:
			position = LEFT;
			break;
		case BOTTOMRIGHT:
			position = BOTTOMLEFT;
			break;
		}
	}
	
	public void setConnectsTiles() {
		connectsTiles = true;
	}
	
	public boolean isConnectingTiles() {
		return connectsTiles;
	}

}
