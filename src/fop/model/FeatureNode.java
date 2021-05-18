package fop.model;

import fop.base.Node;

public class FeatureNode extends Node<FeatureType> {

	private boolean connectsTiles;
	private boolean meepleSpot;
	private Player meeple;

	/**
	 * Creates a new FeatureNode. It is assumed that the feature is present in both
	 * directions.
	 * 
	 * @param type     The features type.
	 * @param position Its position on a tile.
	 */
	public FeatureNode(FeatureType type) {
		super(type);
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

	public void setConnectsTiles() {
		connectsTiles = true;
	}

	public boolean isConnectingTiles() {
		return connectsTiles;
	}

}
