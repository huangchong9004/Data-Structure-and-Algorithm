package deque;
import net.sf.saxon.expr.Component;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    Comparator init;
    public MaxArrayDeque(Comparator<T> c) {
        this.init = c;
        //super();
    }
    public T max() {
        if (super.size() == 0) {
            return null;
        }
        int index = 0;
        T maxItem = super.get(index);
        while (index < super.size()) {
            if (init.compare(maxItem, super.get(index)) < 0) {
                maxItem = super.get(index);
            }
            index += 1;
        }
        return maxItem;
    }
    public T max(Comparator<T> c) {
        if (super.size() == 0) {
            return null;
        }
        int index = 0;
        T maxItem = super.get(index);
        while (index < super.size()) {
            if (c.compare(maxItem, super.get(index)) < 0) {
                maxItem = super.get(index);
            }
            index += 1;
        }
        return maxItem;
    }
    public int compare(T a, T b, Comparator<T> c) {
        return c.compare(a,b);
    }

}
