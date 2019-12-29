package com.zhengsr.viewpagerlib.view.flow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

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
    private Scroller mScroller;
    private int mMaximumVelocity;
    private int mMinimumVelocity;

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
    private FlingRunnable mFling = new FlingRunnable(getContext());
    private int mSSSL;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMovex = event.getX();
                mFling.stop();
                break;
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
               // mScroller.startScroll(getScrollX(),0,1,0);
                mVelocityTracker.computeCurrentVelocity(1000,mMaximumVelocity);
                //获取横向速度
                int velocityX = (int) mVelocityTracker.getXVelocity();

                if (Math.abs(velocityX) > mMinimumVelocity){
                    mSSSL = getScrollX();
                    mScroller.fling(getScrollX(),0,velocityX,0,velocityX,1920,0,0);
                   // mFling.start(getScrollX(),velocityX,getScrollX(),1000);
                  //   mScroller.startScroll(getScrollX(),0,velocityX,0);
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
    protected void onItemViewConfig(BaseFlowAdapter baseAdapter, View view, int position) {

    }


    @Override
    public void resetAll() {

    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int offset = mSSSL - mScroller.getCurrX();
            //scrollBy(offset,0);
            if (getScrollX() > 0) {
                scrollTo(offset, 0);
                invalidate();
            }
        }
    }

    class FlingRunnable implements Runnable {

        private Scroller mScroller;

        private int mInitX;
        private int mMinX;
        private int mMaxX;
        private int mVelocityX;

        FlingRunnable(Context context) {
            this.mScroller = new Scroller(context, null, false);
        }

        void start(int initX,
                   int velocityX,
                   int minX,
                   int maxX) {
            this.mInitX = initX;
            this.mVelocityX = velocityX;
            this.mMinX = minX;
            this.mMaxX = maxX;

            // 先停止上一次的滚动
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }

            // 开始 fling
            mScroller.fling(initX, 0, velocityX,
                    0, 0, maxX, 0, 0);
            post(this);
        }

        @Override
        public void run() {
            // 如果已经结束，就不再进行
            if (!mScroller.computeScrollOffset()) {
                return;
            }

            // 计算偏移量
            int currX = mScroller.getCurrX();
            int diffX = mInitX - currX;

            // 用于记录是否超出边界，如果已经超出边界，则不再进行回调，即使滚动还没有完成
            boolean isEnd = false;

            if (diffX != 0) {

                // 超出右边界，进行修正
              /*  if (getScrollX() + diffX >= mCanvasWidth - mViewWidth) {
                    diffX = (int) (mCanvasWidth - mViewWidth - getScrollX());
                    isEnd = true;
                }*/

                // 超出左边界，进行修正
                if (getScrollX() <= 0) {
                    diffX = -getScrollX();
                    isEnd = true;
                }

                if (!mScroller.isFinished()) {
                    scrollBy(diffX, 0);
                }
                mInitX = currX;
            }

            if (!isEnd) {
                post(this);
            }
        }

        /**
         * 进行停止
         */
        void stop() {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
        }
    }


}
