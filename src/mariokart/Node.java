package mariokart;

/**
 * One link in a singly linked queue. It holds an item (data) and a
 * reference to the next node (next).
 *
 * @param <T> the type of item stored in the node
 * @author Nishitha Kancharla
 * @version 2026.11.11
 */
public class Node<T> {
    //~ Fields ................................................................
    private final T data;
    private Node<T> next;

    //~ Constructors ..........................................................
    /**
     * Creates a node that holds the given item and links to nothing.
     *
     * @param data the item to store
     */
    public Node(T data) {
        this.data = data;
        this.next = null;
    }

    //~ Public Methods ........................................................
    /**
     * Returns the item stored in this node.
     *
     * @return the item
     */
    public T getData() {
        return data;
    }

    /**
     * Returns the node that comes after this one.
     *
     * @return the next node, or null if this is the last node
     */
    public Node<T> getNext() {
        return next;
    }

    /**
     * Sets the node that comes after this one.
     *
     * @param next the new next node (may be null)
     */
    public void setNext(Node<T> next) {
        this.next = next;
    }
}