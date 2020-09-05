package com.zhengsr.viewpagerlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewTreeObserver;

import androidx.appcompat.widget.AppCompatImageView;

import com.zhengsr.viewpagerlib.R;

/**
 * created by zhengshaorui on 2019/7/24
 * Describe: 图片放大缩小类
 */
public class ScaleImageView extends AppCompatImageView implements  ViewTreeObserver.OnGlobalLayoutListener, ScaleGestureDetector.OnScaleGestureListener {
    private static final String TAG = "ScaleImageView";
    /**
     * attrs
     */
    private   int mDoubleAutoTime = 10;
    private   int mScaleMaxFactor = 5;
    private   int mDoubleFactor = 2;
    private   boolean mIsLimitBroad = false;
    private   boolean mIsAutoFit = false;
    private   boolean mRequestParentTouch = false;
    /**
     * 缩放比例
     */
    private float mDeflautScale;
    private float mDeflautScaleY;
    private float mDoubleScale;
    private float mMaxScale;
    private boolean isFirst = true;
    private ScaleGestureDetector mScaleGestureDetector;
    private Matrix mMatrix;

    private boolean isCanDoubleScale = true;


    /**
     * 记录上一次中心点的变化
     */
    private int mLastPointCount;
    private float mLastx,mLasty;
    private GestureDetector mGestureDetector;

    public ScaleImageView(Context context) {
        this(context,null);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScaleImageView);
        mDoubleAutoTime = ta.getInteger(R.styleable.ScaleImageView_scale_auto_time, mDoubleAutoTime);
        mScaleMaxFactor = ta.getInteger(R.styleable.ScaleImageView_scale_max_factor, mScaleMaxFactor);
        mDoubleFactor = ta.getInteger(R.styleable.ScaleImageView_scale_double_factor, mDoubleFactor);
        mIsLimitBroad = ta.getBoolean(R.styleable.ScaleImageView_scale_limit_board,true);
        mIsAutoFit = ta.getBoolean(R.styleable.ScaleImageView_scale_autofit,false);
        mRequestParentTouch = ta.getBoolean(R.styleable.ScaleImageView_scale_interrupt_parent_touch,false);
        ta.recycle();

        this.getViewTreeObserver().addOnGlobalLayoutListener(this);
        setClickable(true);
        setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector = new ScaleGestureDetector(context,this);
        mGestureDetector = new GestureDetector(context, new SimpleGesture());
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //双击，就不传递回去了
        if (mGestureDetector.onTouchEvent(event)){
            return true;
        }
        mScaleGestureDetector.onTouchEvent(event);

        int pointerCount = event.getPointerCount();
        float x = 0;
        float y = 0;
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        x = x / pointerCount;
        y = y / pointerCount;

        if (mLastPointCount != pointerCount){
            mLastx = x;
            mLasty = y;
        }
        mLastPointCount = pointerCount;

        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                requestFocusViewpager();
                break;

            case MotionEvent.ACTION_MOVE:
                requestFocusViewpager();
                float dx = x - mLastx;
                float dy = y - mLasty;
                mMatrix.postTranslate(dx,dy);
                checkBroad();
                setImageMatrix(mMatrix);
                mLastx = x;
                mLasty = y;
                break;
            case MotionEvent.ACTION_UP:
                mLastPointCount = 0;
                break;

            default:break;
        }

        return true;
    }

    @Override
    public void onGlobalLayout() {
        if (isFirst){
            isFirst = false;
            int width = getWidth();
            int height = getHeight();
            Drawable d = getDrawable();
            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();

            /**
             * 拿到最初的缩放比例
             */
            float scale = 1;
            float scaley = 1;



            if (mIsAutoFit) {
                scale = width * 1.0f / dw;
                scaley = height * 1.0f / dh;
            }else{
                //控件宽高都比图片大,即放大了多少倍，拿到宽高的最小放大比例
                if (width > dw && height > dh){
                    scale = Math.min((width * 1.0f / dw),(height * 1.0f / dh));
                }
                //图片宽高比控件大,即缩小了多少倍，，拿到宽高的最大放小比例
                if (dw > width && dh > height){
                    scale = Math.max((width * 1.0f / dw),(height * 1.0f / dh));
                }
                //图片高比控件的高大,即应该缩放多少
                if (dw < width && dh > height){
                    scale =  height * 1.0f / dh;
                }
                //图片宽比控件的宽大,即应该缩放多少
                if (dw > width && dh < height){
                    scale = width * 1.0f / dw;
                }
            }
            /**
             * 初始化缩放比例
             */
            mDeflautScale = scale;
            mDeflautScaleY = scaley;
            mMaxScale = mDeflautScale * mScaleMaxFactor;
            mDoubleScale = mDeflautScale * mDoubleFactor;

            /**
             * 把图片缩放和移动到屏幕中间
             */
            float dx = width * 1.0f / 2 - dw * 1.0f / 2;
            float dy = height * 1.0f / 2 - dh * 1.0f / 2;
            mMatrix = new Matrix();
            mMatrix.postTranslate(dx,dy);
            if (mIsAutoFit) {
                mMatrix.postScale(scale, scaley, width * 1.0f / 2, height * 1.0f / 2);
            }else{
                mMatrix.postScale(mDeflautScale,mDeflautScale,width * 1.0f / 2, height * 1.0f / 2);
            }
            setImageMatrix(mMatrix);
        }
    }

    /**
     * 当于viewpager结合时，屏蔽viewpager 的事件
     */
    private void requestFocusViewpager(){
        if (mRequestParentTouch){
            RectF rectF = getMatrixRectF();
            //加0.01 是为了防止手指缩放导致的问题
            if (rectF.width() > (getWidth()+0.01) || rectF.height() > (getHeight()+0.01)){
                //屏蔽viewpager的事件传递
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
            }
        }
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        //拿到缩放比例
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();
        //拿到缩放中心
        float focusX = detector.getFocusX();
        float focusY = detector.getFocusY();
        //默认不能缩放至屏幕控件大小
        if (mIsLimitBroad) {
            if (scale * scaleFactor < mDeflautScale) {
                //重新复制，让它逼近于最小值
                scaleFactor = mDeflautScale / scale;
            }
            if (scale * scaleFactor > mMaxScale) {
                scaleFactor = mMaxScale / scale;
            }
        }
        mMatrix.postScale(scaleFactor,scaleFactor,focusX,focusY);
        checkBroad();
        setImageMatrix(mMatrix);
        return true;
    }


    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    /**
     * 拿到缩放比例
     * @return
     */
    private float getScale(){
        float[] floats = new float[9];
        mMatrix.getValues(floats);
        return floats[Matrix.MSCALE_X];
    }

    /**
     * 拿到缩放后的图片大小
     * @return
     */
    private RectF getMatrixRectF(){
        Matrix matrix = new Matrix(mMatrix);
        RectF rectF = new RectF();
        Drawable d = getDrawable();
        if (d != null){
            rectF.set(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    class SimpleGesture extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();
            if (isCanDoubleScale) {
                //放大
                if (getScale() < mDoubleScale) {
                    post(new AutoScale(mDoubleScale, x, y));
                } else { //缩放到自身大小
                    post(new AutoScale(mDeflautScale, x, y));
                }
            }
            return true;

        }
    }

    class AutoScale implements Runnable {
        private final float LARGE = 1.07f;
        private final float SMALL = 0.93f;
        float targetScale;
        float x;
        float y;
        float tempScale;
        public AutoScale(float targetScale,float x, float y) {
            this.targetScale = targetScale;

            this.x = x;
            this.y = y;
            isCanDoubleScale = false;
            //放大
            if (targetScale > getScale()){
                tempScale = LARGE;
            }else{
                tempScale = SMALL;
            }
        }

        @Override
        public void run() {

            mMatrix.postScale(tempScale, tempScale , x, y);
            checkBroad();
            setImageMatrix(mMatrix);
            float currentScale = getScale();
            boolean isCanLarge = currentScale < targetScale && tempScale > 1.0f;
            boolean isCanSmall = currentScale > targetScale && tempScale < 1.0f;
            if (isCanLarge || isCanSmall) {

                postDelayed(this, mDoubleAutoTime);
            }else{
                mMatrix.postScale(targetScale/currentScale, targetScale/currentScale , x, y);
                checkBroad();
                setImageMatrix(mMatrix);
                isCanDoubleScale = true;
            }


        }
    }
    /**
     * 检查边界，让它不能有空白和让它一直保在屏幕中心
     */
    private void checkBroad() {
        RectF rectF = getMatrixRectF();
        float dx = 0;
        float dy = 0;
        //检测边界，不能让它留空白
        if (rectF.width() >= getWidth()) {
            if (rectF.right < getWidth()) {
                dx = getWidth() - rectF.right;
            }
            if (rectF.left > 0) {
                dx = -rectF.left;
            }
        }else {
            //保证在屏幕中心
            dx = getWidth() * 1.0f /2 - rectF.right + rectF.width() * 1.0f / 2;
        }
        //检测边界，不能让它留空白
        if (rectF.height() >= getHeight()){
            if (rectF.top > 0){
                dy = -rectF.top;
            }
            if (rectF.bottom < getHeight()){
                dy = getHeight() - rectF.bottom;
            }
        }else{
            //保证在屏幕中心
            dy = getHeight() * 1.0f /2 - rectF.bottom + rectF.height() * 1.0f / 2;
        }

        mMatrix.postTranslate(dx,dy);
    }

    /**
     * 配置参数
     * @param time
     * @return
     */
    /**
     * 双击时的缩放时间
     * @param time
     * @return
     */
    public ScaleImageView autoScaleTime(int time){
        mDoubleAutoTime = time;
        return this;
    }

    /**
     * 限制边界
     * @param isLimit
     * @return
     */
    public ScaleImageView limitBroad(boolean isLimit){
        mIsLimitBroad = isLimit;
        return this;
    }

    /**
     * 自动适配缩放值，有些图片是正方形，如果你的高度没设定好，建议设置为false，不能会变形
     * @param autofit
     * @return
     */
    public ScaleImageView autoFit(boolean autofit){
        mIsAutoFit = autofit;
        return this;
    }

    /**
     * 双击放大倍数
     * @param factor
     * @return
     */
    public ScaleImageView doubleFactor(int factor){
        mDoubleFactor = factor;
        return this;
    }

    /**
     * 可放大的最大倍数
     * @param factor
     * @return
     */
    public ScaleImageView maxFactor(int factor){
        mScaleMaxFactor = factor;
        return this;
    }

    public ScaleImageView disPatchParentTouch(boolean touch){
        mRequestParentTouch = touch;
        return this;
    }
}
