import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] data;
    private int lastIdx;

    public RandomizedQueue() {
        data = (Item[]) new Object[4];
        lastIdx = 0;
    }

    private int capacity() {
        return data.length;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return lastIdx;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }
        if (capacity() == size()) {
            resize(capacity()*2);
        }
        data[lastIdx++] = item;
    }

    private void resize(int newSize) {
        Item[] newData = (Item[]) new Object[newSize];
        for (int i = 0; i < size(); i++)
            newData[i] = data[i];
        data = newData;
    }

    public Item dequeue() {
        if (size() == 0) {
            throw new java.util.NoSuchElementException();
        }
        int idx = StdRandom.uniform(0, size());
        Item toRet = data[idx];
        data[idx] = data[lastIdx-1];
        data[lastIdx-1] = null;
        lastIdx--;
        if (capacity() > 4*size() && size() > 1) {
            resize(size()*2);
        }
        return toRet;



    }
    public Item sample() {
        if (size() == 0) {
            throw new java.util.NoSuchElementException();
        }
        int idx = StdRandom.uniform(0, size());
        return data[idx];
    }

    public Iterator<Item> iterator() {
        return new RandQueueIterator();
    }

    private class RandQueueIterator implements Iterator<Item> {

        private final int[] perm;
        private int curr;

        public RandQueueIterator() {
            perm = StdRandom.permutation(size());
            curr = 0;
        }

        @Override
        public boolean hasNext() {
            return curr != perm.length;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new java.util.NoSuchElementException();

            return data[perm[curr++]];
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(280);
        rq.dequeue();
        rq.enqueue(389);
    }
}