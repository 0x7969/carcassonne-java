package fop.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import fop.model.Position;

public class MeepleOverlayPanel extends JLabel {

	private static final String FOLDER = "resources/meeple/";

	private int size; // both width and height in pixels
	private int rotation; // in degrees

	public MeepleOverlayPanel(int size) {
		this.setLayout(new GridLayout(3, 3));
		this.size = size;
		this.setPreferredSize(new Dimension(size, size));
		rotation = 0;
	}

	public MeepleOverlayPanel(boolean[] meepleSpots, int size) {
		this.setLayout(new GridLayout(3, 3));
		this.size = size;
		this.setPreferredSize(new Dimension(size, size));
		rotation = 0;

		for (int i = 0; i < meepleSpots.length; i++) {
			if (meepleSpots[i])
				add(new MeeplePanel(Position.values()[i]));
			else
				add(new MeeplePanel());
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

	public void addMeepleMouseListener(MouseListener l) {
		this.addMouseListener(l);
	}

}
