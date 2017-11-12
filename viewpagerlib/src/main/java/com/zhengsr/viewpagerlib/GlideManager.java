package com.zhengsr.viewpagerlib;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.zhengsr.viewpagerlib.callback.BitmapListener;

public class GlideManager {
    public static final int BITMAP_SCAN_CENTERN = 1;
    public static final int BITMAP_SCAN_FIT = 2;
    private static final String TAG = "zsr";
    private BitmapListener mBitmapListener;

    public GlideManager(Builder builder){
        RequestOptions options = new RequestOptions()
                .placeholder(builder.placeresid);

        if (builder.eroorresid != 0){
            options.error(builder.eroorresid);
        }


        switch (builder.type){
            case BITMAP_SCAN_CENTERN:
                options.centerCrop();
                break;
            case BITMAP_SCAN_FIT:
                options.fitCenter();
                break;
            default:
                break;
        }
        if (builder.setCircleCrop){
            options.circleCrop();
        }
        if (builder.radius != 0){
            options.transform(new RoundedCorners(builder.radius));
        }

        RequestBuilder requestBuilder = null;

        requestBuilder = Glide.with(builder.context).load(builder.source);

        if (builder.animtime > 0){
            requestBuilder.transition(new DrawableTransitionOptions().crossFade(builder.animtime));
        }

        requestBuilder.apply(options)
                .listener(new LoadListener())
                .into(builder.imageView);


    }


    public GlideManager addLoadLstner(BitmapListener listener){
        mBitmapListener = listener;
        return this;
    }

    class LoadListener implements RequestListener {

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
            if (mBitmapListener != null){
                mBitmapListener.onFailure(e);
            }
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
            if (mBitmapListener != null){
                mBitmapListener.onSuccess(resource);
            }
            return false;
        }
    }

    public static class Builder{
        Context context;
        int eroorresid;
        int placeresid;
        Object source;
        boolean setCircleCrop;
        int radius = 0;
        int type;
        int animtime;
        ImageView imageView;
        public Builder setContext(Context context){
            this.context = context;
            return this;
        }



        public Builder setBitmapScanType(int type){
            this.type = type;
            return this;
        }

        public Builder setLoadingBitmap(int resid){
            this.placeresid = resid;
            return this;
        }

        public Builder setErrorBitmap(int resid){
            this.eroorresid = resid;
            return this;
        }




        public Builder setImgSource(Object source){
            this.source = source;
            return this;
        }


        public Builder setCircleCrop(boolean setCircleCrop){
            this.setCircleCrop = setCircleCrop;
            return this;
        }

        public Builder setRoundCrop(int radius){
            this.radius = radius;
            return this;
        }


        public Builder setAnimation(int animtime){
            this.animtime = animtime;
            return this;
        }


        public Builder setImageView(ImageView imageView){
            this.imageView = imageView;
            return this;
        }

        public GlideManager builder(){
            return new GlideManager(this);
        }



    }


}
