package view;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import model.Gameboard;
import model.TileStack;

public class MainWindow extends JFrame {

	Gameboard gameboard;
	GameboardPanel gameboardPanel;
	JPanel gameboardWrapper;
	ToolbarPanel toolbarPanel;
	TileStack tilestack;
	TileStackPanel tileStackPanel;

	public MainWindow(String title) {
		super(title);
		this.setSize(800, 800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		toolbarPanel = new ToolbarPanel();
		toolbarPanel.addToolbarActionListener((event) -> {
			switch (event.getActionCommand()) {
			case "New tile":
				gameboardPanel.newOverlayedTile("B", toolbarPanel.getXValue(), toolbarPanel.getYValue());
				revalidate(); // nsin
				repaint();
				break;
			case "Quit":
				dispose();
				break;
			case "+":
				gameboardPanel.zoom(5);
				break;
			case "-":
				gameboardPanel.zoom(-5);
				break;
			}
		});

		this.add(toolbarPanel, BorderLayout.NORTH);

		tilestack = new TileStack();
		tileStackPanel = new TileStackPanel(tilestack);
		tilestack.addObserver(tileStackPanel);
		this.add(tileStackPanel, BorderLayout.EAST);

		gameboardWrapper = new JPanel(); // den brauchen wir, um das gameboard verschieben zu können
		gameboardWrapper.setLayout(null); // (gameboard selbst hat ein layout, das selbst für seine dimensionen sorgt
											// und deshalb nicht verschiebbar ist)
		gameboardPanel = new GameboardPanel();
		gameboardPanel.setBounds(-50000 + getWidth() / 2, -50000 + getHeight() / 2, 100000, 100000);

		gameboard = new Gameboard();
		gameboard.addObserver(gameboardPanel);
		gameboard.initGameboard(tilestack.pop()); // The topmost tile of the tilestack is always the start tile.

		gameboardWrapper.add(gameboardPanel);
		this.add(gameboardWrapper);

		gameboardPanel.addMouseWheelListener((event) -> {
			int notches = event.getWheelRotation();
			if (notches < 0) {
				gameboardPanel.zoom(6);
			} else {
				gameboardPanel.zoom(-6);
			}
			revalidate(); // not sure if necessary
		});

		gameboardPanel.addMouseMotionListener(new MouseAdapter() {

			Tile tileWithOverlay;
			Tile overlayedTile;

			@Override
			public void mouseMoved(MouseEvent event) {

				Point p = event.getPoint();
				Tile tile = gameboardPanel.getTileAt(p);

				if (overlayedTile != null && !overlayedTile.getBounds().contains(p)) {
					gameboardPanel.remove(overlayedTile);
					tileWithOverlay.setVisible(true);
					tileWithOverlay = null;
					overlayedTile = null;
					repaint();
				}

				if (tile != null && overlayedTile == null && tile.getType() == "FLIPSIDE") {
					System.out.println(gameboard.isAllowed(tilestack.peek(), gameboardPanel.getGridX(tile),
							gameboardPanel.getGridY(tile)));
					tileWithOverlay = tile;
					tileWithOverlay.setVisible(false);
					overlayedTile = gameboardPanel.newOverlayedTile(tilestack.peek().getType(),
							gameboardPanel.getGridX(tile), gameboardPanel.getGridY(tile));
					overlayedTile.setRotation(tilestack.peek().getRotation());
					repaint();
				}

				// Hier wird die letzte Position des Mauszeigers zwischengespeichert. Benötigt
				// von mouseDragged().
				event.translatePoint(0, 82); // TODO Irgendwie hat das immer ein Offset von ~82 (die Höhe der
												// Fensterdekoration?). Bin gerade zu faul dem nachzugehen.
				gameboardPanel.anchorPoint = event.getPoint();
			}

//			@Override
//			public void mouseMoved(MouseEvent event) {
//
//				Tile tile = gameboardPanel.findTileAt(event.getPoint());
//
//				event.translatePoint(0, 82); // TODO Irgendwie hat das immer ein Offset von ~82 (die Höhe der
//												// Fensterdekoration?). Bin gerade zu faul dem nachzugehen.
//				gameboardPanel.anchorPoint = event.getPoint();
//
//				boolean mouseOverTile = false;
//
//				if (tile != null) {
//					if (lastTileWithOverlay != null && !tile.equals(lastTileWithOverlay))
//						lastTileWithOverlay.unsetOverlayedTile();
//
//					if (tile.getID() == "FLIPSIDE") {
//						tile.setOverlayedTileType(tilestack.peek().getType());
//						tile.setRotation(tilestack.peek().getRotation());
//						repaint();
//						lastTileWithOverlay = tile;
//					}
//
//					mouseOverTile = true;
//				}
//
////				for (Component child : gameboardPanel.getComponents()) {
////					if (new Rectangle(child.getLocationOnScreen(), new Dimension(child.getWidth(), child.getHeight()))
////							.contains(event.getLocationOnScreen())) {
////						if (lastTileWithOverlay != null && !((Tile) child).equals(lastTileWithOverlay))
////							lastTileWithOverlay.unsetOverlayedTile();
////
////						if (((Tile) child).getID() == "FLIPSIDE") {
////							((Tile) child).setOverlayedTileType(tilestack.peekTile().getType());
////							repaint();
////							lastTileWithOverlay = (Tile) child;
////						}
////
////						mouseOverTile = true;
////					}
////				}
//
//				if (lastTileWithOverlay != null && mouseOverTile == false) {
//					lastTileWithOverlay.unsetOverlayedTile();
//					lastTileWithOverlay = null;
//					repaint();
//				}
//
//			}

			@Override
			public void mouseDragged(MouseEvent event) {
				int anchorX = gameboardPanel.anchorPoint.x;
				int anchorY = gameboardPanel.anchorPoint.y;

				Point parentOnScreen = getLocationOnScreen();
				Point mouseOnScreen = event.getLocationOnScreen();
				Point position = new Point(mouseOnScreen.x - parentOnScreen.x - anchorX,
						mouseOnScreen.y - parentOnScreen.y - anchorY);
				gameboardPanel.setLocation(position);
			}
		});

		gameboardPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				// TODO ist die direkte referenz auf gameboardPanel innerhalb des controllers
				// eigentlich erlaubt?
				Tile tile = gameboardPanel.getTileAt(event.getPoint());
				if (tile != null) {
					if (SwingUtilities.isLeftMouseButton(event)) {
						if (tile.getType() == "FLIPSIDE") {
							gameboard.newTile(tilestack.pop(), gameboardPanel.getGridX(tile),
									gameboardPanel.getGridY(tile));
							repaint(); // !
						}
					} else if ((SwingUtilities.isRightMouseButton(event))) {
						tile = gameboardPanel.findTileAt(event.getPoint());
						System.out.println(tile.getType());
						tilestack.rotateTile();
						tile.setRotation(tilestack.peek().getRotation());
						repaint();
					}
				}
				System.out.println(gameboardPanel.getComponentCount());
			}
		});

	}

	public void gameboardChanged(model.Tile[][] board) {
		// Hier könnte man das ganze board neu darstellen
	}

	public static void main(String[] args) {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}

		// Macht man so wegen irgendwas mit threading
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame frame = new MainWindow("Carcassonne");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}