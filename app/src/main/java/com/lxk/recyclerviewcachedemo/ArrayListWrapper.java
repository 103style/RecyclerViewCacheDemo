package com.lxk.recyclerviewcachedemo;

import java.util.ArrayList;

/**
 * @author xiaoke.luo@tcl.com 2019/10/16 18:13
 */
public class ArrayListWrapper<T> extends ArrayList<T> {
    public int maxSize = 0;
    public boolean canReset = true;
    private int lastSize = 0;

    @Override
    public boolean remove(Object o) {
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
        if (canReset) {
            if (size() + 1 > lastSize) {
                maxSize = size() + 1;
            }
        }
        return super.add(t);
    }
}