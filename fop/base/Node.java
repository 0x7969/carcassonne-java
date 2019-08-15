package fop.base;

/**
 * Diese Klasse representiert einen generischen Knoten
 * @param <T>
 */
public class Node<T> {

    private T value;

    /**
     * Erzeugt einen neuen Knoten mit dem gegebenen Wert
     * @param value der Wert des Knotens
     */
    public Node(T value) {
        this.value = value;
    }

    /**
     * Gibt den Wert des Knotens zurück
     * @return der Wert des Knotens
     */
    public T getValue() {
        return value;
    }
    
    public String toString() {
    	return value.toString();
    }
}
