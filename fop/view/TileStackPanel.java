package fop.view;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fop.model.TileStack;

public class TileStackPanel extends JPanel implements Observer<TileStack> {
	TilePanel topTile;
	JLabel tileCounter;
	
	public TileStackPanel() {
		setBorder(BorderFactory.createTitledBorder("Stack"));
		
		topTile = new TilePanel("FLIPSIDE", 100);
		add(topTile);
		
		tileCounter = new JLabel();
		add(tileCounter);
	}

	public void update(TileStack tilestack) {
		topTile.setType(tilestack.peekTile().getType());
		topTile.setRotation(tilestack.peekTile().getRotation());
		tileCounter.setText(Integer.toString(tilestack.remainingTiles()));
		repaint(); // !
	}

}