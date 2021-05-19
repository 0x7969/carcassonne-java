package fop.model;

import java.util.LinkedList;
import java.util.List;

import fop.view.Observer;

//TODO this should not be abstract but coupled to a game instance
public abstract class Observable<T> {

	List<Observer<T>> observers = new LinkedList<Observer<T>>();

	public boolean addObserver(Observer<T> o) {
		return observers.add(o);
	}

	public void push(T t) {
		for (Observer<T> o : observers)
			o.update(t);
	}

}