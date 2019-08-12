package com.zhengsr.viewpagerhelper.activity.loop;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerlib.view.ScaleImageView;

import java.util.ArrayList;
import java.util.List;

public class ScaleImageActivity extends AppCompatActivity {
    private int[] mRes = new int[]{
            R.mipmap.beauty1,R.mipmap.beauty2,R.mipmap.beauty3,R.mipmap.beauty4
    };
    private List<View> mViews = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_image);
        ViewPager viewPager = findViewById(R.id.viewpager);

        for (int i = 0; i < mRes.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_scaleview,null);
            ScaleImageView scaleImageView = view.findViewById(R.id.scaleview);
            scaleImageView.setImageResource(mRes[i]);
            mViews.add(view);
        }
        viewPager.setAdapter(new ImageAdapter());
    }


    class ImageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mViews.size();
        }
        @Override
        // 每次滑动时销毁当前组件
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView(mViews.get(position));
        }
        // 每次滑动时，创建当前组件
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViews.get(position));
            return mViews.get(position);
        }


        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;
        }
    }
}
