package com.zhengsr.viewpagerhelper.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerhelper.fragment.CusFragment;
import com.zhengsr.viewpagerlib.ViewPagerHelperUtils;
import com.zhengsr.viewpagerlib.view.flow.TabFlowLayout;
import com.zhengsr.viewpagerlib.view.flow.adapter.TabFlowAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabFlowActivity extends AppCompatActivity {
    private static final String TAG = "TabFlowActivity";
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitle = Arrays.asList("Java","Python","Kotlin");
    private List<String> mTitle2 = new ArrayList<>(Arrays.asList("An Android TabLayout Lib has 3 kinds of TabLayout at present.".split(" ")));
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);


        for (String string : mTitle) {
            CusFragment fragment = CusFragment.newInStance(string);
            mFragments.add(fragment);
        }
        TabFlowLayout tabFlowLayout = findViewById(R.id.tabflowlayout1);
        tabFlowLayout.setAdapter(new TabAdapter(R.layout.item_tab_text,mTitle));
        mViewPager = findViewById(R.id.viewpager1);
        ViewPagerHelperUtils.initSwitchTime(this,mViewPager,600);
        mViewPager.setAdapter(new CusAdapter(getSupportFragmentManager()));
        tabFlowLayout.setViewPager(mViewPager);

        TabFlowLayout tabFlowLayout2 = findViewById(R.id.tabflowlayout2);
        tabFlowLayout2.setAdapter(new TabAdapter2(R.layout.item_tab_text,mTitle2));
    }



    class CusAdapter extends FragmentPagerAdapter {

        public CusAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }


    class TabAdapter extends TabFlowAdapter<String>{

        public TabAdapter(int layoutId, List<String> datas) {
            super(layoutId, datas);
        }

        @Override
        public void onBindView(View view, String data, int position) {
            setText(view,R.id.item_text,data);
        }

        @Override
        public void onItemClick(View view, String data, int position) {
            super.onItemClick(view, data, position);
            mViewPager.setCurrentItem(position);
        }
    }

    class TabAdapter2 extends TabFlowAdapter<String>{

        public TabAdapter2(int layoutId, List<String> datas) {
            super(layoutId, datas);
        }

        @Override
        public void onBindView(View view, String data, int position) {
            setText(view,R.id.item_text,data);
        }
    }
}
