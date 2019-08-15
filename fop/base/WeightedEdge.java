package fop.base;

/**
 * Diese Klasse representiert eine generische gewichtete Kante zwischen zwei
 * Knoten
 * 
 * @param <T> die zugrunde liegende Datenstruktur
 */
public class WeightedEdge<T> extends Edge<T> {

	private int weight;

	/**
	 * Erstellt eine neue Kante zwischen zwei gegebenen Knoten
	 * 
	 * @param nodeA der erste Knoten
	 * @param nodeB der zweite Knoten
	 */
	public WeightedEdge(Node<T> nodeA, Node<T> nodeB) {
		super(nodeA, nodeB);
		this.weight = 0;
	}

	public WeightedEdge(Node<T> nodeA, Node<T> nodeB, int weight) {
		super(nodeA, nodeB);
		this.weight = weight;
	}

	public int getWeight() {
		return weight;
	}

}
