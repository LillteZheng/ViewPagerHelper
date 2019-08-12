package com.zhengsr.viewpagerhelper.activity.loop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.ZoomIndicator;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

public class MzLoopActivity extends AppCompatActivity {

    private static final int[] RESID = {
            R.mipmap.beauty1,
            R.mipmap.beauty2,
            R.mipmap.beauty3,
            R.mipmap.beauty4

    };
    private static final String[] TEXT = {"图像处理", "LSB开发", "游戏开发","美女测试"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mz_loop);

        initView();
    }

    private void initView() {
        BannerViewPager bannerViewPager = findViewById(R.id.loop_viewpager_mz);
        ZoomIndicator zoomIndicator = findViewById(R.id.scale_indicator);
        List<MzBean> beans = new ArrayList<>();
        for (int i = 0; i < TEXT.length; i++) {
            MzBean bean = new MzBean();
            bean.resId = RESID[i];
            bean.msg = TEXT[i];
            beans.add(bean);
        }

        PageBean pageBean = new PageBean.Builder<MzBean>()
                .data(beans)
                .indicator(zoomIndicator)
                .builder();

        bannerViewPager.setPageListener(pageBean, R.layout.loop_layout, new PageHelperListener<MzBean>() {

            @Override
            public void getItemView(View view, MzBean data) {
                ImageView imageView = view.findViewById(R.id.loop_icon);
                imageView.setImageResource(data.resId);
                TextView textView = view.findViewById(R.id.loop_text);
                textView.setText(data.msg);
            }
        });


    }

    class MzBean{
        public int resId;
        public String msg;
    }
}
