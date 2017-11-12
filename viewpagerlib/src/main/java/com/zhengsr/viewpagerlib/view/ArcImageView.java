package com.zhengsr.viewpagerlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.zhengsr.viewpagerlib.R;

/**
 * Created by zhengshaorui on 2017/11/9.
 * csdn: http://blog.csdn.net/u011418943
 */

public class ArcImageView extends ImageView {
    private static final String TAG = "zsr";
    private Paint mPaint;
    private Path mPath;
    private Bitmap mBitmap;
    /**
     * attrs
     */
    private int mArcHeight ;
    public ArcImageView(Context context) {
        this(context,null);
    }

    public ArcImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ArcImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ArcImageView);
        mArcHeight = ta.getDimensionPixelSize(R.styleable.ArcImageView_arc_height,30);

        ta.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPath = new Path();




    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mBitmap != null){
            Shader shader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP);
            mPaint.setShader(shader);
            canvas.drawPath(mPath,mPaint);
        }

    }

    @Override
    public void setImageResource(int resId) {
        mBitmap = BitmapFactory.decodeResource(getResources(),resId);
        Log.d(TAG, "zsr --> setImageResource: ");
        invalidate();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Log.d(TAG, "zsr --> setImageDrawable: "+drawable);
        if (bd != null){
            mBitmap = bd.getBitmap();
            invalidate();
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        mBitmap = bm;
        Log.d(TAG, "zsr --> setImageBitmap: ");
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        mPath.moveTo(0,0);
        mPath.addRect(0,0,width,height -mArcHeight, Path.Direction.CW);



        mPath.moveTo(0, height - mArcHeight);
        mPath.quadTo(width / 2, height + mArcHeight, width, height - mArcHeight);
        setMeasuredDimension(width, height);

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
