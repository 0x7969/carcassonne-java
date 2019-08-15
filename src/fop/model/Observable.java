package fop.model;

import java.util.LinkedList;
import java.util.List;

import fop.view.Observer;

public abstract class Observable<T> {

	List<Observer<T>> observers = new LinkedList<Observer<T>>();

	public boolean addObserver(Observer<T> o) {
		return observers.add(o);
	}

	public boolean removeObserver(Observer<T> o) {
		return observers.remove(o);
	}

	public void push(T t) {
		for (Observer<T> o : observers)
			o.update(t);
	}

}