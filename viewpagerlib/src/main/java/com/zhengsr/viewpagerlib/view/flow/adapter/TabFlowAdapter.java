package com.zhengsr.viewpagerlib.view.flow.adapter;

import android.view.View;

import java.util.List;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public abstract class TabFlowAdapter<T> extends BaseFlowAdapter<T> {
    public TabFlowAdapter(int layoutId, List<T> datas) {
        super(layoutId, datas);
    }


}
