package rango.tool.androidtool;

import java.util.Arrays;

import rango.tool.androidtool.java.Bob;
import rango.tool.androidtool.java.Person;

public class ActivityThread {


    private static class User {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    private static class Spy {

        private String spyName;
        private int spyAge;

        public void changeName(User user) {
            user.setName("Spy");
            spyName = user.getName();
        }

        public void changeAge(int age) {
            age = 48;
            spyAge = age;
        }
    }

    private static int[] result(int[] nums, int target) {
        int end = nums.length - 1;
        int start = 0;
        boolean isFound = false;
        while (start < nums.length && end >= 0) {
            if (nums[start] == (target - nums[end]) && (start != end)) {
                isFound = true;
                break;
            } else if (nums[start] > (target - nums[end])) {
                end--;
            } else if (nums[start] < (target - nums[end])) {
                start++;
            } else {
                break;
            }
        }

        if (isFound) {
            return new int[]{start, end};
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {

//        int[] array = new int[]{1, 3, 4, 6, 7, 24, 67};
//        int[] array = new int[]{-5, -4, -3, -2, -1};
//        int[] array = new int[]{-5,-1,0,5,9,11,13,15,22,35,46};
//        int[] result = result(array, 8);

        int result = Long.valueOf("1206734247411318784").intValue();


        System.out.println("value = " + result);
    }

}
