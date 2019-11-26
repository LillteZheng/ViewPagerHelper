package com.zhengsr.viewpagerhelper;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zhengsr.viewpagerhelper.activity.ArcActivity;
import com.zhengsr.viewpagerhelper.activity.loop.LoopActivity;
import com.zhengsr.viewpagerhelper.activity.loop.NetWorkActivity;
import com.zhengsr.viewpagerhelper.activity.loop.ScaleImageActivity;
import com.zhengsr.viewpagerhelper.activity.guide.GuideActivity;
import com.zhengsr.viewpagerhelper.tab.TabActivity;
import com.zhengsr.viewpagerlib.view.ArcImageView;
import com.zhengsr.viewpagerlib.view.ColorTextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "zsr";
    private static final int[] RES = {R.mipmap.guide1,R.mipmap.guide2,R.mipmap.guide3,
            R.mipmap.guide4 };
    private ColorTextView mColorTextView;
    private List<String> mTitle = Arrays.asList("新闻\n不是新闻","娱乐","学习","测试后");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mColorTextView = (ColorTextView) findViewById(R.id.colortext);




    }






    public void loop_max(View view) {
        startActivity(new Intent(this, LoopActivity.class));
    }

    public void fragment(View view) {
        startActivity(new Intent(this,TabActivity.class));
    }

    public void leftchange(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mColorTextView,"progress",0,1);
        animator.setDuration(2000).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mColorTextView.setprogress(value,ColorTextView.DEC_LEFT);
            }
        });


    }

    public void rightchange(View view) {

        ObjectAnimator animator = ObjectAnimator.ofFloat(mColorTextView,"progress",0,1);
        animator.setDuration(2000).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mColorTextView.setprogress(value,ColorTextView.DEC_RIGHT);
            }
        });
    }


    public void guide(View view) {
        startActivity(new Intent(this, GuideActivity.class));

    }

    public void arc(View view) {
        startActivity(new Intent(this, ArcActivity.class));
    }
}
