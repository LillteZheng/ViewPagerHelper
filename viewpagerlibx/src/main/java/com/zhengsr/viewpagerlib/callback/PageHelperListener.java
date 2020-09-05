package com.zhengsr.viewpagerlib.callback;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/9.
 */

public abstract class PageHelperListener<T> {
    private static final String TAG = "PageHelperListener";

    private List<Object> mDatas;
    public PageHelperListener() {
        mDatas = new ArrayList<>();
    }

    public void setDatas(List<Object> datas){
        mDatas.clear();
        mDatas.addAll(datas);
    }

    /**
     * 拿到数据
     */
    public abstract void bindView(View view, T data, int position);


    public void onItemClick(View view,T data,int position){};

    /**
     * 子控件点击事件
     * @param childView
     * @param position
     */
    public void onItemChildClick(View childView, T data,int position){}

    /**
     * 子控件长按事件
     * @param childView
     * @param position
     */
    public boolean onItemChildLongClick(View childView,T data,int position){
        return true;
    }

    /**
     * 如果布局里的子控件需要点击事件，需要现在这里注册
     * @param viewId
     * @return
     */
    public PageHelperListener addChildrenClick(View view, int viewId, final int position){
        final View child = view.findViewById(viewId);
        if (child != null) {
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemChildClick(child, (T) mDatas.get(position),position);
                }
            });
        }
        return this;
    }
    /**
     * 如果布局里的子控件需要长按事件，需要现在这里注册
     * @param viewId
     * @return
     */
    public PageHelperListener addChildrenLongClick(View view, int viewId, final int position){
        final View child = view.findViewById(viewId);
        if (child != null) {
            child.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemChildLongClick(child, (T) mDatas.get(position),position);
                }
            });
        }
        return this;
    }




    /**
     * 常用模板
     */
    public PageHelperListener setText(View view, int viewId, int resId) {
        TextView textView = view.findViewById(viewId);
        if (textView != null) {
            textView.setText(resId);
        }
        return this;
    }

    public PageHelperListener setText(View view, int viewId, String msg) {
        TextView textView = view.findViewById(viewId);
        if (textView != null) {
            textView.setText(msg);
        }
        return this;
    }

    public PageHelperListener setTextColor(View view, int viewId, int textColor) {
        TextView textView = view.findViewById(viewId);
        if (textView != null) {
            textView.setTextColor(textColor);
        }
        return this;
    }


    public PageHelperListener setImageView(View view, int viewId, int res){
        ImageView imageView = view.findViewById(viewId);
        if (imageView != null) {
            imageView.setImageResource(res);
        }
        return this;
    }

    public PageHelperListener setImageView(View view, int viewId, Bitmap bitmap){
        ImageView imageView = view.findViewById(viewId);
        if (imageView != null) {
            imageView.setImageBitmap(bitmap);
        }
        return this;
    }


    public PageHelperListener setVisible(View view, int viewId, boolean isVisible) {
        View childView = view.findViewById(viewId);
        if (childView != null) {
            childView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
        return this;
    }
    public PageHelperListener setVisible(View view, int viewId, int visible) {
        View childView = view.findViewById(viewId);
        if (childView != null) {
            childView.setVisibility(visible);
        }
        return this;
    }

}
