package lab05;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * TODO class declaration
 * We want to accept any type which is comparable to itself
 *
 * @param <T>
 */
public class Heap<T extends Comparable<T>> {
    private ArrayList<T> container;

    public Heap() {
        this.container = new ArrayList<>();
    }

    /**
     * @return If size is 0, throw {@link IllegalStateException}.
     *         Otherwise, return the first element of {@link this#container}
     */
    public T peek() throws IllegalStateException {
        // REVIEW
        if (this.container.size() == 0) {
            throw new IllegalStateException();
        }
        return this.container.get(0);
        // return null; // replace this line with implementation
    }

    /**
     *
     * @return If size is 0, throw {@link IllegalStateException}. Otherwise,
     *         temporarily save the first element.
     *         Afterwards, set the first position to the last element, and remove
     *         the last element.
     *         Call {@link this#heapifyDown()}, then return the original first
     *         element
     */
    public T poll() throws IllegalStateException {
        // REVIEW
        int containerSize = this.container.size();
        if (containerSize == 0) {
            throw new IllegalStateException();
        }
        T firstElement = this.container.get(0);
        this.container.set(0, container.get(containerSize - 1));
        this.container.remove(containerSize - 1);
        this.heapifyDown();
        return firstElement;
        // return null; // replace this line with implementation
    }

    private void heapifyDown() {
        int pos = 0;
        while (hasLeft(pos)) {
            int smallerChildIndex = getLeftIndex(pos);
            if (hasRight(pos) && container.get(getRightIndex(pos)).compareTo(container.get(getLeftIndex(pos))) < 0) {
                smallerChildIndex = getRightIndex(pos);
            }
            if (container.get(pos).compareTo(container.get(smallerChildIndex)) < 0) {
                break;
            } else {
                swap(pos, smallerChildIndex);
            }
            pos = smallerChildIndex;
        }
    }

    /**
     * Add the object into {@link this#container}, then call
     * {@link this#heapifyUp()}
     *
     * @param obj the object to add
     */
    public void add(T obj) {
        // REVIEW
        this.container.add(obj);
        this.heapifyUp();
    }

    public void addAll(Collection<T> list) {
        list.forEach(this::add);
    }

    /**
     * While the last element has a parent and is smaller than its parent, swap the
     * two elements. Then, check again
     * with the new parent until there's either no parent or we're larger than our
     * parent.
     */
    private void heapifyUp() {
        // REVIEW
        int idxCurrLastElement = this.container.size() - 1;
        int idxParent = this.getParentIndex(idxCurrLastElement);
        while (this.hasParent(idxCurrLastElement)
                && (this.container.get(idxCurrLastElement)).compareTo(this.container.get(idxParent)) == -1) {
            this.swap(idxCurrLastElement, idxParent);
            idxCurrLastElement = idxParent;
            idxParent = this.getParentIndex(idxCurrLastElement);
        }
    }

    public int size() {
        return container.size();
    }

    private int getParentIndex(int i) {
        return (i - 1) / 2;
    }

    private int getLeftIndex(int i) {
        return 2 * i + 1;
    }

    private int getRightIndex(int i) {
        return 2 * i + 2;
    }

    private boolean hasParent(int i) {
        return getParentIndex(i) >= 0;
    }

    private boolean hasLeft(int i) {
        return getLeftIndex(i) < container.size();
    }

    private boolean hasRight(int i) {
        return getRightIndex(i) < container.size();
    }

    private void swap(int i1, int i2) {
        Collections.swap(container, i1, i2);
    }
}
