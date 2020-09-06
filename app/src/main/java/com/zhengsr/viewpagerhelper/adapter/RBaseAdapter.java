package com.zhengsr.viewpagerhelper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;



import java.util.List;

/**
 * Created by zhengshaorui on 2018/4/13.
 */
public abstract class RBaseAdapter<T> extends RecyclerView.Adapter<RBaseViewholder> {
    private int mLayoutId;
    private List<T> mDatas; //取泛型是因为不知道bean是啥类型的。
    private int mWidth;

    private RBaseViewholder mViewholder;

    public RBaseAdapter(int layoutid,List<T> list) {
        mLayoutId = layoutid;
        mDatas = list;
    }
    public interface onItemClickListener{
        void onItemClick(View view, int position);
    }
    private onItemClickListener mOnItemClickListener;
    public void setonItemClickListener(onItemClickListener listener){
        mOnItemClickListener = listener;
    }


    @Override
    public RBaseViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        mViewholder = RBaseViewholder.getViewHolder(parent.getContext(),
                mLayoutId,parent);
        return mViewholder;
    }

    @Override
    public void onBindViewHolder(RBaseViewholder holder, int position) {
        if (mOnItemClickListener != null){
            final int pos = position;
            holder.getConserView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v,pos);
                }
            });

        }
        getConver(holder,mDatas.get(position)); //提供 viewholder 出去，数据由用户处理
    }

    public abstract void getConver(RBaseViewholder holder,T data);

    //edit by fang
    @Override
    public int getItemCount() {
        return mDatas!=null?mDatas.size():0;
    }
	//end 
}