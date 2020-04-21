package rango.tool.androidtool;

import java.util.Arrays;
import java.util.Stack;

import rango.tool.androidtool.java.Bob;
import rango.tool.androidtool.java.Person;

public class ActivityThread {

    public static class MyQueue {

        private Stack<Integer> inStack;
        private Stack<Integer> outStack;

        public MyQueue() {
            inStack = new Stack<>();
            outStack = new Stack<>();
        }

        public int pop() {
            if (outStack.isEmpty()) {
                while (!inStack.isEmpty()) {
                    outStack.push(inStack.pop());
                }
            }
            return outStack.pop();
        }

        public void add(int value) {
            inStack.push(value);
        }

        public int size() {
            return inStack.size() + outStack.size();
        }

        public boolean isEmpty() {
            return inStack.isEmpty() && outStack.isEmpty();
        }
    }

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

        MyQueue queue = new MyQueue();

        for (int i = 9; i > 0; i--) {
            queue.add(i);
        }

        while (!queue.isEmpty()) {
            System.out.println("value = " + queue.pop());
        }
    }

}
