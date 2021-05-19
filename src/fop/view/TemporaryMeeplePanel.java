package fop.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
public class TemporaryMeeplePanel extends MeeplePanel {

	/**
	 * A meeple panel without a position is considered not to be a spot to place a
	 * meeple on. It's just an invisible panel using up space.
	 */
	TemporaryMeeplePanel() {
		super();
	}

	TemporaryMeeplePanel(Position pos, Player player) {
		super();
		super.position = pos;
		this.colour = player.getMeepleColor();

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
