package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import controller.GameController;
import model.Gameboard;

public class GameboardPanel extends JPanel implements GameboardObserver {
	
	GameController gc;

	Point anchorPoint;

	GridBagLayout gbl;
	GridBagConstraints gbc;
	int scale = 100; // in pixels of each tile

	private TilePanel tileInFocus;
	private TilePanel tileWithOverlay;
	private TilePanel overlayedTile;
	private MeepleOverlayPanel meepleOverlayPanel;

	public GameboardPanel(GameController gc) {
		this.gc = gc;
		
		gbl = new GridBagLayout();
		setLayout(gbl);
		gbc = new GridBagConstraints();

		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				if (overlayedTile != null) {
					if (SwingUtilities.isLeftMouseButton(event)) {
						gc.newTile(gc.pickUpTile(), getGridX(overlayedTile), getGridY(overlayedTile));
						repaint(); // !
					} else if ((SwingUtilities.isRightMouseButton(event))) {
						rotateUntilAllowed();
					}
				}
			}
		});

		this.addMouseMotionListener(new MouseAdapter() {

			@Override
			public void mouseMoved(MouseEvent event) {

				Point p = event.getPoint();

				// save current cursor position, used by mouseDragged()
				anchorPoint = p;

				TilePanel tile = getTileAt(p);
				if (tile != null && tile == tileInFocus)
					return;
				tileInFocus = tile;

				if (overlayedTile != null && !overlayedTile.contains(p)) {
					remove(overlayedTile);
					tileWithOverlay.setVisible(true);
					tileWithOverlay = null;
					overlayedTile = null;
					repaint();
				}

				if (tile != null && overlayedTile == null && tile.getType() == "FLIPSIDE") {
					for (int i = 0; i < 4; i++) {
						if (gc.isTileAllowed(gc.peekTile(), getGridX(tile), getGridY(tile))) {
							tileWithOverlay = tile;
							tileWithOverlay.setVisible(false);
							newOverlayedTile(gc.peekTile().getType(), getGridX(tile), getGridY(tile));
							overlayedTile.setRotation(gc.peekTile().getRotation());
							repaint();
							return;
						} else {
							gc.rotateNextTile();
							repaint();
						}
					}
				}
			}

			@Override
			public void mouseDragged(MouseEvent event) {
				int anchorX = anchorPoint.x;
				int anchorY = anchorPoint.y;

				Point parentOnScreen = getParent().getLocationOnScreen();
				Point mouseOnScreen = event.getLocationOnScreen();
				Point position = new Point(mouseOnScreen.x - parentOnScreen.x - anchorX,
						mouseOnScreen.y - parentOnScreen.y - anchorY);
				setLocation(position);
			}
		});

		this.addMouseWheelListener((event) -> {
			int notches = event.getWheelRotation();
			if (notches < 0) {
				setScale(6);
			} else {
				setScale(-6);
			}
		});
	}

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
					addSurroundingFlipsides(x, y);
					return;
				}
		}

		/**
		 * This part is only reached if there was no TilePanel at given position, which
		 * should only be the case once at game initialization.
		 */
		TilePanel tile = new TilePanel(type, scale);
		tile.setRotation(rotation);
		gbc.gridx = x;
		gbc.gridy = y;
		add(tile, gbc, -1);

		addSurroundingFlipsides(x, y);
		repaint(); // TODO not necessary
	}

	private void addSurroundingFlipsides(int x, int y) {
		gbc.gridy = y;
		gbc.gridx = x - 1;
		if (!hasTile(gbc.gridx, gbc.gridy))
			add(new TilePanel("FLIPSIDE", scale), gbc);
		gbc.gridx = x + 1;
		if (!hasTile(gbc.gridx, gbc.gridy))
			add(new TilePanel("FLIPSIDE", scale), gbc);
		gbc.gridx = x;
		gbc.gridy = y + 1;
		if (!hasTile(gbc.gridx, gbc.gridy))
			add(new TilePanel("FLIPSIDE", scale), gbc);
		gbc.gridy = y - 1;
		if (!hasTile(gbc.gridx, gbc.gridy))
			add(new TilePanel("FLIPSIDE", scale), gbc);
		repaint(); // not sure if necessary
	}
	
	private void rotateUntilAllowed() {
		gc.rotateNextTile();

		for (int i = 0; i < 3; i++) {
			if (gc.isTileAllowed(gc.peekTile(), getGridX(overlayedTile), getGridY(overlayedTile))) {
				overlayedTile.setRotation(gc.peekTile().getRotation());
				repaint();
				return;
			} else {
				gc.rotateNextTile();
				repaint();
			}
		}
	}

	public TilePanel newOverlayedTile(String type, int x, int y) {
		overlayedTile = new TilePanel(type, scale);
		gbc.gridx = x;
		gbc.gridy = y;
		add(overlayedTile, gbc);
		repaint(); // nsin
		return overlayedTile;
	}
	
	// TODO return void reicht?
	public MeepleOverlayPanel showMeepleOverlay(boolean[] meepleSpots, int x, int y) {
		gbc.gridx = x;
		gbc.gridy = y;
		meepleOverlayPanel = new MeepleOverlayPanel(meepleSpots, scale);
		add(meepleOverlayPanel, gbc, 0);
		return meepleOverlayPanel;
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

	private int getGridX(TilePanel t) {
		return gbl.getConstraints(t).gridx;
	}

	private int getGridY(TilePanel t) {
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

	public void setScale(int pixels) {
		if (scale + pixels < 50 || scale + pixels > 150)
			return;
		else {
			for (TilePanel t : getTiles())
				t.setTileSize(t.getTileSize() + pixels);
//			meepleOverlayPanel.setPreferredSize(new Dimension(scale + pixels, scale + pixels));
			scale += pixels;
			revalidate(); // !
		}
	}

	public int getScale() {
		return scale;
	}

	@Override
	public void update(Gameboard o) {
		// TODO Auto-generated method stub
		
	}

}
