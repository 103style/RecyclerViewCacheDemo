package com.lxk.recyclerviewcachedemo.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * created by 103style  2019/10/19 15:52
 */
public class ItemBean implements MultiItemEntity {

    public String name;
    public int type;

    @Override
    public int getItemType() {
        return type;
    }
}
