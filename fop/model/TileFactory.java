package fop.model;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fop.base.Edge;

// TODO da wider erwarten keine unterklassen von tile gebraucht werden, ist das hier eigtl. auch keine factory...
public class TileFactory {

	private List<Tile> tiles;
	Tile startTile;

	static final String TILETYPES_FILE_LOCATION = "resources/TileTypes.xml";
	static final String START_TILE_TYPE = "D";

	public TileFactory() {

		tiles = new LinkedList<Tile>();

		try {

			File tileTypesFile = new File(TILETYPES_FILE_LOCATION);
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(tileTypesFile);
			document.getDocumentElement().normalize();

			NodeList tnl = document.getElementsByTagName("tile");

			for (int i = 0; i < tnl.getLength(); i++) {

				Node tile = tnl.item(i);
				NamedNodeMap tileAttributes = tile.getAttributes();

				// Depending on the ammount of tiles of a type, create the tile x times
				for (int j = 0; j < Integer.valueOf(tileAttributes.getNamedItem("ammount").getNodeValue()); j++) {

					Tile newTile;

					// If the tile has a coat of arms, pass that to the constructor
					if (tileAttributes.getNamedItem("coatofarms") != null
							&& tileAttributes.getNamedItem("coatofarms").getNodeValue().equals(true))
						newTile = new Tile(tileAttributes.getNamedItem("type").getNodeValue(), true);
					else
						newTile = new Tile(tileAttributes.getNamedItem("type").getNodeValue());

					NodeList nnl = ((Element) tile).getElementsByTagName("node");

					// Add every node
					for (int k = 0; k < nnl.getLength(); k++) {
						Node node = nnl.item(k);
						NamedNodeMap nodeAttributes = node.getAttributes();

						FeatureNode fn;

						// If a direction is specified, pass it to the constructor, alongside feature
						// and position
						if (nodeAttributes.getNamedItem("direction") != null) {
							fn = new FeatureNode(
									FeatureType.valueOf(nodeAttributes.getNamedItem("feature").getNodeValue()),
									Position.valueOf(nodeAttributes.getNamedItem("position").getNodeValue()),
									Direction.valueOf(nodeAttributes.getNamedItem("direction").getNodeValue()));
						} else {
							fn = new FeatureNode(
									FeatureType.valueOf(nodeAttributes.getNamedItem("feature").getNodeValue()),
									Position.valueOf(nodeAttributes.getNamedItem("position").getNodeValue()));
						}

						if (nodeAttributes.getNamedItem("meeplespot") != null
								&& nodeAttributes.getNamedItem("meeplespot").getNodeValue() == "true")
							fn.setMeepleSpot();

						newTile.addNode(Position.valueOf(nodeAttributes.getNamedItem("position").getNodeValue()), fn);

					}

					NodeList enl = ((Element) tile).getElementsByTagName("edge");

					// Add every edge
					for (int k = 0; k < enl.getLength(); k++) {

						Node node = enl.item(k);
						NamedNodeMap nodeAttributes = node.getAttributes();
						newTile.addEdge(new Edge<FeatureType>(
								newTile.getNode(Position.valueOf(nodeAttributes.getNamedItem("a").getNodeValue())),
								newTile.getNode(Position.valueOf(nodeAttributes.getNamedItem("b").getNodeValue()))));
					}

					// If there is no start tile yet, save it seperately
					if (startTile == null && newTile.getType().equals(START_TILE_TYPE))
						startTile = newTile;
					else
						tiles.add(newTile);
				}

			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

//		tiles = new LinkedList<Tile>();
//
//		Tile tile = new Tile("B"); // Tile B
//		FeatureNode top = new FeatureNode(FIELDS, TOP);
//		FeatureNode left = new FeatureNode(FIELDS, LEFT);
//		FeatureNode center = new FeatureNode(MONASTERY, CENTER);
//		FeatureNode right = new FeatureNode(FIELDS, RIGHT);
//		FeatureNode bottom = new FeatureNode(FIELDS, BOTTOM);
//		tile.addEdge(new Edge<FeatureType>(left, center));
//		tile.addEdge(new Edge<FeatureType>(top, center));
//		tile.addEdge(new Edge<FeatureType>(right, center));
//		tile.addEdge(new Edge<FeatureType>(bottom, center));
//		tile.addNode(TOP, top);
//		tile.addNode(LEFT, left);
//		tile.addNode(CENTER, center);
//		tile.addNode(RIGHT, right);
//		tile.addNode(BOTTOM, bottom);
//		tiles.add(tile);
//		tiles.add(tile);
//		tiles.add(tile);
//		tiles.add(tile);
//
//		tile = new Tile("C", true); // Tile C
//		top = new FeatureNode(CASTLE, TOP);
//		left = new FeatureNode(CASTLE, LEFT);
//		center = new FeatureNode(CASTLE, CENTER);
//		right = new FeatureNode(CASTLE, RIGHT);
//		bottom = new FeatureNode(CASTLE, BOTTOM);
//		tile.addEdge(new Edge<FeatureType>(left, center));
//		tile.addEdge(new Edge<FeatureType>(top, center));
//		tile.addEdge(new Edge<FeatureType>(right, center));
//		tile.addEdge(new Edge<FeatureType>(bottom, center));
//		tile.addNode(TOP, top);
//		tile.addNode(LEFT, left);
//		tile.addNode(CENTER, center);
//		tile.addNode(RIGHT, right);
//		tile.addNode(BOTTOM, bottom);
//		tiles.add(tile);
//
//		tile = new Tile("D", false); // Tile D
//		top = new FeatureNode(CASTLE, TOP);
//		left = new FeatureNode(ROAD, LEFT);
//		center = new FeatureNode(ROAD, CENTER);
//		right = new FeatureNode(ROAD, RIGHT);
//		bottom = new FeatureNode(FIELDS, BOTTOM);
//		tile.addEdge(new Edge<FeatureType>(left, right));
//		tile.addNode(TOP, top);
//		tile.addNode(LEFT, left);
//		tile.addNode(CENTER, center);
//		tile.addNode(RIGHT, right);
//		tile.addNode(BOTTOM, bottom);
//		tiles.add(tile);
//
//		tile = new Tile("D", false); // Tile D
//		top = new FeatureNode(CASTLE, TOP);
//		left = new FeatureNode(ROAD, LEFT);
//		center = new FeatureNode(ROAD, CENTER);
//		right = new FeatureNode(ROAD, RIGHT);
//		bottom = new FeatureNode(FIELDS, BOTTOM);
//		tile.addEdge(new Edge<FeatureType>(left, right));
//		tile.addNode(TOP, top);
//		tile.addNode(LEFT, left);
//		tile.addNode(CENTER, center);
//		tile.addNode(RIGHT, right);
//		tile.addNode(BOTTOM, bottom);
//
//		tile = new Tile("D", false); // Tile D
//		top = new FeatureNode(CASTLE, TOP);
//		left = new FeatureNode(ROAD, LEFT);
//		center = new FeatureNode(ROAD, CENTER);
//		right = new FeatureNode(ROAD, RIGHT);
//		bottom = new FeatureNode(FIELDS, BOTTOM);
//		tile.addEdge(new Edge<FeatureType>(left, right));
//		tile.addNode(TOP, top);
//		tile.addNode(LEFT, left);
//		tile.addNode(CENTER, center);
//		tile.addNode(RIGHT, right);
//		tile.addNode(BOTTOM, bottom);
//		
//		tile = new Tile("D", false); // Tile D
//		top = new FeatureNode(CASTLE, TOP);
//		left = new FeatureNode(ROAD, LEFT);
//		center = new FeatureNode(ROAD, CENTER);
//		right = new FeatureNode(ROAD, RIGHT);
//		bottom = new FeatureNode(FIELDS, BOTTOM);
//		tile.addEdge(new Edge<FeatureType>(left, right));
//		tile.addNode(TOP, top);
//		tile.addNode(LEFT, left);
//		tile.addNode(CENTER, center);
//		tile.addNode(RIGHT, right);
//		tile.addNode(BOTTOM, bottom);
//		startTile = tile;
////		 There are four tiles of type D but one is the starting tile, which is
////		 generated upon initialising the gameboard.
//
//		tile = new Tile("E"); // Tile E
//		top = new FeatureNode(CASTLE, TOP);
//		left = new FeatureNode(FIELDS, LEFT);
//		center = new FeatureNode(FIELDS, CENTER);
//		right = new FeatureNode(FIELDS, RIGHT);
//		bottom = new FeatureNode(FIELDS, BOTTOM);
//		tile.addEdge(new Edge<FeatureType>(top, center));
//		tile.addEdge(new Edge<FeatureType>(right, center));
//		tile.addEdge(new Edge<FeatureType>(bottom, center));
//		tile.addNode(TOP, top);
//		tile.addNode(LEFT, left);
//		tile.addNode(CENTER, center);
//		tile.addNode(RIGHT, right);
//		tile.addNode(BOTTOM, bottom);
//		tiles.add(tile);
//		tiles.add(tile);
//		tiles.add(tile);
//		tiles.add(tile);
//		tiles.add(tile);

	}

	public List<Tile> getTiles() {
		return tiles;
	}

	public Tile getStartTile() {
		return startTile;
	}

//	public static void main(String args[]) {
//		TileFactory tf = new TileFactory();
//	}

}
