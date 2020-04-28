package rango.tool.androidtool;

import android.animation.ObjectAnimator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Stack;

import rango.tool.androidtool.java.Person;

public class ActivityThread {

    public static void main(String[] args) throws Exception {

        File file = new File("/Users/yuanwei.bai/Desktop/person.txt");

//        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
//        Person person = new Person();
//        person.setName("Rango");
//        person.setAge(18);
//        outputStream.writeObject(person);
//        outputStream.close();


        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
        Object object = inputStream.readObject();
        inputStream.close();

    }
}
