package com.zhengsr.viewpagerhelper.activity.loop;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhengsr.viewpagerhelper.GlideApp;
import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerlib.bean.CirBean;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.CircleIndicator;
import com.zhengsr.viewpagerlib.indicator.RectIndicator;
import com.zhengsr.viewpagerlib.type.BannerTransType;
import com.zhengsr.viewpagerlib.type.CircleIndicatorType;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

public class CircleIndicatorActivity extends AppCompatActivity {
    private static final String TAG = "CircleIndicatorActivity";
    private static final int[] RESID = {
            R.mipmap.beauty1,
            R.mipmap.beauty2,
            R.mipmap.beauty3,
    };
    private static final String[] TEXT = {"图像处理", "LSB开发", "游戏开发"};
    private List<TestBean> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mz_loop);
        mDatas = new ArrayList<>();
        //配置数据，这里是resid和text
        for (int i = 0; i < TEXT.length; i++) {
            TestBean bean = new TestBean();
            bean.resId = RESID[i];
            bean.msg = TEXT[i];
            mDatas.add(bean);
        }

        normal();
        cirToRect();
        circle();
        scale();


    }


    private void normal(){
        BannerViewPager bannerViewPager = findViewById(R.id.normal_banner);
        CircleIndicator indicator = findViewById(R.id.normal_indicator);

        bannerViewPager.addIndicator(indicator);
        bannerViewPager.setCurrentPosition(1);

        showBanner(bannerViewPager,indicator);
    }

    private void cirToRect(){
        BannerViewPager bannerViewPager = findViewById(R.id.rect_banner);
        CircleIndicator indicator = findViewById(R.id.rect_indicator);


        showBanner(bannerViewPager, indicator);
    }

    private void circle(){
        BannerViewPager bannerViewPager = findViewById(R.id.move_banner);
        CircleIndicator indicator = findViewById(R.id.move_indicator);

        bannerViewPager.addIndicator(indicator);

        showBanner(bannerViewPager,indicator);
    }

    private void scale(){
        BannerViewPager bannerViewPager = findViewById(R.id.scale_banner);
        CircleIndicator indicator = findViewById(R.id.scale_indicator);

        bannerViewPager.setCurrentPosition(1);
        CirBean cirBean = new CirBean();
        cirBean.type = CircleIndicatorType.SCALE;
        cirBean.cirSize = 20;
        cirBean.scaleFactor = 1.5f;
        cirBean.horizonMargin = 40;
        cirBean.normalColor = Color.GRAY;
        cirBean.selectedColor = Color.WHITE;

        /**
         * 配置 CircleIndicator 的自定义属性
         */
        indicator.addCirBean(cirBean);

        /**
         * 配置 BannerViewPager 的数据
         */
        PageBean bean = new PageBean();
        bean.isAutoLoop = true;
        bean.smoothScrollTime = 400;
        bean.loopTime = 5000;
        bean.transFormer = BannerTransType.DEPATH;

        bannerViewPager.addPageBean(bean)
                .addIndicator(indicator);

        showBanner(bannerViewPager,indicator);

    }



    private void showBanner(BannerViewPager bannerViewPager, CircleIndicator indicator) {
        bannerViewPager.addIndicator(indicator);

        /**
         * 设置监听即可，loop_layout 为要展示的内容，比如一个 ImageView，或者参考示例
         * 其中，setText 为模板方法，为了简便代码，当然还有其他一些方法，可查阅 PageHelperListener
         * onItemClick 为点击事件，当然还有其他方法，重写即可，比如子控件事件 onItemChildClick，如果有子控件
         * 的点击事件，需要先在 bindView 中注册，比如 addChildrenClick(view,R.id.item_text,position)，
         * 其他一些方法，可查阅 PageHelperListener
         */
        bannerViewPager.setPageListener(R.layout.loop_layout, mDatas, new PageHelperListener<TestBean>() {
            @Override
            public void bindView(View view, final TestBean data, int position) {
               setText(view, R.id.loop_text, data.msg);


                //注册子控件事件
                //addChildrenClick(view,R.id.item_text,position);


                ImageView imageView = view.findViewById(R.id.loop_icon);
                GlideApp.with(view)
                        .load(data.resId)
                        .into(imageView);
            }



            @Override
            public void onItemClick(View view, TestBean data, int position) {
                super.onItemClick(view, data, position);
                Toast.makeText(CircleIndicatorActivity.this, data.msg+" "+position, Toast.LENGTH_SHORT).show();
            }


        });
    }


    class TestBean {
        public int resId;
        public String msg;
    }
}
