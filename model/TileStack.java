package model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import view.Observer;

public class TileStack implements Observable<TileStack> {
	private List<Tile> cardstack; // stack wäre natürlich naheliegender aber eignet sich weniger gut zum mischen
	private TileFactory factory;
	private List<Observer<TileStack>> observers;

	public TileStack() {
		cardstack = new LinkedList<Tile>();
		factory = new TileFactory();
		observers = new LinkedList<Observer<TileStack>>();
		
		for (Tile tile : factory.getTiles()) {
			cardstack.add(tile);
		}
		
		Collections.shuffle(cardstack);
		
		System.out.println("Ammount of tiles in tilestack: " + cardstack.size());
	}

	public Tile pop() {
		Tile topTile = cardstack.get(0);
		cardstack.remove(0);
		for (Observer<TileStack> o : observers)
			o.update(this);
		return topTile;
	}

	public Tile peek() {
		return cardstack.get(0);
	}

	public int remainingTiles() {
		return cardstack.size();
	}

	public void rotateTile() {
		cardstack.get(0).rotate();
	}

	@Override
	public boolean addObserver(Observer<TileStack> o) {
		return observers.add(o);
	}

	@Override
	public boolean removeObserver(Observer<TileStack> o) {
		return observers.remove(o);
	}
	
}