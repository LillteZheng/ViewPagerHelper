package com.zhengsr.viewpagerlib.anim;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

/**
 * @author by zhengshaorui 2020/9/6 12:04
 * describe：
 */
public abstract class Itransformer {
    public ViewPager2.PageTransformer getTransformer2(){
        return new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                transform(page,position);
            }
        };
    }



    public ViewPager.PageTransformer getTransformer(){
        return new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                transform(page, position);
            }
        };
    }

    abstract void transform(View view, float position);
}
