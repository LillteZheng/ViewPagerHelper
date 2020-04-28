package com.zhengsr.viewpagerlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.zhengsr.viewpagerlib.R;
import com.zhengsr.viewpagerlib.ViewPagerHelperUtils;
import com.zhengsr.viewpagerlib.adapter.RAdapter;
import com.zhengsr.viewpagerlib.adapter.RViewholder;
import com.zhengsr.viewpagerlib.anim.CardTransformer;
import com.zhengsr.viewpagerlib.anim.CardTransformer2;
import com.zhengsr.viewpagerlib.anim.DepthPageTransformer;
import com.zhengsr.viewpagerlib.anim.DepthPageTransformer2;
import com.zhengsr.viewpagerlib.anim.MzTransformer;
import com.zhengsr.viewpagerlib.anim.MzTransformer2;
import com.zhengsr.viewpagerlib.anim.ZoomOutPageTransformer;
import com.zhengsr.viewpagerlib.anim.ZoomOutPageTransformer2;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.CircleIndicator;
import com.zhengsr.viewpagerlib.indicator.RectIndicator;
import com.zhengsr.viewpagerlib.indicator.TextIndicator;
import com.zhengsr.viewpagerlib.type.BannerTransType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe: 该类专门支持 viewpager2
 */
public class BannerViewPager2 extends FrameLayout {

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
    //是否自动轮播
    private boolean isAutoLoop;
    //是否填充循环
    private boolean isCycle;
    private int mLoopMaxCount = -1;
    private int mCardHeight;
    private BannerTransType mBannerTransType;
    private View mIndicator;
    private CusViewPagerAdapter adapter;
    /**
     * others
     */
    private int mCurrentIndex = 0;
    private LayoutInflater mInflater;
    private Rect mScreentRect;
    private View mCurrentContent;
    private List<Object> mDatas = new ArrayList<>();
    private PageBean mBean;
    private ViewPager2 mViewPager;

    /**
     * handle
     */
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOOP_MSG) {
                if (isAutoLoop) {
                    mCurrentIndex = mViewPager.getCurrentItem(); //重新获取index
                    if (mCurrentIndex >= LOOP_COUNT / 2) {
                        mCurrentIndex++;
                    }
                    if (mCurrentIndex > LOOP_COUNT) {
                        mCurrentIndex = LOOP_COUNT / 2;
                    }
                    //Log.d(TAG, "zsr --> handleMessage: "+mCurrentIndex);
                    mViewPager.setCurrentItem(mCurrentIndex,true);

                    mHandler.sendEmptyMessageDelayed(LOOP_MSG, mLoopTime);

                }
            }
        }
    };


    public BannerViewPager2(Context context) {
        this(context, null);
    }

    public BannerViewPager2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerViewPager2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BannerViewPager2);
        isAutoLoop = ta.getBoolean(R.styleable.BannerViewPager2_banner2_isAutoLoop, false);
        mLoopTime = ta.getInteger(R.styleable.BannerViewPager2_banner2_looptime, 2000);
        mLoopMaxCount = ta.getInteger(R.styleable.BannerViewPager2_banner2_loop_max_count, -1);
        mCardHeight = ta.getDimensionPixelSize(R.styleable.BannerViewPager2_banner2_card_height, 15);
        isCycle = ta.getBoolean(R.styleable.BannerViewPager2_banner2_iscycle, false);
        int type = ta.getInteger(R.styleable.BannerViewPager2_banner2_transformer, -1);
        /**
         * 如果支持自动轮播，则自动循环填充数据
         */
        if (isAutoLoop) {
            isCycle = true;
        }

        setClipChildren(false);
        setClipToPadding(false);
        if (type != -1) {
            mBannerTransType = BannerTransType.values()[type];
        } else {
            mBannerTransType = BannerTransType.UNKNOWN;
        }


        ta.recycle();
        mInflater = LayoutInflater.from(context);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        mScreentRect = new Rect(0, 0, dm.widthPixels, dm.heightPixels);

        //ViewPager2 为 final 类，不能继承
        mViewPager = new ViewPager2(context);
        FrameLayout.LayoutParams  params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(20,20,20,20);
        mViewPager.setLayoutParams(params);
        removeAllViews();
        addView(mViewPager);
        //设置transformer
        setTransformer(mBannerTransType, mCardHeight);

    }

    private int getStartSelectItem(int readCount) {
        if (readCount == 0) {
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
            while (currentItem % readCount != 0) {
                currentItem++;
            }
            return currentItem;
        } else {
            return 0;
        }
    }

    public BannerViewPager2 setCurrentPosition(int index) {
        mCurrentIndex = index;
        return this;
    }

    /**
     * 配置需要的indicator
     *
     * @param indicator
     * @return
     */
    public BannerViewPager2 addIndicator(View indicator) {
        mIndicator = indicator;
        return this;
    }

    /**
     * 动态配置自定义属性，放在 setPageListener 之前
     *
     * @param bean
     * @return
     */
    public BannerViewPager2 addPageBean(PageBean bean) {
        mBean = bean;
        isAutoLoop = bean.isAutoLoop;
        if (isAutoLoop) {
            isCycle = true;
        }

        if (bean.loopMaxCount != -1) {
            mLoopMaxCount = bean.loopMaxCount;
        }




        if (bean.loopTime != 0) {
            mLoopTime = bean.loopTime;
        }

        if (bean.cardHeight != 0) {
            mCardHeight = bean.cardHeight;
        }

        if (bean.transFormer != BannerTransType.UNKNOWN) {
            mBannerTransType = bean.transFormer;
            setTransformer(mBannerTransType, mCardHeight);
        }

        return this;
    }

    /**
     * 设置数据和监听
     *
     * @param layoutId
     * @param datas
     * @param listener
     * @param <T>
     */
    public <T> void setPageListener(int layoutId, final List<T> datas, final PageHelperListener<T> listener) {
        //先停止动画
        stopAnim();
        mCurrentIndex = 0;
        if (datas == null || datas.isEmpty()) {
            return;
        }
        final int dataCount = datas.size();
        if (mLoopMaxCount != -1) {
            if (dataCount >= mLoopMaxCount) {
                isCycle = true;
            } else {
                isCycle = false;
            }
        }
        mDatas.clear();
        mDatas.addAll(datas);

        /**
         * 判断是否上次有 adapter 的存在
         */

        listener.setDatas(mDatas);
        adapter = new CusViewPagerAdapter<T>(layoutId, datas, listener);
        mViewPager.setAdapter(adapter);
        int startSelectItem = getStartSelectItem(dataCount);
        startSelectItem += mCurrentIndex;
        mViewPager.setCurrentItem(startSelectItem, false);

        mViewPager.setOffscreenPageLimit(3);
        if (mIndicator != null) {
            chooseIndicator(datas.size(), mIndicator);
        }


    }


    /**
     * 选择不同的 indicator
     *
     * @param indicator
     */
    private void chooseIndicator(int count, View indicator) {
        if (indicator instanceof TextIndicator) {
            ((TextIndicator) indicator).addPagerData(count, this);
        } else if (indicator instanceof CircleIndicator) {
            ((CircleIndicator) indicator).addPagerData(count, this);
        } else if (indicator instanceof RectIndicator) {
            ((RectIndicator) indicator).addPagerData(count, this);
        }
    }


    /**
     * 手动停止
     */
    public void stopAnim() {
        if (isAutoLoop) {
            mHandler.removeMessages(LOOP_MSG);
        }
    }

    /**
     * 手动开始
     */
    public void startAnim() {
        if (isAutoLoop) {
            mHandler.removeMessages(LOOP_MSG);
            mHandler.sendEmptyMessageDelayed(LOOP_MSG, mLoopTime);
        }
    }

    /**
     * 设置trnsformer
     *
     * @param transformer
     */
    private void setTransformer(BannerTransType transformer, int cardHeight) {
        if (mViewPager != null) {
            switch (transformer) {
                case CARD:
                    mViewPager.setPageTransformer(new CardTransformer2(cardHeight));
                    break;
                case MZ:
                    mViewPager.setPageTransformer(new MzTransformer2());
                    break;
                case ZOOM:
                    mViewPager.setPageTransformer(new ZoomOutPageTransformer2());
                    break;
                case DEPATH:
                    mViewPager.setPageTransformer(new DepthPageTransformer2());
                    break;
                default:
                    break;
            }

        }
    }


    /**
     * 配置adapter
     *
     * @param <T>
     */

    /**
     * 简单view
     */

    class CusViewPagerAdapter<T> extends RAdapter<T> {

        PageHelperListener listener;

        public CusViewPagerAdapter(int layoutid, List<T> list, PageHelperListener listener) {
            super(layoutid, list, isCycle, listener);
            this.listener = listener;
        }

        @Override
        public void convert(RViewholder holder, T data, int position) {
            listener.bindView(holder.getConserView(), data, position);
            View view = holder.getConserView();

            //viewTouch(view,listener,position);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopAnim();
                break;
            case MotionEvent.ACTION_UP:
                 startAnim();
                break;
                default:break;
        }
        return super.dispatchTouchEvent(ev);
    }


    public ViewPager2 getViewPager() {
        return mViewPager;
    }

    /**
     * 如果退出了，自动停止，进来则自动开始
     *
     * @param visibility
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (isAutoLoop) {
            if (visibility == View.VISIBLE) {
                startAnim();
            } else {
                stopAnim();
            }
        }
    }

    /**
     * 判断是否移出可见屏幕外
     *
     * @return
     */
    public boolean isOutVisiableWindow() {
        int[] pos = new int[2];
        this.getLocationOnScreen(pos);
        //超出屏幕
        boolean isOutVisiabel = pos[1] <= 0 || (pos[1] > (mScreentRect.height() - this.getHeight()));
        return isOutVisiabel;
    }

    @Override
    protected void detachAllViewsFromParent() {
        super.detachAllViewsFromParent();
        mHandler.removeCallbacksAndMessages(null);
    }
}
