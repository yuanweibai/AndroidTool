package rango.tool.androidtool;

import rango.tool.androidtool.java.Bob;
import rango.tool.androidtool.java.Person;

public class ActivityThread {

    public static void main(String[] args) throws Exception {
        Bob.log();
        Person.log();

        Bob bob = new Person();
    }
}
