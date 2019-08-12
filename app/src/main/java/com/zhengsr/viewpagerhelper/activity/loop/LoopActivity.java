package com.zhengsr.viewpagerhelper.activity.loop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhengsr.viewpagerhelper.GlideApp;
import com.zhengsr.viewpagerhelper.LoopBean;
import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerlib.anim.MzTransformer;
import com.zhengsr.viewpagerlib.anim.CardTransformer;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.TextIndicator;
import com.zhengsr.viewpagerlib.indicator.TransIndicator;
import com.zhengsr.viewpagerlib.indicator.ZoomIndicator;
import com.zhengsr.viewpagerlib.view.ArcImageView;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class LoopActivity extends AppCompatActivity {
    private static final String TAG = "LoopActivity";

    private static final Integer[] RES = {R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3,
            R.mipmap.guide4};

    private static final int[] RESURL = {
            R.mipmap.beauty1,
            R.mipmap.beauty2,
            R.mipmap.beauty3,
            R.mipmap.beauty4

    };


    private static final String[] TEXT = {"图像处理", "LSB开发", "游戏开发","美女测试"};
    private static final int LENGTH = TEXT.length;
    private BannerViewPager mBannerCountViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop);

        // 第一个viewpager
        /*mBannerCountViewPager = findViewById(R.id.loop_viewpager);
        ZoomIndicator zoomIndicator = findViewById(R.id.bottom_scale_layout);


        // 设置viewpager的动画，这里提供了三种，分别是MzTransformer，ZoomOutPageTransformer,
        // 和DepthPageTransformer，可以体验一下
        mBannerCountViewPager.setPageTransformer(false, new MzTransformer());
        //
        mBannerCountViewPager.setPageListener(getPageBean(LENGTH, zoomIndicator), R.layout.loop_layout, new PageHelperListener<LoopBean>() {
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
        arcBannerViewPager.setPageTransformer(true, new MzTransformer());

        arcBannerViewPager.setPageListener(getPageBean(LENGTH, arcZoomIndicator), R.layout.arc_loop_layout, new PageHelperListener<LoopBean>() {
            @Override
            public void getItemView(View view, LoopBean data) {
                ArcImageView imageView = view.findViewById(R.id.arc_icon);
                imageView.setImageResource(data.res);
            }
        });


        //第三个轮播图


        BannerViewPager transBannerViewPager = (BannerViewPager) findViewById(R.id.loop_text2);
        TransIndicator transIndicator = (TransIndicator) findViewById(R.id.bottom_trans_layout);
        //配置pagerbean，这里主要是为了viewpager的指示器的作用，注意记得写上泛型

        transBannerViewPager.setPageListener(getPageBean(2, transIndicator), R.layout.loop_layout, new PageHelperListener<LoopBean>() {
            @Override
            public void getItemView(View view, final LoopBean bean) {
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


        textBannerViewPager.setPageListener(getPageBean(2, textIndicator), R.layout.image_layout, new PageHelperListener<LoopBean>() {
            @Override
            public void getItemView(View view, LoopBean data) {
                ImageView imageView = view.findViewById(R.id.icon);
                GlideApp.with(LoopActivity.this)
                        .load(data.res)
                        .into(imageView);

                //如若你要设置点击事件，也可以直接通过这个view 来设置，或者图片的更新等等
            }
        });
*/

        //第五个viewpager,卡片式布局

        /*BannerViewPager cardViewpager = findViewById(R.id.loop_viewpager_card);
      //  cardViewpager.setPageTransformer(true,new CardTransformer());
        cardViewpager.setPageListener(getPageBean(LENGTH, null,true), R.layout.item_card, new PageHelperListener<LoopBean>() {

            @Override
            public void getItemView(View view, LoopBean data) {
                TextView textView = view.findViewById(R.id.item_card_tv);
                textView.setText(data.text);
               // Log.d(TAG, "zsr getItemView: " + data.text);
            }
        });*/
    }


    private PageBean getPageBean(int count, View indicator,boolean userConfig) {
        if (userConfig){
            return new PageBean.Builder<LoopBean>()
                    .indicator(indicator)
                    .data(getLoopBean(count))
                    .useCode(true)
                    .autoLoop(true)
                    .loopTime(5000)
                    .pagerSwitchTime(6000)
                    .builder();

        }else {
            return new PageBean.Builder<LoopBean>()
                    .data(getLoopBean(count))
                    .indicator(indicator)
                    .builder();
        }
    }


    private List<LoopBean> getLoopBean(int count) {
        List<LoopBean> loopBeens = new ArrayList<>();
        if (count > TEXT.length) {
            return loopBeens;
        }
        for (int i = 0; i < count; i++) {
            LoopBean bean = new LoopBean();
            bean.res = RESURL[i];
            bean.text = TEXT[i];
            loopBeens.add(bean);

        }
        return loopBeens;
    }

    @Override
    protected void onPause() {
        super.onPause();
       // mBannerCountViewPager.stopAnim();


    }

    @Override
    protected void onResume() {
        super.onResume();
       // mBannerCountViewPager.startAnim();
    }

    public void card(View view) {
        startActivity(new Intent(this,CardLoopActivity.class));
    }

    public void mz(View view) {
        startActivity(new Intent(this,MzLoopActivity.class));
    }
}
