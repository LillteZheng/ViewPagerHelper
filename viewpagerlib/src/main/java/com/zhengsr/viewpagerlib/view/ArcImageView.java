package com.zhengsr.viewpagerlib.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.zhengsr.viewpagerlib.R;

/**
 * Created by zhengshaorui on 2017/11/9.
 * csdn: http://blog.csdn.net/u011418943
 */

public class ArcImageView extends AppCompatImageView implements ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = "zsr";
    private Paint mPaint;
    private Path mPath;
    private Bitmap mBitmap;
    /**
     * attrs
     */
    private int mArcHeight;
    private int mArcBlur ;
    //-1为白色
    private int mUseColor ;

    public ArcImageView(Context context) {
        this(context, null);
    }

    public ArcImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ArcImageView);
        mArcHeight = ta.getDimensionPixelSize(R.styleable.ArcImageView_arc_height, 30);
        mArcBlur = ta.getInteger(R.styleable.ArcImageView_arc_blur,-1);
        mUseColor = ta.getColor(R.styleable.ArcImageView_arc_use_color,-2);
        ta.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);
        if (mUseColor != -2){
            mPaint.setColor(mUseColor);
        }
        mPath = new Path();

        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawPath(mPath, mPaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int arc = 0;
        if (mArcHeight > 0) {
            arc = mArcHeight;
        }
        mPath.moveTo(0, 0);
        mPath.lineTo(0, h - arc);
        mPath.quadTo(w / 2, h + mArcHeight, w, h-arc);
        mPath.lineTo(w, 0);
        mPath.close();
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

    @Override
    public void onGlobalLayout() {
        if (mUseColor == -2) {
            BitmapDrawable bd = (BitmapDrawable) getDrawable();
            mPaint.setShader(null);
            if (bd != null) {
                mBitmap = bd.getBitmap();
                Shader shader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP,
                        Shader.TileMode.CLAMP);
                if (mArcBlur != -1) {
                    blurByRenderScript(mArcBlur);
                }
                mPaint.setShader(shader);
            }
            invalidate();
        }
    }

    @TargetApi( Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void blurByRenderScript(int radius) {
        RenderScript rs = RenderScript.create(getContext());

        Allocation allocation = Allocation.createFromBitmap(rs, mBitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, allocation.getElement());
        blur.setInput(allocation);
        blur.setRadius(radius);

        blur.forEach(allocation);

        allocation.copyTo(mBitmap);
        rs.destroy();
    }
}
