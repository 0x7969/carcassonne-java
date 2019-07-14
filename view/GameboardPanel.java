package view;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.util.Arrays;

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
	 * schreiben, die die bilder selbst anhand der pixelkoordinaten setzt.
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

//	public void initGameboard(String type, int x, int y) {
//		TilePanel tile = new TilePanel(type, zoom);
//		gbc.gridx = x;
//		gbc.gridy = y;
//		add(tile, gbc);
//		addSurroundingFlipsides(tile);
//	}

	/**
	 * Adds a tile to the gameboard panel at given position (x, y). Actually it just
	 * updates the type and rotation of the tile at the given position, as there
	 * must already be a TilePanel (of type FLIPSIDE) on the same position.
	 * 
	 * @param t
	 * @param x
	 * @param y
	 */
	public void newTile(String type, int rotation, int x, int y) {
		for (TilePanel t : getTiles()) {
			if (gbl.getConstraints(t).gridx == x)
				if (gbl.getConstraints(t).gridy == y) {
					t.setType(type);
					t.setRotation(rotation);
					addSurroundingFlipsides(t);
					return;
				}
		}

		/**
		 * This part is only reached if there was no TilePanel at given position, which
		 * should only be the case once at game initialization.
		 */
		TilePanel tile = new TilePanel(type, zoom);
		tile.setRotation(rotation);
		gbc.gridx = x;
		gbc.gridy = y;
		add(tile, gbc);
		addSurroundingFlipsides(tile);
	}

	private void addSurroundingFlipsides(TilePanel tile) {
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

	public TilePanel newOverlayedTile(String type, int x, int y) {
		overlayedTile = new TilePanel(type, zoom);
		gbc.gridx = x;
		gbc.gridy = y;
		add(overlayedTile, gbc);
		repaint(); // nsin
		return overlayedTile;
	}

	/**
	 * Checks if the gameboard has a tile at (x, y). Strictly speaking, it just
	 * checks if there is _any_ component at (x, y). As we are exclusively adding
	 * TilePanel components to the gameboard, we may assume the component is a tile.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
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

	private TilePanel[] getTiles() {
		return Arrays.stream(getComponents()).filter(c -> c instanceof TilePanel).map(c -> (TilePanel) c)
				.toArray(TilePanel[]::new);
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

	public void zoom(int pixels) {
		int nextZoom = zoom + pixels;
		if (nextZoom < 50 || nextZoom > 150)
			return;
		else {
			for (TilePanel t : getTiles())
				t.setTileSize(t.getTileSize() + pixels);
			zoom = nextZoom;
			revalidate(); // nsin
		}
	}

	public int getZoom() {
		return zoom;
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
