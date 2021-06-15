package fop.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import fop.model.TileType;

class TilePanel extends JPanel {

	private static final String FOLDER = "tiles/";

	private BufferedImage tileImage;
	private TileType type;
	private int rotation; // in degrees

	TilePanel(TileType type, int size) {
		super(true); // enables double buffering
		this.rotation = 0;
		this.setPreferredSize(new Dimension(size, size));
		setType(type);
	}

	public void setType(TileType type) {
		this.type = type;
		updateIcon();
	}

	public TileType getType() {
		return type;
	}

	private void updateIcon() {
		try {
			tileImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(FOLDER + type + ".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.rotate(Math.toRadians(rotation), getWidth() / 2, getHeight() / 2);
		g2d.drawImage(tileImage, 0, 0, getWidth(), getHeight(), null);
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

}
