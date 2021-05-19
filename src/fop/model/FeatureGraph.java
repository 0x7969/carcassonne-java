package fop.model;

import fop.base.Edge;
import fop.base.Graph;

class FeatureGraph extends Graph<FeatureType> {
	
	FeatureGraph() {
		super();
	}

	Edge<FeatureType> addEdge(FeatureNode nodeA, FeatureNode nodeB) {
		nodeA.setConnectsTiles();
		nodeB.setConnectsTiles();
		return super.addEdge(nodeA, nodeB);
	}

}
