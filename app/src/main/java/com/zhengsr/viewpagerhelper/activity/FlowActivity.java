package com.zhengsr.viewpagerhelper.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerlib.view.flow.adapter.TagFlowAdapter;
import com.zhengsr.viewpagerlib.view.flow.TagFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlowActivity extends AppCompatActivity {
    private List<String> mTitle = new ArrayList<>(Arrays.asList("新闻", "娱乐", "学习", "测试后", "新闻", "娱乐", "学习", "测试后"));
    private List<String> mTitle2 = new ArrayList<>(Arrays.asList("An Android TabLayout Lib has 3 kinds of TabLayout at present.".split(" ")));
    private TagAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        TagFlowLayout tagFlowLayout1 = findViewById(R.id.tabflow1);
        mAdapter = new TagAdapter(R.layout.item_textview, mTitle);
        tagFlowLayout1.setAdapter(mAdapter);

        TagFlowLayout tagFlowLayout2 = findViewById(R.id.tabflow2);
        TagAdapter2 adapter2 = new TagAdapter2(R.layout.item_textview, mTitle2);
        tagFlowLayout2.setMaxCount(3);
        tagFlowLayout2.setAdapter(adapter2);


    }

    public void update(View view) {
        mTitle.clear();
        mTitle.addAll(mTitle2);
        mAdapter.notifyDataChanged();
    }

    class TagAdapter extends TagFlowAdapter<String> {

        public TagAdapter(int layoutId, List<String> datas) {
            super(layoutId, datas);
        }

        @Override
        public void onBindView(View view, String data, int position) {
            TextView textView = view.findViewById(R.id.item_text);
            textView.setText(data);
        }

        @Override
        public void onItemClick(View view, String data, int position) {
            super.onItemClick(view, data, position);
            // Log.d(TAG, "zsr - onItemClick: " + "位置： " + position + " " + data);
        }

        @Override
        public void onItemSelectState(View view, boolean isSelected) {
            super.onItemSelectState(view, isSelected);
            TextView textView = view.findViewById(R.id.item_text);
            if (isSelected) {
                textView.setTextColor(Color.WHITE);
            } else {
                textView.setTextColor(Color.GRAY);
            }
        }

    }

    class TagAdapter2 extends TagFlowAdapter<String> {

        public TagAdapter2(int layoutId, List<String> datas) {
            super(layoutId, datas);
        }

        @Override
        public void onBindView(View view, String data, int position) {
            TextView textView = view.findViewById(R.id.item_text);
            textView.setText(data);
        }

        @Override
        public void onItemClick(View view, String data, int position) {
            super.onItemClick(view, data, position);
            // Log.d(TAG, "zsr - onItemClick: " + "位置： " + position + " " + data);
        }

        @Override
        public void onItemSelectState(View view, boolean isSelected) {
            super.onItemSelectState(view, isSelected);
            TextView textView = view.findViewById(R.id.item_text);
            if (isSelected) {
                textView.setTextColor(Color.WHITE);
            } else {
                textView.setTextColor(Color.GRAY);
            }
        }

        @Override
        public void onClickMaxCount(List<Integer> ids, int count) {
            super.onClickMaxCount(ids, count);
            Toast.makeText(FlowActivity.this, "最多只能选中 "+count+" 个"+" 已选中坐标: "+ids, Toast.LENGTH_SHORT).show();
        }


    }
}
