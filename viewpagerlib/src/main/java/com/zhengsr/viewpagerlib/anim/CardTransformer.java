package com.zhengsr.viewpagerlib.anim;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * created by zhengsr on 2019/8/11
 * Describe: 卡片式viewpager
 */
public class CardTransformer implements ViewPager.PageTransformer {
    private float mCardHeight = 10;

    public CardTransformer(float cardheight) {
        this.mCardHeight = cardheight;
    }

    @Override
    public void transformPage(@NonNull View view, float position) {
        if (position <= 0){
            view.setTranslationX(0f);
            view.setClickable(true);
        }else {
            view.setTranslationX(-view.getWidth() * position);
            float scale = (view.getWidth() - mCardHeight * position) / view.getWidth();
            view.setScaleX(scale);
            view.setScaleY(scale);
            view.setClickable(false);
            view.setTranslationY(mCardHeight * position);
        }

    }
}
