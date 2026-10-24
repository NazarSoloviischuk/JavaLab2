package org.example;

import org.junit.jupiter.api.Test;

import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MyArrayListTest extends MyListTest {

    @Override
    protected MyList createList() {
        return new MyArrayList();
    }

    @Test
    void implementsRandomAccess() {
        assertTrue(list instanceof RandomAccess);
    }
}