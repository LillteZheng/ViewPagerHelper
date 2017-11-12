package com.zhengsr.viewpagerlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.zhengsr.viewpagerlib.R;
import com.zhengsr.viewpagerlib.ViewPagerHelperUtils;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.NormalIndicator;
import com.zhengsr.viewpagerlib.indicator.TextIndicator;
import com.zhengsr.viewpagerlib.indicator.TransIndicator;
import com.zhengsr.viewpagerlib.indicator.ZoomIndicator;

import java.util.List;

/**
 * Created by Administrator on 2017/11/9.
 */

public class BannerViewPager extends ViewPager implements View.OnTouchListener {
    /**
     * const
     */
    private static final String TAG = "zsr";
    private static final int LOOP_MSG = 0x1001;
    private static final int LOOP_COUNT = 5000;
    /**
     * attrs
     */
    private int mLoopTime;
    private boolean isLoop;
    private int mSwitchTime;
    /**
     * others
     */
    private boolean isFirst = true;
    private int mCurrentIndex;
    private LayoutInflater mInflater;
    /**
     * handle
     */
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOOP_MSG){
                mCurrentIndex = getCurrentItem(); //重新获取index
                if (mCurrentIndex >= LOOP_COUNT/2){
                    mCurrentIndex ++;
                }
                if (mCurrentIndex > LOOP_COUNT){
                    mCurrentIndex = LOOP_COUNT/2;
                }
                setCurrentItem(mCurrentIndex);
                mHandler.sendEmptyMessageDelayed(LOOP_MSG,mLoopTime);
            }
        }
    };


    public BannerViewPager(Context context) {
        this(context,null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BannerViewPager);
        isLoop = ta.getBoolean(R.styleable.BannerViewPager_isloop,false);
        mLoopTime = ta.getInteger(R.styleable.BannerViewPager_looptime,2000);
        mSwitchTime = ta.getInteger(R.styleable.BannerViewPager_switchtime,600);
        ta.recycle();
        mInflater = LayoutInflater.from(context);
        setOnTouchListener(this);
        ViewPagerHelperUtils.initSwitchTime(getContext(),this,mSwitchTime);
        if (isLoop){
            mHandler.sendEmptyMessageDelayed(LOOP_MSG,mLoopTime);
        }
    }

    public void setPageListener(PageBean bean, int layoutid, PageHelperListener listener){
        CusViewPagerAdapter adapter = new CusViewPagerAdapter<>(bean.datas,layoutid,listener);
        adapter.notifyDataSetChanged();
        setAdapter(adapter);
        setOffscreenPageLimit(3);
        setCurrentItem(bean.datas.size());
        setCurrentItem(ViewPagerHelperUtils.LOOP_COUNT/2);
        if (bean.bottomLayout != null){
            //选择不同的indicator
            if (bean.bottomLayout instanceof NormalIndicator){
                ((NormalIndicator) bean.bottomLayout).addPagerData(bean,this);
            }
            if (bean.bottomLayout instanceof TransIndicator){
                ((TransIndicator) bean.bottomLayout).addPagerData(bean,this);
            }
            if (bean.bottomLayout instanceof ZoomIndicator){
                ((ZoomIndicator) bean.bottomLayout).addPagerData(bean,this);
            }
            if (bean.bottomLayout instanceof TextIndicator){
                ((TextIndicator) bean.bottomLayout).addPagerData(bean,this);
            }
        }

    }

    /**
     * 当有触摸时停止
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mHandler.removeMessages(LOOP_MSG);
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_UP :
                if (isLoop) {
                    mHandler.sendEmptyMessageDelayed(LOOP_MSG, mLoopTime);
                }
                break;

            default:
                break;
        }
        return false;
    }


    public void onPause(){
        if (isFocused()) {
            mHandler.removeMessages(LOOP_MSG);
        }
    }
    public void onReusme(){
        if (isLoop) {
            if (!isFirst) {
                mHandler.removeMessages(LOOP_MSG);
                mHandler.sendEmptyMessageDelayed(LOOP_MSG, mLoopTime);
            }
        }
    }


    /**
     * 配置adapter
     * @param <T>
     */
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
            return this.list.size()+ ViewPagerHelperUtils.LOOP_COUNT;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mInflater.inflate(layoutid,null);
            this.listener.getItemView(view, this.list.get(position%this.list.size()));
            container.addView(view,0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 处理嵌套有其他滑动viewgroup，抢占事件
     */
    private float lastX,lastY;
    private float moveX,moveY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = ev.getX();
                lastY = ev.getY();
                //保证子 view 能够接受 action_move事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = ev.getX() - lastX;
                float dy = ev.getY() - lastY;
                moveX += Math.abs(dx);
                moveY += Math.abs(dy);
                if (moveX - moveY > 0) {
                     getParent().requestDisallowInterceptTouchEvent(true);
                }else{
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }



}
