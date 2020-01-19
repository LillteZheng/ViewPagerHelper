package com.zhengsr.viewpagerlib.indicator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhengsr.viewpagerlib.R;
import com.zhengsr.viewpagerlib.bean.CirBean;
import com.zhengsr.viewpagerlib.type.BannerTransType;
import com.zhengsr.viewpagerlib.type.CircleIndicatorType;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 圆圈的 indicator,支持简单的normal，放大缩小、圆点变矩形、移动等功能
 */
public class CircleIndicator extends LinearLayout {
    private static final String TAG = "CircleIndicator";
    /**
     * static
     */
    private static final int ANIM_IN = 0x1001;
    private static final int ANIM_OUT = 0x1002;
    private static final int ANIM_OUT_TIME = 400;
    private static final int ANIM_IN_TIME = 300;

    /**
     * attrs
     */
    private CircleIndicatorType mType;
    private int mMargin;
    private int mDefaultColor;
    private int mSelectedColor;
    private int mSize;
    private boolean isCanMove;
    private int mRectWidth;
    private float mScaleFactor;
    /**
     * logic
     */
    private int mCount;
    private Paint mPaint;
    private RectF mRect;
    private int mMoveDistance;
    private int mMoveSize;
    private int mLastPosition = 0;
    private ViewPager mViewPager;

    public CircleIndicator(Context context) {
        this(context, null);
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicator);

        int type = ta.getInteger(R.styleable.CircleIndicator_cir_type, 0);
        mMargin = ta.getDimensionPixelSize(R.styleable.CircleIndicator_cir_horizon_margin, 20);
        mSize = ta.getDimensionPixelSize(R.styleable.CircleIndicator_cir_size, 100);
        mDefaultColor = ta.getColor(R.styleable.CircleIndicator_cir_normalColor, Color.GRAY);
        mSelectedColor = ta.getColor(R.styleable.CircleIndicator_cir_selectedColor, Color.WHITE);
        isCanMove = ta.getBoolean(R.styleable.CircleIndicator_cir_canMove, true);
        mRectWidth = ta.getDimensionPixelSize(R.styleable.CircleIndicator_cir_rect_width, mSize);
        mScaleFactor = ta.getFloat(R.styleable.CircleIndicator_cir_scale_factor, 1);
        ta.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mSelectedColor);
        mRect = new RectF();

        mType = CircleIndicatorType.values()[type];

        /**
         * 如果是放大缩小的，不支持移动
         */
        if (mType == CircleIndicatorType.SCALE) {
            isCanMove = false;
        }
    }


    /**
     * 添加数据
     * @param viewPager
     */
    public void addPagerData(int count, ViewPager viewPager) {

        if (count == 0) {
            return;
        }
        mViewPager = viewPager;
        mCount = count;

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setSize(mSize, mSize);
        drawable.setColor(mDefaultColor);
        for (int i = 0; i < mCount; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i == mCount - 1) {
                params.setMargins(mMargin, 0, mMargin, 0);
            } else {
                params.setMargins(mMargin, 0, 0, 0);
            }

            ImageView imageView = new ImageView(getContext());

            imageView.setBackground(drawable);
            imageView.setLayoutParams(params);
            addView(imageView);
        }
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(new PagerListener());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        ImageView child = (ImageView) getChildAt(0);
        if (child != null) {
            float cl;
            float cr;
            if (mType == CircleIndicatorType.CIRTORECT) {
                int offset = (mRectWidth - mSize) / 2;
                cl = child.getLeft() - offset;
                cr = cl + mRectWidth;
            } else {
                cl = child.getLeft();
                cr = cl + child.getMeasuredWidth();

            }
            float ct = child.getTop();

            float cb = ct + child.getMeasuredHeight();
            mRect.set(cl, ct, cr, cb);
            mMoveSize = mMargin + mSize;

            if (mType == CircleIndicatorType.SCALE) {
                doScaleAnim(child, ANIM_OUT);
            }

        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        super.dispatchDraw(canvas);
        if (!mRect.isEmpty() && mType != CircleIndicatorType.SCALE) {
            canvas.save();
            canvas.translate(mMoveDistance, 0);
            canvas.drawRoundRect(mRect, mSize, mSize, mPaint);
            canvas.restore();
        }
    }

    /**
     * 在配置 Banner 的setPageListener 之前
     * @param bean
     */
    public void addCirBean(CirBean bean) {
        if (bean.type != CircleIndicatorType.UNKNOWN){
            mType = bean.type;
        }
        if (bean.normalColor != -2) {
            mDefaultColor = bean.normalColor;
        }
        if (bean.selectedColor != -2){
            mSelectedColor = bean.selectedColor;
        }
        if (bean.cirSize != 0){
            mSize = bean.cirSize;
        }
        if (bean.horizonMargin != 0){
            mMargin = bean.horizonMargin;
        }
        if (bean.scaleFactor != 1){
            mScaleFactor = bean.scaleFactor;
        }

        if (bean.rectWidth != 0){
            mRectWidth = bean.rectWidth;
        }
        if (isCanMove != bean.isCanMove) {
            isCanMove = bean.isCanMove;
        }
        /**
         * 如果是放大缩小的，不支持移动
         */
        if (mType == CircleIndicatorType.SCALE) {
            isCanMove = false;
        }
    }

    class PagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            /**
             * 由于距离时确定的，所以很好判断移动的渐变
             */
            if (isCanMove) {
                if (position % mCount == (mCount - 1) && positionOffset > 0) {
                    mMoveDistance = 0;
                } else {
                    mMoveDistance = (int) (positionOffset * mMoveSize + position % mCount * mMoveSize);
                }
                invalidate();
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (!isCanMove) {
                /**
                 * 处理不移动的情况
                 */
                position = position % mCount;
                position = position % mCount;

                mMoveDistance =   position * mMoveSize;

                /**
                 * 处理放大缩小的
                 */
                if (mType == CircleIndicatorType.SCALE) {
                    ImageView lastView;
                    if (mLastPosition >= 0) {
                        lastView = (ImageView) getChildAt(mLastPosition);
                        if (lastView != null) {
                            lastView.setSelected(false);
                            doScaleAnim(lastView, ANIM_IN);
                        }
                    }
                    ImageView currentView = (ImageView) getChildAt(position);
                    if (currentView != null) {
                        currentView.setSelected(true);
                        doScaleAnim(currentView, ANIM_OUT);
                    }
                    mLastPosition = position;
                }
                invalidate();
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    /**
     * 用于小圆点的放大缩小
     *
     * @param view
     * @param type
     */
    private void doScaleAnim(final ImageView view, final int type) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX;
        ObjectAnimator scaleY;
        ObjectAnimator colorAnim;
        final GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setSize(mSize, mSize);
        if (type == ANIM_OUT) {
            scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1, mScaleFactor);
            scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1, mScaleFactor);
            animatorSet.setDuration(ANIM_OUT_TIME);
            colorAnim = ObjectAnimator.ofInt(view, "color", mDefaultColor, mSelectedColor);

        } else {
            scaleX = ObjectAnimator.ofFloat(view, "scaleX", mScaleFactor, 1);
            scaleY = ObjectAnimator.ofFloat(view, "scaleY", mScaleFactor, 1);


            colorAnim = ObjectAnimator.ofInt(view, "color", mSelectedColor, mDefaultColor);

            animatorSet.setDuration(ANIM_IN_TIME);

        }
        colorAnim.setEvaluator(new HsvEvaluator());
        animatorSet.play(scaleX).with(scaleY).with(colorAnim);

        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();

        colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue();
                drawable.setColor(color);
                view.setBackground(drawable);
            }
        });


    }

    /**
     * 自定义 HslEvaluator
     * 处理颜色渐变
     */
    private class HsvEvaluator implements TypeEvaluator<Integer> {
        float[] startHsv = new float[3];
        float[] endHsv = new float[3];
        float[] outHsv = new float[3];

        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            // 把 ARGB 转换成 HSV
            Color.colorToHSV(startValue, startHsv);
            Color.colorToHSV(endValue, endHsv);

            // 计算当前动画完成度（fraction）所对应的颜色值
            if (endHsv[0] - startHsv[0] > 180) {
                endHsv[0] -= 360;
            } else if (endHsv[0] - startHsv[0] < -180) {
                endHsv[0] += 360;
            }
            outHsv[0] = startHsv[0] + (endHsv[0] - startHsv[0]) * fraction;
            if (outHsv[0] > 360) {
                outHsv[0] -= 360;
            } else if (outHsv[0] < 0) {
                outHsv[0] += 360;
            }
            outHsv[1] = startHsv[1] + (endHsv[1] - startHsv[1]) * fraction;
            outHsv[2] = startHsv[2] + (endHsv[2] - startHsv[2]) * fraction;

            // 计算当前动画完成度（fraction）所对应的透明度
            int alpha = startValue >> 24 + (int) ((endValue >> 24 - startValue >> 24) * fraction);

            // 把 HSV 转换回 ARGB 返回
            return Color.HSVToColor(alpha, outHsv);
        }
    }


}
