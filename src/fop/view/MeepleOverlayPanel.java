package fop.view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;

import fop.model.Player;
import fop.model.Position;

class MeepleOverlayPanel extends JLabel {

	MeepleOverlayPanel(int size) {
		this.setLayout(new GridLayout(3, 3));
		this.setPreferredSize(new Dimension(size, size));
	}

	MeepleOverlayPanel(boolean[] meepleSpots, int size, Player player) {
		this(size);

		for (int i = 0; i < meepleSpots.length; i++) {
			if (meepleSpots[i])
				add(new MeeplePanel(Position.values()[i], player));
			else
				add(new MeeplePanel());
		}
	}

	public void setSize(int size) {
		this.setPreferredSize(new Dimension(size, size));
	}

}
