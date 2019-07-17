import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    /**
     *  Private Internal class to hold the generic object details.
     *  This data-structure will be modelled as a doubly linked-list,
     *  so this class will have a pointer to prev and next node.
     */
    private class Node {
        private Item item;
        private Node prev, next;

        public Node(Item item) {
            this.item = item;
        }
    }

    private Node front = null;
    private Node last = null;

    private int N;

    /**
     *  Constructor.
     */
    public Deque() {
    }

    /**
     * Is the deque empty ?
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * Return the number of items on the deque
     *
     * @return int
     */
    public int size() {
        return N;
    }

    /**
     * Add the item to the front
     *
     * @param item
     */
    public void addFirst(Item item) {
        validateNull(item);

        if (isEmpty()) {
            front = new Node(item);
            last = front;
        } else {
            Node oldFront = front;
            front = new Node(item);
            front.next = oldFront;
            oldFront.prev = front;
        }

        incrementSize();
    }

    /**
     * Add the item to the end
     *
     * @param item
     */
    public void addLast(Item item) {
        validateNull(item);

        if (isEmpty()) {
            front = new Node(item);
            last = front;
        } else {
            Node oldLast = last;
            last = new Node(item);
            last.prev = oldLast;
            oldLast.next = last;
        }

        incrementSize();
    }

    /**
     * remove and return the item from the front
     *
     * @return
     */
    public Item removeFirst() {
        validateEmpty();
        decrementSize();

        Node oldFront = front;
        Item item = oldFront.item;

        if (isEmpty()) {
            front = last = null;
            return item;
        }

        front = oldFront.next;
        oldFront.next = null;
        front.prev = null;

        return item;
    }

    /**
     * remove and return the item from the end
     *
     * @return
     */
    public Item removeLast() {
        validateEmpty();
        decrementSize();

        Node oldLast = last;
        Item item = oldLast.item;

        if (isEmpty()) {
            front = last = null;
            return item;
        }

        last = oldLast.prev;
        oldLast.prev = null;
        last.next = null;

        return item;
    }

    /**
     * Returns an iterator that iterates over the items in this queue in FIFO order.
     *
     * @return an iterator that iterates over the items in this queue in FIFO order
     */
    public Iterator<Item> iterator()  {
        return new ListIterator();
    }

    /**
     * An iterator, doesn't implement remove() since it's optional
     */
    private class ListIterator implements Iterator<Item> {

        private Node start = front;

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return start != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the Deque");
            }

            Item item = start.item;
            start = start.next;

            return item;
        }
    }

    private void validateNull(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item should not be null.");
        }
    }

    private void validateEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot remove from Deque as it is empty");
        }
    }

    private void incrementSize() {
        N++;
    }

    private void decrementSize() {
        N--;
    }

    /**
     * unit testing (optional)
     *
     * @param args
     */
    public static void main(String[] args) {
    }
}