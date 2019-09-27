package fop.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import fop.view.Observer;

public class TileStack extends Observable<TileStack> {
	private List<Tile> cardstack; // stack wäre natürlich naheliegender aber eignet sich weniger gut zum mischen
	// TODO in stack umwandeln. einfach vorher factory.gettiles.. und das shufflen. danach erst auf stack legen.
	private TileFactory factory;

	public TileStack() {
		cardstack = new LinkedList<Tile>();
		factory = new TileFactory();

		for (Tile tile : factory.getTiles()) {
			cardstack.add(0, tile);
		}

		Collections.shuffle(cardstack);

		cardstack.add(0, factory.getStartTile());
	}

	public Tile pickUpTile() {
		Tile topTile = cardstack.get(0);
		cardstack.remove(0);
		return topTile;
	}

	public Tile peekTile() {
		return cardstack.get(0);
	}

	public int remainingTiles() {
		return cardstack.size();
	}

	public void rotateTopTile() {
		cardstack.get(0).rotateRight();
		for (Observer<TileStack> o : observers)
			o.update(this);
	}
}