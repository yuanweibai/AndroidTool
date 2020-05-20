package rango.tool.androidtool;

import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

import rango.tool.androidtool.java.Person;

public class ActivityThread {



    private static int mFlags;

    public static void main(String[] args) throws Exception {




    }

    static void setFlags(int flags, int mask) {
        mFlags = (mFlags & ~mask) | (flags & mask);
    }
}
