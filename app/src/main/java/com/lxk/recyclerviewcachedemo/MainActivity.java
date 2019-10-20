package com.lxk.recyclerviewcachedemo;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxk.recyclerviewcachedemo.bean.Item1Bean;
import com.lxk.recyclerviewcachedemo.bean.Item2Bean;
import com.lxk.recyclerviewcachedemo.bean.ItemBean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * created by 103style  2019/10/19 16:03
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final String RV_PKG = "androidx.recyclerview.widget.RecyclerView";
    private final String LLM_PKG = "androidx.recyclerview.widget.LinearLayoutManager";
    private TestRecyclerView recyclerView;
    private TestAdapter adapter;
    private List<ItemBean> datas;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.list);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setLayoutListener(new TestRecyclerView.LayoutListener() {
            @Override
            public void onBeforeLayout() {
                try {
                    Field mRecycler = access(Class.forName(RV_PKG), "mRecycler");
                    RecyclerView.Recycler recyclerInstance =
                            (RecyclerView.Recycler) mRecycler.get(recyclerView);

                    Class<?> recyclerClass = Class.forName(mRecycler.getType().getName());
                    Field mAttachedScrap = access(recyclerClass, "mAttachedScrap");
                    mAttachedScrap.set(recyclerInstance, new ArrayListWrapper<RecyclerView.ViewHolder>());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAfterLayout() {
                showMessage();
            }
        });
        initDatas();
        adapter = new TestAdapter(this, datas);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == 0) {
                    showMessage();
                }
            }
        });

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemBean itemBean = System.currentTimeMillis() % 2 == 0 ? new Item1Bean("added 1") : new Item2Bean("added 2");
                adapter.add(itemBean, 1);
            }
        });

        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.remove(1);
            }
        });
    }

    private Field access(Class className, String name) throws NoSuchFieldException {
        Field field = className.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    private void showMessage() {
        try {
            long time = System.currentTimeMillis();
            Field mRecycler = access(Class.forName(RV_PKG), "mRecycler");
            RecyclerView.Recycler recyclerInstance = (RecyclerView.Recycler) mRecycler.get(recyclerView);

            Field childHelperFiled = access(Class.forName(RV_PKG), "mChildHelper");
            Field hiddenViewFiled = access(childHelperFiled.get(recyclerView).getClass(), "mHiddenViews");
            List<View> mHiddenViews = (List<View>) hiddenViewFiled.get(childHelperFiled.get(recyclerView));
            Log.e(TAG + time, "ChildHelper.mHiddenViews =" + (mHiddenViews == null ? 0 : mHiddenViews.size()));

            Field mLayoutStateField = access(Class.forName(LLM_PKG), "mLayoutState");
            Field mScrapListField = access(mLayoutStateField.get(linearLayoutManager).getClass(), "mScrapList");
            List<RecyclerView.ViewHolder> mScrapList = (List<RecyclerView.ViewHolder>) mScrapListField.get(mLayoutStateField.get(linearLayoutManager));
            Log.e(TAG + time, "LayoutManager.LayoutState.mScrapList =" + (mScrapList == null ? 0 : mScrapList.size()));

            Class<?> recyclerClass = Class.forName(mRecycler.getType().getName());
            Field mViewCacheMax = access(recyclerClass, "mViewCacheMax");
            Field mAttachedScrap = access(recyclerClass, "mAttachedScrap");
            Field mChangedScrap = access(recyclerClass, "mChangedScrap");
            Field mCachedViews = access(recyclerClass, "mCachedViews");
            Field mRecyclerPool = access(recyclerClass, "mRecyclerPool");


            int mViewCacheSize = (int) mViewCacheMax.get(recyclerInstance);
            Log.e(TAG + time, "Recycler.mViewCacheMax =" + mViewCacheSize);
            ArrayListWrapper<RecyclerView.ViewHolder> mAttached = (ArrayListWrapper<RecyclerView.ViewHolder>) mAttachedScrap.get(recyclerInstance);
            Log.e(TAG + time, "Recycler.mAttachedScrap.size() = " + (mAttached == null ? 0 : mAttached.maxSize));
            logData(mAttached);
            ArrayList<RecyclerView.ViewHolder> mChanged = (ArrayList<RecyclerView.ViewHolder>) mChangedScrap.get(recyclerInstance);
            Log.e(TAG + time, "Recycler.mChangedScrap.size() = " + (mChanged == null ? 0 : mChanged.size()));
            logData(mChanged);
            ArrayList<RecyclerView.ViewHolder> mCached = (ArrayList<RecyclerView.ViewHolder>) mCachedViews.get(recyclerInstance);
            Log.e(TAG + time, "Recycler.mCachedViews.size() = " + (mCached == null ? 0 : mCached.size()));
            logData(mCached);

            RecyclerView.RecycledViewPool recycledViewPool = (RecyclerView.RecycledViewPool) mRecyclerPool.get(recyclerInstance);
            Class<?> recyclerPoolClass = Class.forName(mRecyclerPool.getType().getName());

            Field mScrapField = access(recyclerPoolClass, "mScrap");
            SparseArray mScrap = (SparseArray) mScrapField.get(recycledViewPool);

            //这里的 1 和 2 分别是 adapter中 数据的 viewType
            for (int i = 1; i <= mScrap.size(); i++) {
                Object object = mScrap.get(i);
                Field mScrapHeapField = access(object.getClass(), "mScrapHeap");
                Field mMaxScrapField = access(object.getClass(), "mMaxScrap");
                Log.e(TAG + time, "RecycledViewPool.mScrap.get(viewType = " + i + ").mMaxScrapField = " + mMaxScrapField.get(object));
                ArrayList<RecyclerView.ViewHolder> mScrapHeap = (ArrayList<RecyclerView.ViewHolder>) mScrapHeapField.get(object);
                Log.e(TAG + time, "RecycledViewPool.mScrap.get(viewType = " + i + ").mScrapHeap.size() = " + mScrapHeap.size());
                logData(mScrapHeap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void logData(ArrayList<RecyclerView.ViewHolder> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            Log.e(TAG, "RecycledViewPool.mScrap.mScrapHeap " + i + " = " + list.get(i).toString());
        }

    }

    private void initDatas() {
        datas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            ItemBean itemBean;
            if (i % 2 == 0) {
                itemBean = new Item1Bean(String.valueOf(i));
            } else {
                itemBean = new Item2Bean(String.valueOf(i));
            }
            datas.add(itemBean);
        }
    }
}
