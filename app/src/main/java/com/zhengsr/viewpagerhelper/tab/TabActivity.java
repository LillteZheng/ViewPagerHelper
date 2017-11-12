package com.zhengsr.viewpagerhelper.tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhengsr.viewpagerhelper.R;

import java.util.ArrayList;
import java.util.List;

public class TabActivity extends AppCompatActivity {
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_page);



    }

    public void tri(View view) {
        startActivity(new Intent(this,TriTabActivity.class));
    }

    public void rect(View view) {
        startActivity(new Intent(this,RectTabActivity.class));
    }

    public void color(View view) {
        startActivity(new Intent(this,ColorTabActivity.class));
    }



}
