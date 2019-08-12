package com.zhengsr.viewpagerhelper.activity.loop;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhengsr.viewpagerhelper.GlideApp;
import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.ZoomIndicator;
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
        ZoomIndicator zoomIndicator = findViewById(R.id.bottom_zoom_arc);
        List<ArcBean> beans = new ArrayList<>();
        for (int i = 0; i < RESID.length; i++) {
            ArcBean bean = new ArcBean();
            bean.resId = RESID[i];
            beans.add(bean);
        }

        PageBean pageBean = new PageBean.Builder<ArcBean>()
                .data(beans)
                .indicator(zoomIndicator)
                .builder();

        bannerViewPager.setPageListener(pageBean, R.layout.arc_loop_layout, new PageHelperListener<ArcBean>() {

            @Override
            public void getItemView(View view, ArcBean data) {
                ArcImageView imageView = view.findViewById(R.id.arc_icon);
                GlideApp.with(view)
                        .load(data.resId)
                        .into(imageView);

            }
        });


    }

    class ArcBean {
        public int resId;
    }
}
