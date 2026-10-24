package org.example;

public class MyLinkedList implements MyList {

    // Вузол списку: сам елемент і посилання на сусідів
    private static class Node {
        Object item;
        Node prev;
        Node next;

        Node(Node prev, Object item, Node next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    private Node first;
    private Node last;
    private int size;

    // Для get, set, remove: індекс від 0 до size - 1
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }
    }

    // Для add: індекс від 0 до size
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }
    }

    // Шукає вузол за індексом, йдучи з того кінця, що ближче
    private Node node(int index) {
        if (index < size / 2) {
            Node x = first;
            for (int i = 0; i < index; i++) {
                x = x.next;
            }
            return x;
        } else {
            Node x = last;
            for (int i = size - 1; i > index; i--) {
                x = x.prev;
            }
            return x;
        }
    }

    // Додає елемент в кінець
    private void linkLast(Object e) {
        Node l = last;
        Node newNode = new Node(l, e, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    // Вставляє елемент перед вузлом succ
    private void linkBefore(Object e, Node succ) {
        Node pred = succ.prev;
        Node newNode = new Node(pred, e, succ);
        succ.prev = newNode;
        if (pred == null) {
            first = newNode;
        } else {
            pred.next = newNode;
        }
        size++;
    }

    // Виключає вузол зі списку і повертає його елемент
    private Object unlink(Node x) {
        Object item = x.item;
        Node prev = x.prev;
        Node next = x.next;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
        return item;
    }

    @Override
    public void add(Object e) {
        linkLast(e);
    }

    @Override
    public void add(int index, Object element) {
        checkIndexForAdd(index);
        if (index == size) {
            linkLast(element);
        } else {
            linkBefore(element, node(index));
        }
    }

    @Override
    public void addAll(Object[] c) {
        for (Object e : c) {
            linkLast(e);
        }
    }

    @Override
    public void addAll(int index, Object[] c) {
        checkIndexForAdd(index);
        if (c.length == 0) {
            return;
        }
        if (index == size) {
            addAll(c);
            return;
        }
        // Усі нові елементи вставляємо перед одним і тим самим вузлом,
        // тому їх порядок зберігається
        Node succ = node(index);
        for (Object e : c) {
            linkBefore(e, succ);
        }
    }

    @Override
    public Object get(int index) {
        checkIndex(index);
        return node(index).item;
    }

    @Override
    public Object remove(int index) {
        checkIndex(index);
        return unlink(node(index));
    }

    @Override
    public void set(int index, Object element) {
        checkIndex(index);
        node(index).item = element;
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        for (Node x = first; x != null; x = x.next) {
            if (o == null ? x.item == null : o.equals(x.item)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node x = first; x != null; x = x.next) {
            result[i++] = x.item;
        }
        return result;
    }
}