package com.zhengsr.viewpagerlib.indicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhengsr.viewpagerlib.R;
import com.zhengsr.viewpagerlib.bean.PageBean;


/**
 * Created by zhengshaorui on 2017/10/29.
 * csdn: http://blog.csdn.net/u011418943
 */
public class NormalIndicator extends LinearLayout implements ViewPager.OnPageChangeListener {
    private static final String TAG = "zsr";


    /**
     * normal and logic
     */
    private Context mContext;
    private int mLastPosition ;
    private int mCount = 0;
    /**
     * attrs
     */
    private int mSelector;
    private int mLeftMargin;
    private boolean mDismissOpen; //是否隐藏底部指示器，当“立即体验”的view 出现的时候。
    /**
     * ui
     */
    private View mOpenView;


    public NormalIndicator(Context context) {
        this(context,null);
    }

    public NormalIndicator(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NormalIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NormalIndicator);
        mSelector = ta.getResourceId(R.styleable.NormalIndicator_normal_selector,
                R.drawable.page_bottom_circle);
        mLeftMargin = (int) ta.getDimension(R.styleable.NormalIndicator_normal_leftmargin,15);
        mDismissOpen =  ta.getBoolean(R.styleable.NormalIndicator_normal_dismiss_open,false);
        setGravity(Gravity.CENTER);
        ta.recycle();
    }


    public void addPagerData(PageBean bean, ViewPager viewPager){
        if (bean != null) {
            mCount = bean.datas.size();
            //这里加小圆点
            LayoutParams params = new
                    LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(mLeftMargin,0,0,0);
            for (int i = 0; i < mCount; i++) {
                ImageView imageView = new ImageView(mContext);
                if (i == 0){
                    imageView.setSelected(true);
                }else{
                    imageView.setSelected(false);
                }
                imageView.setLayoutParams(params);
                imageView.setBackgroundResource(mSelector);

                addView(imageView);
            }

            if (viewPager != null) {
                viewPager.addOnPageChangeListener(this);
            }
        }
        if (bean.openview != null){
            mOpenView = bean.openview;
        }
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
     *  用于viewpager 滑动时，底部圆点的状态显示
     * @param position
     */
    private void viewPagerSeleted(int position) {
        View lastView ;
        if (mLastPosition >= 0){
            lastView = getChildAt(mLastPosition);
            if (lastView != null) {
                lastView.setSelected(false);

            }
        }

        View currentView = getChildAt(position);
        if (currentView != null){
            currentView.setSelected(true);
        }
        mLastPosition = position;
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
}
