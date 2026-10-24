package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.LongSupplier;

public class MyCache<K, V> {

    // Запис кешу: значення, час, коли вона застаріє, і посилання на сусідів у списку
    private static class Node<K, V> {
        final K key;
        V value;
        long expiresAt;
        Node<K, V> prev;
        Node<K, V> next;

        Node(K key, V value, long expiresAt) {
            this.key = key;
            this.value = value;
            this.expiresAt = expiresAt;
        }
    }

    private final int capacity;
    private final long ttlMillis;
    private final LongSupplier clock;
    private final Map<K, Node<K, V>> map = new HashMap<>();

    private Node<K, V> head; // найнедавніше використаний запис
    private Node<K, V> tail; // найдавніше використаний запис

    // ttlMillis <= 0 означає, що записи не застарівають
    public MyCache(int capacity, long ttlMillis) {
        this(capacity, ttlMillis, System::currentTimeMillis);
    }

    // Окремий конструктор з годинником потрібен для тестів
    MyCache(int capacity, long ttlMillis, LongSupplier clock) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
        this.ttlMillis = ttlMillis;
        this.clock = clock;
    }

    public void put(K key, V value) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");

        long expiresAt = ttlMillis > 0 ? clock.getAsLong() + ttlMillis : 0;
        Node<K, V> node = map.get(key);

        if (node != null) {
            node.value = value;
            node.expiresAt = expiresAt;
            unlink(node);
            addFirst(node);
            return;
        }

        // Місця немає: спочатку викидаємо застарілі записи,
        // і тільки якщо цього мало, вилучаємо найдавніший за використанням
        if (map.size() >= capacity) {
            purgeExpired();
        }
        if (map.size() >= capacity) {
            removeNode(tail);
        }

        node = new Node<>(key, value, expiresAt);
        map.put(key, node);
        addFirst(node);
    }

    public V get(K key) {
        Objects.requireNonNull(key, "key");

        Node<K, V> node = map.get(key);
        if (node == null) {
            return null;
        }
        if (isExpired(node)) {
            removeNode(node);
            return null;
        }
        unlink(node);
        addFirst(node);
        return node.value;
    }

    public boolean containsKey(K key) {
        Objects.requireNonNull(key, "key");

        Node<K, V> node = map.get(key);
        if (node == null) {
            return false;
        }
        if (isExpired(node)) {
            removeNode(node);
            return false;
        }
        return true;
    }

    public V remove(K key) {
        Objects.requireNonNull(key, "key");

        Node<K, V> node = map.get(key);
        if (node == null) {
            return null;
        }
        removeNode(node);
        return isExpired(node) ? null : node.value;
    }

    public int size() {
        purgeExpired();
        return map.size();
    }

    public void clear() {
        map.clear();
        head = null;
        tail = null;
    }

    private boolean isExpired(Node<K, V> node) {
        return ttlMillis > 0 && clock.getAsLong() >= node.expiresAt;
    }

    // Проходить по всьому списку і видаляє застарілі записи
    private void purgeExpired() {
        if (ttlMillis <= 0) {
            return;
        }
        Node<K, V> x = head;
        while (x != null) {
            Node<K, V> next = x.next;
            if (isExpired(x)) {
                removeNode(x);
            }
            x = next;
        }
    }

    private void removeNode(Node<K, V> node) {
        unlink(node);
        map.remove(node.key);
    }

    // Виключає вузол зі списку
    private void unlink(Node<K, V> n) {
        if (n.prev == null) {
            head = n.next;
        } else {
            n.prev.next = n.next;
        }
        if (n.next == null) {
            tail = n.prev;
        } else {
            n.next.prev = n.prev;
        }
        n.prev = null;
        n.next = null;
    }

    // Ставить вузол на початок списку
    private void addFirst(Node<K, V> n) {
        n.next = head;
        if (head != null) {
            head.prev = n;
        }
        head = n;
        if (tail == null) {
            tail = n;
        }
    }
}