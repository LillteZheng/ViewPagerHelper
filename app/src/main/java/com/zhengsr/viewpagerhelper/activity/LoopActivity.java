package com.zhengsr.viewpagerhelper.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhengsr.viewpagerhelper.GlideApp;
import com.zhengsr.viewpagerhelper.LoopBean;
import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerlib.anim.MzTransformer;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.TextIndicator;
import com.zhengsr.viewpagerlib.indicator.TransIndicator;
import com.zhengsr.viewpagerlib.indicator.ZoomIndicator;
import com.zhengsr.viewpagerlib.view.ArcImageView;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

public class LoopActivity extends AppCompatActivity {
    private static final String TAG = "LoopActivity";

    private static final Integer[] RES = {R.mipmap.guide1,R.mipmap.guide2,R.mipmap.guide3,
            R.mipmap.guide4 };

    private static final int[] RESURL = {
           R.mipmap.beauty1,
           R.mipmap.beauty2,
           R.mipmap.beauty3,
           R.mipmap.beauty4,};


    private static final String[] TEXT = {"图像处理","LSB开发","游戏开发","梦想"};
    private BannerViewPager mBannerCountViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop);

        // 第一个viewpager
        mBannerCountViewPager = (BannerViewPager) findViewById(R.id.loop_viewpager);
        ZoomIndicator zoomIndicator = (ZoomIndicator) findViewById(R.id.bottom_scale_layout);


        //配置数据
        List<LoopBean> loopBeens = new ArrayList<>();
        for (int i = 0; i < TEXT.length; i++) {
           LoopBean bean = new LoopBean();
            bean.res = RESURL[i];
            bean.text = TEXT[i];
            loopBeens.add(bean);

        }
        //配置pagerbean，这里主要是为了viewpager的指示器的作用，注意记得写上泛型
        PageBean bean = new PageBean.Builder<LoopBean>()
                .setDataObjects(loopBeens)
                .setIndicator(zoomIndicator)
                .builder();
        // 设置viewpager的动画，这里提供了三种，分别是MzTransformer，ZoomOutPageTransformer,
        // 和DepthPageTransformer，可以体验一下
        mBannerCountViewPager.setPageTransformer(false,new MzTransformer());
        //
        mBannerCountViewPager.setPageListener(bean, R.layout.loop_layout, new PageHelperListener<LoopBean>() {
            @Override
            public void getItemView(View view, LoopBean data) {

                ImageView imageView = view.findViewById(R.id.loop_icon);
                imageView.setImageResource(data.res);
                TextView textView = view.findViewById(R.id.loop_text);
                textView.setText(data.text);

                //如若你要设置点击事件，也可以直接通过这个view 来设置，或者图片的更新等等
            }
        });


        //第二个轮播图

        BannerViewPager arcBannerViewPager = (BannerViewPager) findViewById(R.id.loop_viewpager_arc);
        ZoomIndicator arcZoomIndicator = (ZoomIndicator) findViewById(R.id.bottom_zoom_arc);
        arcBannerViewPager.setPageTransformer(false,new MzTransformer());
        PageBean arcbean = new PageBean.Builder<LoopBean>()
                .setDataObjects(loopBeens)
                .setIndicator(arcZoomIndicator)
                .builder();
        arcBannerViewPager.setPageListener(arcbean, R.layout.arc_loop_layout, new PageHelperListener<LoopBean>() {
            @Override
            public void getItemView(View view, LoopBean data) {
                ArcImageView imageView = view.findViewById(R.id.arc_icon);
                imageView.setImageResource(data.res);
            }
        });


        //第三个轮播图

        //配置数据
        loopBeens = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            LoopBean bean2 = new LoopBean();
            bean2.res = RESURL[i];
            bean2.text = TEXT[i];
            loopBeens.add(bean2);

        }
        BannerViewPager transBannerViewPager = (BannerViewPager) findViewById(R.id.loop_text2);
        TransIndicator transIndicator = (TransIndicator) findViewById(R.id.bottom_trans_layout);
        //配置pagerbean，这里主要是为了viewpager的指示器的作用，注意记得写上泛型
        bean = new PageBean.Builder<LoopBean>()
                .setDataObjects(loopBeens)
                .setIndicator(transIndicator)
                .builder();

        transBannerViewPager.setPageListener(bean, R.layout.loop_layout, new PageHelperListener<LoopBean>() {
            @Override
            public void getItemView(View view,final LoopBean bean) {
                ImageView imageView = view.findViewById(R.id.loop_icon);
                imageView.setImageResource(bean.res);
                TextView textView = view.findViewById(R.id.loop_text);
                textView.setText(bean.text);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(LoopActivity.this, bean.text, Toast.LENGTH_SHORT).show();
                    }
                });

                //如若你要设置点击事件，也可以直接通过这个view 来设置，或者图片的更新等等
            }
        });

        //第四个轮播图

        BannerViewPager textBannerViewPager = (BannerViewPager) findViewById(R.id.loop_viewpager_text);
        TextIndicator textIndicator = (TextIndicator) findViewById(R.id.bottom_text_layout);


        bean = new PageBean.Builder<LoopBean>()
                .setDataObjects(loopBeens)
                .setIndicator(textIndicator)
                .builder();

        textBannerViewPager.setPageListener(bean, R.layout.image_layout,new PageHelperListener<LoopBean>() {
            @Override
            public void getItemView(View view, LoopBean data) {
                ImageView imageView = view.findViewById(R.id.icon);
                GlideApp.with(LoopActivity.this)
                        .load(data.res)
                        .into(imageView);

                //如若你要设置点击事件，也可以直接通过这个view 来设置，或者图片的更新等等
            }
        });




    }

    @Override
    protected void onPause() {
        super.onPause();
        mBannerCountViewPager.stopAnim();


    }

    @Override
    protected void onResume() {
        super.onResume();
        mBannerCountViewPager.startAnim();
    }
}
