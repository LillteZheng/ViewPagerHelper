package com.zhengsr.viewpagerhelper.activity.loop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhengsr.viewpagerhelper.GlideApp;
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
    };
    private static final String[] TEXT = {"图像处理", "LSB开发", "游戏开发"};

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
        //配置数据，这里是resid和text
        for (int i = 0; i < TEXT.length; i++) {
            MzBean bean = new MzBean();
            bean.resId = RESID[i];
            bean.msg = TEXT[i];
            beans.add(bean);
        }

        /**
         * PageBean 必填，记得泛型写上自己的类型
         */
        PageBean pageBean = new PageBean.Builder<MzBean>()
                .data(beans)
                .indicator(zoomIndicator)
                .builder();

        /**
         * 可以在 PageHelperListener 写上泛型，这样就可以直接拿到数据了
         */
        bannerViewPager.setPageListener(pageBean, R.layout.loop_layout, new PageHelperListener<MzBean>() {

            @Override
            public void getItemView(View view, final MzBean data) {
                ImageView imageView = view.findViewById(R.id.loop_icon);
                GlideApp.with(view)
                        .load(data.resId)
                        .into(imageView);
                TextView textView = view.findViewById(R.id.loop_text);
                textView.setText(data.msg);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MzLoopActivity.this, data.msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    class MzBean{
        public int resId;
        public String msg;
    }
}
