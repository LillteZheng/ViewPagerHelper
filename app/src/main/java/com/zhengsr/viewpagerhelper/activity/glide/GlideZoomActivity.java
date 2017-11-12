package com.zhengsr.viewpagerhelper.activity.glide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhengsr.viewpagerhelper.MainActivity;
import com.zhengsr.viewpagerhelper.R;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.indicator.ZoomIndicator;
import com.zhengsr.viewpagerlib.view.GlideViewPager;

import java.util.ArrayList;
import java.util.List;

public class GlideZoomActivity extends AppCompatActivity {

    private static final int[] RES = {R.mipmap.guide1,R.mipmap.guide2,R.mipmap.guide3,
            R.mipmap.guide4 };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_zoom);
        initView();
    }

    public void initView() {

        GlideViewPager viewPager = (GlideViewPager) findViewById(R.id.splase_viewpager);
        ZoomIndicator zoomIndicator = (ZoomIndicator) findViewById(R.id.splase_bottom_layout);
        Button button = (Button) findViewById(R.id.splase_start_btn);


        //先把本地的图片 id 装进 list 容器中
        List<Integer> images = new ArrayList<>();
        for (int i = 0; i < RES.length; i++) {
            images.add(RES[i]);

        }
        //配置pagerbean，这里主要是为了viewpager的指示器的作用，注意记得写上泛型
        PageBean bean = new PageBean.Builder<Integer>()
                .setDataObjects(images)
                .setIndicator(zoomIndicator)
                .setOpenView(button)
                .builder();

        // 把数据添加到 viewpager中，并把view提供出来，这样除了方便调试，也不会出现一个view，多个
        // parent的问题，这里在轮播图比较明显
        viewPager.setPageListener(bean, R.layout.image_layout, new PageHelperListener() {
            @Override
            public void getItemView(View view, Object data) {
                //通过获取到这个view，你可以随意定制你的内容
                ImageView imageView = view.findViewById(R.id.icon);
                imageView.setImageResource((Integer) data);
            }
        });




        //点击实现跳转功能
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GlideZoomActivity.this,MainActivity.class));
                Toast.makeText(GlideZoomActivity.this, "回到首页", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
