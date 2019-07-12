package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class Tile extends JLabel {

	private BufferedImage tileImage;
	private String type;
	private String overlayedTileID;
	private int size; // both height and width in pixels
	private static final String folder = "resources/tiles_/";
	private int rotation;

	public Tile(String id, int size) {
		this.rotation = 0;
		this.size = size;
		setType(id);
	}

	public int getTileSize() {
		return size;
	}

	public void resizeTile(int size) {
		this.size = size;
//		setIcon(new ImageIcon(tileImage.getScaledInstance(size, size, Image.SCALE_SMOOTH)));
	}

	public void setType(String id) {
		this.type = id;
		updateIcon();
	}

	public String getType() {
		return type;
	}

	public void setOverlayedTileType(String id) {
		overlayedTileID = id;
		updateIcon();
	}

	public void unsetOverlayedTile() {
		overlayedTileID = null;
		updateIcon();
	}

	private void updateIcon() {
		String filename = null;

		if (overlayedTileID != null)
			filename = overlayedTileID + ".jpg";
		else
			filename = type + ".jpg";

		try {
			tileImage = ImageIO.read(new File(folder + filename));
//			setIcon(new ImageIcon(tileImage.getScaledInstance(size, size, Image.SCALE_SMOOTH)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		revalidate();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(size, size);
	}

	@Override
	protected void paintComponent(Graphics g) {
//    	super.paintComponent(g); // nsin
//    	AffineTransform at = AffineTransform.getScaleInstance(size / 100, size / 100);
//    	at.rotate(Math.PI / 2, size / 2, size / 2);
//    	
//    	Graphics2D g2d = (Graphics2D) g;
//    	g2d.drawImage(tileImage, at, null);
//    	repaint();

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.rotate(Math.toRadians(rotation), size / 2, size / 2);
		g2d.drawImage(tileImage, 0, 0, size, size, null);
		revalidate();
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

}
