package com.zhengsr.viewpagerhelper.activity.loop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhengsr.viewpagerhelper.R;

public class LoopActivity extends AppCompatActivity {
    private static final String TAG = "LoopActivity";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop);
    }




    public void card(View view) {
        startActivity(new Intent(this,CardLoopActivity.class));
    }

    public void circleIndicator(View view) {
        startActivity(new Intent(this, CircleIndicatorActivity.class));
    }

    public void arc(View view) {
        startActivity(new Intent(this,ArcLoopActivity.class));
    }


    public void net(View view) {
        startActivity(new Intent(this,NetWorkActivity.class));
    }

    public void rectIndicator(View view) {
        startActivity(new Intent(this,RectIndicatorActivity.class));
    }

    public void recttext(View view) {

        startActivity(new Intent(this,TextIndicatorActivity.class));
    }

    public void scaleiamge(View view) {
        startActivity(new Intent(this,ScaleImageActivity.class));
    }
}
