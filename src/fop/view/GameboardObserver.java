package fop.view;

import fop.model.Gameboard;

public interface GameboardObserver extends Observer<Gameboard> {
		
	public void newTile(String id, int rotation, int x, int y);

}
