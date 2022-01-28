package hashmap;

import java.util.*;
import java.util.Iterator;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
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
    private int n;
    private int m;
    private int initialSize = 16;
    private double loadFactor = 0.75;
    private int resizeFactor = 2;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this.m = this.initialSize;
        buckets = createTable(m);
    }
    public MyHashMap(int initialSize) {
        this.m = initialSize;
        buckets = createTable(m);
    }
    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.m = initialSize;
        this.loadFactor = maxLoad;
        buckets = createTable(m);
    }
     /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        Node temp = new Node(key, value);
        return temp;
    }
    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
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
        return new LinkedList<>();
    }
    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node> [] temp = (Collection<Node> []) new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            temp[i] = createBucket();
        }
        return temp;
    }
    // Your code won't compile until you do so!
    public void clear() {
        n = 0;
        for (int i = 0; i < m; i++) {
            buckets[i] = null;
        }
    }
    public boolean containsKey(K key) {
        return get(key) != null;
    }
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("null key is not supported");
        }
        Node temp = getNode(key);
        if (temp == null) {
            return null;
        }
        return temp.value;
    }
    private Node getNode(K key) {
        int i = hash(key);
        if (buckets == null || buckets[i] == null) {
            return null;
        }
        for (Node node : buckets[i]) {
            if (key.equals(node.key)) {
                return node;
            }
        }
        return null;
    }
    public int size() {
        return n;
    }
    public void resize(int newSize) {
        MyHashMap<K, V> temp = new MyHashMap<K, V>(newSize);
        for (int i = 0; i < m; i++) {
            for (Node node : buckets[i]) {
                temp.put(node.key, node.value);
            }
        }
        this.m = temp.m;
        this.n = temp.n;
        this.buckets = temp.buckets;
    }
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Null key is not supported");
        }
        if (value == null) {
            remove(key);
        }
        Node temp = new Node(key, value);
        Node oldNode = getNode(key);
        if (oldNode != null) {
            oldNode.value = value;
        } else {
            if ((double) n/m > loadFactor) {
                resize(resizeFactor * m);
            }
            int i = hash(key);
            buckets[i].add(temp);
            n += 1;
        }
    }
    public Set<K> keySet() {
        Set<K> returnSet = new HashSet<>();
        for (int i = 0; i < m; i++) {
            for (Node node : buckets[i]) {
                returnSet.add(node.key);
            }
        }
        return returnSet;
    }
    public V remove(K key) {
        int i = hash(key);
        Node temp = getNode(key);
        if (temp == null) {
            throw new NoSuchElementException(key + " does not exist");
        } else {
            buckets[i].remove(temp);
        }
        return temp.value;
    }
    public V remove(K key, V value) {
        int i = hash(key);
        Node temp = getNode(key);
        if (temp == null) {
            throw new NoSuchElementException(key + " does not exist");
        } else if (temp.value == value) {
            buckets[i].remove(temp);
            return temp.value;
        }
        return null;
    }
    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % m;
    }
    public Iterator<K> iterator() {
        //return new HashMapIterator();
        return keySet().iterator();
    }
    /**
    public class HashMapIterator implements Iterator<K> {
        @Override
        public boolean hasNext() {
            int pos = 0;
            return pos < size();
        }
        @Override
        public K next() {
            if (hasNext()) {
                Node temp = this.keySet().next();
                return temp.key;
            }
            return null;
        }

    }*/
}
