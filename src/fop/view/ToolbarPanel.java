package fop.view;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fop.model.Player;

public class ToolbarPanel extends JPanel implements Observer<Player[]> {

	JButton quitButton;
	JButton skipButton;
	JLabel[] playerLabels;

	public ToolbarPanel(Player[] players) {
//		setBorder(BorderFactory.createTitledBorder("Menu"));
		setLayout(new FlowLayout(FlowLayout.LEFT));

		playerLabels = new JLabel[players.length];
		for (int i = 0; i < players.length; i++) {
			playerLabels[i] = new JLabel(players[i].getName() + " Score:  " + players[i].getScore() + " Meeples: "
					+ players[i].getMeeples());
			add(playerLabels[i]);
		}

		quitButton = new JButton("Zum Hauptmenü");
		add(quitButton);

		skipButton = new JButton("Skip");
		add(skipButton);
	}

	public void addToolbarActionListeners(ActionListener l) {
		quitButton.addActionListener(l);
		skipButton.addActionListener(l);
	}

	public void showSkipButton() {
		skipButton.setVisible(true);
	}

	public void hideSkipButton() {
		skipButton.setVisible(false);
	}

	@Override
	public void update(Player[] players) {
		for (int i = 0; i < players.length; i++) {
			playerLabels[i].setText(players[i].getName() + " Score:  " + players[i].getScore() + " Meeples: "
					+ players[i].getMeeples());
		}
	}

}
