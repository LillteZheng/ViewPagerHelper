package com.zhengsr.viewpagerlib.view.flow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.zhengsr.viewpagerlib.R;
import com.zhengsr.viewpagerlib.view.flow.adapter.BaseFlowAdapter;
import com.zhengsr.viewpagerlib.view.flow.adapter.TabFlowAdapter;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: tab 横向布局，可实现各种效果
 */
public class TabFlowLayout extends FlowLayout implements BaseFlowAdapter.DataListener {
    private static final String TAG = "TabFlowLayout";
    private TabFlowAdapter mAdapter;
    private int mSlop;
    private float mDownX;
    private float mMovex;
    private int mRightBoard;
    private int mScreenWidth;

    public TabFlowLayout(Context context) {
        this(context, null);
    }

    public TabFlowLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabFlowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        mSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int childCount = getChildCount();
        Log.d(TAG, "zsr - onLayout: "+childCount);
        if (childCount > 0){
            mRightBoard = getChildAt(childCount - 1).getRight();
        }
        Log.d(TAG, "zsr - onLayout: "+mRightBoard);
    }

    /**
     * 是否屏蔽子控件的事件传递
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mMovex = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs((ev.getX() - mDownX));
                //确定是移动了，屏蔽子控件事件传递
                if (dx > mSlop){
                    return true;
                }
                break;
                default:break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mMovex = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                //如果要加滑行的效果，可以用手势的 onFlining
                int dx = (int) (mMovex - event.getX());

                //判断边界
                int scrollX = getScrollX();
                if (scrollX + dx <= 0){
                    //直接移到左边界
                    scrollTo(0,0);
                    return true;
                }else if (scrollX + dx + mScreenWidth>= mRightBoard){
                    scrollTo(mRightBoard - mScreenWidth,0);
                    return true;
                }

                scrollBy(dx,0);
                mMovex = event.getX();
                break;
                default:break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean isVertical() {
        return false;
    }


    @Override
    protected void onViewClick(BaseFlowAdapter baseAdapter, View view, int position) {

    }


}
