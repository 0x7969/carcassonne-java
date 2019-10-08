package fop.model;

import fop.base.Node;

public class FeatureNode extends Node<FeatureType> {

	private Position position;
	private Direction direction;
	private boolean connectsTiles;
	private boolean meepleSpot;
	private Player meeple;

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
		this.direction = Direction.BOTH;
		this.connectsTiles = false;
	}

	public void setMeepleSpot() {
		this.meepleSpot = true;
	}

	public boolean hasMeepleSpot() {
		return meepleSpot;
	}

	public void setPlayer(Player p) {
		meeple = p;
	}

	public boolean hasMeeple() {
		if (meeple != null)
			return true;
		else
			return false;
	}

	public Player getPlayer() {
		return meeple;
	}

	public FeatureType getType() {
		return getValue();
	}

	public Direction getDirection() {
		return direction;
	}

	public void switchDirection() {
		if (direction == Direction.BOTH)
			return;
		else
			direction = direction.getOpposite();
	}

	public void setConnectsTiles() {
		connectsTiles = true;
	}

	public boolean isConnectingTiles() {
		return connectsTiles;
	}

}
