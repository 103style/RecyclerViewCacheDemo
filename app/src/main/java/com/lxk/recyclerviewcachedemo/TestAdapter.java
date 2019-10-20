package com.lxk.recyclerviewcachedemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lxk.recyclerviewcachedemo.bean.ItemBean;

import java.util.List;

/**
 * created by 103style  2019/10/19 16:02
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private static final String TAG = "TestAdapter";
    private Context context;
    private List<ItemBean> datas;

    private int onCreateViewHolderCount = 0;


    private int MaxItemSizeOneScreen = 8;

    private int height;

    public TestAdapter(Context context, List<ItemBean> data) {
        this.context = context;
        this.datas = data;
        height = context.getResources().getDisplayMetrics().heightPixels;
    }

    public void remove(int position) {
        datas.remove(position);
        notifyItemRemoved(position);
    }

    public void add(ItemBean itemBean, int position) {
        datas.add(position, itemBean);
        notifyItemInserted(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        onCreateViewHolderCount++;
        Log.e(TAG, "onCreateViewHolder onCreateViewHolderCount = " + onCreateViewHolderCount);
        int itemHeight = height / MaxItemSizeOneScreen;
        switch (viewType) {
            case 2:
                View itemView = LayoutInflater.from(context).inflate(R.layout.item_view_2, parent, false);
                itemView.setMinimumHeight(itemHeight);
                return new ViewHolder2(itemView);
            default:
                View itemView1 = LayoutInflater.from(context).inflate(R.layout.item_view_1, parent, false);
                itemView1.setMinimumHeight(itemHeight);
                return new ViewHolder(itemView1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder position = " + position);
        if (holder instanceof ViewHolder2) {
            ViewHolder2 holder2 = (ViewHolder2) holder;
            holder2.tvTime.setText(String.valueOf(System.currentTimeMillis()));
        }
        holder.tvShow.setText(datas.get(position).name);
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).type;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvShow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShow = itemView.findViewById(R.id.tv_show);
        }
    }

    static class ViewHolder2 extends ViewHolder {
        private TextView tvTime;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }


}
