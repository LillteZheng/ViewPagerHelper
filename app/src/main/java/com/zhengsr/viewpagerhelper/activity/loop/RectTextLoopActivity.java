package com.zhengsr.viewpagerhelper.activity.loop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhengsr.viewpagerhelper.GlideApp;
import com.zhengsr.viewpagerhelper.LoopBean;
import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.TextIndicator;
import com.zhengsr.viewpagerlib.indicator.TransIndicator;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

public class RectTextLoopActivity extends AppCompatActivity {

    private static final int[] RESID = {
            R.mipmap.beauty1,
            R.mipmap.beauty2
    };


    private static final String[] TEXT = {"图像处理", "LSB开发"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rect_text_loop);

        initRect();
        initText();
    }

    /**
     * 文字效果
     */
    private void initText() {
        BannerViewPager textBannerViewPager = findViewById(R.id.loop_viewpager_text);
        TextIndicator textIndicator = findViewById(R.id.bottom_text_layout);

        textBannerViewPager.setPageListener(getPageBean(2, textIndicator), R.layout.loop_layout,
                new PageHelperListener<DataBean>() {
                    @Override
                    public void getItemView(View view, final DataBean bean) {
                        ImageView imageView = view.findViewById(R.id.loop_icon);
                        GlideApp.with(view)
                                .load(bean.resId)
                                .into(imageView);
                        TextView textView = view.findViewById(R.id.loop_text);
                        textView.setText(bean.msg);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(RectTextLoopActivity.this, bean.msg, Toast.LENGTH_SHORT).show();
                            }
                        });

                        //如若你要设置点击事件，也可以直接通过这个view 来设置，或者图片的更新等等
                    }
                });

    }

    /**
     * 矩形效果
     */
    private void initRect() {
        BannerViewPager transBannerViewPager =  findViewById(R.id.loop_rect);
        TransIndicator transIndicator = findViewById(R.id.bottom_trans_layout);

        transBannerViewPager.setPageListener(getPageBean(2, transIndicator), R.layout.loop_layout,
                new PageHelperListener<DataBean>() {
            @Override
            public void getItemView(View view, final DataBean bean) {
                ImageView imageView = view.findViewById(R.id.loop_icon);
                GlideApp.with(view)
                        .load(bean.resId)
                        .into(imageView);
                TextView textView = view.findViewById(R.id.loop_text);
                textView.setText(bean.msg);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(RectTextLoopActivity.this, bean.msg, Toast.LENGTH_SHORT).show();
                    }
                });

                //如若你要设置点击事件，也可以直接通过这个view 来设置，或者图片的更新等等
            }
        });
    }


    private PageBean getPageBean(int count, View indicator) {

        List<DataBean> beans = new ArrayList<>();
        for (int i = 0; i < TEXT.length; i++) {
            DataBean bean = new DataBean();
            bean.resId = RESID[i];
            bean.msg = TEXT[i];
            beans.add(bean);
        }

        return new PageBean.Builder<DataBean>()
                .data(beans)
                .indicator(indicator)
                .builder();

    }

    class DataBean{
        public int resId;
        public String msg;
    }
}
