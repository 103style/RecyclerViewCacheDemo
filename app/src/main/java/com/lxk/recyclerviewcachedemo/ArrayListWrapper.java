package com.lxk.recyclerviewcachedemo;

import android.util.Log;

import java.util.ArrayList;

public class ArrayListWrapper<T> extends ArrayList<T> {
    private static final String TAG = "ArrayListWrapper";
    public int maxSize = 0;
    public boolean canReset = true;
    private int lastSize = 0;

    @Override
    public boolean remove(Object o) {
//        Log.e(TAG, "remove: " + o.toString());
        if (size() > maxSize) {
            maxSize = size();
            canReset = false;
        }
        if (size() == 0) {
            canReset = true;
        }
        return super.remove(o);
    }

    @Override
    public boolean add(T t) {
//        Log.e(TAG, "add: " + t.toString());
        if (canReset) {
            if (size() + 1 > lastSize) {
                maxSize = size() + 1;
            }
        }
        return super.add(t);
    }
}
