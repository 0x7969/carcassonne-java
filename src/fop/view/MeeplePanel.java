package fop.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import fop.model.MeepleColour;
import fop.model.Player;
import fop.model.Position;

/**
 * A MeeplePanel is one of nine Panels inside a MeepleOverlayPanel. It is either
 * a possible spot to place a Meeple on or not, in which case it is still part
 * of the MeepleOverlayPanel to get the spacing right.
 * 
 * @author yi
 *
 */
public class MeeplePanel extends JPanel {

	private static final String FOLDER = "resources/meeple/";

	private BufferedImage meepleImage;
	private Position position; // the meeple spots position inside the tile its on
	private MeepleColour colour;

	/**
	 * A meeple panel without a position is considered not to be a spot to place a
	 * meeple on. It's just an invisible panel using up space.
	 */
	MeeplePanel() {
		this.setOpaque(false);
	}

	MeeplePanel(Position pos, Player player) {
		this.setOpaque(false);
		this.position = pos;
		this.colour = player.getColour();

		try {
			meepleImage = ImageIO.read(new File(FOLDER + colour.toString().toLowerCase() + "_outline.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				getParent().getParent().dispatchEvent(event); // dispatches event to GameBoardPanel
			}

			@Override
			public void mouseEntered(MouseEvent event) {
				try {
					meepleImage = ImageIO.read(new File(FOLDER + colour.toString().toLowerCase() + ".png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				repaint();
			}

			@Override
			public void mouseExited(MouseEvent event) {
				try {
					meepleImage = ImageIO.read(new File(FOLDER + colour.toString().toLowerCase() + "_outline.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
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

	Position getPosition() {
		return position;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g; // nsin
		g2d.drawImage(meepleImage, 0, 0, getWidth(), getHeight(), null);
		revalidate(); // nsin
	}

}
