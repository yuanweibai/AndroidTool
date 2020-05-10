package rango.tool.androidtool;

import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Stack;

import rango.tool.androidtool.java.Person;

public class ActivityThread {

    static final int FLAG_BOUND = 1 << 0;

    /**
     * The data this ViewHolder's view reflects is stale and needs to be rebound
     * by the adapter. mPosition and mItemId are consistent.
     */
    static final int FLAG_UPDATE = 1 << 1;

    /**
     * This ViewHolder's data is invalid. The identity implied by mPosition and mItemId
     * are not to be trusted and may no longer match the item view type.
     * This ViewHolder must be fully rebound to different data.
     */
    static final int FLAG_INVALID = 1 << 2;

    /**
     * This ViewHolder points at data that represents an item previously removed from the
     * data set. Its view may still be used for things like outgoing animations.
     */
    static final int FLAG_REMOVED = 1 << 3;

    private static int mFlags;

    public static void main(String[] args) throws Exception {

        setFlags(FLAG_BOUND,
                FLAG_BOUND | FLAG_UPDATE | FLAG_INVALID);

        System.out.println("result = " + ((mFlags & FLAG_BOUND) != 0));

    }

    static void setFlags(int flags, int mask) {
        mFlags = (mFlags & ~mask) | (flags & mask);
    }
}
