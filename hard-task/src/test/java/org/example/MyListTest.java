package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

abstract class MyListTest {

    protected MyList list;

    protected abstract MyList createList();

    @BeforeEach
    void setUp() {
        list = createList();
    }

    @Test
    void newListIsEmpty() {
        assertEquals(0, list.size());
        assertArrayEquals(new Object[0], list.toArray());
    }

    @Test
    void addAppendsToEnd() {
        list.add("a");
        list.add("b");
        assertArrayEquals(new Object[]{"a", "b"}, list.toArray());
    }

    @Test
    void addByIndexInsertsAtStartMiddleAndEnd() {
        list.add("b");
        list.add(0, "a");
        list.add(2, "d");
        list.add(2, "c");
        assertArrayEquals(new Object[]{"a", "b", "c", "d"}, list.toArray());
    }

    @Test
    void addAllAppendsAndInserts() {
        list.add("a");
        list.addAll(new Object[]{"d", "e"});
        list.addAll(1, new Object[]{"b", "c"});
        assertArrayEquals(new Object[]{"a", "b", "c", "d", "e"}, list.toArray());
    }

    @Test
    void getAndSet() {
        list.addAll(new Object[]{"a", "b", "c"});
        list.set(1, "X");
        assertEquals("a", list.get(0));
        assertEquals("X", list.get(1));
        assertEquals("c", list.get(2));
    }

    @Test
    void removeFirstMiddleLast() {
        list.addAll(new Object[]{"a", "b", "c", "d", "e"});
        assertEquals("a", list.remove(0));
        assertEquals("c", list.remove(1));
        assertEquals("e", list.remove(2));
        assertArrayEquals(new Object[]{"b", "d"}, list.toArray());
        assertEquals(2, list.size());
    }

    @Test
    void indexOfFindsFirstOccurrenceAndNull() {
        list.addAll(new Object[]{"a", null, "b", "a"});
        assertEquals(0, list.indexOf("a"));
        assertEquals(1, list.indexOf(null));
        assertEquals(-1, list.indexOf("zzz"));
    }

    @Test
    void worksWithManyElements() {
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }
        assertEquals(1000, list.size());
        assertEquals(0, list.get(0));
        assertEquals(999, list.get(999));
    }

    @Test
    void wrongIndexThrows() {
        list.add("a");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(5, "x"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(3, "x"));
    }
}