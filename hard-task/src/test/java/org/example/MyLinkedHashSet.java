package org.example;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class MyLinkedHashSet<E> implements Iterable<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    // Запис у множині: значення, хеш і три посилання
    private static class Entry<E> {
        final E value;
        final int hash;
        Entry<E> nextInBucket; // наступний запис у цьому ж кошику
        Entry<E> before;       // попередній за порядком додавання
        Entry<E> after;        // наступний за порядком додавання

        Entry(E value, int hash) {
            this.value = value;
            this.hash = hash;
        }
    }

    private Entry<E>[] table;
    private Entry<E> head;
    private Entry<E> tail;
    private int size;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Entry<E>[]) new Entry[DEFAULT_CAPACITY];
    }

    // Перемішуємо біти хешу, щоб кошики заповнювалися рівномірніше
    private static int hash(Object o) {
        if (o == null) {
            return 0;
        }
        int h = o.hashCode();
        return h ^ (h >>> 16);
    }

    // Довжина таблиці завжди степінь двійки, тому індекс = хеш & (довжина - 1)
    private static int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private Entry<E> find(Object o) {
        int h = hash(o);
        for (Entry<E> x = table[indexFor(h, table.length)]; x != null; x = x.nextInBucket) {
            if (x.hash == h && Objects.equals(x.value, o)) {
                return x;
            }
        }
        return null;
    }

    public boolean contains(Object o) {
        return find(o) != null;
    }

    public boolean add(E e) {
        if (find(e) != null) {
            return false;
        }
        int h = hash(e);
        int index = indexFor(h, table.length);

        Entry<E> entry = new Entry<>(e, h);
        entry.nextInBucket = table[index];
        table[index] = entry;
        linkLast(entry);
        size++;

        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
        return true;
    }

    // Додає запис у кінець списку порядку
    private void linkLast(Entry<E> entry) {
        if (tail == null) {
            head = entry;
        } else {
            tail.after = entry;
            entry.before = tail;
        }
        tail = entry;
    }

    // Збільшує таблицю вдвічі і заново розкладає записи по кошиках
    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<E>[] bigger = (Entry<E>[]) new Entry[table.length * 2];
        for (Entry<E> e = head; e != null; e = e.after) {
            int index = indexFor(e.hash, bigger.length);
            e.nextInBucket = bigger[index];
            bigger[index] = e;
        }
        table = bigger;
    }

    public boolean remove(Object o) {
        int h = hash(o);
        int index = indexFor(h, table.length);

        Entry<E> prev = null;
        for (Entry<E> x = table[index]; x != null; prev = x, x = x.nextInBucket) {
            if (x.hash == h && Objects.equals(x.value, o)) {
                if (prev == null) {
                    table[index] = x.nextInBucket;
                } else {
                    prev.nextInBucket = x.nextInBucket;
                }
                unlink(x);
                size--;
                return true;
            }
        }
        return false;
    }

    // Виключає запис зі списку порядку
    private void unlink(Entry<E> x) {
        if (x.before == null) {
            head = x.after;
        } else {
            x.before.after = x.after;
        }
        if (x.after == null) {
            tail = x.before;
        } else {
            x.after.before = x.before;
        }
        x.before = null;
        x.after = null;
        x.nextInBucket = null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        Arrays.fill(table, null);
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private Entry<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                E value = current.value;
                current = current.after;
                return value;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Entry<E> x = head; x != null; x = x.after) {
            if (x != head) {
                sb.append(", ");
            }
            sb.append(x.value);
        }
        return sb.append("]").toString();
    }
}