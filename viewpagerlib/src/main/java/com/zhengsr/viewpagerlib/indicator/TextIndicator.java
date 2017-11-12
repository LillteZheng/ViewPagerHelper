package com.zhengsr.viewpagerlib.indicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.zhengsr.viewpagerlib.R;
import com.zhengsr.viewpagerlib.bean.PageBean;


/**
 * Created by zhengshaorui on 2017/10/29.
 * csdn: http://blog.csdn.net/u011418943
 */
public class TextIndicator extends LinearLayout implements ViewPager.OnPageChangeListener {
    private static final String TAG = "zsr";


    /**
     * normal and logic
     */
    private Context mContext;
    private boolean mShowCircle;


    private int mCount = 0;
    private boolean mDismissOpen; //是否隐藏底部指示器，当“立即体验”的view 出现的时候。

    private Paint mPaint;
    private Paint mTextPaint;

    private View mOpenView;
    private int mRadius;
    private String mTextString;



    public TextIndicator(Context context) {
        this(context,null);
    }

    public TextIndicator(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TextIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TextIndicator);
        mShowCircle = ta.getBoolean(R.styleable.TextIndicator_show_circle,false);
        int circleColor = ta.getResourceId(R.styleable.TextIndicator_circle_color,
                R.color.page_black_cc);
        int textcolor = ta.getResourceId(R.styleable.TextIndicator_text_color,R.color.page_white);
        int textsize = ta.getDimensionPixelSize(R.styleable.TextIndicator_text_size,15);
        Log.d(TAG, "zsr --> TextIndicator: "+textsize);
        ta.recycle();
        setGravity(Gravity.CENTER);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(circleColor));
        mPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textsize);
        mTextPaint.setColor(getResources().getColor(textcolor));

    }



    /**
     * 获取自定义控件，防止wrap_content的情况
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = measureHeight(heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        mRadius = Math.min(height,width)/2;
        setMeasuredDimension(width,height);

    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mShowCircle) {
            canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);

        }

        //绘制文字

        float textWidth = mTextPaint.measureText(mTextString);
        float x = mRadius - textWidth/2;
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float dy = (metrics.descent+metrics.ascent)/2;
        float ty = mRadius - dy;
        canvas.drawText(mTextString,x,ty,mTextPaint);

    }

    public void addPagerData(PageBean bean, ViewPager viewPager){
        if (bean != null) {
            mCount = bean.datas.size();
            mTextString = 1+"/"+mCount;
            if (viewPager != null){
                viewPager.addOnPageChangeListener(this);
            }
        }
        if (bean.openview != null){
            mOpenView = bean.openview;
        }
        invalidate();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }

    @Override
    public void onPageSelected(int position) {
        viewPagerSeleted(position%mCount);
        showStartView(position%mCount);
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     *  用于viewpager 滑动时，文字的显示
     * @param position
     */
    private void viewPagerSeleted(int position) {
        mTextString = (position+1)+"/"+mCount;
        invalidate();
    }


    /**
     * 显示最后一页的状态
     * @param position
     */
    private void showStartView(int position) {
        // 最后一页
        if (position == mCount - 1) {
            if (mOpenView != null) {
                mOpenView.setVisibility(VISIBLE);
                ObjectAnimator animator = ObjectAnimator.ofFloat(mOpenView,
                        "alpha", 0, 1);
                animator.setDuration(500);
                animator.setInterpolator(new LinearInterpolator());
                animator.start();
                Log.d(TAG, "zsr --> showStartView: "+mDismissOpen);
                if (mDismissOpen){
                    setVisibility(View.GONE);
                }
            }
        } else {
            if (mOpenView != null) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(mOpenView,
                        "alpha", 1, 0);
                animator.setDuration(300);
                animator.setInterpolator(new LinearInterpolator());
                animator.start();
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mOpenView.setVisibility(GONE);
                    }
                });
                if (mDismissOpen){
                    setVisibility(VISIBLE);
                }
            }
        }
    }



    //设置高的大小
    private int measureHeight(int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int result = 0;
        //获取模式和大小
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }else{
            result = 100; //如果是wrap_content ,给个初始值
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
    //设置宽的大小
    private int measureWidth(int widthMeasureSpec) {
        // TODO Auto-generated method stub
        int result = 0;
        //获取模式和大小
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }else{
            result = 100; //如果是wrap_content ,给个初始值
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

}
