## 轮播效果图

<table  align="center">
 <tr>
    <th>魅族效果</th>
    <th>扇形效果</th>
  </tr>
   <tr>
    <td><a href="url"><img src="https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/cirtorect.gif" align="left" height="200" width="340"></a></td>
    <td><a href="url"><img src="https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/loop_arc.gif" align="left" height="200" width="340" ></a></td>
  </tr>

</table>

<table  align="center">
 <tr>
    <th>条状效果</th>
    <th>文字效果</th>
  </tr>
   <tr>
    <td><a href="url"><img src="https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/loop_line.gif" align="left" height="200" width="340"></a></td>
    <td><a href="url"><img src="https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/loop_text.gif" align="left" height="200" width="340" ></a></td>
  </tr>

</table>

<table  align="center">
 <tr>
    <th>图片放大效果</th>
    <th>卡片效果</th>
  </tr>
   <tr>
    <td><a href="url"><img src="https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/scale.gif" align="left" height="480" width="340"></a></td>
    <td><a href="url"><img src="https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/card.gif" align="left" height="480" width="340" ></a></td>
  </tr>

</table>

## 使用

主要为 **BannerViewPager** 这个类，在布局中，添加自己喜欢的 Indicator 即可。


## ViewPager2 的使用

ViewPager2 使用的类是 BannerViewPager2，所以它的使用与 BannerViewPager 差不多，只是魅族方法中，
偏移量使用的是 **banner2_l_margin和banner2_r_margin**，也可以动态设置，使用**setMzMargin()**,
当然，此时 xml 就不要用 layout_margin = "20dp"这样的方法了。
为什么不统一？ViewPager2 是final，继承不了，没辙。。。。

### Banner + Indicator
首先，一个简单的 banner + indicator 如下，如果想要第一种效果，记得父布局 clipChildren="false"：
```
<FrameLayout
    android:layout_width="match_parent"
    android:layout_height="200dp"
    android:clipChildren="false">

    <com.zhengsr.viewpagerlib.view.BannerViewPager
        android:id="@+id/loop_viewpager_mz"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginLeft="20dp"
        android:layout_marginRight="20dp"
        app:banner_isAutoLoop="true"
        app:banner_looptime="3000"
        app:banner_transformer="mz"
        app:banner_iscycle="true"
        app:banner_switchtime="600" />

    <com.zhengsr.viewpagerlib.indicator.CircleIndicator
        android:id="@+id/rect_indicator"
        android:layout_width="wrap_content"
        android:layout_height="30dp"
        android:layout_gravity="end|bottom"
        android:layout_marginEnd="20dp"
        app:cir_size="5dp"
        app:cir_rect_width="10dp"
        app:cir_normalColor="@color/gray"
        app:cir_selectedColor="@color/white"
        app:cir_horizon_margin="10dp"
        app:cir_type="cirToRect" />

</FrameLayout>
```

首先，设置 Banner 是自动播放且循环的，然后 indicator 为圆点，选中的矩形；当然这些自定义属性也支持动态配置，
具体参考demo或者看示例、

接着，在 Activity 中，这样配置：

```
BannerViewPager bannerViewPager = findViewById(R.id.rect_banner);
CircleIndicator indicator = findViewById(R.id.rect_indicator);
//指定从哪一页开始
//bannerViewPager.setCurrentPosition(1);
//添加自定义数据,PageBean
//bannerViewPager.addPageBean(bean)
//把需要的 indicator 添加进去
bannerViewPager.addIndicator(indicator);


/**
 * 设置监听即可，loop_layout 为要展示的内容，比如一个 ImageView，或者参考示例
 * 其中，setText 为模板方法，为了简便代码，当然还有其他一些方法，可查阅 PageHelperListener
 * onItemClick 为点击事件，当然还有其他方法，重写即可，比如子控件事件 onItemChildClick，如果有子控件
 * 的点击事件，需要先在 bindView 中注册，比如 addChildrenClick(view,R.id.item_text,position)，
 * 其他一些方法，可查阅 PageHelperListener
 */
bannerViewPager.setPageListener(R.layout.loop_layout, mDatas, new PageHelperListener<TestBean>() {
    @Override
    public void bindView(View view, final TestBean data, int position) {
        setText(view, R.id.loop_text, data.msg);
        
        //注册子控件事件
        //addChildrenClick(view,R.id.item_text,position);

        ImageView imageView = view.findViewById(R.id.loop_icon);
        GlideApp.with(view)
                .load(data.resId)
                .into(imageView);
    }

    @Override
    public void onItemClick(View view, TestBean data, int position) {
        super.onItemClick(view, data, position);
        Toast.makeText(CircleIndicatorActivity.this, data.msg+" "+position, Toast.LENGTH_SHORT).show();
    }
    
    
});

```

**关于Databinding的使用**

由于库并没有支持 databinding，所以可以在banner的布局中，添加自定义的 bindingAdapter ，再去配置数据，如下：
**xml**
```
<com.zhengsr.wanandroid_jetpack.ui.BannerView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:banner="@{vm.bannerBeans}"
    app:layout_scrollFlags="exitUntilCollapsed|scroll|enterAlwaysCollapsed"
    />
```
**代码**
```
    @JvmStatic
    @BindingAdapter("banner")
    fun banner(bannerViewPager2: BannerViewPager2,datas: MutableList<BannerBean>?){
        //直接自定义viewgroup，将banner和indicator封装成一个view，然后通过 BindingAdapter 传递数据即可
        datas?.let {
            bannerView.setData(datas)
        }
    }
```


如果你要使用弧形图片，可以用在你的layout 中，使用 ArcImageView 这个控件，可以这样配置：
```
 <!--弧形图片，arc_height 为弧度的高度-->
    <com.zhengsr.viewpagerlib.view.ArcImageView
        android:id="@+id/arc_icon"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:arc_height="15dp"
        android:scaleType="centerCrop"/>
```
如果你要使用缩放控件，可以用 ScaleImageView 这个控件，可以这样配置：
```
 <!--缩放图片控件，属性可参看最后面的说明-->
<com.zhengsr.viewpagerlib.view.ScaleImageView
    android:id="@+id/scaleview"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:scale_double_factor="2"
    app:scale_auto_time="5"
    app:scale_autofit="true"
    app:scale_max_factor="6"
    app:scale_limit_board="true"
    app:scale_interrupt_parent_touch="true"/>
```

### 卡片效果
PageBean 为要添加的数据，这里也支持动态数据添加，比如卡片效果，它的xml如下：
```
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="20dp"
    android:clipChildren="false"
    android:orientation="vertical">


     <com.zhengsr.viewpagerlib.view.BannerViewPager
         android:id="@+id/loop_viewpager_card"
         android:layout_width="match_parent"
         android:layout_height="match_parent"
         android:clipChildren="false"
         android:layout_marginBottom="20dp"
         app:banner_card_height="20dp"
         app:banner_transformer="card"
         />

</LinearLayout>
```

## Indicator

在本库中，只提供三种 Indicator，如圆点 CircleIndicator，矩形 RectIndicator 和文字 TextIndicator 三种；

效果如下:

<table  align="center">
 <tr>
    <th>CircleIndicator</th>
    <th>RectIndicator</th>
  </tr>
   <tr>
    <td><a href="url"><img src="https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/circleindicator.gif" align="left" height="600" width="350"></a></td>
    <td><a href="url"><img src="https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/rectindicator.gif" align="left" height="600" width="350" ></a></td>
  </tr>

</table>

## 其他说明

- startAnim()  启动轮播，即开始轮播
- stopAnim() 关闭轮播，可以通过这两个api实现轮播与否
- isOutVisiableWindow() 判断轮播图是否超过可视界面


## 代码帮助
可以通过代码去查看怎么配置

[CircleIndicator xml](https://github.com/LillteZheng/ViewPagerHelper/blob/master/app/src/main/res/layout/activity_mz_loop.xml)
[CircleIndicator Activity](https://github.com/LillteZheng/ViewPagerHelper/blob/master/app/src/main/java/com/zhengsr/viewpagerhelper/activity/loop/CircleIndicatorActivity.java)

[卡片xml](https://github.com/LillteZheng/ViewPagerHelper/blob/master/app/src/main/res/layout/activity_card_loop.xml)
[卡片Activity](https://github.com/LillteZheng/ViewPagerHelper/blob/master/app/src/main/java/com/zhengsr/viewpagerhelper/activity/loop/CardLoopActivity.java)
