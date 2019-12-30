package com.zhengsr.viewpagerlib.view.flow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.zhengsr.viewpagerlib.view.flow.adapter.BaseFlowAdapter;
import com.zhengsr.viewpagerlib.view.flow.adapter.TabFlowAdapter;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: tab 横向布局，可实现各种效果
 */
abstract class HorizonFlow extends FlowLayout implements BaseFlowAdapter.DataListener {
    private static final String TAG = "TabFlowLayout";
    private int mSlop;
    private float mDownX;
    private float mMovex;
    private int mRightBoard;
    private int mScreenWidth;
    private Scroller mScroller;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    public HorizonFlow(Context context) {
        this(context, null);
    }

    public HorizonFlow(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizonFlow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        mSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mScroller = new Scroller(context);
        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int childCount = getChildCount();
        if (childCount > 0) {
            mRightBoard = getChildAt(childCount - 1).getRight();
        }
    }

    /**
     * 是否屏蔽子控件的事件传递
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mMovex = ev.getX();
                mScroller.abortAnimation();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs((ev.getX() - mDownX));
                //确定是移动了，屏蔽子控件事件传递
                if (dx > mSlop) {
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
    private VelocityTracker mVelocityTracker;
    private int mSSSL;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //如果要加滑行的效果，可以用手势的 onFlining
                int dx = (int) (mMovex - event.getX());

                //判断边界
                int scrollX = getScrollX();
                if (scrollX + dx <= 0) {
                    //直接移到左边界
                    scrollTo(0, 0);
                    return true;
                } else if (scrollX + dx + mScreenWidth >= mRightBoard) {
                    scrollTo(mRightBoard - mScreenWidth, 0);
                    return true;
                }

                scrollBy(dx, 0);
                mMovex = event.getX();

                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000,mMaximumVelocity);
                //获取横向速度
                int velocityX = (int) mVelocityTracker.getXVelocity();

                if (Math.abs(velocityX) > mMinimumVelocity){
                    mSSSL = getScrollX();
                    mScroller.fling(getScrollX(),0,velocityX,0,0,mRightBoard - mScreenWidth,0,0);
                }

                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }

                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean isVertical() {
        return false;
    }




    @Override
    public void resetAll() {

    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int diffX = mSSSL - mScroller.getCurrX();
            // 超出右边界，进行修正
            if (getScrollX() + diffX >= mRightBoard - mScreenWidth) {
                diffX = (int) (mRightBoard - mScreenWidth - getScrollX());
            }

            // 超出左边界，进行修正
            if (getScrollX() <= 0) {
                diffX = -getScrollX();
            }
            scrollBy(diffX, 0);
            postInvalidate();
        }
    }

    
}
