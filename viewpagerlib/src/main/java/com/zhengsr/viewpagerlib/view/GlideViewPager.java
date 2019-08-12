package com.zhengsr.viewpagerlib.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.indicator.NormalIndicator;
import com.zhengsr.viewpagerlib.indicator.TextIndicator;
import com.zhengsr.viewpagerlib.indicator.TransIndicator;
import com.zhengsr.viewpagerlib.indicator.ZoomIndicator;

import java.util.List;

/**
 * Created by Administrator on 2017/11/9.
 */

public class GlideViewPager extends ViewPager {
    private LayoutInflater mInflater;
    public GlideViewPager(Context context) {
        this(context,null);
    }

    public GlideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
    }
    public void setPageListener(PageBean bean, int layoutid, PageHelperListener listener){
        CusViewPagerAdapter adapter = new CusViewPagerAdapter<>(bean.getParams().getDatas(),layoutid,listener);
        setAdapter(adapter);
        setOffscreenPageLimit(3);
        setCurrentItem(0);
        View indicator = bean.getParams().getIndicator();
        if (indicator != null){
            //选择不同的indicator
            if (indicator instanceof NormalIndicator){
                ((NormalIndicator)indicator).addPagerData(bean,this);
            } if (indicator instanceof TransIndicator){
                ((TransIndicator) indicator).addPagerData(bean,this);
            } if (indicator instanceof ZoomIndicator){
                ((ZoomIndicator) indicator).addPagerData(bean,this);
            }if (indicator instanceof TextIndicator){
                ((TextIndicator) indicator).addPagerData(bean,this);
            }
        }
    }

    class CusViewPagerAdapter<T> extends PagerAdapter{
        PageHelperListener listener;
        List<T> list;
        int layoutid;
        public CusViewPagerAdapter(List<T> list,
                                   int layoutid,PageHelperListener listener) {
            this.listener = listener;
            this.list = list;
            this.layoutid = layoutid;
        }

        @Override
        public int getCount() {
            return this.list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mInflater.inflate(layoutid,null);
            this.listener.getItemView(view, this.list.get(position));
            container.addView(view,0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
