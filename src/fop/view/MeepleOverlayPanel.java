package fop.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MeepleOverlayPanel extends JLabel {

	private static final String FOLDER = "resources/meeple/";

	private int size; // both width and height in pixels
	private int rotation; // in degrees

	public MeepleOverlayPanel(boolean[] meepleSpots, int size) {
		this.setLayout(new GridLayout(3, 3));
		this.size = size;
		this.setPreferredSize(new Dimension(size, size));
		rotation = 0;

		for (boolean spot : meepleSpots) {
			if (spot)
				add(new MeeplePanel(true));
			else
				add(new MeeplePanel(false));
		}
	}

//	@Override
//	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		g.setColor(Color.YELLOW);
//		g.fillOval(this.getX(), this.getY(), 50, 50);
//		revalidate();
//	}

//	@Override
//	public Dimension getPreferredSize() {
//		return new Dimension(size, size);
//	}

	public void setSize(int size) {
		this.setPreferredSize(new Dimension(size, size));
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public static void main(String args[]) {
		JFrame f = new JFrame();
		GridBagConstraints gbc = new GridBagConstraints();
		f.setLayout(new GridBagLayout());
		f.setSize(500, 500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gbc.gridx = 0;
		gbc.gridy = 0;
		f.add(new MeepleOverlayPanel(new boolean[] { true, false, false, false, true, false, false, true, false }, 400),
				gbc);
		f.add(new TilePanel("A", 400), gbc);

		f.setVisible(true);
	}

}
