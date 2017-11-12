package com.zhengsr.viewpagerhelper.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerhelper.fragment.CusFragment;
import com.zhengsr.viewpagerlib.indicator.TabIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RectTabActivity extends AppCompatActivity {
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitle = Arrays.asList("新闻","娱乐","学习",
            "新闻","娱乐","学习",
            "新闻","娱乐","学习");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recttab_page);

        for (String string : mTitle) {
            CusFragment fragment = CusFragment.newInStance(string);
            mFragments.add(fragment);
        }
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabIndicator tritabIndecator = (TabIndicator) findViewById(R.id.line_indicator);
        viewPager.setAdapter(new CusAdapter(getSupportFragmentManager()));
       // tritabIndecator.setTabData(viewPager,mTitle);
        tritabIndecator.setViewPagerSwitchSpeed(viewPager,600);
        tritabIndecator.setTabData(viewPager,mTitle, new TabIndicator.TabClickListener() {
            @Override
            public void onClick(int position) {
                viewPager.setCurrentItem(position);
            }
        });


    }


    class CusAdapter extends FragmentPagerAdapter{

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

}
