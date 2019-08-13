package com.zhengsr.viewpagerhelper.activity.loop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.type.BannerTransType;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

public class CardLoopActivity extends AppCompatActivity {


    String[] TEXT = new String[]{"1","2","3","4"};
    private BannerViewPager mBannerViewPager;

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

        PageBean pageBean = new PageBean.Builder<CardBean>()
                .useCode(true) //比填，不然不起作用
                .autoLoop(true) //自动轮播
                .pagerSwitchTime(600) //切换速度
                .loopTime(4000)  //轮播事件
                .data(beans) //添加数据
                .cardHeight(30)  //卡片的高度
                .cycle(true)  //是否填充可以循环
                .bannerTransformer(BannerTransType.CARD)  //设置 transformer，即卡片效果，也可以是魅族，zoom，depath的效果
                .builder();


        /**
         * 配置数据，记得在 PageHelperListener 配置你的泛型数据哦
         */
        mBannerViewPager.setPageListener(pageBean, R.layout.item_card, new PageHelperListener<CardBean>() {

            @Override
            public void getItemView(View view, CardBean data) {
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
