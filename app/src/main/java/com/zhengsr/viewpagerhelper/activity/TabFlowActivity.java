package com.zhengsr.viewpagerhelper.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerlib.view.flow.TabFlowLayout;
import com.zhengsr.viewpagerlib.view.flow.adapter.TabFlowAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabFlowActivity extends AppCompatActivity {
    private static final String TAG = "TabFlowActivity";
    private List<String> mTitle2 = new ArrayList<>(Arrays.asList("An Android TabLayout Lib has 3 kinds of TabLayout at present.".split(" ")));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        TabFlowLayout tabFlowLayout = findViewById(R.id.tabflowlayout);
        tabFlowLayout.setAdapter(new TabAdapter(R.layout.item_tab_text,mTitle2));
    }


    class TabAdapter extends TabFlowAdapter<String>{

        public TabAdapter(int layoutId, List<String> datas) {
            super(layoutId, datas);
        }

        @Override
        public void onBindView(View view, String data, int position) {
            setText(view,R.id.item_text,data);
        }
    }
}
