package com.zhengsr.viewpagerhelper.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerlib.view.ArcImageView;


public class ArcActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc);

        ArcImageView imageView = findViewById(R.id.image);

        Glide.with(this)
                .load(R.mipmap.beauty1)
                .into(imageView);

     /*   imageView.arcHeight(50)
                .blur(10)
                .scaleFactor(1.2f)
                .scaleX(400)
                .scaleY(0)
                .update();*/





    }

}
