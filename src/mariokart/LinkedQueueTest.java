package mariokart;

import java.util.NoSuchElementException;
import student.TestCase;

/**
 * Tests every method of LinkedQueue: one normal case and one
 * bad-input or edge case each.
 *
 * @author Nishitha Kancharla
 * @version 2026.11.11
 */
public class LinkedQueueTest extends TestCase {
    //~ Fields ................................................................
    private LinkedQueue<String> queue;

    //~ Setup .................................................................
    /**
     * Makes a fresh empty queue before each test.
     */
    public void setUp() {
        queue = new LinkedQueue<String>();
    }

    //~ Public Methods ........................................................
    /**
     * Tests enqueue stores items in the order they were added.
     */
    public void testEnqueue() {
        queue.enqueue("Alex");
        queue.enqueue("Jordan");
        queue.enqueue("Sam");
        assertEquals(3, queue.size());
        assertEquals("Alex", queue.peek());
        Object[] items = queue.toArray();
        assertEquals("Alex", items[0]);
        assertEquals("Jordan", items[1]);
        assertEquals("Sam", items[2]);
    }

    /**
     * Tests enqueue rejects null and leaves the queue unchanged.
     */
    public void testEnqueueNull() {
        queue.enqueue("Alex");
        Exception thrown = null;
        try {
            queue.enqueue(null);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals(1, queue.size());
        assertEquals("Alex", queue.peek());
    }

    /**
     * Tests dequeue removes from the front, one at a time.
     */
    public void testDequeue() {
        queue.enqueue("Alex");
        queue.enqueue("Jordan");
        assertEquals("Alex", queue.dequeue());
        assertEquals(1, queue.size());
        assertEquals("Jordan", queue.peek());
        assertEquals("Jordan", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    /**
     * Tests dequeue on an empty queue throws, and that the queue still
     * works after emptying it and adding again.
     */
    public void testDequeueEmpty() {
        Exception thrown = null;
        try {
            queue.dequeue();
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof NoSuchElementException);
        assertEquals(0, queue.size());

        queue.enqueue("Alex");
        queue.dequeue();
        queue.enqueue("Jordan");
        queue.enqueue("Sam");
        assertEquals(2, queue.size());
        assertEquals("Jordan", queue.dequeue());
        assertEquals("Sam", queue.dequeue());
    }

    /**
     * Tests peek returns the front without removing it.
     */
    public void testPeek() {
        queue.enqueue("Alex");
        queue.enqueue("Jordan");
        assertEquals("Alex", queue.peek());
        assertEquals("Alex", queue.peek());
        assertEquals(2, queue.size());
    }

    /**
     * Tests peek on an empty queue throws and changes nothing.
     */
    public void testPeekEmpty() {
        Exception thrown = null;
        try {
            queue.peek();
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof NoSuchElementException);
        assertEquals(0, queue.size());
    }

    /**
     * Tests isEmpty is false when someone is waiting.
     */
    public void testIsEmpty() {
        queue.enqueue("Alex");
        queue.enqueue("Jordan");
        assertFalse(queue.isEmpty());
    }

    /**
     * Tests isEmpty is true for a new queue and after everyone leaves.
     */
    public void testIsEmptyEdge() {
        assertTrue(queue.isEmpty());
        queue.enqueue("Alex");
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    /**
     * Tests size counts items as they join and leave.
     */
    public void testSize() {
        assertEquals(0, queue.size());
        queue.enqueue("Alex");
        queue.enqueue("Jordan");
        assertEquals(2, queue.size());
        queue.dequeue();
        assertEquals(1, queue.size());
    }

    /**
     * Tests size does not change after a rejected enqueue or a failed
     * dequeue.
     */
    public void testSizeAfterBadInput() {
        try {
            queue.enqueue(null);
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        assertEquals(0, queue.size());
        try {
            queue.dequeue();
        }
        catch (NoSuchElementException e) {
            // expected
        }
        assertEquals(0, queue.size());
    }

    /**
     * Tests toArray returns the items front to back.
     */
    public void testToArray() {
        queue.enqueue("Alex");
        queue.enqueue("Jordan");
        queue.enqueue("Sam");
        Object[] items = queue.toArray();
        assertEquals(3, items.length);
        assertEquals("Alex", items[0]);
        assertEquals("Jordan", items[1]);
        assertEquals("Sam", items[2]);
        assertEquals(3, queue.size());
    }

    /**
     * Tests toArray on an empty queue gives an empty array, and that
     * changing the returned array does not change the queue.
     */
    public void testToArrayEdge() {
        assertEquals(0, queue.toArray().length);

        queue.enqueue("Alex");
        Object[] items = queue.toArray();
        items[0] = "Hacker";
        assertEquals("Alex", queue.peek());
        assertEquals("Alex", queue.toArray()[0]);
    }
}