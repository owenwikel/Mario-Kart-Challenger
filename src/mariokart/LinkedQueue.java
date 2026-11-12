package mariokart;

import java.util.NoSuchElementException;

/**
 * A hand-written singly linked FIFO queue. Items join at the back and
 * leave from the front. This is the waiting line in Mario Kart Challenger.
 *
 * @param <T> the type of item stored in the queue
 * @author Nishitha Kancharla
 * @version 2026.11.11
 */
public class LinkedQueue<T> implements QueueInterface<T> {
    //~ Fields ................................................................
    private Node<T> front;
    private Node<T> back;
    private int size;

    //~ Constructors ..........................................................
    /**
     * Creates an empty queue.
     */
    public LinkedQueue() {
        front = null;
        back = null;
        size = 0;
    }

    //~ Public Methods ........................................................
    /**
     * Adds an item to the back of the queue.
     *
     * @param item the item to add
     * @throws IllegalArgumentException if item is null
     */
    @Override
    public void enqueue(T item) throws IllegalArgumentException {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add a null item"
                + " to the queue.");
        }
        Node<T> node = new Node<T>(item);
        if (isEmpty()) {
            front = node;
        }
        else {
            back.setNext(node);
        }
        back = node;
        size++;
    }

    /**
     * Removes and returns the item at the front of the queue.
     *
     * @return the item that was at the front
     * @throws NoSuchElementException if the queue is empty
     */
    @Override
    public T dequeue() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("The queue is empty.");
        }
        T item = front.getData();
        front = front.getNext();
        size--;
        if (front == null) {
            back = null;
        }
        return item;
    }

    /**
     * Returns the item at the front of the queue without removing it.
     *
     * @return the item at the front
     * @throws NoSuchElementException if the queue is empty
     */
    @Override
    public T peek() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("The queue is empty.");
        }
        return front.getData();
    }

    /**
     * Checks whether the queue has no items.
     *
     * @return true if the queue is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of items in the queue.
     *
     * @return the number of items
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns a copy of the queue's items in order, front to back.
     *
     * @return an array of the items, front first
     */
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        Node<T> current = front;
        int i = 0;
        while (current != null) {
            result[i] = current.getData();
            current = current.getNext();
            i++;
        }
        return result;
    }
}