package com.zhengsr.viewpagerhelper.activity.guide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhengsr.viewpagerhelper.R;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
    }

    public void glide(View view) {
        startActivity(new Intent(this, GlidenormalActivity.class));
    }

    public void glide_tran(View view) {
        startActivity(new Intent(this, GlideTransActivity.class));
    }

    public void glide_scale(View view) {
        startActivity(new Intent(this, GlideZoomActivity.class));
    }
}
