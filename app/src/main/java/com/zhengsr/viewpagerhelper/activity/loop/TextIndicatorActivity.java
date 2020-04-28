package com.zhengsr.viewpagerhelper.activity.loop;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zhengsr.viewpagerhelper.GlideApp;
import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.TextIndicator;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

public class TextIndicatorActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_text_indicator);
        mDatas = new ArrayList<>();
        //配置数据，这里是resid和text
        for (int i = 0; i < TEXT.length; i++) {
            TestBean bean = new TestBean();
            bean.resId = RESID[i];
            bean.msg = TEXT[i];
            mDatas.add(bean);
        }
        BannerViewPager bannerViewPager = findViewById(R.id.normal_banner);
        TextIndicator indicator = findViewById(R.id.normal_indicator);

        bannerViewPager.addIndicator(indicator);

        bannerViewPager.setPageListener(R.layout.image_layout, mDatas, new PageHelperListener<TestBean>() {
            @Override
            public void bindView(View view, TestBean data, int position) {

                ImageView imageView = view.findViewById(R.id.icon);
                GlideApp.with(view)
                        .load(data.resId)
                        .into(imageView);
            }
        });


    }

    class TestBean {
        public int resId;
        public String msg;
    }
}
