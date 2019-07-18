package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class TilePanel extends JPanel {
	
	private static final String FOLDER = "resources/tiles_/";

	private BufferedImage tileImage;
	private String type;
	private int size; // both height and width in pixels
	private int rotation; // in degrees

	TilePanel(String id, int size) {
		this.rotation = 0;
		this.size = size;
		setType(id);
	}

	public int getTileSize() {
		return size;
	}

	public void setTileSize(int size) { // TODO kann man stattdessen auch das setSize von JLabel nutzen? mal auschecken
		this.size = size;
	}

	public void setType(String id) {
		this.type = id;
		updateIcon();
	}

	public String getType() {
		return type;
	}

	private void updateIcon() {
		try {
			tileImage = ImageIO.read(new File(FOLDER + type + ".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	
	// TODO nur debug
	public int getRotation() {
		return rotation;
	}

}
