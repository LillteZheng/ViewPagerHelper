package com.zhengsr.viewpagerlib.view.flow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther by zhengshaorui on 2019/12/21
 * describe: 流瀑布布局,提供横竖两种布局
 */
public class FlowLayout extends ViewGroup {
    private static final String TAG = "FlowLayout";
    private List<List<View>> mAllViews = new ArrayList<>();
    private List<Integer> mLineHeights = new ArrayList<>();

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 模式为竖向测量
     * @return
     */
    public boolean isVertical(){
        return true;
    }

    /**
     * FlowLayout 宽度肯定是确定的，高度是由用户确定的
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //可能添加多次，所以需要清掉
        mAllViews.clear();
        mLineHeights.clear();

        if (isVertical()){
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        }else {
            measureHorizonTal(widthMeasureSpec,heightMeasureSpec);
        }
    }

    /**
     * 测量横向，常用 与 tab 标签，常用于 viewpager 结合
     * 高度是基本确定的，宽度则是根据子控件来的
     */
    private void measureHorizonTal(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 拿到viewgroup为子控件推荐的宽高和模式
         */
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 计算所有的 children 的宽高
        int count = getChildCount();
        int width = 0;
        int height = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE){
                continue;
            }
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            int cWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int cHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            width += cWidth;
            height = Math.max(cHeight,height);
        }
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED){
            height += getPaddingTop()+getPaddingBottom();
        }else{
            height = heightSize;
        }
        setMeasuredDimension(width,height);
    }

    /**
     * 测量竖直方向，常用瀑布流布局 tag 标签
     * 其中宽度是不变的，而高度是根据子控件来的
     */
    private void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 拿到viewgroup为子控件推荐的宽高和模式
         */
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        // 计算所有的 children 的宽高
        int count = getChildCount();
        int lineWidth = 0;
        //拿到行高，取换行前，child 的最大值
        int lineHeight = 0;
        // viewgroup 的高度
        int height = 0;


        List<View> lineViews = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            /**
             * child 的宽高由两方面确定
             *  1、自己本身在 xml 的大小，比如 30dp，match_parent,wrap_content
             *  2、父控件的模式对它其子控件的测量影响，如下：
             *
             * 当 父控件的的模式为 EXACTLY 时：
             *  1、如果 child 的宽是确定的，或者 match_parent ，则 child 的模式也是 EXACTLY
             *  2、如果 chihld 的宽时 wrap_parent ，则child 为 AT_MOST 模式。
             * 当 父控件的模式 AT_MOST是：
             *  1、child 是确定的，则 mode 为 EXACTLY,其他的都是  AT_MOST 模式
             * 当 父控件的模式为 UNSPECIFIED 时；
             *  1、child 如果是 EXACTLY ，则 mode 也是 EXACTLY ，其他都为 UNSPECIFIED
             */
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            /**
             * 经过measureChild 之后，就可以拿到 child 的测量宽高了
             */
            //这里直接强转，为什么不报错，由下面4个方法去转化
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            int cWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int cHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            /**
             * 确定是否换行
             */

            if (lineWidth + cWidth > widthSize - (getPaddingLeft() + getPaddingRight())) {
                //换行
                height += lineHeight;

                mAllViews.add(lineViews);
                mLineHeights.add(lineHeight);
                lineViews = new ArrayList<>();
                lineViews.add(child);

                //重置为下一个child 的宽度
                lineWidth = cWidth;
                lineHeight = cHeight;
            } else {
                //未换行
                lineWidth += cWidth;
                lineHeight = Math.max(lineHeight, cHeight);
                lineViews.add(child);
            }

            //加最后一行
            if (i == count - 1) {
                height += lineHeight;
                mAllViews.add(lineViews);
                mLineHeights.add(lineHeight);
            }
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(height, heightSize);
            height += getPaddingTop() + getPaddingBottom();
        }else{
            height += getPaddingTop() + getPaddingBottom();
        }
        //把测量完成的高，重设置给父控件
        setMeasuredDimension((widthMode== MeasureSpec.EXACTLY) ? widthSize : lineWidth, height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        /**
         * View 的摆放
         * 需要给每一行的 View 设置 child.layout(l,t,r,b) ，行高也要设置，
         * 这些数据从onMeasure 中已经计算好了，所以，只需要把值拿到就可以了
         */
        if (isVertical()){
            int size = mAllViews.size();
            int left = getPaddingLeft();
            int top = getPaddingTop();
            for (int i = 0; i < size; i++) {
                List<View> lineViews = mAllViews.get(i);
                for (View lineView : lineViews) {
                    MarginLayoutParams params = (MarginLayoutParams) lineView.getLayoutParams();
                    int cWidth = lineView.getMeasuredWidth();
                    int cHeight = lineView.getMeasuredHeight();

                    //确定位置和具体控件大小
                    int cl = left + params.leftMargin;
                    int ct = top + params.topMargin;
                    int cr = cl + cWidth;
                    int cb = ct + cHeight;
                    lineView.layout(cl, ct, cr, cb);
                    left += cWidth + params.leftMargin + params.rightMargin;
                }
                left = getPaddingLeft();
                top += mLineHeights.get(i);
            }
        }else {
            int count = getChildCount();
            int left = getPaddingLeft();
            int top = getPaddingTop();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
                int cWidth = child.getMeasuredWidth();
                int cHeight = child.getMeasuredHeight();
                //确定位置和具体控件大小
                int cl = left + params.leftMargin;
                int ct = top + params.topMargin;
                int cr = cl + cWidth;
                int cb = ct + cHeight;
                child.layout(cl,ct,cr,cb);
                left += cWidth + params.leftMargin + params.rightMargin;
            }
        }
    }

    //当你没有写 layoutparams 时
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    //在xml 写了
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    //当你写了 layoutparams ，就需要来转换了
    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

}
