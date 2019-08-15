package fop.base;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Diese Klasse representiert einen generischen Graphen mit einer Liste aus
 * Knoten und Kanten.
 *
 * @param <T> Die zugrundeliegende Datenstruktur, beispielsweise
 *            {@link game.map.Castle}
 */
public class Graph<T> {

	private List<WeightedEdge<T>> edges;
	private List<Node<T>> nodes;

	/**
	 * Konstruktor für einen neuen, leeren Graphen
	 */
	public Graph() {
		this.nodes = new ArrayList<>();
		this.edges = new LinkedList<>();
	}

	/**
	 * Einen neuen Knoten zum Graphen hinzufügen
	 * 
	 * @param value Der Wert des Knotens
	 * @return Der erstellte Knoten
	 */
	public Node<T> addNode(T value) {
		Node<T> node = new Node<>(value);
		this.nodes.add(node);
		return node;
	}

	// TODO könnte man hier jetzt eigentlich auch ein Node<T> einfügen oder muss es
	// eine klasse sein, die von Node<T> erbt?
	public boolean addAllNodes(Collection<? extends Node<T>> nodes) {
		return this.nodes.addAll(nodes);
	}

	/**
	 * Eine neue Kante zwischen zwei Knoten hinzufügen. Sollte die Kante schon
	 * existieren, wird die vorhandene Kante zurückgegeben.
	 * 
	 * @param nodeA Der erste Knoten
	 * @param nodeB Der zweite Knoten
	 * @return Die erstellte oder bereits vorhandene Kante zwischen beiden gegebenen
	 *         Knoten
	 */
	public WeightedEdge<T> addEdge(Node<T> nodeA, Node<T> nodeB) {
		WeightedEdge<T> edge = getEdge(nodeA, nodeB);
		if (edge != null) {
			return edge;
		}

		edge = new WeightedEdge<>(nodeA, nodeB);
		this.edges.add(edge);
		return edge;
	}

	public WeightedEdge<T> addEdge(Node<T> nodeA, Node<T> nodeB, int weight) {
		WeightedEdge<T> edge = getEdge(nodeA, nodeB);
		if (edge != null) {
			return edge;
		}

		edge = new WeightedEdge<>(nodeA, nodeB, weight);
		this.edges.add(edge);
		return edge;
	}

	public boolean addAllEdges(Collection<WeightedEdge<T>> edges) {
		return this.edges.addAll(edges); // TODO kann das probleme machen? die methode addEdge prüft vorher noch ob sie
											// bereits vorhanden ist.
	}

	/**
	 * Gibt die Liste aller Knoten zurück
	 * 
	 * @return die Liste aller Knoten
	 */
	public List<Node<T>> getNodes() {
		return this.nodes;
	}

	/**
	 * Gibt die Liste aller Kanten zurück
	 * 
	 * @return die Liste aller Kanten
	 */
	public List<WeightedEdge<T>> getEdges() {
		return this.edges;
	}

	/**
	 * Diese Methode gibt alle Werte der Knoten in einer Liste mittels Streams
	 * zurück.
	 * 
	 * @see java.util.stream.Stream#map(Function)
	 * @see java.util.stream.Stream#collect(Collector)
	 * @return Eine Liste aller Knotenwerte
	 */
	public List<T> getAllValues() {
		return this.nodes.stream().map(Node::getValue).collect(Collectors.toList());
	}

	/**
	 * Diese Methode gibt alle Kanten eines Knotens als Liste mittels Streams
	 * zurück.
	 * 
	 * @param node Der Knoten für die dazugehörigen Kanten
	 * @see java.util.stream.Stream#filter(Predicate)
	 * @see java.util.stream.Stream#collect(Collector)
	 * @return Die Liste aller zum Knoten zugehörigen Kanten
	 */
	public List<WeightedEdge<T>> getEdges(Node<T> node) {
		return this.edges.stream().filter(edge -> edge.contains(node)).collect(Collectors.toList());
	}

	/**
	 * Diese Methode sucht eine Kante zwischen beiden angegebenen Knoten und gibt
	 * diese zurück oder null, falls diese Kante nicht existiert
	 * 
	 * @param nodeA Der erste Knoten
	 * @param nodeB Der zweite Knoten
	 * @return Die Kante zwischen beiden Knoten oder null
	 */
	public WeightedEdge<T> getEdge(Node<T> nodeA, Node<T> nodeB) {
		return this.edges.stream().filter(edge -> (edge.getNodeA() == nodeA && edge.getNodeB() == nodeB)
				|| (edge.getNodeA() == nodeB && edge.getNodeB() == nodeA)).findFirst().orElse(null);
	}

	/**
	 * Gibt den ersten Knoten mit dem angegebenen Wert zurück oder null, falls
	 * dieser nicht gefunden wurde
	 * 
	 * @param value Der zu suchende Wert
	 * @return Ein Knoten mit dem angegebenen Wert oder null
	 */
	public Node<T> getNode(T value) {
		return this.nodes.stream().filter(n -> n.getValue() == value).findFirst().orElse(null);
	}

	/**
	 * Returns all nodes with given value
	 * 
	 * @param value
	 * @return All nodes with given value
	 */
	public List<Node<T>> getNodes(T value) {
		return this.nodes.stream().filter(n -> n.getValue() == value).collect(Collectors.toList());
	}

	/**
	 * Überprüft, ob alle Knoten in dem Graphen erreichbar sind.
	 * 
	 * @return true, wenn alle Knoten erreichbar sind
	 */
	public boolean allNodesConnected() {
		List<Node<T>> nodeList = new ArrayList<>(this.nodes);
		if (nodeList.size() <= 1)
			return true;

		ArrayDeque<Node<T>> queue = new ArrayDeque<>();
		queue.push(nodeList.remove(0));

		while (!queue.isEmpty()) {
			Node<T> node = queue.pop();
			List<WeightedEdge<T>> edges = this.getEdges(node);
			for (WeightedEdge<T> edge : edges) {
				Node<T> nextNode = edge.getOtherNode(node);
				if (nodeList.contains(nextNode)) {
					queue.push(nextNode);
					nodeList.remove(nextNode);
				}
			}
		}

		return nodeList.isEmpty();
	}
}