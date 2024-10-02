package hashmap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 *  A hash table-backed Map implementation.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int size;
    private double loadFactor = 0.75;
    private int capacity = 16;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this.buckets = (Collection<Node>[]) new Collection[capacity];
    }

    public MyHashMap(int initialCapacity) {
        this.capacity = initialCapacity;
        this.buckets = (Collection<Node>[]) new Collection[initialCapacity];
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        this.loadFactor = loadFactor;
        this.capacity = initialCapacity;
        this.buckets = (Collection<Node>[]) new Collection[initialCapacity];
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *  Note that that this is referring to the hash table bucket itself,
     *  not the hash map itself.
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        // TODO: Fill in this method.
        return new java.util.LinkedList<>();
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    @Override
    public void put(K key, V value) {
        int index = Math.floorMod(key.hashCode(), capacity);
        float currentLoadFactor = (float) size / capacity;

        // Resize if load factor exceeds threshold
        if (currentLoadFactor > loadFactor) {
            resize();
            index = Math.floorMod(key.hashCode(), capacity); // Recompute index after resize
        }

        // Initialize bucket if null
        if (buckets[index] == null) {
            buckets[index] = createBucket();
        }

        // Check if key exists and update value
        for (Node node : buckets[index]) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
        }

        // Add new node
        buckets[index].add(new Node(key, value));
        size++;
    }

    private void resize() {
        Collection<Node>[] oldBuckets = buckets;
        capacity *= 2;
        buckets = (Collection<Node>[]) new Collection[capacity];
        size = 0;

        for (Collection<Node> bucket : oldBuckets) {
            if (bucket != null) {
                for (Node node : bucket) {
                    put(node.key, node.value);
                }
            }
        }
    }

    @Override
    public V get(K key) {
        int index = Math.floorMod(key.hashCode(), capacity);
        if (buckets[index] == null) {
            return null;
        } else {
            for (Node node : buckets[index]) {
                if (node.key.equals(key)) {
                    return node.value;
                }
            }
            return null;
        }
    }

    @Override
    public boolean containsKey(K key) {
        int index = Math.floorMod(key.hashCode(), capacity);
        if (buckets[index] == null) {
            return false;
        } else {
            for (Node node : buckets[index]) {
                if (node.key.equals(key)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        this.buckets = (Collection<Node>[]) new Collection[capacity];
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new java.util.HashSet<>();
        for (Collection<Node> bucket : buckets) {
            if (bucket != null) {
                for (Node node : bucket) {
                    keySet.add(node.key);
                }
            }
        }
        return keySet;
    }

    @Override
    public V remove(K key) {
        int index = Math.floorMod(key.hashCode(), capacity);
        if (buckets[index] != null) {
            for (Node node : buckets[index]) {
                if (node.key.equals(key)) {
                    buckets[index].remove(node);
                    size--;
                    return node.value;
                }
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

}
