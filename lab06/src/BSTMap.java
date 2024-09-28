import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap <K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        private K key;
        private V value;
        private Node left;
        private Node right;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    private Node root = null;
    private int size = 0;

    @Override
    public void put(K key, V value) {
        if (root == null) {
            root = new Node(key, value);
        } else {
            Node p = root;
            while (true) {
                if (key.compareTo(p.key) < 0) {
                    if (p.left == null) {
                        p.left = new Node(key, value);
                        break;
                    } else {
                        p = p.left;
                    }
                } else if (key.compareTo(p.key) > 0) {
                    if (p.right == null) {
                        p.right = new Node(key, value);
                        break;
                    } else {
                        p = p.right;
                    }
                } else {
                    p.value = value;
                    return;
                }
            }
        }
        this.size++;
    }

    @Override
    public V get(K key) {
        Node p = root;
        while (p != null) {
            if (key.compareTo(p.key) < 0) {
                p = p.left;
            } else if (key.compareTo(p.key) > 0) {
                p = p.right;
            } else {
                return p.value;
            }
        }
    return null;
    }

    @Override
    public boolean containsKey(K key) {
        Node p = root;
        while (p != null) {
            if (key.compareTo(p.key) < 0) {
                p = p.left;
            } else if (key.compareTo(p.key) > 0) {
                p = p.right;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public Set<K> keySet() {
        return keySet_helper(root);
    }

    private Set<K> keySet_helper(Node p) {
        Set<K> keySet = new HashSet<K>();
        if (p == null) {
            return keySet;
        } else {
            keySet.add(p.key);
            keySet.addAll(keySet_helper(p.left));
            keySet.addAll(keySet_helper(p.right));
        }
        return keySet;
    }

    @Override
    public V remove(K key) {

        if (!containsKey(key)) {
            return null;
        }

        Node p = root;
        Node parent = null;
        V value = null;
        while (p != null) {
            if (key.compareTo(p.key) < 0) {
                parent = p;
                p = p.left;
            } else if (key.compareTo(p.key) > 0) {
                parent = p;
                p = p.right;
            } else {
                break;
            }
        }
        value = p.value;

        if (p.left != null && p.right != null) {
            Node q = p.right;
            Node parent_q = p;
            while (q.left != null) {
                parent_q = q;
                q = q.left;
            }
            p.key = q.key;
            p.value = q.value;
            if (parent_q == p) {
                parent_q.right = q.right;
            } else {
                parent_q.left = q.right;
            }
        } else {
            Node child = p.left != null ? p.left : p.right;
            if (p == root) {
                root = child;
            } else {
                if (p == parent.left) {
                    parent.left = child;
                } else {
                    parent.right = child;
                }
            }
        }

        this.size--;
        return value;
    }


    @Override
    public Iterator<K> iterator() {
        return null;
    }

    private class BSTMapIterator implements Iterator<K> {

        Node p = root;

        @Override
        public boolean hasNext() {
            return p != null;
        }

        @Override
        public K next() {
            return p.key;
        }
    }


}
