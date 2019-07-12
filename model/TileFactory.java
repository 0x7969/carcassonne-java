package model;

import static model.Position.*;
import static model.FeatureType.*;

import java.util.LinkedList;
import java.util.List;

import base.Edge;

// TODO da wider erwarten keine unterklassen von tile gebraucht werden, ist das hier eigtl. auch keine factory...
public class TileFactory {

	private List<Tile> tiles;
	Tile startTile;

	public TileFactory() {

		tiles = new LinkedList<Tile>();

		Tile tile = new Tile("B"); // Tile B
		FeatureNode top = new FeatureNode(FIELDS, TOP);
		FeatureNode left = new FeatureNode(FIELDS, LEFT);
		FeatureNode center = new FeatureNode(MONASTERY, CENTER);
		FeatureNode right = new FeatureNode(FIELDS, RIGHT);
		FeatureNode bottom = new FeatureNode(FIELDS, BOTTOM);
		tile.addEdge(new Edge<FeatureType>(left, center));
		tile.addEdge(new Edge<FeatureType>(top, center));
		tile.addEdge(new Edge<FeatureType>(right, center));
		tile.addEdge(new Edge<FeatureType>(bottom, center));
		tile.addNode(top);
		tile.addNode(left);
		tile.addNode(center);
		tile.addNode(right);
		tile.addNode(bottom);
		tiles.add(tile);
		tiles.add(tile);
		tiles.add(tile);
		tiles.add(tile);

		tile = new Tile("C", true); // Tile C
		top = new FeatureNode(CASTLE, TOP);
		left = new FeatureNode(CASTLE, LEFT);
		center = new FeatureNode(CASTLE, CENTER);
		right = new FeatureNode(CASTLE, RIGHT);
		bottom = new FeatureNode(CASTLE, BOTTOM);
		tile.addEdge(new Edge<FeatureType>(left, center));
		tile.addEdge(new Edge<FeatureType>(top, center));
		tile.addEdge(new Edge<FeatureType>(right, center));
		tile.addEdge(new Edge<FeatureType>(bottom, center));
		tile.addNode(top);
		tile.addNode(left);
		tile.addNode(center);
		tile.addNode(right);
		tile.addNode(bottom);
		tiles.add(tile);

		tile = new Tile("D", false); // Tile D
		top = new FeatureNode(CASTLE, TOP);
		left = new FeatureNode(ROAD, LEFT);
		center = new FeatureNode(ROAD, CENTER);
		right = new FeatureNode(ROAD, RIGHT);
		bottom = new FeatureNode(FIELDS, BOTTOM);
		tile.addEdge(new Edge<FeatureType>(left, right));
		tile.addNode(top);
		tile.addNode(left);
		tile.addNode(center);
		tile.addNode(right);
		tile.addNode(bottom);
		tiles.add(tile);
		tiles.add(tile);
		tiles.add(tile);
		startTile = tile;
		// There are four tiles of type D but one is the starting tile, which is
		// generated upon initialising the gameboard.

		tile = new Tile("E"); // Tile E
		top = new FeatureNode(CASTLE, TOP);
		left = new FeatureNode(FIELDS, LEFT);
		center = new FeatureNode(FIELDS, CENTER);
		right = new FeatureNode(FIELDS, RIGHT);
		bottom = new FeatureNode(FIELDS, BOTTOM);
		tile.addEdge(new Edge<FeatureType>(top, center));
		tile.addEdge(new Edge<FeatureType>(right, center));
		tile.addEdge(new Edge<FeatureType>(bottom, center));
		tile.addNode(top);
		tile.addNode(left);
		tile.addNode(center);
		tile.addNode(right);
		tile.addNode(bottom);
		tiles.add(tile);
		tiles.add(tile);
		tiles.add(tile);
		tiles.add(tile);
		tiles.add(tile);

	}

	public List<Tile> getTiles() {
		return tiles;
	}
	
	public Tile getStartTile() {
		return startTile;
	}

}
