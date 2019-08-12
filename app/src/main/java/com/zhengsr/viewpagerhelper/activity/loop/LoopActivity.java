package com.zhengsr.viewpagerhelper.activity.loop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhengsr.viewpagerhelper.GlideApp;
import com.zhengsr.viewpagerhelper.LoopBean;
import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerlib.anim.MzTransformer;
import com.zhengsr.viewpagerlib.anim.CardTransformer;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.TextIndicator;
import com.zhengsr.viewpagerlib.indicator.TransIndicator;
import com.zhengsr.viewpagerlib.indicator.ZoomIndicator;
import com.zhengsr.viewpagerlib.view.ArcImageView;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

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

    public void mz(View view) {
        startActivity(new Intent(this,MzLoopActivity.class));
    }

    public void arc(View view) {
        startActivity(new Intent(this,ArcLoopActivity.class));
    }

    public void recttext(View view) {
        startActivity(new Intent(this,RectTextLoopActivity.class));
    }

    public void scaleiamge(View view) {
        startActivity(new Intent(this,ScaleImageActivity.class));
    }

    public void net(View view) {
        startActivity(new Intent(this,NetWorkActivity.class));
    }
}
