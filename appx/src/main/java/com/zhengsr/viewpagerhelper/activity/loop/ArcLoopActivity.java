package com.zhengsr.viewpagerhelper.activity.loop;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.CircleIndicator;
import com.zhengsr.viewpagerlib.view.ArcImageView;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

public class ArcLoopActivity extends AppCompatActivity {

    private static final int[] RESID = {
            R.mipmap.beauty1,
            R.mipmap.beauty2,
            R.mipmap.beauty3,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc_loop);
        initView();
    }

    private void initView() {
        BannerViewPager bannerViewPager = findViewById(R.id.loop_viewpager_arc);
        CircleIndicator indicator = findViewById(R.id.bottom_indicator);
        List<ArcBean> beans = new ArrayList<>();
        for (int i = 0; i < RESID.length; i++) {
            ArcBean bean = new ArcBean();
            bean.resId = RESID[i];
            beans.add(bean);
        }

        //添加 indicator
        bannerViewPager.addIndicator(indicator);

        bannerViewPager.setPageListener(R.layout.arc_loop_layout, beans, new PageHelperListener<ArcBean>() {
            @Override
            public void bindView(View view, ArcBean data, int position) {
                ArcImageView imageView = view.findViewById(R.id.arc_icon);
                Glide.with(view)
                        .load(data.resId)
                        .into(imageView);
            }
        });


    }

    class ArcBean {
        public int resId;
    }
}
