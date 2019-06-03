import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private Node head, tail;
    private int n;

    private class Node {
        Item value;
        Node prev, next;
    }



    public Deque() {
        head = null;
        tail = null;
        n = 0;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        return n;
    }

    public void addFirst(Item item) {
        if (item == null)
            throw new java.lang.IllegalArgumentException();
        Node newHead = new Node();
        newHead.value = item;
        newHead.next = head;
        newHead.prev = null;
        if (head != null)
            head.prev = newHead;
        head = newHead;
        if (tail == null)
            tail = head;
        n++;
    }

    public void addLast(Item item) {
        if (item == null)
            throw new java.lang.IllegalArgumentException();
        Node newTail = new Node();
        newTail.value = item;
        newTail.next = null;
        newTail.prev = tail;
        if (tail != null)
            tail.next = newTail;
        tail = newTail;
        if (head == null)
            head = tail;
        n++;
    }

    public Item removeFirst() {
        if (isEmpty())
            throw new java.util.NoSuchElementException();

        Item ret = head.value;
        if (n == 1) {
            head = null;
            tail = null;
        }
        else {
            Node newHead = head.next;
            head.next = null;
            newHead.prev = null;
            head = newHead;
        }
        n--;
        return ret;

    }
    public Item removeLast() {
        if (isEmpty())
            throw new java.util.NoSuchElementException();
        Item ret = tail.value;
        if (n == 1) {
            head = null;
            tail = null;
        }
        else {
            Node newTail = tail.prev;
            tail.prev = null;
            newTail.next = null;
            tail = newTail;
        }
        n--;
        return ret;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        Node current = head;
        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new java.util.NoSuchElementException();

            Item currentValue = current.value;
            current = current.next;
            return currentValue;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        deque.addLast(1);
        deque.removeLast();
        deque.addFirst(3);
        deque.addFirst(4);
        deque.addLast(5);
        deque.addLast(6);
        deque.addLast(7);
        deque.removeFirst();
        deque.addLast(9);
        deque.removeLast();

        for (int i: deque) {
            StdOut.print(i);
            StdOut.print(' ');
        }
    }
}
