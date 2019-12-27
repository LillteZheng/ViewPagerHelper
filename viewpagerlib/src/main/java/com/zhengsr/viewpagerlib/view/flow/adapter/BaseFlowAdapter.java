package com.zhengsr.viewpagerlib.view.flow.adapter;

import android.util.SparseArray;
import android.view.View;

import java.util.List;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 基类的数据，不公布给外部
 */
public abstract class BaseFlowAdapter<T> {
    protected int layoutId;
    protected List<T> datas;

    public BaseFlowAdapter(int layoutId, List<T> datas) {
        this.layoutId = layoutId;
        this.datas = datas;
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

    public interface DataListener{
        void notifyDataChanged();
    }
    private DataListener listener;
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
    }
}
