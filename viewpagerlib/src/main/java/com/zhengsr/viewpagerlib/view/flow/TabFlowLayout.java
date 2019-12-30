package com.zhengsr.viewpagerlib.view.flow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zhengsr.viewpagerlib.R;
import com.zhengsr.viewpagerlib.view.flow.adapter.BaseFlowAdapter;
import com.zhengsr.viewpagerlib.view.flow.adapter.TabFlowAdapter;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public class TabFlowLayout extends HorizonFlow {
    private static final String TAG = "TabFlowLayout";
    private Paint mPaint;
    private TabFlowAdapter mAdapter;
    private int mLastPosition = 0;
    private Path mPath;
    private Bitmap mBitmap;
    private Rect mSrcRect;
    private RectF mDetRect;
    private float mTransX = 0;
    public TabFlowLayout(Context context) {
        this(context,null);
    }
    
    public TabFlowLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    
    public TabFlowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
        mPath = new Path();
        mSrcRect = new Rect(0,0,mBitmap.getWidth(),mBitmap.getHeight());
    }

    public void setViewPager(ViewPager viewPager){
        viewPager.addOnPageChangeListener(new ViewPagerListener());
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        //canvas.drawPath(mPath,mPaint);
        canvas.save();
        canvas.translate(mTransX,0);
        canvas.drawBitmap(mBitmap,mSrcRect,mDetRect,null);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        //拿到一个child的大小
        View child = getChildAt(0);

        int cl = child.getLeft();
        int ct = child.getTop();
        int cr = cl + child.getWidth();
        int cb = ct + child.getBottom();

        mPath.addRect(cl,ct,cr,cb, Path.Direction.CCW);
        mDetRect = new RectF(cl,ct,cr,cb);



    }


    @Override
    protected void onItemViewConfig(final BaseFlowAdapter baseAdapter,final  View view,final int position) {
        mAdapter = (TabFlowAdapter) baseAdapter;
        if (position == 0){
            mAdapter.onItemViewFocus(view,view,0);
        }
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.onItemClick(view,mAdapter.getDatas().get(position),position);
                if (position != mLastPosition) {
                    mAdapter.onItemViewFocus(view, getChildAt(mLastPosition),position);

                }
                mLastPosition = position;
            }
        });
    }

    /**
     * viewpager 切换监听
     */
    private float mLastOffset;
    class ViewPagerListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
          //  int tabWidth = mWidth / mVisiableSize;
           /* mLineTransX = (int) (tabWidth * position + tabWidth * offset);
            if (position >= (mVisiableSize - 1) && offset > 0) {
                scrollTo(
                        (position - (mVisiableSize - 1)) * tabWidth + (int) (tabWidth * offset),
                        0);
            }*/




           int tabWidth = getChildAt(position).getWidth();

         //  mTransX = tabWidth * position + tabWidth * positionOffset;
          //  mTransX = positionOffsetPixels;

           postInvalidate();



            mLastOffset = positionOffset;
           
        }

        @Override
        public void onPageSelected(int position) {
            mLastPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
           // Log.d(TAG, "zsr - onPageScrollStateChanged: "+i);
            if (state == 0){
                View child = getChildAt(mLastPosition);
           //     mDetRect.set(0,0,child.getWidth(),child.getHeight());
            }
        }
    }
}
