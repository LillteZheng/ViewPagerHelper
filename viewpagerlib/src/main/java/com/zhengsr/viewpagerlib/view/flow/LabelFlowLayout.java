package com.zhengsr.viewpagerlib.view.flow;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zhengsr.viewpagerlib.R;
import com.zhengsr.viewpagerlib.view.flow.adapter.BaseFlowAdapter;
import com.zhengsr.viewpagerlib.view.flow.adapter.LabelFlowAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: TagFlowLayout 为标签瀑布流布局，支持单选，多选
 */
public class LabelFlowLayout extends FlowLayout  {
    private static final String TAG = "TagFlowLayout";
    private int mMaxSelectCount ;
    private int mLastPosition = 0;
    private LabelFlowAdapter mAdapter;

    public LabelFlowLayout(Context context) {
        this(context, null);
    }

    public LabelFlowLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelFlowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LabelFlowLayout);
        mMaxSelectCount = ta.getInteger(R.styleable.LabelFlowLayout_label_max_count,1);
        ta.recycle();
    }

    @Override
    protected void onItemViewConfig(final BaseFlowAdapter baseAdapter, final View view, final int position) {
        mAdapter = (LabelFlowAdapter) baseAdapter;
        final LabelFlowAdapter adapter = (LabelFlowAdapter) baseAdapter;
        //单选
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

        view.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return adapter.onItemLongClick(view,position);
            }
        });
    }


    public LabelFlowLayout setMaxCount(int maxCount) {
        mMaxSelectCount = maxCount;
        return this;
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

    @Override
    public void resetAll() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            view.setSelected(false);
            mAdapter.onItemSelectState(view,false);
        }
    }
}
