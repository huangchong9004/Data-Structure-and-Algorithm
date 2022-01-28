package deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int size;
    private int front;
    private int start = 6;
    private int last;
    int rFactor = 2;

    public ArrayDeque() {
        items = (T[]) new Object[10];
        size = 0;
    }

    public void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        if (front > last) {
            System.arraycopy(items, 0, a, 0, (last + 1));
            for (int i = 1; i < size - last; i++) {
                a[capacity - size + last + i] = items[front + i - 1];
            }
            front = capacity - size + last + 1;
            items = a;
        } else if (front < last) {
            if (capacity > items.length) {
                System.arraycopy(items, front, a, front, size);
                items = a;
            } else if (capacity < items.length) {
                if (front < last) {
                    System.arraycopy(items, front, a, 0, size);
                    front = 0;
                    last = size - 1;
                    items = a;
                } else if (front > last) {
                    System.arraycopy(items, 0, a, 0, (last + 1));
                    for (int i = 1; i < size - last; i++) {
                        a[capacity - size + last + i] = items[front + i - 1];
                    }
                    front = capacity - size + last + 1;
                    items = a;
                }
            }
        }
    }
    public void addFirst(T item) {
        if (this.size == 0) {
            items[start] = item;
            front = start;
            last = start;
            size += 1;
            return;
        } else if (this.size == items.length) {
            resize(size * rFactor);
        }
        if (front == 0) {
            items[items.length - 1] = item;
            front = items.length - 1;
        } else {
            items[front - 1] = item;
            front = front - 1;
        }
        this.size += 1;
    }

    public void addLast(T item) {
        if (this.size == 0) {
            items[start] = item;
            front = start;
            last = start;
            size += 1;
            return;
        } else if (this.size == items.length) {
            resize(size * rFactor);
        }
        if (last == items.length - 1) {
            items[0] = item;
            last = 0;
        } else {
            items[last + 1] = item;
            last = last + 1;
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

    public void printDeque() {
        if (front < last) {
            for (int i = front; i <= last; i++) {
                System.out.print(items[i]);
                System.out.print(" ");
            }
            System.out.println();
        } else {
            for (int i = front; i < items.length; i++) {
                System.out.print(items[i]);
                System.out.print(" ");
            }
            for (int i = 0; i <= last; i++) {
                System.out.print(items[i]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public T removeFirst() {
        if (this.size == 0) {
            return null;
        }
        T temp = items[front];
        float useRatio = ((this.size - 1) * 1.0f) / items.length;
        if (useRatio < 0.25 && items.length >= 16) {
            resize(items.length / 2);
        }
        if (front == items.length - 1) {
            front = 0;
        } else {
            //if (front == items.length - 1)
            front = front + 1;
        }
        this.size -= 1;
        return temp;
    }

    public T removeLast() {
        if (this.size == 0) {
            return null;
        }
        T temp = items[last];
        float useRatio = ((this.size - 1) * 1.0f) / items.length;
        if (useRatio < 0.25 && items.length >= 16) {
            resize(items.length / 2);
        }
        if (last == 0) {
            last = items.length - 1;
        } else {
            last = last - 1;
        }
        this.size -= 1;
        return temp;
    }
    public T get(int index) {
        /**
         if (this.size == 0) {
         return null;
         } else if (front > last && index > last && index < front) {
         return null;
         } else if (front < last && (index < front || index > last)) {
         return null;
         } else {
         return items[index];
         }*/
        if (this.size == 0 || index < 0 || index > this.size) {
            return null;
        } else if (front < last) {
            return items[front + index];
        } else {
            if (front + index < items.length) {
                return items[front + index];
            } else {
                return items[front + index - items.length];
            }
        }
    }
    public Iterator<T> iterator() {
        return new ArrayListIterator();
    }
    public class ArrayListIterator implements Iterator<T> {
        private int index;
        public ArrayListIterator() {
            this.index = 0;
        }
        @Override
        public boolean hasNext() {
            return index < size;
        }
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T returnItem = items[index];
            index += 1;
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
        ArrayDeque other = (ArrayDeque) o;
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
