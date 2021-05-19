package fop.view;

import fop.model.Player;
import fop.model.Position;

class TemporaryMeepleOverlayPanel extends MeepleOverlayPanel {

	TemporaryMeepleOverlayPanel(boolean[] meepleSpots, int size, Player player) {
		super(size);

		for (int i = 0; i < meepleSpots.length; i++) {
			if (meepleSpots[i])
				add(new TemporaryMeeplePanel(Position.values()[i], player));
			else
				add(new TemporaryMeeplePanel());
		}
	}

}
