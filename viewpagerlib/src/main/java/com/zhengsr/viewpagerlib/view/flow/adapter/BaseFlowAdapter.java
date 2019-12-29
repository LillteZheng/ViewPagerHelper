package com.zhengsr.viewpagerlib.view.flow.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.zhengsr.viewpagerlib.R;

import java.util.List;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 基类的数据，不公布给外部
 */
public abstract class BaseFlowAdapter<T> {
    protected int layoutId;
    protected List<T> datas;
    protected DataListener listener;
    private SparseArray<View> sparseArray;

    public BaseFlowAdapter(int layoutId, List<T> datas) {
        this.layoutId = layoutId;
        this.datas = datas;
        sparseArray = new SparseArray<>();
    }

    /**
     * 获取数据大小
     * @return
     */
    public int getItemCount(){
        return datas != null ? datas.size() : 0;
    }

    /**
     * 获取view的layoutid
     * @return
     */
    public int getLayoutId(){
        return layoutId;
    }

    /**
     * 获取数据
     * @return
     */
    public List<T> getDatas() {
        return datas;
    }


    /**
     * 绑定数据
     */
    public abstract void onBindView(View view, T data, int position);


    /**
     * 点击事件
     */
    public void onItemClick(View view,T data,int position){}

    /**
     * 长按
     */
    public  boolean onItemLongClick(View view,int position){
        return true;
    };

    public void onItemChildClick(View childView,int position){}

    public interface DataListener{
        void notifyDataChanged();
        void resetAll();
    }

    public void setListener(DataListener listener){
        this.listener = listener;
    }

    /**
     * 重新设置数据
     */
    public void setDatas(List<T> datas){
        this.datas = datas;
    }



    /**
     * 刷新数据
     */
    public void notifyDataChanged(){
        if (listener != null) {
            listener.notifyDataChanged();
        }
        sparseArray.clear();
    }

    public SparseArray<View> getSparseArray() {
        return sparseArray;
    }

    /**
     * 一些常用方法
     */

    public BaseFlowAdapter setText(View view,int viewId,int resId){
        TextView textView = view.findViewById(viewId);
        if (textView != null) {
            textView.setText(resId);
        }
        return this;
    }
    public BaseFlowAdapter setText(View view,int viewId,String data){
        TextView textView = view.findViewById(viewId);
        if (textView != null) {
            textView.setText(data);
        }
        return this;
    }
    public BaseFlowAdapter setVisiable(View view,int viewId,boolean isVisiable){
        View viewById = view.findViewById(viewId);
        if (viewById != null) {
            if (isVisiable){
                viewById.setVisibility(View.VISIBLE);
            }else{
                viewById.setVisibility(View.GONE);
            }
        }
        return this;
    }

    /**
     * 如果布局里的子控件需要点击事件，需要现在这里注册
     * @param viewId
     * @return
     */
    public BaseFlowAdapter addChildrenClick(View view, int viewId, final int position){
        final View viewById = view.findViewById(viewId);
        if (viewById != null) {
            viewById.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemChildClick(viewById,position);
                }
            });
        }
        return this;
    }


}
