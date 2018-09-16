package com.zhengsr.viewpagerhelper.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhengsr.viewpagerhelper.GlideApp;
import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerhelper.bean.BannerBean;
import com.zhengsr.viewpagerhelper.rx.RxNetClient;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.TextIndicator;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NetWorkActivity extends AppCompatActivity {
    private static final String WANANDROID_BANNER = "http://www.wanandroid.com/banner/json";
    private FrameLayout mFrameLayout;
    private BannerViewPager mBannerViewPager;
    private TextIndicator mTextIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        initView();
    }

    private void initView() {
        mFrameLayout = findViewById(R.id.content);

        mBannerViewPager = (BannerViewPager) findViewById(R.id.recy_banner);
        mTextIndicator = (TextIndicator) findViewById(R.id.recy_text_indicator);


        loadBannerData();
    }


    public void loadBannerData(){
        Observable<String> observable = new RxNetClient.Builder()
                .setUrl(WANANDROID_BANNER)
                .builder()
                .get();
         observable.subscribeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<List<BannerBean>>>() {
                    @Override
                    public ObservableSource<List<BannerBean>> apply(String s) throws Exception {
                        JSONObject jsonObject = JSON.parseObject(s);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        List<BannerBean> beans = JSON.parseArray(jsonArray.toJSONString(), BannerBean.class);
                        return Observable.just(beans);
                    }
                })
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Consumer<List<BannerBean>>() {
                     @Override
                     public void accept(List<BannerBean> bannerBeans) throws Exception {
                         PageBean bean = new PageBean.Builder<BannerBean>()
                                 .setDataObjects(bannerBeans)
                                 .setIndicator(mTextIndicator)
                                 .builder();

                         mBannerViewPager.setPageListener(bean, R.layout.loop_layout, new PageHelperListener<BannerBean>() {
                             @Override
                             public void getItemView(View view, BannerBean data) {
                                 mFrameLayout.setVisibility(View.GONE);
                                 ImageView imageView = view.findViewById(R.id.loop_icon);
                                 TextView textView = view.findViewById(R.id.loop_text);

                                 GlideApp.with(view)
                                         .load(data.getImagePath())
                                         .into(imageView);
                                 textView.setText(data.getTitle());
                             }

                         });
                     }
                 });

    }


}
