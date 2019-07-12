package view;

import model.Gameboard;

public interface GameboardObserver extends Observer<Gameboard> {
	
	public void initGameboard(String id, int x, int y);
	
	public void setTileTypeAndRotation(String id, int rotation, int x, int y);

}
