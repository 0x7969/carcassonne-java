package fop.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class TilePanel extends JPanel {
	
	private static final String FOLDER = "res/tiles_/";

	private BufferedImage tileImage;
	private String type;
	private int rotation; // in degrees

	TilePanel(String id, int size) {
		super(true); // enables double buffering
		this.rotation = 0;
		this.setPreferredSize(new Dimension(size, size));
		setType(id);
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
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.rotate(Math.toRadians(rotation), getWidth() / 2, getHeight() / 2);
		g2d.drawImage(tileImage, 0, 0, getWidth(), getHeight(), null);
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	
	// TODO nur debug
	public int getRotation() {
		return rotation;
	}

}
