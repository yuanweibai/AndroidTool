package com.rango.wanjava;

import java.security.MessageDigest;

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
        int[] array = new int[]{10, 9, 3, 90, 87, 3232, 0, 3, 10, 4, 87, 13, 34};
//        quicklySort(array, 0, array.length - 1);
        insertBinarySort(array);
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }

    private static void insertSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int standard = array[i];
            int k = i - 1;
            while (k >= 0 && standard < array[k]) {
                array[k + 1] = array[k];
                k--;
            }
            array[k + 1] = standard;
        }
    }

    private static void insertBinarySort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int standard = array[i];
            int low = 0;
            int high = i - 1;
            int middle;
            while (low <= high) {
                middle = (low + high) / 2;
                if (standard < array[middle]) {
                    high = middle - 1;
                } else {
                    low = middle + 1;
                }
            }
            int k = i;
            while (k > low) {
                array[k] = array[k - 1];
                k--;
            }
            array[k] = standard;
        }
    }


    private static void quicklySort(int[] array, int start, int end) {
        if (start >= end) {
            return;
        }

        int index = start;
        int standard = array[index];
        int left = index;
        int right = end;
        while (left < right) {

            while (array[right] >= standard && left < right) {
                right--;
            }
            array[index] = array[right];
            index = right;

            while (array[left] <= standard && left < right) {
                left++;
            }
            if (left < right) {
                array[index] = array[left];
                index = left;
            }
        }

        array[index] = standard;
        quicklySort(array, start, index - 1);
        quicklySort(array, index + 1, end);
    }

    private static void bubbleSort(int[] array) {
        for (int i = array.length - 1; i >= 0; i--) {
            for (int k = array.length - 1; k > 0; k--) {
                if (array[k] > array[k - 1]) {
                    int temp = array[k];
                    array[k] = array[k - 1];
                    array[k - 1] = temp;
                }
            }
        }
    }
}