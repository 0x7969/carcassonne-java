package fop.view.menu;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import fop.model.MeepleColor;

class MeepleOverlayPanel extends JLabel {

	MeepleOverlayPanel(List<MeepleColor> meepleColors) {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		for (MeepleColor color : meepleColors) {
			add(new MeeplePanel(color));
		}
	}
}
