package rango.tool.androidtool.java;

import rango.tool.androidtool.java.Person;

public class Bob extends Person {

    public static void log() {
        System.out.println("Bob");
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    void setName(String name) {
        super.setName(name);
    }
}
