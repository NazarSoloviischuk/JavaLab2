package org.example;

import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();

        Person original = new Person("Соловійщук", "Назар", 20);
        System.out.println("Початковий об'єкт: " + original);

        String json = gson.toJson(original);
        System.out.println("JSON: " + json);

        Person restored = gson.fromJson(json, Person.class);
        System.out.println("Відновлений об'єкт: " + restored);

        System.out.println("equals: " + original.equals(restored));
    }
}
