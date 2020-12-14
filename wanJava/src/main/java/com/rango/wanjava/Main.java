package com.rango.wanjava;

public class Main {

    public static class Data {

        private int value;

        public Data(int value) {
            this.value = value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static void main(String[] args) {
        String str = "12345u889896";
        String value = str.substring(0, 5);
        System.out.println("value = " + value);
    }

    private static void changeData(Data data) {
        data = null;
    }
}