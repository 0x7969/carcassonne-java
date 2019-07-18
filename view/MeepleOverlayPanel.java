package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MeepleOverlayPanel extends JLabel {
	
	private static final String folder = "resources/meeple/";

	private int size; // both width and height in pixels
	private int rotation; // in degrees

	public MeepleOverlayPanel(int size) {
		this.setLayout(new GridLayout(3, 3));
		this.size = size;
		rotation = 0;
		
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
		
		for (Component c : getComponents()) {
			((JLabel) c).setIcon(new ImageIcon(folder + "meeple_road.png"));;
//			((JLabel) c).setOpaque(false);
			((JLabel) c).setBackground(new Color(0,0,0,0));
//			((JLabel) c).setContentAreaFilled(false);
		}
		repaint();
	}

//	@Override
//	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		g.setColor(Color.YELLOW);
//		g.fillOval(this.getX(), this.getY(), 50, 50);
//		revalidate();
//	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(size, size);
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public static void main(String args[]) {
		JFrame f = new JFrame();
		GridBagConstraints gbc = new GridBagConstraints();
		f.setLayout(new GridBagLayout());
		f.setSize(800, 800);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gbc.gridx = 0;
		gbc.gridy = 0;
//		f.add(new MeepleOverlayPanel(400), gbc);
		f.add(new TilePanel("A", 400), gbc);

		f.setVisible(true);
	}

}
