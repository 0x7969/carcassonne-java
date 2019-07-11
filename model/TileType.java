package model;

import static model.FeatureType.*;

import base.Node;

public enum TileType {
	
	// To represent a tile, we divide it into a grid of nine cells, that is
	// saved as an array, going from top left to top right, then center left to
	// center right, then bottom left to bottom right.
	// Where there are conflicting features on one of those cells,
	// (which is the case on every tile with fields and a castle)
	// we label it as FIELDS.
	A(2, false, FIELDS, FIELDS, FIELDS, FIELDS, MONASTERY, FIELDS, FIELDS, ROAD, FIELDS),
	B(4, false, FIELDS, FIELDS, FIELDS, FIELDS, MONASTERY, FIELDS, FIELDS, FIELDS, FIELDS),
	C(1, true, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE),
	D(4, false, FIELDS, ROAD, FIELDS, FIELDS, ROAD, CASTLE, FIELDS, ROAD, FIELDS),
	E(5, false, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS),
	F(2, true, FIELDS, FIELDS, FIELDS, CASTLE, CASTLE, CASTLE, FIELDS, FIELDS, FIELDS),
	G(1, false, FIELDS, FIELDS, FIELDS, CASTLE, CASTLE, CASTLE, FIELDS, FIELDS, FIELDS),
	H(3, false, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, CASTLE, FIELDS),
	I(2, false, NULL, CASTLE, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS),
	J(3, false, FIELDS, CASTLE, FIELDS, FIELDS, ROAD, ROAD, FIELDS, ROAD, FIELDS),
	K(3, false, FIELDS, CASTLE, FIELDS, ROAD, ROAD, FIELDS, FIELDS, ROAD, FIELDS),
	L(3, false, FIELDS, CASTLE, FIELDS, ROAD, NULL, ROAD, FIELDS, ROAD, FIELDS),
	M(2, true, CASTLE, CASTLE, FIELDS, CASTLE, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS),
	N(3, false, CASTLE, CASTLE, FIELDS, CASTLE, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS),
	O(2, true, CASTLE, CASTLE, FIELDS, CASTLE, ROAD, ROAD, FIELDS, ROAD, FIELDS),
	P(3, false, CASTLE, CASTLE, FIELDS, CASTLE, ROAD, ROAD, FIELDS, ROAD, FIELDS),
	Q(1, true, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, FIELDS, FIELDS, FIELDS),
	R(3, false, Q.terrain),
	S(2, true, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, FIELDS, ROAD, FIELDS),
	T(1, false, S.terrain),
	U(8, false, FIELDS, FIELDS, FIELDS, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS),
	V(9, false, FIELDS, FIELDS, FIELDS, ROAD, ROAD, FIELDS, FIELDS, ROAD, FIELDS),
	W(4, false, FIELDS, FIELDS, FIELDS, ROAD, NULL, ROAD, FIELDS, ROAD, FIELDS),
	X(1, false, FIELDS, ROAD, FIELDS, ROAD, NULL, ROAD, FIELDS, ROAD, FIELDS);
		
	private final int amount;
	private final FeatureType[] terrain;
	
	TileType(int amount, boolean coatOfArms, FeatureType... terrain) {
        this.amount = amount;
        this.terrain = terrain;
    }
	
}
