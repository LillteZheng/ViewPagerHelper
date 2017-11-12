package com.zhengsr.viewpagerhelper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/11/10.
 */

public class CusFragment extends Fragment {

    public static final String ARGUMENT = "argument";
    private String mTitle;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        TextView textView = new TextView(getActivity());
        if (bundle != null){
            textView.setText(bundle.getString(ARGUMENT));
        }else{
            textView.setText("nothing here");
        }
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    /**
     * 弄一个静态工厂的方法调用 用于传参
     * @param key
     * @return
     */
    public static CusFragment newInStance(String key){
        CusFragment fragment = new CusFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT,key);
        fragment.setArguments(bundle);
        return fragment;
    }
}
