package view;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.TileStack;

public class TileStackPanel extends JPanel implements Observer<TileStack> {
	Tile topTile;
	JLabel tileCounter;
	
	public TileStackPanel(TileStack tilestack) {
		setBorder(BorderFactory.createTitledBorder("Stack"));
		topTile = new Tile(tilestack.peek().getType(), 100);
		add(topTile);
		tileCounter = new JLabel(Integer.toString(tilestack.remainingTiles()));
		add(tileCounter);
	}

	public void update(TileStack tilestack) {
		topTile.setID(tilestack.peek().getType());
		topTile.setRotation(tilestack.peek().getRotation());
		tileCounter.setText(Integer.toString(tilestack.remainingTiles()));
	}

}