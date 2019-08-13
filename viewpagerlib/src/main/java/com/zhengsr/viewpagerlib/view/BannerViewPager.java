package com.zhengsr.viewpagerlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.zhengsr.viewpagerlib.R;
import com.zhengsr.viewpagerlib.ViewPagerHelperUtils;
import com.zhengsr.viewpagerlib.anim.CardTransformer;
import com.zhengsr.viewpagerlib.anim.DepthPageTransformer;
import com.zhengsr.viewpagerlib.anim.MzTransformer;
import com.zhengsr.viewpagerlib.anim.ZoomOutPageTransformer;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.NormalIndicator;
import com.zhengsr.viewpagerlib.indicator.TextIndicator;
import com.zhengsr.viewpagerlib.indicator.TransIndicator;
import com.zhengsr.viewpagerlib.indicator.ZoomIndicator;
import com.zhengsr.viewpagerlib.type.BannerTransType;

import java.util.List;

/**
 * Created by Administrator on 2017/11/9.
 */

public class BannerViewPager extends ViewPager implements View.OnTouchListener {
    /**
     * const
     */
    private static final String TAG = "BannerViewPager";
    private static final int LOOP_MSG = 0x1001;
    private static final int LOOP_COUNT = 5000;
    /**
     * attrs
     */
    private int mLoopTime;
    private boolean isAutoLoop; //是否自动轮播
    private boolean isCycle; //是否填充循环
    private int mSwitchTime;
    private int mLoopMaxCount = -1;
    //private boolean isCanLoopMove; //是否可以轮播滑动

    private int mCardHeigth;
    private BannerTransType mBannerTransType;
    /**
     * others
     */
    private int mCurrentIndex;
    private LayoutInflater mInflater;
    private Rect mScreentRect;
    private View mCurrentContent;
    /**
     * handle
     */
    private  Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOOP_MSG){
                if (isAutoLoop) {
                    isDataConfigFinish = true;
                    mCurrentIndex = getCurrentItem(); //重新获取index
                    if (mCurrentIndex >= LOOP_COUNT / 2) {
                        mCurrentIndex++;
                    }
                    if (mCurrentIndex > LOOP_COUNT) {
                        mCurrentIndex = LOOP_COUNT / 2;
                    }
                    //Log.d(TAG, "zsr --> handleMessage: "+mCurrentIndex);
                    setCurrentItem(mCurrentIndex);

                    mHandler.sendEmptyMessageDelayed(LOOP_MSG, mLoopTime);

                }
            }
        }
    };



    public BannerViewPager(Context context) {
        this(context,null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BannerViewPager);
        isAutoLoop = ta.getBoolean(R.styleable.BannerViewPager_banner_isAutoLoop,false);
        mLoopTime = ta.getInteger(R.styleable.BannerViewPager_banner_looptime,2000);
        mSwitchTime = ta.getInteger(R.styleable.BannerViewPager_banner_switchtime,600);
        mLoopMaxCount = ta.getInteger(R.styleable.BannerViewPager_banner_loop_max_count,-1);
        mCardHeigth = ta.getDimensionPixelSize(R.styleable.BannerViewPager_banner_card_height,15);
        isCycle = ta.getBoolean(R.styleable.BannerViewPager_banner_iscycle,false);

        if (mLoopMaxCount != -1){
            isCycle = false;
        }

        int type = ta.getInteger(R.styleable.BannerViewPager_banner_transformer,-1);
        if (type != -1){
            mBannerTransType = BannerTransType.values()[type];
        }else{
            mBannerTransType = BannerTransType.UNKNOWN;
        }
        //设置transformer
        setTransformer(mBannerTransType, mCardHeigth);

        ta.recycle();
        mInflater = LayoutInflater.from(context);
        setOnTouchListener(this);
        ViewPagerHelperUtils.initSwitchTime(getContext(),this,mSwitchTime);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        mScreentRect = new Rect(0,0,dm.widthPixels,dm.heightPixels);

    }


    private int getStartSelectItem(int readCount){
        if(readCount == 0){
            return 0;
        }
        if (isCycle) {
            int count = ViewPagerHelperUtils.LOOP_COUNT / 2;
            // 我们设置当前选中的位置为Integer.MAX_VALUE / 2,这样开始就能往左滑动
            // 但是要保证这个值与getRealPosition 的 余数为0，因为要从第一页开始显示
            int currentItem = count;
            if (count % readCount == 0) {
                return currentItem;
            }
            // 直到找到从0开始的位置
            while (currentItem % readCount != 0){
                currentItem++;
            }
            return currentItem;
        }else{
            return 0;
        }
    }



    /**
     * 设置监听
     * @param bean 配置的数据
     * @param layoutid 子控件的 layout
     * @param listener 这里可以把 子控件的 layout 获得的view公布出去
     */
    public void setPageListener(final PageBean bean, int layoutid, final PageHelperListener listener){
        final PageBean.Builder params = bean.getParams();
        //动态配置参数
        configParams(params);
        if (mLoopMaxCount != -1 && params.getDatas().size() >= mLoopMaxCount){
            isCycle = true;
        }
        CusViewPagerAdapter adapter = new CusViewPagerAdapter<>(params.getDatas(),layoutid,listener);
        adapter.notifyDataSetChanged();
        setAdapter(adapter);
        int startSelectItem = getStartSelectItem(params.getDatas().size());
        Log.d(TAG, "zsr setPageListener: "+mLoopMaxCount+" "+isCycle+" "+startSelectItem);
        setCurrentItem(startSelectItem);
        setOffscreenPageLimit(3);
        View indicator = params.getIndicator();
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

        }else {
            addOnPageChangeListener(new OnPageChangeListener() {

                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int position) {
                    Log.d(TAG, "zsr onPageSelected: "+position);
                    if (!isCycle) {
                        listener.getItemView(mCurrentContent,params.getDatas().get(position%params.getDatas().size()));
                    }
                }

                @Override
                public void onPageScrollStateChanged(int i) {
                }
            });
        }


    }

    private void configParams(PageBean.Builder params) {
        if (params.isUseCode()){
            isAutoLoop = params.isAutoLoop();
            isCycle = params.isCycle();
            if (params.getLoopMaxCount() != -1) {
                mLoopMaxCount = params.getLoopMaxCount();
                isCycle = false;
            }
            if (params.getPagerSwitchTime() != -1) {
                mSwitchTime = params.getPagerSwitchTime();
            }
            if (params.getLoopTime() != -1) {
                mLoopTime = params.getLoopTime();
            }
            if (params.getCardHeight() != -1){
                mCardHeigth = params.getCardHeight();
            }
            if (params.getBannerTransformer() != BannerTransType.UNKNOWN) {
                mBannerTransType = params.getBannerTransformer();
            }
            setTransformer(mBannerTransType, mCardHeigth);
        }
    }

    /**
     * 当有触摸时停止
     */
    private boolean isDataConfigFinish = false;
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mHandler.removeMessages(LOOP_MSG);
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                isDataConfigFinish = false;
                break;
            case MotionEvent.ACTION_UP :
                isDataConfigFinish = true;
                if (isAutoLoop) {
                    mHandler.sendEmptyMessageDelayed(LOOP_MSG, mLoopTime);
                }
                break;

            default:
                break;
        }
        return false;
    }

    /**
     * 手动停止
     */
    public void stopAnim(){
        if (isAutoLoop) {
            mHandler.removeMessages(LOOP_MSG);
        }
    }

    /**
     * 手动开始
     */
    public void startAnim(){
        if (isAutoLoop) {
            mHandler.removeMessages(LOOP_MSG);
            mHandler.sendEmptyMessageDelayed(LOOP_MSG, mLoopTime);


        }
    }

    /**
     * 设置trnsformer
     * @param transformer
     */
    private void setTransformer(BannerTransType transformer,int cardHeight) {
        switch (transformer){
            case CARD:
                setPageTransformer(true,new CardTransformer(cardHeight));
                break;
            case MZ:
                setPageTransformer(false,new MzTransformer());
                break;
            case ZOOM:
                setPageTransformer(false,new ZoomOutPageTransformer());
                break;
            case DEPATH:
                setPageTransformer(false,new DepthPageTransformer());
                break;

            default:break;
        }
    }


    /**
     * 配置adapter
     * @param <T>
     */
    class CusViewPagerAdapter<T> extends PagerAdapter{
        PageHelperListener listener;
        List<T> list;
        int layoutid ;

        public CusViewPagerAdapter(List<T> list, @Nullable int layoutId, PageHelperListener listener) {
            this.listener = listener;
            this.list = list;
            this.layoutid = layoutId;

        }

        @Override
        public int getCount() {
            if (isCycle) {
                return this.list.size() + ViewPagerHelperUtils.LOOP_COUNT;
            }else{
                return this.list.size();
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            mCurrentContent = mInflater.inflate(layoutid,BannerViewPager.this,false);
            final int index ;
            /*if (isCycle) {
                int cacheSize = BannerViewPager.this.getOffscreenPageLimit();
                if (cacheSize == list.size() - 1 && isDataConfigFinish) {
                    if (position > ViewPagerHelperUtils.LOOP_COUNT / 2) {
                        position++;
                    } else {
                        position--;
                    }
                }
            }*/
            index = position % list.size();
            if (isCycle) {
                listener.getItemView(mCurrentContent, this.list.get(index));
            }else{
                position = position % list.size();
                listener.getItemView(mCurrentContent,this.list.get(position));
            }
            container.addView(mCurrentContent);
            return mCurrentContent;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }





    /**
     * 如果退出了，自动停止，进来则自动开始
     * @param visibility
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (isAutoLoop){
            if (visibility == View.VISIBLE){
                startAnim();
            }else{
               stopAnim();
            }
        }
    }

    /**
     * 判断是否移出可见屏幕外
     * @return
     */
    public boolean isOutVisiableWindow(){
        int[] pos = new int[2];
        this.getLocationOnScreen(pos);
        //超出屏幕
        boolean isOutVisiabel = pos[1] <= 0 || (pos[1] > (mScreentRect.height() - this.getHeight()));
        return isOutVisiabel ;
    }

    @Override
    protected void detachAllViewsFromParent() {
        super.detachAllViewsFromParent();
        mHandler.removeCallbacksAndMessages(null);
    }

}
