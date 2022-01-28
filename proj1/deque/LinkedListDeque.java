package deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListDeque <T> implements Deque<T> {
    public class IntNode {
        public T item;
        IntNode next;
        IntNode prev;
        public IntNode(T item, IntNode next, IntNode prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }
    private IntNode sentinel;
    private int size;
    public void addFirst(T item) {
        if (this.size == 0) {
            this.sentinel = new IntNode(item, null, null);
            this.sentinel.next = new IntNode(item,sentinel,sentinel);
            this.sentinel.prev = this.sentinel.next;
        } else {
            IntNode oldFront = this.sentinel.next;
            IntNode newNode = new IntNode(item, oldFront, this.sentinel);
            this.sentinel.next = newNode;
            oldFront.prev = newNode;
        }
        this.size += 1;
    }
    public void addLast(T item) {
        if (this.size == 0) {
            this.sentinel = new IntNode(item, null, null);
            this.sentinel.next = new IntNode(item,sentinel,sentinel);
            this.sentinel.prev = this.sentinel.next;

        } else {
            IntNode oldLast = this.sentinel.prev;
            IntNode newNode = new IntNode(item, this.sentinel, oldLast);
            oldLast.next = newNode;
            this.sentinel.prev = newNode;
        }
        this.size += 1;
    }
    /*
    public boolean isEmpty() {
        return (this.size == 0);
    }*/
    public int size() {
        return this.size;
    }
    public T removeFirst() {
        if (this.sentinel == null || this.sentinel.next == null) {
            return null;
        }
        this.size -= 1;
        T temp = this.sentinel.next.item;
        this.sentinel.next.prev = null;
        IntNode backup = this.sentinel.next.next;
        this.sentinel.next.next = null;
        this.sentinel.next = backup;
        backup.prev = this.sentinel;
        return temp;
    }
    public T removeLast() {
        if (this.sentinel == null || this.sentinel.prev == null) {
            return null;
        }
        this.size -= 1;
        T temp = this.sentinel.prev.item;
        this.sentinel.prev.next = null;
        IntNode backup = this.sentinel.prev.prev;
        this.sentinel.prev.prev = null;
        this.sentinel.prev = backup;
        backup.next = this.sentinel;
        return temp;
    }
    public T get(int index) {
        if (index > this.size - 1 || index < 0) {
            return null;
        }
        IntNode p = this.sentinel.next;
        while (index > 0) {
            p = p.next;
            index -= 1;
        }
        return p.item;
    }
    public void printDeque() {
        IntNode p = this.sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item);
            System.out.print(" ");
            p = p.next;
        }
        System.out.println();
    }
    public Iterator<T> iterator() {
        return new LinkedListIterator(this.sentinel.next);
    }
    public class LinkedListIterator implements Iterator<T> {
        private IntNode p;
        public LinkedListIterator(IntNode p) {
            this.p = p;
        }
        @Override
        public boolean hasNext() {
            while (p.next != sentinel) {
                return true;
            }
            return false;
        }
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T returnItem = p.item;
            p = p.next;
            return returnItem;
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (this.getClass() != o.getClass()) {
            return false;
        } else if (o == null) {
            return false;
        }
        LinkedListDeque other = (LinkedListDeque) o;
        if (this.size() != other.size()) {
            return false;
        }
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) != other.get(i)) {
                return false;
            }
        }
        return true;
    }
}
