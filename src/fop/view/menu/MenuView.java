package fop.view.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EnumSet;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import fop.controller.GameController;
import fop.model.MeepleColor;
import fop.model.Player;
import fop.model.State;

public class MenuView extends JPanel {

	private GameController gc;
	private JPanel playerEntries;

	public MenuView(GameController gc) {

		this.gc = gc;

		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(600, 310));

		JButton startButton = new JButton("Start game");
		startButton.addActionListener(event -> {
			List<Player> players = gc.getPlayers();
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).getName() == null)
					players.get(i).setName("Player " + Integer.toString(i));
			}

			gc.setState(State.GAME_START);
		});

		playerEntries = new JPanel();
		playerEntries.setLayout(new BoxLayout(playerEntries, BoxLayout.Y_AXIS));

		addPlayer();
		addPlayer();

		this.add(playerEntries, BorderLayout.NORTH);
		this.add(startButton, BorderLayout.SOUTH);
	}

	private boolean addPlayer() {
		if (gc.addPlayer()) {
			updatePlayerEntries();
			return true;
		} else
			return false;
	}

	private boolean removePlayer(Player p) {
		if (gc.removePlayer(p)) {
			updatePlayerEntries();
			return true;
		} else
			return false;
	}

	private void updatePlayerEntries() {
		playerEntries.removeAll();

		EnumSet<MeepleColor> remainingMeepleColors = EnumSet.allOf(MeepleColor.class);
		for (Player p : gc.getPlayers()) {
			if (p.getMeepleColor() != null)
				remainingMeepleColors.remove(p.getMeepleColor());
		}

		for (int i = 0; i < gc.getPlayers().size(); i++) {
			Player p = gc.getPlayers().get(i);

			JPanel playerEntry = new JPanel();
			playerEntry.setLayout(new FlowLayout());
			playerEntry.setPreferredSize(new Dimension(250, 44));

			JTextField nameField = new JTextField();
			nameField.setPreferredSize(new Dimension(200, 36));
			if (p.getName() != null) {
				nameField.setText(p.getName());
			} else {
				nameField.setText("Player " + Integer.toString(i));
			}

			nameField.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					nameField.selectAll();
				}
			});

			// Cumbersome but this seems to be the proper way to listen to changes in text
			// fields.
			nameField.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {
					changeName();
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					changeName();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					changeName();
				}

				private void changeName() {
					p.setName(nameField.getText());
				}
			});
			playerEntry.add(nameField);

			if (i + 1 == gc.getPlayers().size()) {
				JButton addPlayerButton = new JButton("+");
				addPlayerButton.addActionListener(event -> {
					addPlayer();
				});
				playerEntry.add(addPlayerButton);
			} else {
				nameField.setPreferredSize(new Dimension(234, 36));
			}

			JButton deletePlayerButton = new JButton("-");
			deletePlayerButton.addActionListener(event -> {
				removePlayer(p);
			});
			playerEntry.add(deletePlayerButton);

			JPanel meepleColorOptions = new JPanel();
			meepleColorOptions.setLayout(new BoxLayout(meepleColorOptions, BoxLayout.X_AXIS));
			meepleColorOptions.setPreferredSize(new Dimension(200, 36));
			meepleColorOptions.setBorder(new EtchedBorder());
			for (MeepleColor color : EnumSet.allOf(MeepleColor.class)) {

				MeeplePanel meeplePanel = new MeeplePanel(color);
				if (p.getMeepleColor() == color) {
					meeplePanel.setFilled(true);

					meeplePanel.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							p.setMeepleColor(null);
							updatePlayerEntries();
						}
					});
				} else if (!remainingMeepleColors.contains(color)) {
					meeplePanel.setTransparent(true);
				} else {
					meeplePanel.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseEntered(MouseEvent e) {
							meeplePanel.setFilled(true);
						}

						@Override
						public void mouseExited(MouseEvent e) {
							meeplePanel.setFilled(false);
						}

						@Override
						public void mouseClicked(MouseEvent e) {
							p.setMeepleColor(color);
							updatePlayerEntries();
						}
					});
				}
				meepleColorOptions.add(meeplePanel);
			}
			playerEntry.add(meepleColorOptions);

			playerEntries.add(playerEntry);
			revalidate(); // nsin
		}

	}
}
