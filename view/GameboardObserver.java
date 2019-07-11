package view;

import model.Gameboard;

public interface GameboardObserver extends Observer<Gameboard> {
	
	public void setTileID(String id, int x, int y);

}
