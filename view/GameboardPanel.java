package view;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;

import javax.swing.JPanel;

import model.Gameboard;

public class GameboardPanel extends JPanel implements GameboardObserver {

	Point anchorPoint;

	GridBagLayout gbl;
	GridBagConstraints gbc;
	int zoom = 100; // in pixels of each tile

	public GameboardPanel() {
		gbl = new GridBagLayout();
		setLayout(gbl);
		gbc = new GridBagConstraints();
	}

	/**
	 * das hier war ein anfang, um statt gridbaglayout eine eigene zeichenmethode zu
	 * schreiben, die die bilder selbst anhand der pixel setzt.
	 **/

//	public void paintComponent(Graphics g) {
//		for (model.Tile[] row : board.getBoard()) {
//			for (model.Tile tile : row) {
//				try {
//					BufferedImage tileImage = ImageIO.read(new File("resources/tiles/" + tile.getType() + "0.png"));
//					g.drawImage(tileImage, 0, 0, null);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
	
	public Tile newTemporaryTile(String id, int x, int y) {
		// TODO Check if there already is a tile on x, y
		Tile tile = new Tile(id, zoom);
		gbc.gridx = x;
		gbc.gridy = y;
		add(tile, gbc);
		repaint(); // nsin
		return tile;
	}

	public void newTile(String id, int x, int y) {
		// TODO Check if there already is a tile on x, y
		Tile tile = new Tile(id, zoom);
		gbc.gridx = x;
		gbc.gridy = y;
		add(tile, gbc);
		tilePlaced(tile);
	}

	public void zoom(int pixels) {
		int nextZoom = zoom + pixels;
		if (nextZoom < 50 || nextZoom > 150)
			return;
		else {
			for (Component c : getComponents())
				((Tile) c).resizeTile(((Tile) c).getTileSize() + pixels);
			zoom = nextZoom;
			revalidate(); // nsin
		}
	}

	public int getZoom() {
		return zoom;
	}

	private boolean hasTile(int x, int y) {
		for (Component c : getComponents()) {
			if (gbl.getConstraints(c).gridx == x)
				if (gbl.getConstraints(c).gridy == y)
					return true;
		}
		return false;
	}

	int getGridX(Tile t) {
		return gbl.getConstraints(t).gridx;
	}

	int getGridY(Tile t) {
		return gbl.getConstraints(t).gridy;
	}

	public void tilePlaced(Tile tile) {
		int x = gbl.getConstraints(tile).gridx;
		int y = gbl.getConstraints(tile).gridy;
		gbc.gridy = y;
		gbc.gridx = x - 1;
		if (!hasTile(gbc.gridx, gbc.gridy))
			add(new Tile("FLIPSIDE", zoom), gbc);
		gbc.gridx = x + 1;
		if (!hasTile(gbc.gridx, gbc.gridy))
			add(new Tile("FLIPSIDE", zoom), gbc);
		gbc.gridx = x;
		gbc.gridy = y + 1;
		if (!hasTile(gbc.gridx, gbc.gridy))
			add(new Tile("FLIPSIDE", zoom), gbc);
		gbc.gridy = y - 1;
		if (!hasTile(gbc.gridx, gbc.gridy))
			add(new Tile("FLIPSIDE", zoom), gbc);
		repaint(); // not sure if necessary
	}

	public void setTileID(String id, int x, int y) {
		System.out.println("tryna CLICC on " + x + "/" + y);
		for (Component c : getComponents()) {
			if (gbl.getConstraints(c).gridx == x)
				if (gbl.getConstraints(c).gridy == y) {
					((Tile) c).setID(id);
					tilePlaced((Tile) c);
					System.out.println("CONNECC");
				}
		}
	}

	public Tile findTileAt(Point p) {
		Component c = findComponentAt(p);
		if (c instanceof Tile)
			return (Tile) c;
		else
			return null;
	}

	@Override
	public void update(Gameboard o) {
		// TODO Auto-generated method stub

	}

}
