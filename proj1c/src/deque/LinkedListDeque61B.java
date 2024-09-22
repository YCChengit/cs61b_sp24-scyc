package deque;

import java.util.Iterator;
import java.util.List;

public class LinkedListDeque61B<T> implements Deque61B<T> {
    @Override
    public Iterator<T> iterator() {
        return new LinkedListDeque61BIterator();
    }

    private class LinkedListDeque61BIterator implements Iterator<T> {

        private Node p;

        public LinkedListDeque61BIterator() {
            p = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return p != sentinel;
        }

        @Override
        public T next() {
            T value = p.value;
            p = p.next;
            return value;
        }
    }

    private class Node {
        private T value;
        private Node next;
        private Node prev;

        Node(T value, Node next, Node prev) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque61B() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T x) {
        Node newNode = new Node(x, sentinel.next, sentinel);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size++;
    }

    @Override
    public void addLast(T x) {
        Node newNode = new Node(x, sentinel, sentinel.prev);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> list = new java.util.LinkedList<>();
        Node p = sentinel.next;
        for (int i = 0; i < size; i++) {
            list.add(p.value);
            p = p.next;
        }
        return list;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        T value = sentinel.next.value;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size--;
        return value;
    }

    @Override
    public T removeLast() {
        T value = sentinel.prev.value;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size--;
        return value;
    }

    @Override
    public T get(int index) {
        T value = null;
        assert index >= 0 && index < size;
        Node p = sentinel.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        value = p.value;
        return value;
    }

    @Override
    public T getRecursive(int index) {
        assert index >= 0 && index < size;
        return getRecursiveHelper(sentinel.next, index);
    }

    private T getRecursiveHelper(Node p, int index) {
        if (index == 0) {
            return p.value;
        }
        return getRecursiveHelper(p.next, index - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Deque61B)) {
            return false;
        }
        Deque61B<T> other = (Deque61B<T>) o;
        if (size() != other.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            if (!get(i).equals(other.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node p = sentinel.next;
        for (int i = 0; i < size; i++) {
            sb.append(p.value);
            sb.append(" ");
            p = p.next;
        }
        return sb.toString();
    }
}
