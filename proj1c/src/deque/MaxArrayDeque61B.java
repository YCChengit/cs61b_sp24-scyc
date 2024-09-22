package deque;

import java.util.Comparator;

public class MaxArrayDeque61B<T> extends ArrayDeque61B<T>{

    private Comparator<T> comparator;

    public MaxArrayDeque61B(Comparator<T> comparator) {
        super();
        this.comparator = comparator;
    }

    public T max() {
        T max = get(0);
        for (int i = 1; i < size(); i++) {
            if (comparator.compare(get(i), max) > 0) {
                max = get(i);
            }
        }
        return max;
    }

    public T max(Comparator<T> comparator) {
        T max = get(0);
        for (int i = 1; i < size(); i++) {
            if (comparator.compare(get(i), max) > 0) {
                max = get(i);
            }
        }
        return max;
    }
}
