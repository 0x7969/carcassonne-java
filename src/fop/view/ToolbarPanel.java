package fop.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import fop.model.Player;

public class ToolbarPanel extends JPanel implements Observer<List<Player>> {

	private JButton menuButton;
	private JButton skipButton;
	private JLabel[] playerLabels;

	ToolbarPanel(List<Player> players) {
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		skipButton = new JButton("Skip");
		add(skipButton);

		playerLabels = new JLabel[players.size()];
		for (int i = 0; i < players.size(); i++) {
			playerLabels[i] = new JLabel();
			playerLabels[i].setBorder(
					BorderFactory.createTitledBorder(null, players.get(i).getName(), TitledBorder.DEFAULT_JUSTIFICATION,
							TitledBorder.DEFAULT_POSITION, null, players.get(i).getMeepleColor().getColor()));
			playerLabels[i].setPreferredSize(new Dimension(80, 65));
			add(playerLabels[i]);
		}

		menuButton = new JButton("Main menu");
		add(menuButton);

	}

	/**
	 * Adds an action listener to the menu and skip buttons.
	 * 
	 * @param l The action listener
	 */
	public void addToolbarActionListener(ActionListener l) {
		menuButton.addActionListener(l);
		skipButton.addActionListener(l);
	}

	/**
	 * Reveals the skip button in the top toolbar.
	 */
	public void showSkipButton() {
		skipButton.setVisible(true);
	}

	/**
	 * Hides the skip button in the top toolbar.
	 */
	public void hideSkipButton() {
		skipButton.setVisible(false);
	}

	/**
	 * Updates the current score and meeple count for each player.
	 * 
	 * @param players An Array of the players
	 */
	private void updatePlayers(List<Player> players) {
		for (int i = 0; i < players.size(); i++) {
			playerLabels[i].setText("<html>Score:  " + players.get(i).getScore() + "<br>Meeples: "
					+ players.get(i).getMeepleAmount() + "<br></html>");
		}
	}

	@Override
	public void update(List<Player> players) {
		updatePlayers(players);
	}

}
