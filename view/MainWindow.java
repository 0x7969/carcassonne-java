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
		gameboard.initGameboard(tilestack.drawTile()); // The topmost tile of the tilestack is always the start tile.

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

			@Override
			public void mouseMoved(MouseEvent event) {

				Point p = event.getPoint();
				// Hier wird die letzte Position des Mauszeigers zwischengespeichert. Benötigt
				// von mouseDragged(). TODO Hat immer offset von ~82. Wg. Menubar?
				event.translatePoint(0, 82);
				gameboardPanel.anchorPoint = event.getPoint();

				// TODO bei jeder mausbewegung wird alles neu berechnet. vielleicht am anfang
				// fragen, ob es die gleiche tile wie letztes mal ist (oder gar keine) und wenn
				// ja sofort return. das erübrigt vllt. auch spätere abfragen davon.

				TilePanel tile = gameboardPanel.getTileAt(p);
				if (tile != null && tile == gameboardPanel.getTileInFocus())
					return;
				gameboardPanel.setTileInFocus(tile);

				if (tile != null) // debug
					gameboardPanel.getTileInFocus().toString();

				if (gameboardPanel.getOverlayedTile() != null && !gameboardPanel.getOverlayedTile().contains(p)) {
					gameboardPanel.remove(gameboardPanel.getOverlayedTile());
					gameboardPanel.getTileWithOverlay().setVisible(true);
					gameboardPanel.setTileWithOverlay(null);
					gameboardPanel.setOverlayedTile(null);
					repaint();
				}

				if (tile != null && gameboardPanel.getOverlayedTile() == null && tile.getType() == "FLIPSIDE") {
					for (int i = 0; i < 4; i++) {
						if (gameboard.isAllowed(tilestack.peek(), gameboardPanel.getGridX(tile),
								gameboardPanel.getGridY(tile))) {
							gameboardPanel.setTileWithOverlay(tile);
							gameboardPanel.getTileWithOverlay().setVisible(false);
							gameboardPanel.newOverlayedTile(tilestack.peek().getType(),
									gameboardPanel.getGridX(tile), gameboardPanel.getGridY(tile));
							gameboardPanel.getOverlayedTile().setRotation(tilestack.peek().getRotation());
							repaint();
							return;
						} else {
							tilestack.rotateTopTile();
							repaint();
						}
					}
				}
			}

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
				TilePanel tile = gameboardPanel.getOverlayedTile();

				if (tile != null) {
					if (SwingUtilities.isLeftMouseButton(event)) {
						gameboard.newTile(tilestack.drawTile(), gameboardPanel.getGridX(tile),
								gameboardPanel.getGridY(tile));
						repaint(); // !
					} else if ((SwingUtilities.isRightMouseButton(event))) {

						if (gameboardPanel.getOverlayedTile() != null) {
							tilestack.rotateTopTile();

							for (int i = 0; i < 3; i++) {
								if (gameboard.isAllowed(tilestack.peek(), gameboardPanel.getGridX(tile),
										gameboardPanel.getGridY(tile))) {
									gameboardPanel.getOverlayedTile().setRotation(tilestack.peek().getRotation());
									repaint();
									return;
								} else {
									tilestack.rotateTopTile();
									repaint();
								}
							}
						}
					}
				}
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