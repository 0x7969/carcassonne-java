package fop.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MeeplePanel extends JPanel {

	private static final String FOLDER = "resources/meeple/";

	private BufferedImage meepleImage;
	Color color;

	MeeplePanel() {
		this.setOpaque(false);
		try {
			meepleImage = ImageIO.read(new File(FOLDER + "meeple_road.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent event) {
				setOpaque(true);
				repaint();
			}
			
			@Override
			public void mouseExited(MouseEvent event) {
				setOpaque(false);
				repaint();
			}
		});
		
//		this.addMouseMotionListener(new MouseAdapter() {
//
//			@Override
//			public void mouseMoved(MouseEvent event) {
//
//				Point p = event.getPoint();
//
//
//			@Override
//			public void mouseDragged(MouseEvent event) {
//				int anchorX = anchorPoint.x;
//				int anchorY = anchorPoint.y;
//
//				Point parentOnScreen = getParent().getLocationOnScreen();
//				Point mouseOnScreen = event.getLocationOnScreen();
//				Point position = new Point(mouseOnScreen.x - parentOnScreen.x - anchorX,
//						mouseOnScreen.y - parentOnScreen.y - anchorY);
//				SwingUtilities.getAncestorNamed("GameboardPanel", this.).setLocation(position);
//			}
//		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g; // nsin
		g2d.drawImage(meepleImage, 0, 0, getWidth(), getHeight(), null);
		revalidate(); // nsin
	}

}
