package com.zhengsr.viewpagerlib;

import android.content.Context;
import android.widget.Scroller;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import java.lang.reflect.Field;

/**
 * Created by zhengshaorui on 2017/11/5.
 * csdn: http://blog.csdn.net/u011418943
 */

public class ViewPagerHelperUtils {
    public static final int LOOP_COUNT = 5000;
    public static final int LOOP_TAIL_MODE = 0x1001;

    public static final int LOOP_MODE = 0x1002;
    public static final int GLIDE_MODE = 0x1002;

    public static final int VIEWPAGER_DATA_URL = 0x2002;
    public static final int VIEWPAGER_DATA_RES = 0x2003;
    public static final int VIEWPAGER_DATA_VIEW = 0x2004;



    /**
     * 设置viewpager 之间的切换速度
     */
    public static void initSwitchTime(Context context, ViewPager viewPager, int time){
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            field.set(viewPager,new ViewPagerScroller(context,time));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ViewPagerScroller extends Scroller {
        int time;
        public ViewPagerScroller(Context context, int time) {
            super(context);
            this.time = time;
        }

        /*@Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, time);
        }
*/
        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, time);
        }
    }


    public static void initSwitchTime(Context context,ViewPager2 viewPager2,int time) {
        try {
            //控制切换速度，采用反射方。法方法只会调用一次，替换掉内部的RecyclerView的LinearLayoutManager
            RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
            recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
            ProxyLayoutManger proxyLayoutManger = new ProxyLayoutManger(context, viewPager2.getOrientation(),time);
            recyclerView.setLayoutManager(proxyLayoutManger);

            Field LayoutMangerField = ViewPager2.class.getDeclaredField("mLayoutManager");
            LayoutMangerField.setAccessible(true);
            LayoutMangerField.set(viewPager2, proxyLayoutManger);

            Field pageTransformerAdapterField = ViewPager2.class.getDeclaredField("mPageTransformerAdapter");
            pageTransformerAdapterField.setAccessible(true);
            Object mPageTransformerAdapter = pageTransformerAdapterField.get(viewPager2);
            if (mPageTransformerAdapter != null) {
                Class<?> aClass = mPageTransformerAdapter.getClass();
                Field layoutManager = aClass.getDeclaredField("mLayoutManager");
                layoutManager.setAccessible(true);
                layoutManager.set(mPageTransformerAdapter, proxyLayoutManger);
            }
            Field scrollEventAdapterField = ViewPager2.class.getDeclaredField("mScrollEventAdapter");
            scrollEventAdapterField.setAccessible(true);
            Object mScrollEventAdapter = scrollEventAdapterField.get(viewPager2);
            if (mScrollEventAdapter != null) {
                Class<?> aClass = mScrollEventAdapter.getClass();
                Field layoutManager = aClass.getDeclaredField("mLayoutManager");
                layoutManager.setAccessible(true);
                layoutManager.set(mScrollEventAdapter, proxyLayoutManger);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static class ProxyLayoutManger extends LinearLayoutManager {

        int time;
        ProxyLayoutManger(Context context, int orientation,int time) {
            super(context, orientation, false);
            this.time = time;
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
                @Override
                protected int calculateTimeForDeceleration(int dx) {
                    return (int) (time * (1 - .3356));
                }
            };
            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }
    }

}
