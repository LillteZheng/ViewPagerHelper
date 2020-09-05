package com.zhengsr.viewpagerlib.indicator;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;


/**
 * Created by zhengshaorui on 2017/10/29.
 * csdn: http://blog.csdn.net/u011418943
 * modify on 2020/1/19 ,用 textview 就可以了，没必要自定义
 */
public class TextIndicator extends AppCompatTextView implements ViewPager.OnPageChangeListener {
    private static final String TAG = "zsr";


    /**
     * normal and logic
     */
    private int mCount = 0;
    private String mTextString = "";


    public TextIndicator(Context context) {
        this(context, null);
    }

    public TextIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }


    public void addPagerData(int count, ViewPager viewPager) {
        if (count == 0){
            return;
        }
        mCount = count;
        mTextString = 1 + "/" + mCount;
        setText(mTextString);
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(this);
            viewPagerSeleted(viewPager.getCurrentItem());
        }
    }

    public void addPagerData(int count, ViewPager2 viewPager2){
        if (count == 0){
            return;
        }
        mCount = count;
        mTextString = 1 + "/" + mCount;
        setText(mTextString);
        if (viewPager2 != null) {
            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    viewPagerSeleted(position);
                }
            });
            viewPagerSeleted(viewPager2.getCurrentItem());

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }

    @Override
    public void onPageSelected(int position) {
        viewPagerSeleted(position);
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 用于viewpager 滑动时，文字的显示
     *
     * @param position
     */
    private void viewPagerSeleted(int position) {
        position = position % mCount;
        mTextString = (position + 1) + "/" + mCount;
        setText(mTextString);
    }



}
