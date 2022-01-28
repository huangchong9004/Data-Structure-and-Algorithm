package bstmap;

import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{
    public BSTNode root;
    private class BSTNode {
        private V val;
        private K key;
        private BSTNode left;
        private BSTNode right;
        private int size;
        public BSTNode(K key, V val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }
    public BSTMap() {
        //this.root = null;
    }
    public void clear() {
        this.root = null;
    }
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        return getKey(key) != null;
    }
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        return getKeyVal(key, this.root);
    }
    private BSTNode getKeyNode(K key, BSTNode x) {
        if (x == null) {
            return null;
        } else {
            int cmp = key.compareTo(x.key);
            if (cmp > 0) {
                return getKeyNode(key, x.right);
            } else if (cmp < 0) {
                return getKeyNode(key, x.left);
            }
            return x;
        }
    }
    private V getKeyVal(K key, BSTNode x) {
        if (x == null) {
            return null;
        }
        if (getKeyNode(key, x) == null) {
            return null;
        }
        return getKeyNode(key, x).val;
    }
    private K getKey(K key) {
        return getKeyKey(key, this.root);
    }
    private K getKeyKey(K key, BSTNode x) {
        if (x == null) {
            return null;
        }
        if (getKeyNode(key, x) == null) {return null;}
        return getKeyNode(key, x).key;
    }
    public int size(){
        if (this.root == null) {return 0;}
        return this.root.size;
    }
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        putKey(key, value);
    }
    private void putKey(K key, V value) {
        this.root = putKeyIn(key, value, this.root);
    }
    private BSTNode putKeyIn(K key, V value, BSTNode x) {
        if (x == null) {
            return new BSTNode(key, value, 1);
        }
        int cmp = key.compareTo(x.key);
        x.size += 1;
        if (cmp > 0) {
            x.right = putKeyIn(key, value, x.right);
        } else if (cmp < 0) {
            x.left = putKeyIn(key, value, x.left);
        } else {
            x.val = value;
        }
        //root.size = root.left.size + root.right.size;
        return x;
    }
    public Set<K> keySet() {
        //return getKeySet(this.root);


        throw new UnsupportedOperationException();
        //return Set;
    }

    private Set<K> getKeySet(BSTNode x) {
        Set<K> returnSet = new HashSet<>();
        if (x != null) {
            returnSet.add(x.key);
            //returnSet.add();
        }
        return returnSet;
    }
    private BSTNode smallest(BSTNode x) {
        if (x == null) {
            return null;
        } else if (x.left == null) {
            return x;
        } else {
            return smallest(x.left);
        }
    }
    private boolean isLeaf(BSTNode x) {
        return (x.left == null) && (x.right == null);
    }
    private BSTNode largest(BSTNode x) {
        if (x == null) {
            return null;
        } else if (x.right == null) {
            return x;
        } else {
            return largest(x.right);
        }
    }
    private void removeKey(K key) {
        this.root = removeKeyNode(key, this.root);
    }
    private BSTNode removeKeyNode(K key, BSTNode x) {
        if (x == null) { throw new NoSuchElementException();
        }
        int cmp = key.compareTo(x.key);
        x.size -= 1;
        if (cmp == 0) {
            if (x.left == null ) {
                return x.right;
            }
            if (x.right == null ) {
                return x.left;
            }
            BSTNode BackUpNode = largest(x.left);
            x.key = BackUpNode.key;
            x.val = BackUpNode.val;
            //root.size = sizeBackup;
            x.left = deleteMax(x.left);

        } else if (cmp < 0) {
            x.left = removeKeyNode(key, x.left);
        } else {
            x.right = removeKeyNode(key, x.right);
        }
        return x;
    }

    private BSTNode deleteMax(BSTNode x) {
        if (x.right == null) {
            x = x.left;
        } else {
            x.right = deleteMax(x.right);
            x.size -= 1;
        }
        return x;
    }
    private BSTNode deleteMin(BSTNode x) {
        if (x.left == null) {
            x = x.right;
        } else {
            x.left = deleteMax(x.left);
            x.size -= 1;
        }
        return x;
    }
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (!containsKey(key)) {
            throw new NoSuchElementException("no such key in the map");
        }
        V temp = getKeyVal(key, this.root);
        removeKey(key);
        return temp;
    }
    public V remove(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (!containsKey(key)) {
            throw new NoSuchElementException("no such key in the map");
        }
        V temp = getKeyVal(key, this.root);
        if (temp.equals(value)) {
            removeKey(key);
            return temp;
        }
        return null;
    }
    public Iterator<K> iterator() {
        return new BSTIterator();
    }
    public class BSTIterator implements Iterator<K> {
        public BSTNode root;
        @Override
        public boolean hasNext() {
            return true;
        }
        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return root.key;
        }
    }
    public void toKey(K key) {
        BSTNode temp = smallestToKey(key, this.root);
        if (temp != null) {
            System.out.println(temp.key);
        } else {throw new NoSuchElementException("no such element exist in BST");};
    }
    public BSTNode smallestToKey(K key, BSTNode x) {
        if (x == null) {
            return null;
        }
        if (key.compareTo(largest(x).key) >= 0) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp == 0) {
            if (!isLeaf(x)) {
                return smallest(x.right);
            } else {
                return null;
            }
        } else if (cmp < 0) {
            BSTNode temp = smallestToKey(key, x.left);
            if (temp != null) {
                return temp;
            } else {
                return x;
            }
        } else if (cmp > 0) {
            BSTNode temp = smallestToKey(key, x.right);
            if (temp != null) {
                return temp;
            } else return x;
        }
        throw new IllegalArgumentException();
    }
    public void printInOrder() {
        inOrder(this.root);

    }
    private void inOrder(BSTNode x) {
        if (x != null) {
            inOrder(x.left);
            System.out.println(x.key);
            inOrder(x.right);
        } else {
            return;
        }
    }
    public void printPreOrder() {
        preOrder(this.root);
    }
    private void preOrder(BSTNode x) {
        if (x != null) {
            System.out.println(x.key);
            preOrder(x.left);
            preOrder(x.right);
        } else {
            return;
        }
    }
    public void printPostOrder() {
        postOrder(this.root);
    }
    private void postOrder(BSTNode x) {
        if (x != null) {
            postOrder(x.left);
            postOrder(x.right);
            System.out.println(x.key);
        } else {
            return;
        }

    }
}
