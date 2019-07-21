package fop.view;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import fop.controller.GameController;
import fop.model.Gameboard;
import fop.model.TileStack;

public class GameBoardPanel extends JPanel implements GameboardObserver {

	private GameController gc;

	private Point anchorPoint;

	private GridBagLayout gbl;
	private GridBagConstraints gbc;
	private int scale = 100; // in pixels of each tile

	private TilePanel tileInFocus;
	private TilePanel tileWithOverlay;
	private TileOverlayPanel tileOverlay;
	private MeepleOverlayPanel meepleOverlay;

	public GameBoardPanel(GameController gc) {
		this.gc = gc;

		gbl = new GridBagLayout();
		setLayout(gbl);
		gbc = new GridBagConstraints();

		tileOverlay = new TileOverlayPanel("FLIPSIDE", scale);
		tileOverlay.setVisible(false);
		gc.addTileStackObserver(tileOverlay);

		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				if (tileOverlay.isVisible()) {
					if (SwingUtilities.isLeftMouseButton(event)) {
						gc.newTile(gc.pickUpTile(), getGridX(tileOverlay), getGridY(tileOverlay));
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

				if (tileOverlay.isVisible() && !tileOverlay.contains(p)) {
					tileOverlay.setVisible(false);
					remove(tileOverlay);
					tileWithOverlay.setVisible(true);
					tileWithOverlay = null;
//					tileOverlay = null;
					repaint();
				}

				if (tile != null && !tileOverlay.isVisible() && tile.getType() == "FLIPSIDE") {
					for (int i = 0; i < 4; i++) {
						if (gc.isTileAllowed(gc.peekTile(), getGridX(tile), getGridY(tile))) {
							tileWithOverlay = tile;
							tileWithOverlay.setVisible(false);
							showTileOverlay(gc.peekTile().getType(), getGridX(tile), getGridY(tile));
							repaint();
							return;
						} else {
							gc.rotateTopTile();
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
		add(tile, gbc);

		addSurroundingFlipsides(x, y);
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
		repaint(); // TODO not sure if necessary
	}

	private boolean rotateUntilAllowed() {
		boolean allowed = false;
		gc.rotateTopTile();

		for (int i = 0; i < 3; i++) {
			if (!gc.isTileAllowed(gc.peekTile(), getGridX(tileOverlay), getGridY(tileOverlay))) {
				gc.rotateTopTile();
			} else {
				allowed = true;
			}
		}
		return allowed;
	}

	private TileOverlayPanel showTileOverlay(String type, int x, int y) {
		tileOverlay.setVisible(true);
		gbc.gridx = x;
		gbc.gridy = y;
		add(tileOverlay, gbc);
		repaint(); // nsin
		return tileOverlay;
	}

	private class TileOverlayPanel extends TilePanel implements Observer<TileStack> {

		TileOverlayPanel(String id, int size) {
			super(id, size);
		}

		@Override
		public void update(TileStack ts) {
			System.out.println("updated");
			setType(ts.peekTile().getType());
			setRotation(ts.peekTile().getRotation());
			repaint();
		}
	}

	// TODO return void reicht?
	public MeepleOverlayPanel showMeepleOverlay(boolean[] meepleSpots, int x, int y) {
		gbc.gridx = x;
		gbc.gridy = y;
		meepleOverlay = new MeepleOverlayPanel(meepleSpots, scale);
		add(meepleOverlay, gbc); // auf z-axis ist kein verlass, lieber mit setVisible
		return meepleOverlay;
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

	// TODO unused?
	private int getGridX(TilePanel t) {
		return gbl.getConstraints(t).gridx;
	}

	// TODO unused?
	private int getGridY(TilePanel t) {
		return gbl.getConstraints(t).gridy;
	}

	private Point getGridPosition(TilePanel t) {
		return new Point(gbl.getConstraints(t).gridx, gbl.getConstraints(t).gridy);
	}

	public Point getOverlayedTileGridPosition() {
		return getGridPosition(tileOverlay);
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
		// pass
	}

	public boolean hasOverlay() {
		return tileOverlay != null;
	}

	public Object getOverlayedTile() {
		return tileOverlay;
	}

}
