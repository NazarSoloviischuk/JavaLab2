package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyLinkedHashSetTest {

    private MyLinkedHashSet<Object> set;

    @BeforeEach
    void setUp() {
        set = new MyLinkedHashSet<>();
    }

    private List<Object> toList() {
        List<Object> result = new ArrayList<>();
        for (Object o : set) {
            result.add(o);
        }
        return result;
    }

    @Test
    void newSetIsEmpty() {
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
    }

    @Test
    void addIgnoresDuplicates() {
        assertTrue(set.add("a"));
        assertFalse(set.add("a"));
        assertEquals(1, set.size());
    }

    @Test
    void keepsInsertionOrder() {
        set.add("c");
        set.add("a");
        set.add("b");
        set.add("a");
        assertEquals(List.of("c", "a", "b"), toList());
    }

    @Test
    void containsFindsElements() {
        set.add("x");
        assertTrue(set.contains("x"));
        assertFalse(set.contains("y"));
    }

    @Test
    void removeKeepsOrderOfOthers() {
        set.add("a");
        set.add("b");
        set.add("c");
        assertTrue(set.remove("b"));
        assertFalse(set.remove("b"));
        assertEquals(List.of("a", "c"), toList());
        assertEquals(2, set.size());
    }

    @Test
    void readdedElementGoesToEnd() {
        set.add("a");
        set.add("b");
        set.remove("a");
        set.add("a");
        assertEquals(List.of("b", "a"), toList());
    }

    @Test
    void acceptsNull() {
        set.add("a");
        set.add(null);
        set.add(null);
        assertTrue(set.contains(null));
        assertEquals(2, set.size());
        assertEquals(Arrays.asList("a", null), toList());
    }

    @Test
    void survivesResize() {
        for (int i = 0; i < 1000; i++) {
            set.add(i);
        }
        assertEquals(1000, set.size());
        assertTrue(set.contains(0));
        assertTrue(set.contains(999));
        assertFalse(set.contains(1000));
        assertEquals(0, toList().get(0));
        assertEquals(999, toList().get(999));
    }

    @Test
    void clearEmptiesSet() {
        set.add("a");
        set.add("b");
        set.clear();
        assertTrue(set.isEmpty());
        assertFalse(set.contains("a"));
        assertEquals(List.of(), toList());
    }
}