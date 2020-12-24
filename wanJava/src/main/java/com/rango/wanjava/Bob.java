package com.rango.wanjava;

public class Bob extends Person {

    public Bob() {
        System.out.println("4");
    }

    {
        System.out.println("5");
    }

    static {
        System.out.println("6");
    }
}
