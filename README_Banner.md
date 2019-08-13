## 轮播效果图

<table  align="center">
 <tr>
    <th>魅族效果</th>
    <th>扇形效果</th>
  </tr>
   <tr>
    <td><a href="url"><img src="https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/loop_scale.gif" align="left" height="200" width="340"></a></td>
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

### 魅族效果
比如魅族的效果，布局如下,记得 clipChildren="false"：
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

    <com.zhengsr.viewpagerlib.indicator.ZoomIndicator
        android:id="@+id/scale_indicator"
        android:layout_width="wrap_content"
        android:layout_height="30dp"
        android:layout_gravity="bottom|right"
        android:layout_marginRight="20dp"
        android:gravity="center"
        app:zoom_alpha_min="0.5"
        app:zoom_leftmargin="10dp"
        app:zoom_max="1.5"
        />

</FrameLayout>
```
这里仅仅是 viewpager + indicator ， 具体的图片和局部需要自己写，往下看。

ZoomIndicator 圆圈放大效果，banner_transformer 可以配置不同效果，共有4中，card、zoom、mz、depath ，可以自行体验
接着去到配置数据：
```
private void initView() {
        BannerViewPager bannerViewPager = findViewById(R.id.loop_viewpager_mz);
        ZoomIndicator zoomIndicator = findViewById(R.id.scale_indicator);
        List<MzBean> beans = new ArrayList<>();
        //配置数据，这里是resid和text
        for (int i = 0; i < TEXT.length; i++) {
            MzBean bean = new MzBean();
            bean.resId = RESID[i];
            bean.msg = TEXT[i];
            beans.add(bean);
        }

        /**
         * PageBean 必填，记得泛型写上自己的类型
         */
        PageBean pageBean = new PageBean.Builder<MzBean>()
                .data(beans)
                .indicator(zoomIndicator)
                .builder();

        /**
         * 可以在 PageHelperListener 写上泛型，这样就可以直接拿到数据了
         */
        bannerViewPager.setPageListener(pageBean, R.layout.loop_layout, new PageHelperListener<MzBean>() {

            @Override
            public void getItemView(View view, final MzBean data) {
                ImageView imageView = view.findViewById(R.id.loop_icon);
                GlideApp.with(view)
                        .load(data.resId)
                        .into(imageView);
                TextView textView = view.findViewById(R.id.loop_text);
                textView.setText(data.msg);
                
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MzLoopActivity.this, data.msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
```
R.layout.loop_layout 为你真正要添加的布局，即上图的 iamgeview+textview的显示。

如果你要使用弧形图片，可以用 ArcImageView 这个控件，可以这样配置：
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
        android:layout_marginBottom="20dp"
        />

</LinearLayout>
```
看到我们只是添加 BannerViewPager，数据都是动态的，如：
```
private void initView() {
    mBannerViewPager = findViewById(R.id.loop_viewpager_card);

    List<CardBean> beans = new ArrayList<>();
    for (int i = 0; i < TEXT.length; i++) {
        CardBean bean = new CardBean();
        bean.msg = TEXT[i];
        beans.add(bean);
    }

    PageBean pageBean = new PageBean.Builder<CardBean>()
            .useCode(true) //比填，不然不起作用
            .autoLoop(true) //自动轮播
            .pagerSwitchTime(600) //切换速度
            .loopTime(4000)  //轮播事件
            .data(beans) //添加数据
            .cardHeight(30)  //卡片的高度
            .cycle(true)  //是否填充可以循环
            .bannerTransformer(BannerTransType.CARD)  //设置 transformer，即卡片效果，也可以是魅族，zoom，depath的效果
            .builder();


    /**
     * 配置数据，记得在 PageHelperListener 配置你的泛型数据哦
     */
    mBannerViewPager.setPageListener(pageBean, R.layout.item_card, new PageHelperListener<CardBean>() {

        @Override
        public void getItemView(View view, CardBean data) {
            TextView textView = view.findViewById(R.id.item_card_tv);
            textView.setText(data.msg);
        }
    });
}
```

只需要动态配置 PageBean 即可。

## 其他说明

- startAnim()  启动轮播，即开始轮播
- stopAnim() 关闭轮播，可以通过这两个api实现轮播与否
- isOutVisiableWindow() 判断轮播图是否超过可视界面


## 代码帮助
可以通过代码去查看怎么配置

[魅族xml](https://github.com/LillteZheng/ViewPagerHelper/blob/master/app/src/main/res/layout/activity_mz_loop.xml)
[魅族Activity](https://github.com/LillteZheng/ViewPagerHelper/blob/master/app/src/main/java/com/zhengsr/viewpagerhelper/activity/loop/MzLoopActivity.java)

[卡片xml](https://github.com/LillteZheng/ViewPagerHelper/blob/master/app/src/main/res/layout/activity_card_loop.xml)
[卡片Activity](https://github.com/LillteZheng/ViewPagerHelper/blob/master/app/src/main/java/com/zhengsr/viewpagerhelper/activity/loop/CardLoopActivity.java)
