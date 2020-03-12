package rango.tool.androidtool.java;

public class Person {

    private String name;

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public static void log() {
        System.out.println("Person");
    }
}
