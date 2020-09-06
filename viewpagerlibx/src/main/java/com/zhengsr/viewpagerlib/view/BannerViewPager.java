package com.zhengsr.viewpagerlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.zhengsr.viewpagerlib.R;
import com.zhengsr.viewpagerlib.ViewPagerHelperUtils;
import com.zhengsr.viewpagerlib.anim.CardTransformer;
import com.zhengsr.viewpagerlib.anim.DepthPageTransformer;
import com.zhengsr.viewpagerlib.anim.Itransformer;
import com.zhengsr.viewpagerlib.anim.MzTransformer;
import com.zhengsr.viewpagerlib.anim.ZoomOutPageTransformer;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.CircleIndicator;
import com.zhengsr.viewpagerlib.indicator.RectIndicator;
import com.zhengsr.viewpagerlib.indicator.TextIndicator;
import com.zhengsr.viewpagerlib.type.BannerTransType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/9.
 */

public class BannerViewPager extends ViewPager {
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
    private int mSmoothTime;
    private int mLoopMaxCount = -1;
    private int mCardHeight;
    private BannerTransType mBannerTransType;
    private View mIndicator;
    /**
     * others
     */
    private int mCurrentIndex = 0;
    private LayoutInflater mInflater;
    private Rect mScreentRect;
    private View mCurrentContent;
    private List<Object> mDatas = new ArrayList<>();
    /**
     * handle
     */
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOOP_MSG) {
                if (isAutoLoop) {
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
    private CusViewPagerAdapter adapter;
    private int mDataCount;


    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BannerViewPager);
        isAutoLoop = ta.getBoolean(R.styleable.BannerViewPager_banner_isAutoLoop, false);
        mLoopTime = ta.getInteger(R.styleable.BannerViewPager_banner_looptime, 2000);
        mSmoothTime = ta.getInteger(R.styleable.BannerViewPager_banner_switchtime, 600);
        mLoopMaxCount = ta.getInteger(R.styleable.BannerViewPager_banner_loop_max_count, -1);
        mCardHeight = ta.getDimensionPixelSize(R.styleable.BannerViewPager_banner_card_height, 15);
        isCycle = ta.getBoolean(R.styleable.BannerViewPager_banner_iscycle, false);
        int type = ta.getInteger(R.styleable.BannerViewPager_banner_transformer, -1);
        /**
         * 如果支持自动轮播，则自动循环填充数据
         */
        if (isAutoLoop) {
            isCycle = true;
        }

        if (type != -1) {
            mBannerTransType = BannerTransType.values()[type];
        } else {
            mBannerTransType = BannerTransType.UNKNOWN;
        }
        //设置transformer
        setTransformer(mBannerTransType, mCardHeight);

        ta.recycle();
        mInflater = LayoutInflater.from(context);
        ViewPagerHelperUtils.initSwitchTime(getContext(), this, mSmoothTime);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        mScreentRect = new Rect(0, 0, dm.widthPixels, dm.heightPixels);

    }

    private boolean firstLayout = true;
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //处理因为recyclerview的回收机制，导致轮播图不起作用的问题
        if (getAdapter() != null) {
            try {
                Field mFirstLayout = ViewPager.class.getDeclaredField("mFirstLayout");
                mFirstLayout.setAccessible(true);
                mFirstLayout.set(this, firstLayout);
                setCurrentItem(getCurrentItem());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        /**
         * 在 onDetachedFromWindow 由于 mscroller 动画被取消了，所以会出现 页面在中间的情况，
         * 可以不调用 super.onDetachedFromWindow() ，这样动画不会停止，不太好；后面再看看用什么方法解决
         */
        firstLayout = false;
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

    public BannerViewPager setCurrentPosition(int index){
        mCurrentIndex = index;
        return this;
    }

    /**
     * 配置需要的indicator
     *
     * @param indicator
     * @return
     */
    public BannerViewPager addIndicator(View indicator) {
        mIndicator = indicator;
        return this;
    }

    /**
     * 动态配置自定义属性，放在 setPageListener 之前
     *
     * @param bean
     * @return
     */
    public BannerViewPager addPageBean(PageBean bean) {

        isAutoLoop = bean.isAutoLoop;
        if (isAutoLoop) {
            isCycle = true;
        }

        if (bean.loopMaxCount != -1) {
            mLoopMaxCount = bean.loopMaxCount;
        }


        if (bean.smoothScrollTime != 0) {
            mSmoothTime = bean.smoothScrollTime;
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
      //  mCurrentIndex = 0;
        if (datas == null || datas.isEmpty()) {
            return;
        }
        mDataCount = datas.size();
        if (mLoopMaxCount != -1) {
            if (mDataCount >= mLoopMaxCount) {
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
        adapter = new CusViewPagerAdapter<T>(datas, layoutId, listener);
        setAdapter(adapter);
        final int startSelectItem = getStartSelectItem(mDataCount)+mCurrentIndex;

        setOffscreenPageLimit(3);

        setCurrentItem(startSelectItem);

        if (mIndicator != null) {
            chooseIndicator(datas.size(), mIndicator);
        }

       /* getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setCurrentItem(startSelectItem);
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });*/
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
        Itransformer itransformer = null;
        switch (transformer) {
            case CARD:
                itransformer = new CardTransformer(cardHeight);
                break;
            case MZ:
                itransformer = new MzTransformer();
                break;
            case ZOOM:
                itransformer = new ZoomOutPageTransformer();
                break;
            case DEPATH:
                itransformer = new DepthPageTransformer();
                break;
            default:
                break;
        }
        if (itransformer != null) {
            if (itransformer instanceof CardTransformer){
                setPageTransformer(true,itransformer.getTransformer());
            }else {
                setPageTransformer(false,itransformer.getTransformer());
            }
        }
    }


    /**
     * 配置adapter
     *
     * @param <T>
     */
    class CusViewPagerAdapter<T> extends PagerAdapter {
        PageHelperListener listener;
        List<T> list;
        int layoutId;

        public CusViewPagerAdapter(List<T> list, @Nullable int layoutId, PageHelperListener listener) {
            this.listener = listener;
            this.list = list;
            this.layoutId = layoutId;

        }

        @Override
        public int getCount() {
            if (isCycle) {
                return this.list.size() + ViewPagerHelperUtils.LOOP_COUNT;
            } else {
                return this.list.size();
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            mCurrentContent = mInflater.inflate(layoutId, BannerViewPager.this, false);
            final int index;
            if (isCycle) {
                index = position % list.size();
            }else{
                index = position;
            }
            listener.bindView(mCurrentContent, this.list.get(index), index);

            container.addView(mCurrentContent);

            viewTouch(mCurrentContent,listener,index);
            return mCurrentContent;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private long mLastTime;

    private void viewTouch(final View view, final PageHelperListener listener, final int position) {
        if (view != null) {
            view.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    stopAnim();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mLastTime = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_UP:
                            long time = System.currentTimeMillis() - mLastTime;
                            if (time < 200){
                                if (listener != null && mDatas.size() > 0) {
                                    listener.onItemClick(view,mDatas.get(position),position);
                                }
                            }
                            startAnim();
                            break;

                            default:break;
                    }
                    return false;
                }
            });


        }
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
