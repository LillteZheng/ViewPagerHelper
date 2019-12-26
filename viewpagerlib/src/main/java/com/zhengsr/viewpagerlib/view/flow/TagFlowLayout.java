package com.zhengsr.viewpagerlib.view.flow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.zhengsr.viewpagerlib.view.flow.adapter.BaseFlowAdapter;
import com.zhengsr.viewpagerlib.view.flow.adapter.TagFlowAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: TagFlowLayout 为标签瀑布流布局，支持单选，多选
 */
public class TagFlowLayout extends FlowLayout implements BaseFlowAdapter.DataListener {
    private static final String TAG = "TagFlowLayout";
    private TagFlowAdapter mAdapter;
    private int mMaxSelectCount = 1;
    private int mLastPosition = 0;

    public TagFlowLayout(Context context) {
        this(context, null);
    }

    public TagFlowLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagFlowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(TagFlowAdapter adapter) {
        mAdapter = adapter;
        mAdapter.setListener(this);
        notifyDataChanged();
    }

    public TagFlowLayout setMaxCount(int maxCount) {
        mMaxSelectCount = maxCount;
        return this;
    }


    @Override
    public void notifyDataChanged() {
        removeAllViews();
        final TagFlowAdapter adapter = mAdapter;
        int count = adapter.getItemCount();
        for (int i = 0; i < count; i++) {
            View view = LayoutInflater.from(getContext()).inflate(adapter.getLayoutId(), this, false);
            adapter.onBindView(view, adapter.getDatas().get(i), i);
            addView(view);
            final int finalI = i;
            onItemClick(adapter, view, finalI);

            //判断开始是否有人是选中的
            if (view.isSelected()) {
                adapter.onItemSelectState(view, true);
            }
        }
    }

    /**
     * 单个view 点击
     *
     * @param adapter
     * @param view
     */
    private void onItemClick(final TagFlowAdapter adapter, final View view, final int position) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.onItemClick(v, adapter.getDatas().get(position), position);

                //是否为单选
                if (mMaxSelectCount == 1) {
                    if (mLastPosition != position) {
                        View selectedView = getSelectedView();
                        if (selectedView != null) {
                            selectedView.setSelected(false);
                            adapter.onItemSelectState(selectedView, false);
                        }
                        adapter.onFocusChanged(selectedView, v);
                        //进行反选
                        if (v.isSelected()) {
                            v.setSelected(false);
                            adapter.onItemSelectState(v, false);
                        } else {
                            v.setSelected(true);
                            adapter.onItemSelectState(v, true);
                        }
                    }
                } else {
                    //进行反选
                    if (v.isSelected()) {
                        v.setSelected(false);
                        adapter.onItemSelectState(v, false);
                    } else {
                        v.setSelected(true);
                        adapter.onItemSelectState(v, true);
                    }
                    if (getSelectedCount() > mMaxSelectCount) {
                        v.setSelected(false);
                        adapter.onItemSelectState(v, false);
                        adapter.onClickMaxCount(getSelecteds(), mMaxSelectCount);
                        return;
                    }
                }

                mLastPosition = position;
            }
        });
    }

    /**
     * 获取选中的个数
     *
     * @return
     */
    private int getSelectedCount() {
        int count = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.isSelected()) {
                count++;
            }
        }
        return count;
    }

    /**
     * 拿到当前选中的view
     * 适合单选的时候
     *
     * @return
     */
    public View getSelectedView() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.isSelected()) {
                return view;
            }
        }
        return null;
    }

    public List<Integer> getSelecteds(){
        List<Integer> indexs = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.isSelected()) {
                indexs.add(i);
            }
        }
        return indexs;
    }

}
