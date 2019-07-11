package model;

import view.Observer;

public interface Observable<T> {
	
	public boolean addObserver(Observer<T> o);
	
	public boolean removeObserver(Observer<T> o);

}
