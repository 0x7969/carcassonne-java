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

	private TilePanel tileInFocus;
	private TilePanel tileWithOverlay;
	private TilePanel overlayedTile;

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

	// TODO das sollte von selbst gleich overlayedTile setzen...
	public TilePanel newOverlayedTile(String type, int x, int y) {
		TilePanel tile = new TilePanel(type, zoom);
		gbc.gridx = x;
		gbc.gridy = y;
		add(tile, gbc);
		repaint(); // nsin
		return tile;
	}

	public void initGameboard(String id, int x, int y) {
		// TODO Check if there already is a tile on x, y
		TilePanel tile = new TilePanel(id, zoom);
		gbc.gridx = x;
		gbc.gridy = y;
		add(tile, gbc);
		addSurroundingFlipsides(tile);
	}

	public void zoom(int pixels) {
		int nextZoom = zoom + pixels;
		if (nextZoom < 50 || nextZoom > 150)
			return;
		else {
			for (Component c : getComponents())
				((TilePanel) c).setTileSize(((TilePanel) c).getTileSize() + pixels);
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

	int getGridX(TilePanel t) {
		return gbl.getConstraints(t).gridx;
	}

	int getGridY(TilePanel t) {
		return gbl.getConstraints(t).gridy;
	}

	public void addSurroundingFlipsides(TilePanel tile) {
		int x = gbl.getConstraints(tile).gridx;
		int y = gbl.getConstraints(tile).gridy;
		gbc.gridy = y;
		gbc.gridx = x - 1;
		if (!hasTile(gbc.gridx, gbc.gridy))
			add(new TilePanel("FLIPSIDE", zoom), gbc);
		gbc.gridx = x + 1;
		if (!hasTile(gbc.gridx, gbc.gridy))
			add(new TilePanel("FLIPSIDE", zoom), gbc);
		gbc.gridx = x;
		gbc.gridy = y + 1;
		if (!hasTile(gbc.gridx, gbc.gridy))
			add(new TilePanel("FLIPSIDE", zoom), gbc);
		gbc.gridy = y - 1;
		if (!hasTile(gbc.gridx, gbc.gridy))
			add(new TilePanel("FLIPSIDE", zoom), gbc);
		repaint(); // not sure if necessary
	}

	public void setTileTypeAndRotation(String type, int rotation, int x, int y) {
		for (Component c : getComponents()) {
			if (gbl.getConstraints(c).gridx == x)
				if (gbl.getConstraints(c).gridy == y) {
					((TilePanel) c).setType(type);
					((TilePanel) c).setRotation(rotation);
					addSurroundingFlipsides((TilePanel) c);
					return;
				}
		}
	}

	public TilePanel findTileAt(Point p) {
		Component c = findComponentAt(p);
		if (c instanceof TilePanel)
			return (TilePanel) c;
		else
			return null;
	}

	// getComponentAt gibt im Gegensatz zu findComponentAt die unterste/älteste?
	// tile zurück. kann man sich darauf verlassen?
	// ist nicht direkt teil der spezifikation...
	public TilePanel getTileAt(Point p) {
		Component c = getComponentAt(p);
		if (c instanceof TilePanel)
			return (TilePanel) c;
		else
			return null;
	}

	@Override
	public void update(Gameboard o) {
		// TODO Auto-generated method stub

	}

	public void setTileInFocus(TilePanel lastTileInFocus) {
		this.tileInFocus = lastTileInFocus;
	}

	public TilePanel getTileInFocus() {
		return tileInFocus;
	}

	public void setTileWithOverlay(TilePanel tileWithOverlay) {
		this.tileWithOverlay = tileWithOverlay;
	}

	public TilePanel getTileWithOverlay() {
		return tileWithOverlay;
	}

	public void setOverlayedTile(TilePanel overlayedTile) {
		this.overlayedTile = overlayedTile;
	}

	public TilePanel getOverlayedTile() {
		return overlayedTile;
	}

}
