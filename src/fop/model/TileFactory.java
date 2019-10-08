package fop.model;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fop.base.WeightedEdge;

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

					// If the tile has a pennant, pass that to the constructor
					if (tileAttributes.getNamedItem("pennant") != null
							&& tileAttributes.getNamedItem("pennant").getNodeValue().equals("true"))
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
									Direction.valueOf(nodeAttributes.getNamedItem("direction").getNodeValue()));
						} else {
							fn = new FeatureNode(
									FeatureType.valueOf(nodeAttributes.getNamedItem("feature").getNodeValue()));
						}

						if (nodeAttributes.getNamedItem("meeplespot") != null
								&& nodeAttributes.getNamedItem("meeplespot").getNodeValue().equals("true"))
							fn.setMeepleSpot();

						newTile.addNode(Position.valueOf(nodeAttributes.getNamedItem("position").getNodeValue()), fn);

					}

					NodeList enl = ((Element) tile).getElementsByTagName("edge");

					// Add every edge
					for (int k = 0; k < enl.getLength(); k++) {

						Node node = enl.item(k);
						NamedNodeMap nodeAttributes = node.getAttributes();
						newTile.addEdge(new WeightedEdge<FeatureType>(
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
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	public Tile getStartTile() {
		return startTile;
	}
}
