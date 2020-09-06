package com.zhengsr.viewpagerhelper.tab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


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
