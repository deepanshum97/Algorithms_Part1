import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] arr;

    private int front;
    private int rear;

    private int N;
    private int capacity;

    /**
     * Construct an empty randomized queue
     */
    public RandomizedQueue() {
        capacity = 4;
        arr = (Item[]) new Object[capacity];
    }

    /**
     *
     * Is the RandomizedQueue empty ?
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * Return the number of items on the randomized queue
     *
     * @return int
     */
    public int size() {
        return N;
    }

    /**
     * Add the item in the randomized queue
     *
     * @param item
     */
    public void enqueue(Item item) {
        validateNull(item);
        considerResizingIfRequired(size() == capacity - 1, capacity, capacity * 2);
        arr[rear] = item;

        // Update the rear value
        rear = (rear + 1 + capacity) % capacity;
        incrementSize();
    }

    /**
     * Remove and return a random item
     *
     * @return Item
     */
    public Item dequeue() {
        validateEmpty();
        randomizeFront();

        Item item = arr[front];
        arr[front] = null;
        front = (front + 1) % capacity;

        decrementSize();
        considerResizingIfRequired(size() < capacity / 4 && capacity > 4, capacity, capacity / 2);
        return item;
    }

    private void randomizeFront() {
        int randomIndex = size() > 0 ? StdRandom.uniform(size()) : 0;
        Item temp = arr[front];
        arr[front] = arr[(front + randomIndex) % capacity];
        arr[(front + randomIndex) % capacity] = temp;
    }

    private void considerResizingIfRequired(boolean isRequired, int oldCapacity, int newCapacity) {
        if (isRequired) {
            capacity = newCapacity;

            Item newArr[] = (Item[]) new Object[capacity];
            for (int i = 0 ; i < size() ; i++) {
                newArr[i % capacity] = arr[(front + i + oldCapacity) % oldCapacity];
                arr[(front + i + oldCapacity) % oldCapacity] = null;
            }

            front = 0;
            rear = size() % capacity;
            arr = newArr;
        }
    }

    /**
     * Return a random item (but do not remove it)
     *
     * @return Item
     */
    public Item sample() {
        validateEmpty();
        int randomIndex = size() > 0 ? StdRandom.uniform(size()) : 0;

        return arr[(front + randomIndex) % capacity];
    }

    /**
     * Return an independent iterator over items in random order
     *
     * @return Iterator
     */
    public Iterator<Item> iterator() {
        return new ListIterator();
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
     * An iterator, doesn't implement remove() since it's optional
     */
    private class ListIterator implements Iterator<Item> {

        private int start;
        private Item[] iterArr;

        public ListIterator() {
            iterArr = (Item [])new Object[size()];
            for (int i = 0 ; i < size() ; i++) {
                iterArr[i] = arr[(front + i) % capacity];
            }

            StdRandom.shuffle(iterArr);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return start != iterArr.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the Deque");
            }

            return iterArr[start++];
        }
    }

    /**
     * Unit testing (optional)
     *
     * @param args
     */
    public static void main(String[] args) {
    }
}
