package org.example;

import java.util.Objects;

public final class Person {

    private String surname;
    private String name;
    private int age;

    public Person(String surname, String name, int age) {
        this.surname = surname;
        this.name = name;
        this.age = age;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        Person other = (Person) o;
        return age == other.age
                && Objects.equals(surname, other.surname)
                && Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, name, age);
    }

    @Override
    public String toString() {
        return surname + " " + name + ", " + age;
    }
}