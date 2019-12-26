package com.zhengsr.viewpagerlib.view.flow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.zhengsr.viewpagerlib.R;
import com.zhengsr.viewpagerlib.view.flow.adapter.BaseFlowAdapter;
import com.zhengsr.viewpagerlib.view.flow.adapter.TabFlowAdapter;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: tab 横向布局，可实现各种效果
 */
public class TabFlowLayout extends FlowLayout implements BaseFlowAdapter.DataListener {
    private TabFlowAdapter mAdapter;
    public TabFlowLayout(Context context) {
        this(context,null);
    }

    public TabFlowLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TabFlowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean isVertical() {
        return false;
    }

    public void setAdapter(TabFlowAdapter adapter){
        mAdapter = adapter;
        mAdapter.setListener(this);
    }

    @Override
    public void notifyDataChanged() {

        int childCount = mAdapter.getItemCount();
        for (int i = 0; i < childCount; i++) {
            View view = LayoutInflater.from(getContext()).inflate(mAdapter.getLayoutId(),this,false);
            mAdapter.onBindView(view,mAdapter.getDatas().get(i),i);
            addView(view);
        }
    }
}
