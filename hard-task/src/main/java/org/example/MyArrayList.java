package org.example;

import java.util.RandomAccess;

public class MyArrayList implements MyList, RandomAccess {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] data;
    private int size;

    public MyArrayList() {
        data = new Object[DEFAULT_CAPACITY];
    }

    // Якщо масив замалий, створюємо більший і копіюємо туди елементи
    private void ensureCapacity(int needed) {
        if (needed > data.length) {
            int newCapacity = Math.max(needed, data.length * 3 / 2 + 1);
            Object[] bigger = new Object[newCapacity];
            System.arraycopy(data, 0, bigger, 0, size);
            data = bigger;
        }
    }

    // Для get, set, remove: індекс має бути від 0 до size - 1
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }
    }

    // Для add: можна вставляти і в кінець, тобто індекс від 0 до size
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }
    }

    @Override
    public void add(Object e) {
        ensureCapacity(size + 1);
        data[size++] = e;
    }

    @Override
    public void add(int index, Object element) {
        checkIndexForAdd(index);
        ensureCapacity(size + 1);
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }

    @Override
    public void addAll(Object[] c) {
        addAll(size, c);
    }

    @Override
    public void addAll(int index, Object[] c) {
        checkIndexForAdd(index);
        if (c.length == 0) {
            return;
        }
        ensureCapacity(size + c.length);
        System.arraycopy(data, index, data, index + c.length, size - index);
        System.arraycopy(c, 0, data, index, c.length);
        size += c.length;
    }

    @Override
    public Object get(int index) {
        checkIndex(index);
        return data[index];
    }

    @Override
    public Object remove(int index) {
        checkIndex(index);
        Object old = data[index];
        int moved = size - index - 1;
        if (moved > 0) {
            System.arraycopy(data, index + 1, data, index, moved);
        }
        data[--size] = null;
        return old;
    }

    @Override
    public void set(int index, Object element) {
        checkIndex(index);
        data[index] = element;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? data[i] == null : o.equals(data[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Object[] toArray() {
        Object[] copy = new Object[size];
        System.arraycopy(data, 0, copy, 0, size);
        return copy;
    }
}