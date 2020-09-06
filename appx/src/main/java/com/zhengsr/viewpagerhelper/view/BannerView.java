package com.zhengsr.viewpagerhelper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerhelper.bean.BannerBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.CircleIndicator;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import java.util.List;

/**
 * Created by zhengshaorui
 * time: 2018/9/2
 */

public class BannerView extends FrameLayout {


    private View mView;
    private BannerViewPager mBannerViewPager;

    public BannerView(@NonNull Context context) {
        this(context,null);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        removeAllViews();
        mView = LayoutInflater.from(getContext()).inflate(R.layout.banner_layout,this,false);
        addView(mView);

        mBannerViewPager = mView.findViewById(R.id.banner);
        CircleIndicator indicator = mView.findViewById(R.id.banner_indicator);

        mBannerViewPager.setCurrentPosition(1);

        mBannerViewPager.addIndicator(indicator);
    }


    public void setData(List<BannerBean> beans){
        if (beans != null && beans.size() >0){

            mBannerViewPager.setPageListener(R.layout.banner_item_layout, beans, new PageHelperListener<BannerBean>() {
                @Override
                public void bindView(View view, BannerBean data, int position) {
                 //   setText(view,R.id.banner_text,data.getTitle());
                    ImageView imageView = view.findViewById(R.id.banner_icon);
                    Glide.with(getContext())
                            .load(data.getImagePath())
                            .into(imageView);
                }
            });

        }
    }




}
