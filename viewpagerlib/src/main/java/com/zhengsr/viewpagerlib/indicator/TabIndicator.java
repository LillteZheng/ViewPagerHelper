package com.zhengsr.viewpagerlib.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.zhengsr.viewpagerlib.R;
import com.zhengsr.viewpagerlib.ViewPagerHelperUtils;
import com.zhengsr.viewpagerlib.type.TabShapeType;
import com.zhengsr.viewpagerlib.type.TabTextType;
import com.zhengsr.viewpagerlib.view.ColorTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/21.
 */

/**
 * 不再维护这个类，扩展性差，维护麻烦
 * 建议使用新库：https://github.com/LillteZheng/FlowHelper
 */
@Deprecated()
public class TabIndicator extends LinearLayout implements ViewPager.OnPageChangeListener, ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = "TabIndicator";
    /**
     * const
     */
    private static final int TRI_TAB = 0;
    private static final int RECT_TAB = 1;
    /**
     * attr
     */
    private int mTabWidth;
    private int mTabHeight;
    private int mTabColor;
    private int mVisiableSize;
    private int mHeight;
    private int mWidth;
    private int mDefaultColor = 0xff000000;
    private int mChangeColor = 0xffff0000;
    private int mTextSize;
    private TabShapeType mTabtyle;
    private int mLineTransX = 0; //移动的位置
    private TabTextType mTextType;
    private boolean isShowTab = false;
    private boolean isCanScroll = true; //是否能移动，默认为true
    /**
     * others
     */
    private Path mPath;
    private Paint mPaint;
    private Scroller mScroller;
    private float mSnap;
    private float mDownX, mMoveX;
    private float mLastMoveX;
    private boolean isColorMove;
    private List<String> mTitles = new ArrayList<>();
    private TabClickListener mListener;
    private boolean isFirstLoadData = true;
    private int mMoveClickIndex;
    private int mMoveLastIndex;

    public TabIndicator(Context context) {
        this(context, null);
    }

    public TabIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TabIndicator);
        mVisiableSize = ta.getInt(R.styleable.TabIndicator_visiabel_size, 4);
        mTabWidth = ta.getDimensionPixelSize(R.styleable.TabIndicator_tab_width, 30);
        mTabHeight = ta.getDimensionPixelSize(R.styleable.TabIndicator_tab_height, 10);
        mTabColor = ta.getResourceId(R.styleable.TabIndicator_tab_color, R.color.page_white);
        mDefaultColor = ta.getColor(R.styleable.TabIndicator_tab_text_default_color, mDefaultColor);
        mChangeColor = ta.getColor(R.styleable.TabIndicator_tab_text_change_color, mChangeColor);
        mTextSize = ta.getDimensionPixelSize(R.styleable.TabIndicator_tab_textsize,
                getResources().getDimensionPixelSize(R.dimen.tabsize));
        int tt = ta.getInteger(R.styleable.TabIndicator_tap_type, 0);
        if (tt == 0) {
            mTabtyle = TabShapeType.TRI;
        } else {
            mTabtyle = TabShapeType.ROUND;
        }


        int type = ta.getInteger(R.styleable.TabIndicator_tab_text_type, 1);
        if (type == 1) {
            mTextType = TabTextType.COLOR;
        } else {
            mTextType = TabTextType.NORMAL;
        }
        isShowTab = ta.getBoolean(R.styleable.TabIndicator_tab_show, isShowTab);
        isCanScroll = ta.getBoolean(R.styleable.TabIndicator_tab_iscanscroll, true);
        ta.recycle();
        initData();

        getViewTreeObserver().addOnGlobalLayoutListener(this);

    }

    private void initData() {
        setClickable(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(mTabColor));

        //实例一个 scroller
        mScroller = new Scroller(getContext());
        mSnap = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    }

    /**
     * 重绘三角形或者圆条
     *
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        //偏移 canvas 的起始位置
        if (isShowTab) {
            canvas.save();
            canvas.translate(mLineTransX, 0);
            canvas.drawPath(mPath, mPaint);
            canvas.restore();
        }
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int count = getChildCount();
        if (count > 0) {
            mRightBorder = getChildAt(count - 1).getRight();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (isCanScroll) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = event.getRawX();
                    mLastMoveX = mDownX;
                    break;
                case MotionEvent.ACTION_MOVE:
                    mMoveX = event.getRawX();
                    float dx = Math.abs(mMoveX - mDownX);
                    mLastMoveX = mMoveX;
                    if (dx > mSnap) {
                        //可以移动了，屏蔽子控件，接着在 ontouchEvent 中去处理移动
                        return true;
                    }
                    break;
                default:
                    break;

            }
        }
        return super.onInterceptTouchEvent(event);
    }

    private int mRightBorder;

    private boolean isMove = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isCanScroll) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isMove = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    mMoveX = event.getRawX();
                    int scrolledX = (int) (mLastMoveX - mMoveX);
                    //边界判断
                    if (getScrollX() + scrolledX < 0) {
                        scrollTo(0, 0);
                        return true;
                    } else if (getScrollX() + getWidth() + scrolledX > mRightBorder) {
                        scrollTo(mRightBorder - getWidth(), 0);
                        return true;
                    }
                    scrollBy(scrolledX, 0);
                    isColorMove = true;
                    mLastMoveX = mMoveX;
                    break;
                case MotionEvent.ACTION_UP:
                    //invalidate();
                    isMove = true;
                    break;
                default:
                    break;

            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        //开始移动
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            invalidate();
        }
    }


    /**
     * 设置viewpager的切换速度
     *
     * @param viewPager
     * @param time
     */
    public void setViewPagerSwitchSpeed(ViewPager viewPager, int time) {
        ViewPagerHelperUtils.initSwitchTime(getContext(), viewPager, time);
    }

    @Override
    public void onGlobalLayout() {
        if (isFirstLoadData) {
            isFirstLoadData = false;
            if (!mTitles.isEmpty() && mListener != null) {
                readyToLoadData(mTitles, mListener);
            }
        }
    }


    /**
     * 顶部点击事件的监听
     */
    public interface TabClickListener {
        void onClick(int position);
    }

    /**
     * 这个函数，则表示使用在 TabIndicator xml 的控件
     */
    public void setTabData(ViewPager viewPager, TabClickListener listener) {
        setTabData(viewPager, null, listener);
    }

    /**
     * 如果使用这个函数，则在xml里面的子控件将被清除，
     *
     * @param viewPager
     * @param titles    textview 的内容
     */
    public void setTabData(final ViewPager viewPager, final List<String> titles,
                           final TabClickListener listener) {
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(this);
        }
        mTitles.clear();
        mTitles.addAll(titles);
        mListener = listener;
        if (mWidth > 0) {
            readyToLoadData(titles, listener);
        }


    }

    /**
     * 开始加载数据
     *
     * @param titles
     * @param listener
     */
    private void readyToLoadData(List<String> titles, final TabClickListener listener) {
        if (titles.size() > 0) {
            removeAllViews();
            for (int i = 0; i < titles.size(); i++) {
                String title = titles.get(i);

                if (mTextType == TabTextType.COLOR) {
                    ColorTextView textView = new ColorTextView(getContext());
                    LayoutParams params = new LayoutParams(0,
                            LayoutParams.MATCH_PARENT);
                    params.width = (int) (mWidth * 1.0f / mVisiableSize);
                    textView.setText(title);
                    textView.setGravity(Gravity.CENTER);
                    textView.setLayoutParams(params);

                    textView.setCusTextColor(mDefaultColor, mChangeColor, mTextSize);
                    if (i == 0) {
                        textView.setTextColor(mChangeColor);
                    }
                    addView(textView);
                } else {
                    TextView textView = new TextView(getContext());
                    LayoutParams params = new LayoutParams(0,
                            LayoutParams.MATCH_PARENT);
                    params.width = (int) (mWidth * 1.0f / mVisiableSize);
                    textView.setText(title);
                    textView.setLayoutParams(params);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
                    if (i == 0) {
                        textView.setTextColor(mChangeColor);
                    } else {
                        textView.setTextColor(mDefaultColor);
                    }
                    addView(textView);
                }
            }

        }

        for (int i = 0; i < getChildCount(); i++) {
            final int finalI = i;
            getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(finalI);
                    //在点击的时候，我们需要做一些处理
                    int moveX = getScrollX();
                    mMoveLastIndex = mMoveClickIndex;
                    mMoveClickIndex = finalI;
                    if (moveX < mWidth) {
                        if (isMove) {
                            mScroller.startScroll(0, 0, 0, 0);
                            //invalidate();
                            isMove = false;
                        }
                    }

                }
            });
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
        mPath = new Path();
        int width = mWidth / mVisiableSize;
        if (mTabtyle == TabShapeType.TRI) {
            //画三角形
            mPaint.setPathEffect(new CornerPathEffect(2)); //使三角形更加圆润
            mPath.moveTo((width - mTabWidth) / 2, mHeight);
            mPath.lineTo((width + mTabWidth) / 2, mHeight);
            mPath.lineTo(width / 2, mHeight - mTabHeight);

        } else {
            mPath.close();
            //画条状
            mPath.moveTo((width - mTabWidth) / 2, mHeight);
            mPath.lineTo((width + mTabWidth) / 2, mHeight);
            mPath.lineTo((width + mTabWidth) / 2, mHeight - mTabHeight);
            mPath.lineTo((width - mTabWidth) / 2, mHeight - mTabHeight);
            mPath.close();
        }


    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        onScroll(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        if (mTextType == TabTextType.NORMAL) {
            for (int i = 0; i < getChildCount(); i++) {
                if (i == position) {
                    TextView currentView = (TextView) getChildAt(position);
                    if (currentView != null) {
                        currentView.setTextColor(mChangeColor);
                    }
                } else {
                    TextView lastview = (TextView) getChildAt(i);
                    lastview.setTextColor(mDefaultColor);
                }
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        /**
         * 当viewpager停止了，再去取消颜色值，避免卡顿
         */
        if (isColorMove && mTextType == TabTextType.COLOR && state == 0) {
            ColorTextView lastView = (ColorTextView) getChildAt(mMoveLastIndex);
            if (lastView != null) {
                lastView.setTextColor(mDefaultColor);
            }
            ColorTextView textView = (ColorTextView) getChildAt(mMoveClickIndex);
            if (textView != null) {
                textView.setTextColor(mChangeColor);
            }
            isColorMove = false;
        }
    }

    private void onScroll(int position, float offset) {
        int tabWidth = mWidth / mVisiableSize;
        mLineTransX = (int) (tabWidth * position + tabWidth * offset);
        if (position >= (mVisiableSize - 1) && offset > 0) {
            scrollTo(
                    (position - (mVisiableSize - 1)) * tabWidth + (int) (tabWidth * offset),
                    0);
        }
        if (mTextType == TabTextType.COLOR) {
            if (offset >= 0) {
                try {
                     if (!isColorMove){
                        ColorTextView leftView = (ColorTextView) getChildAt(position);
                        ColorTextView rightView = (ColorTextView) getChildAt(position + 1);
                        leftView.setprogress(1 - offset, ColorTextView.DEC_RIGHT);
                        rightView.setprogress(offset, ColorTextView.DEC_LEFT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        invalidate();
    }


    public TabIndicator visiabelSize(int size) {
        mVisiableSize = size;
        return this;
    }

    public TabIndicator tabColor(int color) {
        mTabColor = color;
        return this;
    }

    public TabIndicator showTab(boolean showtab) {
        isShowTab = showtab;
        return this;
    }

    public TabIndicator tabTextType(TabTextType textType) {
        mTextType = textType;
        return this;
    }

    public TabIndicator tabWidth(int width) {
        mTabWidth = width;
        return this;
    }

    public TabIndicator tabHeight(int height) {
        mTabHeight = height;
        return this;
    }


    public TabIndicator textSize(int size) {
        mTextSize = size;
        return this;
    }

    public TabIndicator defaultColor(int color) {
        mDefaultColor = color;
        return this;
    }

    public TabIndicator moveColor(int color) {
        mChangeColor = color;
        return this;
    }

    public TabIndicator tabShapeType(TabShapeType type) {
        mTabtyle = type;
        return this;
    }

    public TabIndicator isCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
        return this;
    }

}
