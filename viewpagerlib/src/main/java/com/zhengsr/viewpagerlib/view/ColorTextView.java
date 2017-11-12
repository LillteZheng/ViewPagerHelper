package com.zhengsr.viewpagerlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zhengsr.viewpagerlib.R;

/**
 * Created by zhengshaorui on 2017/11/12.
 * csdn: http://blog.csdn.net/u011418943
 */

public class ColorTextView extends TextView {
    /**
     * const
     */
    public static final int DEC_LEFT = 1;
    public static final int DEC_RIGHT = 2;

    private Paint mPaint;
    /**
     * attrs
     */
    private int mWidth,mHeight;
    private int mDefaultColor = 0xff000000;
    private int mChangeColor = 0xffff0000;
    private int mDecection = DEC_LEFT;
    public ColorTextView(Context context) {
        this(context,null);
    }

    public ColorTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);



    }

    public ColorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ColorTextView);
        mDefaultColor = ta.getColor(R.styleable.ColorTextView_colortext_default_color,mDefaultColor);
        mChangeColor = ta.getColor(R.styleable.ColorTextView_colortext_change_color,mChangeColor);
        int textsize = ta.getDimensionPixelSize(R.styleable.ColorTextView_colortext_size,20);
        ta.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setTextSize(textsize);
    }


    /**
     * 公布出去，可以手动设置颜色和字体大小
     * @param defaultColor
     * @param changeColor
     * @param textsize
     */
    public void setTextColor(int defaultColor,int changeColor,int textsize){
        mDefaultColor = defaultColor;
        mChangeColor = changeColor;
        mPaint.setTextSize(textsize);
        invalidate();
    }
    private float mProgress = 0;
    public void setprogress(float progress,int decection) {
        mDecection = decection;
        mProgress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDecection == DEC_RIGHT) {
            //绘制一遍黑色
            drawText(canvas, 0, mWidth, mDefaultColor);
            // 再绘制一遍其他颜色
            drawText(canvas, (int) ((1 - mProgress) * mWidth), mWidth, mChangeColor);
        }else{
            //绘制一遍黑色
            drawText(canvas, 0, mWidth, mDefaultColor);
            // 再绘制一遍其他颜色
            drawText(canvas, 0,(int) ( mProgress * mWidth), mChangeColor);
        }
    }
    private void drawText(Canvas canvas,int start,int end,int color){
        mPaint.setColor(color);
        canvas.save();
        canvas.clipRect(start,0,end,getHeight());
        String text = getText().toString();

        //绘制颜色居中
        float textWidth = mPaint.measureText(text);
        float x = (mWidth - textWidth)/2;
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        float dy = (metrics.descent+metrics.ascent)/2;
        float ty = mHeight/2 - dy;

        canvas.drawText(text,x,ty,mPaint);
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = measureWidth(widthMeasureSpec);
        mHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(mWidth,mHeight);
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
        }else{
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
        }else{
            result = 100; //如果是wrap_content ,给个初始值
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


}
