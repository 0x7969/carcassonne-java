package base;

import java.lang.reflect.Array;

import model.Direction;

public class QuadItem<T> {
	
	private T key;
	private QuadItem<T>[] items;
	
	public QuadItem() {
		initItemArray();
	}
	
	public QuadItem(T key) {
		this.key = key;
		initItemArray();
	}
	
	@SuppressWarnings("unchecked")
	private void initItemArray() {
		items = (QuadItem<T>[]) Array.newInstance(QuadItem.class, 4);
	}
	
	// weiß grad nicht mehr, was ich hier mit direction vorhatte...
	public boolean setNeighbour(QuadItem<T> qi, Direction d) {
		items[d.ordinal()] = qi;
		return true;
	}
	
	public QuadItem<T> getNeighbour(Direction d) {
		return items[d.ordinal()];
	}
	
	public void setKey(T key) {
		this.key = key;
	}
	
	public T getKey() {
		return key;
	}

}
