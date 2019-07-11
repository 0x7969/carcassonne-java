package model;

public enum Direction {
	
	X, Y, BOTH;
	
	// TODO unbenutzt
	public Direction getOpposite(Direction d) {
		if (d == BOTH)
			throw new IllegalArgumentException("Direction BOTH does not have an opposite direction.");
		else return d == X ? Y : X;
	}
	
}
