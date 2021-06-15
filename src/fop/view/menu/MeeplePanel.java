package fop.view.menu;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import fop.model.MeepleColor;

/**
 * A MeeplePanel is one of nine Panels inside a MeepleOverlayPanel. It is either
 * a possible spot to place a Meeple on or not, in which case it is still part
 * of the MeepleOverlayPanel to get the spacing right.
 * 
 * @author yi
 *
 */
class MeeplePanel extends JPanel {

	protected BufferedImage meepleImage;
	protected BufferedImage meepleImageFilled;
	protected BufferedImage meepleImageTransparency10;
	protected BufferedImage meepleImageTransparency50;

	private final String FOLDER = "meeple/";;

	MeeplePanel(MeepleColor meepleColor) {
		this.setOpaque(true); // nsin
		
		try {
			meepleImageFilled = ImageIO.read(getClass().getClassLoader()
					.getResourceAsStream(FOLDER + meepleColor.toString().toLowerCase() + ".png"));
			meepleImageTransparency10 = ImageIO.read(getClass().getClassLoader()
					.getResourceAsStream(FOLDER + meepleColor.toString().toLowerCase() + "_transparency10.png"));
			meepleImage = meepleImageTransparency50 = ImageIO.read(getClass().getClassLoader()
					.getResourceAsStream(FOLDER + meepleColor.toString().toLowerCase() + "_outline.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setFilled(boolean filled) {
		if (filled) {
			meepleImage = meepleImageFilled;

		} else
			meepleImage = meepleImageTransparency50;
		repaint();
	}

	public void setTransparent(boolean transparent) {
		if (transparent)
			meepleImage = meepleImageTransparency10;
		else
			meepleImage = meepleImageTransparency50;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g; // nsin
		g2d.drawImage(meepleImage, 0, 0, getWidth(), getHeight(), null);
		revalidate(); // nsin
	}

}
