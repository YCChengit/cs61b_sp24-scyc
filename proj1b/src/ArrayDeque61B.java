import java.util.List;
import java.lang.Math;

public class ArrayDeque61B<T> implements Deque61B<T> {

    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque61B() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }

    @Override
    public void addFirst(T x) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = x;
        nextFirst = Math.floorMod(nextFirst - 1, items.length);
        size += 1;
    }

    @Override
    public void addLast(T x) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = x;
        nextLast = Math.floorMod(nextLast + 1, items.length);
        size += 1;
    }

    @Override
    public List<T> toList() {
        List<T> list = new java.util.ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(get(i));
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
        T x = items[Math.floorMod(nextFirst + 1, items.length)];
        items[nextFirst] = null;
        nextFirst = Math.floorMod(nextFirst + 1, items.length);
        size -= 1;
        return x;
    }

    @Override
    public T removeLast() {
        nextLast = Math.floorMod(nextLast - 1, items.length);
        T x = items[nextLast];
        items[nextLast] = null;
        size -= 1;
        return x;
    }

    @Override
    public T get(int index) {
        return items[Math.floorMod(nextFirst + 1 + index, items.length)];
    }

    @Override
    public T getRecursive(int index) {
        throw new UnsupportedOperationException("No need to implement getRecursive for proj 1b");
    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            a[i] = get(Math.floorMod(nextFirst  + i, items.length));
        }
        items = a;
        nextFirst = capacity - 1;
        nextLast = size;
    }
}
