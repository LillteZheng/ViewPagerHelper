package com.zhengsr.viewpagerlib.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RSRuntimeException;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatImageView;

import com.zhengsr.viewpagerlib.R;


/**
 * Created by zhengshaorui on 2017/11/9.
 * csdn: http://blog.csdn.net/u011418943
 */

public class ArcImageView extends AppCompatImageView {
    private static final String TAG = "zsr";
    private Paint mPaint;
    private Path mPath;
    private Bitmap mBitmap;
    private Bitmap mBlurBitmap;
    private RectF mRectF;
    /**
     * attrs
     */
    private int mArcHeight;
    private int mArcBlur;
    //-1为白色
    private int mUseColor;
    private Matrix mMatrix;
    private float mFactor;
    private float mScaleX;
    private float mScaleY;
    /**
     * 是否自动适配
     */
    private boolean mAutoFix;

    private Shader mShader;

    public ArcImageView(Context context) {
        this(context, null);
    }

    public ArcImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ArcImageView);
        mArcHeight = ta.getDimensionPixelSize(R.styleable.ArcImageView_arc_height, 0);
        mArcBlur = ta.getInteger(R.styleable.ArcImageView_arc_blur, -1);
        mUseColor = ta.getColor(R.styleable.ArcImageView_arc_use_color, -2);
        mFactor = ta.getFloat(R.styleable.ArcImageView_arc_scaleFactor, -1);
        mScaleX = ta.getDimensionPixelSize(R.styleable.ArcImageView_arc_scaleX, -1);
        mScaleY = ta.getDimensionPixelSize(R.styleable.ArcImageView_arc_scaleY, -1);
        mAutoFix = ta.getBoolean(R.styleable.ArcImageView_arc_auto_fix, true);
        ta.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);
        if (mUseColor != -2) {
            mPaint.setColor(mUseColor);
        }else{
            setBackgroundColor(Color.TRANSPARENT);
        }
        mPath = new Path();
        mMatrix = new Matrix();

        initializeBitmap();



    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initializeBitmap();
    }

    /**
     * 配置弧形
     */
    private void configPath() {
        float w = getWidth();
        float h = getHeight();
        int arc = 0;
        /**
         * 这里为什么 arcHeight 大于 0 时，才对 h 切掉一段呢？
         * 因为 h + mArcHeight 始终是比控件大的，虽然可以使用clipChildren = false来规避
         * 但在 viewpager 时，虽然可以使用clipChildren是失效的。
         * 而 arcHeight小于0，则不存在这个问题
         */
        if (mArcHeight > 0) {
            arc = mArcHeight;
        }
        mPath.moveTo(0, 0);
        mPath.lineTo(0, h - arc);
        mPath.quadTo(w / 2, h + mArcHeight, w, h - arc);
        mPath.lineTo(w, 0);

    }


    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }
    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }


    /**
     * 拿到bitmap
     */
    private void initializeBitmap() {
        BitmapDrawable bd = (BitmapDrawable) getDrawable();
        if (bd != null) {
            mBitmap = bd.getBitmap();
        }
        setUp();
    }

    /**
     * 配置bitmap，比如高斯模糊，自适应大小等
     */
    private void setUp() {
        if (getWidth() == 0 || getHeight() == 0 || mBitmap == null) {
            return;
        }

        configPath();
        if (mUseColor == -2) {
            mPaint.setShader(null);

            if (mArcBlur != -1) {
                mBlurBitmap = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
                //用新的bitmap来区分，避免干扰原图片
                mBitmap = blur(getContext(), mBlurBitmap, mArcBlur);
            }
            mShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP);

            mPaint.setShader(mShader);
            updateMatrix();
        }
        postInvalidate();
    }

    /**
     * 更新matrix大小，比如缩放等
     */
    private void updateMatrix(){
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        if (mAutoFix) {
            int width = getWidth();
            int height = getHeight();
            /**
             * 拿到最初的缩放比例
             */
            float factorX = width * 1.0f / mBitmap.getWidth();
            float factorY = height * 1.0f / mBitmap.getHeight();


            mMatrix.reset();
            mMatrix.postScale(factorX, factorY);
            if (mFactor != -1 && mScaleX != -1 && mScaleY != -1) {
                mMatrix.postScale(mFactor, mFactor, mScaleX , mScaleY);
            }else if (mFactor != -1){
                mMatrix.postScale(mFactor,mFactor);
            }
            mShader.setLocalMatrix(mMatrix);
        }
    }






    /**
     * 高斯模糊，glide代码
     *
     * @param context
     * @param bitmap
     * @param radius
     * @return
     * @throws RSRuntimeException
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public Bitmap blur(Context context, Bitmap bitmap, int radius) throws RSRuntimeException {
        RenderScript rs = null;
        try {
            rs = RenderScript.create(context);
            rs.setMessageHandler(new RenderScript.RSMessageHandler());
            Allocation input =
                    Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                            Allocation.USAGE_SCRIPT);
            Allocation output = Allocation.createTyped(rs, input.getType());
            ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

            blur.setInput(input);
            blur.setRadius(radius);
            blur.forEach(output);
            output.copyTo(bitmap);
        } finally {
            if (rs != null) {
                rs.destroy();
            }
        }

        return bitmap;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = measureWidth(widthMeasureSpec);
        int h = measureHeight(heightMeasureSpec);
        setMeasuredDimension(w, h);

    }

    //设置高的大小
    private int measureHeight(int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int result = 0;
        //获取模式和大小
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 100; //如果是wrap_content ,给个初始值
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    //设置宽的大小
    private int measureWidth(int widthMeasureSpec) {
        // TODO Auto-generated method stub
        int result = 0;
        //获取模式和大小
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 100; //如果是wrap_content ,给个初始值
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    public ArcImageView arcHeight(int arcHeight){
        mArcHeight = arcHeight;
        return this;
    }
    public ArcImageView blur(int blur){
        mArcBlur = blur;
        return this;
    }
    public ArcImageView scaleFactor(float scaleFactor){
        mFactor = scaleFactor;
        return this;
    }
    public ArcImageView scaleX(float scaleX){
        mScaleX = scaleX;
        return this;
    }
    public ArcImageView scaleY(float scaleY){
        mScaleY = scaleY;
        return this;
    }
    public ArcImageView autoFit(boolean autoFit){
        mAutoFix = autoFit;
        return this;
    }

    public void update(){
        postInvalidate();
    }


}
