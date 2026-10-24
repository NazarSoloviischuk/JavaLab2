package org.example;

class MyLinkedListTest extends MyListTest {

    @Override
    protected MyList createList() {
        return new MyLinkedList();
    }
}