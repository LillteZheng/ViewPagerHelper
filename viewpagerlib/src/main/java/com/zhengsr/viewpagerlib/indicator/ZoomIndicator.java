package com.zhengsr.viewpagerlib.indicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhengsr.viewpagerlib.R;
import com.zhengsr.viewpagerlib.bean.PageBean;


/**
 * Created by zhengshaorui on 2017/10/29.
 * csdn: http://blog.csdn.net/u011418943
 */
public class ZoomIndicator extends LinearLayout implements ViewPager.OnPageChangeListener {
    private static final String TAG = "zsr";
    /**
     * const
     */
    private static final int ANIM_IN = 0x1001;
    private static final int ANIM_OUT = 0x1002;
    private static final float ALPHA_MAX = 1.0f;

    private static final float SCALE_MIN = 1.0f;
    private static final int ANIM_OUT_TIME = 400;
    private static final int ANIM_IN_TIME = 300;

    private float mAlpha_min;
    private float mScale_max;


    /**
     * normal and logic
     */
    private Context mContext;
    private int mLastPosition ;
    private int mSelector;
    private int mLeftMargin;
    private boolean isFirst = true;
    private int mCount = 0;
    private boolean mDismissOpen; //是否隐藏底部指示器，当“立即体验”的view 出现的时候。
    /**
     * ui
     */
    private View mOpenView;


    public ZoomIndicator(Context context) {
        this(context,null);
    }

    public ZoomIndicator(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ZoomIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ZoomIndicator);
        mSelector = ta.getResourceId(R.styleable.ZoomIndicator_zoom_selector,
                R.drawable.page_bottom_circle);
        mLeftMargin = (int) ta.getDimension(R.styleable.ZoomIndicator_zoom_leftmargin,15);
        mAlpha_min = ta.getFloat(R.styleable.ZoomIndicator_zoom_alpha_min,0.5f);
        mScale_max = ta.getFloat(R.styleable.ZoomIndicator_zoom_max,1.5f);
        mDismissOpen = ta.getBoolean(R.styleable.ZoomIndicator_zoom_dismiss_open,false);
        setGravity(Gravity.CENTER);
        ta.recycle();
    }






    public void addPagerData(PageBean bean, ViewPager viewPager) {
        if (bean != null){
            mCount = bean.datas.size();
            //这里加小圆点
            for (int i = 0; i < mCount; i++) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                if (i == mCount - 1){ //防止indicator的宽度为wrap_content，放大后被遮盖住的问题
                    params.setMargins(mLeftMargin,0,mLeftMargin,0);
                }else{
                    params.setMargins(mLeftMargin,0,0,0);
                }

                ImageView imageView = new ImageView(mContext);
            /*    if (mSelector == 0){
                }else {
                }*/
                imageView.setBackgroundResource(mSelector);
                imageView.setLayoutParams(params);
                imageView.setAlpha(mAlpha_min);

                addView(imageView);
            }
        }
        if (bean.openview != null){
            mOpenView = bean.openview;
        }
        if (viewPager != null){
            viewPager.addOnPageChangeListener(this);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (isFirst){
            isFirst = false;
            if (getChildAt(0) != null) {
                targetViewAnim(getChildAt(0), ANIM_OUT);
            }

        }

    }

    @Override
    public void onPageSelected(int position) {
      /*  viewPagerSeleted(mBannerStyle.getRealPosition(mMode,position));
        //用于glide是否显示 openview
        mBannerStyle.getPageSeleted(position);*/
        showStartView(position%mCount);
        viewPagerSeleted(position%mCount);
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
                targetViewAnim(lastView, ANIM_IN);
            }
        }
        View currentView = getChildAt(position);
        if (currentView != null){
            currentView.setSelected(true);
            targetViewAnim(currentView, ANIM_OUT);
        }
        mLastPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
                if (mDismissOpen){
                    setVisibility(GONE);
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


    /**
     * 用于小圆点的放大缩小
     * @param view
     * @param type
     */
    private void targetViewAnim(final View view, final int type){
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = null;
        ObjectAnimator scaleY = null;
        ObjectAnimator alpha = null;
        if (type == ANIM_OUT){
            scaleX = ObjectAnimator.ofFloat(view,"scaleX",SCALE_MIN,mScale_max);
            scaleY = ObjectAnimator.ofFloat(view,"scaleY",SCALE_MIN,mScale_max);
            alpha = ObjectAnimator.ofFloat(view,"alpha",mAlpha_min,ALPHA_MAX);
            animatorSet.setDuration(ANIM_OUT_TIME);
        }else{
            scaleX = ObjectAnimator.ofFloat(view,"scaleX",mScale_max,SCALE_MIN);
            scaleY = ObjectAnimator.ofFloat(view,"scaleY",mScale_max,SCALE_MIN);
            alpha = ObjectAnimator.ofFloat(view,"alpha",ALPHA_MAX,mAlpha_min);
            animatorSet.setDuration(ANIM_IN_TIME);
        }
        animatorSet.play(scaleX).with(scaleY).with(alpha);

        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();

    }


}
