package com.zhengsr.viewpagerhelper.activity.loop;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhengsr.viewpagerhelper.GlideApp;
import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerlib.bean.CirBean;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.bean.RectBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.CircleIndicator;
import com.zhengsr.viewpagerlib.indicator.RectIndicator;
import com.zhengsr.viewpagerlib.type.BannerTransType;
import com.zhengsr.viewpagerlib.type.CircleIndicatorType;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

public class RectIndicatorActivity extends AppCompatActivity {
    private static final String TAG = "RectIndicatorActivity";
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
        setContentView(R.layout.activity_rect_inficator);
        mDatas = new ArrayList<>();
        //配置数据，这里是resid和text
        for (int i = 0; i < TEXT.length; i++) {
            TestBean bean = new TestBean();
            bean.resId = RESID[i];
            bean.msg = TEXT[i];
            mDatas.add(bean);
        }

        //todo  viewpager 默认选中某个，indicator 可以点击

        normal();
        config();

    }



    private void normal(){
        BannerViewPager bannerViewPager = findViewById(R.id.normal_banner);
        RectIndicator indicator = findViewById(R.id.normal_indicator);

        bannerViewPager.addIndicator(indicator);

        showBanner(bannerViewPager);
    }




    private void showBanner(BannerViewPager bannerViewPager) {

        bannerViewPager.setPageListener(R.layout.loop_layout, mDatas, new PageHelperListener<TestBean>() {
            @Override
            public void bindView(View view, final TestBean data, int position) {
                setText(view, R.id.loop_text, data.msg);
                ImageView imageView = view.findViewById(R.id.loop_icon);
                GlideApp.with(view)
                        .load(data.resId)
                        .into(imageView);
            }

            @Override
            public void onItemClick(View view, TestBean data, int position) {
                super.onItemClick(view, data, position);
                Toast.makeText(RectIndicatorActivity.this, data.msg+" "+position, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void config() {
        BannerViewPager bannerViewPager = findViewById(R.id.scale_banner);
        RectIndicator indicator = findViewById(R.id.scale_indicator);

        RectBean rectBean = new RectBean();
        rectBean.horizonMargin = 30;
        rectBean.normalColor = Color.GRAY;
        rectBean.selectedColor = Color.WHITE;
        rectBean.width = 30;
        rectBean.height = 10;
        rectBean.roundRadius = 5;

        /**
         * 配置 CircleIndicator 的自定义属性
         */
        indicator.addRectBean(rectBean);

        /**
         * 配置 BannerViewPager 的数据
         */
        PageBean bean = new PageBean();
        bean.isAutoLoop = true;
        bean.smoothScrollTime = 400;
        bean.loopTime = 5000;
        bean.transFormer = BannerTransType.MZ;

        bannerViewPager.addPageBean(bean)
                .addIndicator(indicator)
                .setCurrentPosition(1);

        showBanner(bannerViewPager);
    }

    class TestBean {
        public int resId;
        public String msg;

        @Override
        public String toString() {
            return "TestBean{" +
                    "resId=" + resId +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }
}
