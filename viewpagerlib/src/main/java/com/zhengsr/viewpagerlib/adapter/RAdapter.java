package com.zhengsr.viewpagerlib.adapter;

import android.view.View;
import android.view.ViewGroup;


import androidx.recyclerview.widget.RecyclerView;

import com.zhengsr.viewpagerlib.ViewPagerHelperUtils;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;

import java.util.List;

/**
 * @auther by zhengshaorui on 2020/4/28
 * describe:
 */
public abstract class RAdapter<T> extends RecyclerView.Adapter<RViewholder> {
    private int mLayoutId;
    private List<T> mDatas; //取泛型是因为不知道bean是啥类型的。
    private int mWidth;

    private RViewholder mViewholder;
    private boolean isCycle;
    private PageHelperListener mListener;
    public RAdapter(int layoutid, List<T> list, boolean isCycle, PageHelperListener listener) {
        mLayoutId = layoutid;
        mDatas = list;
        this.isCycle = isCycle;
        mListener = listener;
    }
    public interface onItemClickListener{
        void onItemClick(View view, int position);
    }



    @Override
    public RViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        mViewholder = RViewholder.getViewHolder(parent.getContext(),
                mLayoutId,parent);
        return mViewholder;
    }

    @Override
    public void onBindViewHolder(final RViewholder holder, final int position) {
        final int index;
        if (isCycle) {
            index = position % mDatas.size();
        }else{
            index = position;
        }

        holder.getConserView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(holder.getConserView(),mDatas.get(index),index);
                }
            }
        });

        //提供 viewholder 出去，数据由用户处理
        convert(holder,mDatas.get(index),index);
    }

    public abstract void convert(RViewholder holder, T data,int position);

    //edit by fang
    @Override
    public int getItemCount() {
        if (isCycle){
            return mDatas != null ? mDatas.size() + ViewPagerHelperUtils.LOOP_COUNT : 0;
        }else {
            return mDatas != null ? mDatas.size() : 0;
        }
    }
	//end 
}