package mariokart;

/**
 * The contract for a first-in, first-out (FIFO) queue. In Mario Kart
 * Challenger, the queue is the waiting line of players.
 *
 * @param <T> the type of item stored in the queue
 * @author Nishitha Kancharla
 * @version 2026.11.11
 */
public interface QueueInterface<T> {
    /**
     * Adds an item to the back of the queue.
     *
     * @param item the item to add
     * @throws IllegalArgumentException if item is null
     */
    void enqueue(T item);

    /**
     * Removes and returns the item at the front of the queue.
     *
     * @return the item that was at the front
     * @throws java.util.NoSuchElementException if the queue is empty
     */
    T dequeue();

    /**
     * Returns the item at the front of the queue without removing it.
     *
     * @return the item at the front
     * @throws java.util.NoSuchElementException if the queue is empty
     */
    T peek();

    /**
     * Checks whether the queue has no items.
     *
     * @return true if the queue is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Returns the number of items in the queue.
     *
     * @return the number of items
     */
    int size();

    /**
     * Returns a copy of the queue's items in order, front to back.
     * Changing the returned array does not change the queue.
     *
     * @return an array of the items, front first
     */
    Object[] toArray();
}