package com.zhengsr.viewpagerhelper.activity.loop;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.view.BannerViewPager2;

import java.util.ArrayList;
import java.util.List;

public class CardLoopActivity extends AppCompatActivity {


    String[] TEXT = new String[]{"1","2","3","4"};
    private BannerViewPager2 mBannerViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_loop);

        initView();
    }

    private void initView() {
        mBannerViewPager = findViewById(R.id.loop_viewpager_card);

        List<CardBean> beans = new ArrayList<>();
        for (int i = 0; i < TEXT.length; i++) {
            CardBean bean = new CardBean();
            bean.msg = TEXT[i];
            beans.add(bean);
        }

        mBannerViewPager.setCurrentPosition(1);


       mBannerViewPager.setPageListener(R.layout.item_card, beans, new PageHelperListener<CardBean>() {
           @Override
           public void bindView(View view, CardBean data, int position) {
               TextView textView = view.findViewById(R.id.item_card_tv);
               textView.setText(data.msg);
           }
       });

    }


    class CardBean{
        public String msg;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBannerViewPager.stopAnim();
    }
}
