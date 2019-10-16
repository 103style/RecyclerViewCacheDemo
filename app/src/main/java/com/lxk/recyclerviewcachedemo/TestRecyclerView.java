package com.lxk.recyclerviewcachedemo;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author xiaoke.luo@tcl.com 2019/10/16 18:05
 */
public class TestRecyclerView extends RecyclerView {
    private LayoutListener layoutListener;

    public TestRecyclerView(@NonNull Context context) {
        super(context);
    }

    public TestRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setLayoutListener(LayoutListener layoutListener) {
        this.layoutListener = layoutListener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (layoutListener != null) {
            layoutListener.onBeforeLayout();
        }
        super.onLayout(changed, l, t, r, b);

        if (layoutListener != null) {
            layoutListener.onAfterLayout();
        }
    }

    public interface LayoutListener {

        void onBeforeLayout();

        void onAfterLayout();
    }

}