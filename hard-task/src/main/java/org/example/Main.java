package org.example;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        MyList list = new MyLinkedList();
        list.add("a");
        list.add("b");
        list.add(1, "x");
        list.addAll(new Object[]{"p", "q"});
        System.out.println(Arrays.toString(list.toArray()));

        list.remove(0);
        list.set(0, "Z");
        System.out.println(Arrays.toString(list.toArray()));
        System.out.println("indexOf(q) = " + list.indexOf("q") + ", size = " + list.size());
    }
}