package view;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.TileStack;

public class TileStackPanel extends JPanel implements Observer<TileStack> {
	TilePanel topTile;
	JLabel tileCounter;
	
	public TileStackPanel(TileStack tilestack) {
		setBorder(BorderFactory.createTitledBorder("Stack"));
		topTile = new TilePanel(tilestack.peek().getType(), 100);
		add(topTile);
		tileCounter = new JLabel(Integer.toString(tilestack.remainingTiles()));
		add(tileCounter);
	}

	public void update(TileStack tilestack) {
		topTile.setType(tilestack.peek().getType());
		topTile.setRotation(tilestack.peek().getRotation());
		tileCounter.setText(Integer.toString(tilestack.remainingTiles()));
	}

}