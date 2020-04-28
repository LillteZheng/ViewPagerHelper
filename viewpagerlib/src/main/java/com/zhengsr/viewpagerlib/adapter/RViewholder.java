package com.zhengsr.viewpagerlib.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @auther by zhengshaorui on 2020/4/28
 * describe:
 */
public class RViewholder extends RecyclerView.ViewHolder {
    private static final String TAG = "RViewholder";
    //用来保存 itemview；
    private SparseArray<View> mViewSparseArray;
    private View mConserView;
    public RViewholder(View itemView) {
        super(itemView);
        mViewSparseArray = new SparseArray<>();
        mConserView = itemView;
    }

    /**
     * 这里相当于在 oncreateViewholder 中，设置好数据，然后返回
     * 可以在这里设置宽度，这样就可以动态居中了
     * @param context
     * @param layoutid
     * @param parent
     * @return
     */
    public static RViewholder getViewHolder(Context context, int layoutid, ViewGroup parent){
        View view = LayoutInflater.from(context).inflate(layoutid,parent,false);
        RViewholder viewholder = new RViewholder(view);
        return viewholder;
    }


    public View getConserView(){
      return mConserView;
    }


    /**
     * getItemview，通过 id获取view
     * @param resId
     * @return
     */
    public <T extends View> T getItemView(int resId){
        View view = mViewSparseArray.get(resId);
        if (view == null){
            view = mConserView.findViewById(resId);
            mViewSparseArray.put(resId,view);
        }
        return (T) view;
    }

    /**
     * 设置数据，直接通过id获取到view，然后设置数据
     * @param viewId
     * @param data
     * @return
     */
    public RViewholder setText(int viewId, String data){
        TextView textView = (TextView) getItemView(viewId);
        textView.setText(data);
        return this;
    }
    public RViewholder setDrawable(int viewId, int resId){
        ImageView imageView = (ImageView) getItemView(viewId);
        imageView.setBackgroundResource(resId);
        return this;
    }

    public void onItemClickListener(View v,int position){};



}