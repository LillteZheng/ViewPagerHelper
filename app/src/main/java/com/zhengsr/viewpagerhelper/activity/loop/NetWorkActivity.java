package com.zhengsr.viewpagerhelper.activity.loop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhengsr.viewpagerhelper.GlideApp;
import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerhelper.bean.ArticleData;
import com.zhengsr.viewpagerhelper.bean.BannerBean;
import com.zhengsr.viewpagerhelper.bean.BaseResponse;
import com.zhengsr.viewpagerhelper.bean.PageDataInfo;
import com.zhengsr.viewpagerhelper.rx.HttpCreate;
import com.zhengsr.viewpagerhelper.rx.RxUtils;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.CircleIndicator;
import com.zhengsr.viewpagerlib.indicator.TabIndicator;
import com.zhengsr.viewpagerlib.indicator.TextIndicator;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class NetWorkActivity extends AppCompatActivity {
    private static final String TAG = "NetWorkActivity";
    private static final String WANANDROID_BANNER = "https://www.wanandroid.com/banner/json";

    private RecyclerView mRecyclerView;
    private List<ArticleData> mArticleBeans = new ArrayList<>();
    private ArticleAdapter mAdapter;
    private BannerViewPager mBannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        initView();
    }

    private void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new ArticleAdapter(R.layout.item_article_recy_layout, mArticleBeans);
        View headerView = LayoutInflater.from(this).inflate(R.layout.banner_layout,null);
        mBannerView = headerView.findViewById(R.id.banner);
        CircleIndicator indicator = headerView.findViewById(R.id.banner_indicator);
        mBannerView.addIndicator(indicator);
        mAdapter.addHeaderView(headerView);
        mRecyclerView.setAdapter(mAdapter);

        loadData();

    }

    public void zengjia(View view) {
        loadData();
    }

    private void loadData() {
        //banner
        HttpCreate.getServer().getBanner()
                .compose(RxUtils.<BaseResponse<List<BannerBean>>>rxScheduers())
                .compose(RxUtils.<List<BannerBean>>handleResult())
                .subscribe(new Observer<List<BannerBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<BannerBean> bannerBeans) {

                        mBannerView.setPageListener(R.layout.banner_item_layout, bannerBeans, new PageHelperListener<BannerBean>() {
                            @Override
                            public void bindView(View view, BannerBean data, int position) {
                                ImageView imageView = view.findViewById(R.id.banner_icon);
                                TextView textView = view.findViewById(R.id.banner_text);
                                Glide.with(NetWorkActivity.this)
                                        .load(data.getImagePath())
                                        .into(imageView);
                                textView.setText(data.getTitle());
                            }
                        });


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "zsr - onError: "+e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


        HttpCreate.getServer().getArticle(0)
                .compose(RxUtils.<BaseResponse<PageDataInfo<List<ArticleData>>>>rxScheduers())
                .compose(RxUtils.<PageDataInfo<List<ArticleData>>>handleResult())
                .subscribe(new Observer<PageDataInfo<List<ArticleData>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PageDataInfo<List<ArticleData>> listPageDataInfo) {
                        mAdapter.setNewData(listPageDataInfo.getDatas());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }




    public class ArticleAdapter extends BaseQuickAdapter<ArticleData, BaseViewHolder> {


        public ArticleAdapter(int layoutResId, @Nullable List<ArticleData> data) {
            super(layoutResId, data);
        }


        @Override
        protected void convert(BaseViewHolder helper, ArticleData item) {
            String msg;
            if (!TextUtils.isEmpty(item.getSuperChapterName())){
                msg = item.getSuperChapterName()+"/"+item.getChapterName();
            }else{
                msg = item.getChapterName();
            }
            String author = (item.getAuthor() != null && item.getAuthor().length() > 0) ? item.getAuthor():item.getShareUser();
            helper.setText(R.id.item_article_author,author)
                    .setText(R.id.item_article_chapat, msg)
                    .setText(R.id.item_article_title,item.getTitle())
                    .setText(R.id.item_article_time,item.getNiceDate());

        }


    }
}
