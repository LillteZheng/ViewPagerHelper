package com.zhengsr.viewpagerlib.view.flow.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public abstract class LabelFlowAdapter<T>  extends BaseFlowAdapter<T>{


    public LabelFlowAdapter(int layoutId, List<T> datas) {
        super(layoutId, datas);
    }

    public void onItemSelectState(View view, boolean isSelected){}

    public  void onClickMaxCount(List<Integer> ids, int count){};

    public  void onFocusChanged(View oldView, View newView){};

    /**
     * 恢复所有状态
     */
    public void resetStatus(){
        if (listener != null) {
            listener.resetAll();
        }
    }

}
