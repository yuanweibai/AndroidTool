package rango.tool.androidtool;

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

    public static void main(String[] args) throws Exception {

        User user = new User();
        user.setName("Child");
        user.setAge(12);

        Spy spy = new Spy();
        spy.changeName(user);
        spy.changeAge(user.getAge());

        System.out.println("name = " + user.getName() + ", age = " + user.getAge());
    }
}
