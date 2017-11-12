package com.zhengsr.viewpagerhelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhengsr.viewpagerlib.GlideManager;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.anim.MzTransformer;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.indicator.TextIndicator;
import com.zhengsr.viewpagerlib.indicator.TransIndicator;
import com.zhengsr.viewpagerlib.indicator.ZoomIndicator;
import com.zhengsr.viewpagerlib.view.ArcImageView;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

public class LoopActivity extends AppCompatActivity {


    private static final Integer[] RES = {R.mipmap.guide1,R.mipmap.guide2,R.mipmap.guide3,
            R.mipmap.guide4 };

    private static final String[] RESURL = {
            "http://img.mukewang.com/54bf7e1f000109c506000338-590-330.jpg",
            "http://upload.techweb.com.cn/2015/0114/1421211858103.jpg",
            "http://img1.cache.netease.com/catchpic/A/A0/A0153E1AEDA115EAE7061A0C7EBB69D2.jpg",
            "http://image.tianjimedia.com/uploadImages/2015/202/27/57RF8ZHG8A4T_5020a2a4697650b89" +
                    "c394237ba9ffbb45fe8555a2cbec-6O6nmI_fw658.jpg"};

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
            bean.url = RESURL[i];
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
        mBannerCountViewPager.setPageListener(bean, R.layout.loop_layout, new PageHelperListener() {
            @Override
            public void getItemView(View view, Object data) {
                ImageView imageView = view.findViewById(R.id.loop_icon);
                LoopBean bean = (LoopBean) data;
                new GlideManager.Builder()
                        .setContext(LoopActivity.this)
                        .setImgSource(bean.url)
                        .setLoadingBitmap(R.mipmap.ic_launcher)
                        .setImageView(imageView)
                        .builder();
                TextView textView = view.findViewById(R.id.loop_text);
                textView.setText(bean.text);

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
        arcBannerViewPager.setPageListener(arcbean, R.layout.arc_loop_layout, new PageHelperListener() {
            @Override
            public void getItemView(View view, Object data) {
                ArcImageView imageView = view.findViewById(R.id.arc_icon);
                LoopBean bean = (LoopBean) data;
                new GlideManager.Builder()
                        .setContext(LoopActivity.this)
                        .setImgSource(bean.url)
                        .setLoadingBitmap(R.mipmap.ic_launcher)
                        .setImageView(imageView)
                        .builder();
            }
        });


        //第三个轮播图

        //配置数据
        loopBeens = new ArrayList<>();
        for (int i = 0; i < TEXT.length; i++) {
            LoopBean bean2 = new LoopBean();
            bean2.res = RES[i];
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

        transBannerViewPager.setPageListener(bean, R.layout.loop_layout, new PageHelperListener() {
            @Override
            public void getItemView(View view, Object data) {
                ImageView imageView = view.findViewById(R.id.loop_icon);
                final LoopBean bean = (LoopBean) data;
                new GlideManager.Builder()
                        .setContext(LoopActivity.this)
                        .setImgSource(bean.res)
                        .setLoadingBitmap(R.mipmap.ic_launcher)
                        .setImageView(imageView)
                        .builder();
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

        textBannerViewPager.setPageListener(bean, R.layout.image_layout, new PageHelperListener() {
            @Override
            public void getItemView(View view, Object data) {
                ImageView imageView = view.findViewById(R.id.icon);
                LoopBean bean = (LoopBean) data;
                new GlideManager.Builder()
                        .setContext(LoopActivity.this)
                        .setImgSource(bean.res)
                        .setLoadingBitmap(R.mipmap.ic_launcher)
                        .setImageView(imageView)
                        .builder();


                //如若你要设置点击事件，也可以直接通过这个view 来设置，或者图片的更新等等
            }
        });




    }

    @Override
    protected void onPause() {
        super.onPause();
        mBannerCountViewPager.onPause();


    }

    @Override
    protected void onResume() {
        super.onResume();
        mBannerCountViewPager.onReusme();
    }
}
