package com.zhengsr.viewpagerhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhengsr.viewpagerhelper.activity.ArcActivity;
import com.zhengsr.viewpagerhelper.activity.loop.CircleIndicatorActivity;
import com.zhengsr.viewpagerhelper.activity.loop.LoopActivity;
import com.zhengsr.viewpagerhelper.activity.loop.RectIndicatorActivity;
import com.zhengsr.viewpagerhelper.activity.loop.TextIndicatorActivity;
import com.zhengsr.viewpagerhelper.tab.TabActivity;
import com.zhengsr.viewpagerlib.indicator.TextIndicator;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "zsr";
    private static final int[] RES = {R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3,
            R.mipmap.guide4};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this, LoopActivity.class));



    }




    public void loop_max(View view) {
        startActivity(new Intent(this, LoopActivity.class));
    }

    public void fragment(View view) {
        startActivity(new Intent(this, TabActivity.class));
    }



    public void arc(View view) {
        startActivity(new Intent(this, ArcActivity.class));
    }

}
