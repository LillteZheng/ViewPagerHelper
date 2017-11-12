

# ViewPagerHelper
这是一个 viewpager 的工具类，可以帮助你快速实现，app启动页、图片轮播、viewpager + fragment 等功能，并添加多种指示器功能

## 使用

```
allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
然后添加：
```
dependencies {
        compile 'com.github.LillteZheng:viewpagerhelper:v1.0'
    }
```

**目前还只是添加了app首次启动引导页，还是图片轮播图；后期将会加入更多的内容**

## 图片轮播
首先，先看四种效果：

**指示器为放大缩小，加上viewpager也是放大缩小的**



![image](https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/loop_mz.gif)

**指示器为放大缩小，图片为比较流行的弧形图片**

![image](https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/loop_arc.gif)

**指示器为移动版的，注意小圆点的移动**

![image](https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/loop_trans.gif)

**指示器文字，可以去掉文字的圆圈和设置文字的颜色**

![image](https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/loop_text.gif)


如果你要使用图片功能，只需要几步即可：

**step1：**

配置数据,这里把 url 的连接封装到list中
```
        List<LoopBean> loopBeens = new ArrayList<>();
        for (int i = 0; i < TEXT.length; i++) {
           LoopBean bean = new LoopBean();
            bean.url = RESURL[i];
            bean.text = TEXT[i];
            loopBeens.add(bean);

        }
```
**step2：**

配置PageBean，这里主要是为了viewpager的指示器的作用，注意记得写上泛型.这里为上面的LoopBean

```
PageBean bean = new PageBean.Builder<LoopBean>()
                .setDataObjects(loopBeens)
                .setIndicator(zoomIndicator)
                .builder();
```

设置viewpager的动画，这里提供了三种，分别是MzTransformer，ZoomOutPageTransformer, 和DepthPageTransformer，可以体验一下,这里可有可无，这里我设置了魅族的效果，这样加上弧形的图片更好看。
```
mBannerCountViewPager.setPageTransformer(false,new MzTransformer());
```
**step3：**

 view 的接口提供出来，供大家自行配置，这样的好处在于，实用性更高，你可以添加gif，或者视频，或者简单的图片。
```
mBannerCountViewPager.setPagerListener(bean, R.layout.loop_layout, new PageHelperListener() {
            @Override
            public void getItemView(View view, Object data) {
                ImageView imageView = view.findViewById(R.id.loop_icon);
                LoopBean bean = (LoopBean) data;
                new GlideManager.Builder()
                        .setContext(LoopActivity.this)
                        .setImgSource(bean.url)
                        .setLoadingBitmap(R.mipmap.ic_launcher)
                        .setImageView(imageView)
                        .builder();
                TextView textView = view.findViewById(R.id.loop_text);
                textView.setText(bean.text);

                //如若你要设置点击事件，也可以直接通过这个view 来设置，或者图片的更新等等
            }
        });
``` 

**xml的配置，其实就是一个viewpager 加一个 linearlayout，位置你自己去摆放**
```
<TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="center_horizontal"
            android:padding="10dp"
            android:text="放大版，弧形不轮播加魅族效果"
            android:background="@color/white"
            android:textSize="24sp" />

        <FrameLayout
            android:layout_width="match_parent"
            android:layout_height="200dp"
            android:clipChildren="false">

            <com.zhengsr.viewpagerlib.view.BannerViewPager
                android:id="@+id/loop_viewpager_arc"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:layout_marginLeft="20dp"
                android:layout_marginRight="20dp"
                android:clipChildren="false"
                zsr:isloop="false"
                zsr:switchtime="600" />

            <com.zhengsr.viewpagerlib.indicator.ZoomIndicator
                android:id="@+id/bottom_zoom_arc"
                android:layout_width="match_parent"
                android:layout_height="30dp"
                android:layout_gravity="bottom|right"
                android:layout_marginRight="20dp"
                android:gravity="center"
                zsr:zoom_alpha_min="0.5"
                zsr:zoom_leftmargin="10dp"
                zsr:zoom_max="1.5"
                zsr:zoom_selector="@drawable/bottom_circle" />
        </FrameLayout>
```

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


### app 首次启动引导页

如果你要使用 引导页，也是非常简单，只需要使用 GlideViewPager 即可。配置与上面基本一致，只需要三部，我们就可以实现引导图；小看效果

![image](https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/glidenormal.gif)    ![image](https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/glidetrans.gif)        ![image](https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/glidezoom.gif)        

看一个完整的配置：
```
 GlideViewPager viewPager = (GlideViewPager) findViewById(R.id.splase_viewpager);
        ZoomIndicator zoomIndicator = (ZoomIndicator) findViewById(R.id.splase_bottom_layout);
        Button button = (Button) findViewById(R.id.splase_start_btn);


        //先把本地的图片 id 装进 list 容器中
        List<Integer> images = new ArrayList<>();
        for (int i = 0; i < RES.length; i++) {
            images.add(RES[i]);

        }
        //配置pagerbean，这里主要是为了viewpager的指示器的作用，注意记得写上泛型
        PagerBean bean = new PagerBean.Builder<Integer>()
                .setDataObjects(images)
                .setIndicator(zoomIndicator)
                .setOpenView(button)
                .builder();

        // 把数据添加到 viewpager中，并把view提供出来，这样除了方便调试，也不会出现一个view，多个
        // parent的问题，这里在轮播图比较明显
        viewPager.setPagerListener(bean, R.layout.image_layout, new PagerHelperListener() {
            @Override
            public void getItemView(View view, Object data) {
                //通过获取到这个view，你可以随意定制你的内容
                ImageView imageView = view.findViewById(R.id.icon);
                imageView.setImageResource((Integer) data);
            }
        });
```

**xml 的配置如下：**

```
<com.zhengsr.viewpagerlib.view.GlideViewPager
        android:id="@+id/splase_viewpager"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

    <Button
        android:id="@+id/splase_start_btn"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_marginLeft="100dp"
        android:layout_marginRight="100dp"
        android:layout_marginBottom="50dp"
        android:background="@drawable/glide_bottom_btn_bg"
        android:textColor="@color/white"
        android:text="@string/start"
        android:textSize="18sp"
        android:visibility="gone"
        />

   <com.zhengsr.viewpagerlib.indicator.ZoomIndicator
        android:id="@+id/splase_bottom_layout"
        android:layout_alignParentBottom="true"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:gravity="center"
        zsr:zoom_selector="@drawable/bottom_circle"
        zsr:zoom_leftmargin="10dp"
        zsr:zoom_max="1.5"
        zsr:zoom_alpha_min="0.4"
        zsr:zoom_dismiss_open="true"
        android:orientation="horizontal"/>
```
可以就可以了，其实就是viewpager 跟 indicator指示器的配合。

下面可以看一下自定义的属性，方便大家在用的时候调用：

**一些自定义属性：**

```
<!--放大缩小的 indicator-->
    <declare-styleable name="ZoomIndicator">
        <attr name="zoom_selector" format="reference"/> <!--selecotr，一般就一个圆点即可-->
        <attr name="zoom_leftmargin" format="dimension"/><!--两个圆点之间的间距-->
        <attr name="zoom_max" format="float"/> <!--小圆点放大的倍数-->
        <attr name="zoom_alpha_min" format="float"/><!--小圆点缩小的倍数-->
        <attr name="zoom_dismiss_open" format="boolean"/><!--做引导页的最后一页，是否隐藏圆点-->
    </declare-styleable>

    <!--与上面同理-->
    <declare-styleable name="NormalIndicator">
        <attr name="normal_selector" format="reference"/>
        <attr name="normal_leftmargin" format="dimension"/>
        <attr name="normal_dismiss_open" format="boolean"/>
    </declare-styleable>

    <!--移动小球 indicator-->
    <declare-styleable name="TransIndicator">
        <attr name="trans_size" format="dimension"/> <!--移动小球的大小-->
        <attr name="trans_defaultcolor" format="reference"/> <!--小球默认的颜色-->
        <attr name="trans_leftmargin" format="dimension"/>
        <attr name="trans_movecolor" format="reference"/> <!--小球移动的颜色-->
        <attr name="trans_dismiss_open" format="boolean"/>
    </declare-styleable>

    <!--文字indicator-->
    <declare-styleable name="TextIndicator">
        <attr name="show_circle" format="boolean"/> <!--是否显示外援的圆圈-->
        <attr name="circle_color" format="reference"/> <!--圆圈的颜色-->
        <attr name="text_color" format="reference"/> <!--文字颜色-->
        <attr name="text_size" format="dimension"/> <!--文字大小-->
    </declare-styleable>

    <!--轮播控件的属性-->
    <declare-styleable name="BannerViewPager">
        <attr name="isloop" format="boolean"/> <!--是否自动轮播-->
        <attr name="looptime" format="integer"/> <!--自动轮播的时间-->
        <attr name="switchtime" format="integer"/> <!--轮播时，viewpager的切换速度-->
    </declare-styleable>

    <!--弧形图片-->
    <declare-styleable name="ArcImageView">
        <attr name="arc_height" format="dimension"/> <!--弧度的高度-->
    </declare-styleable>
```

如有问题，或者你需要哪种效果，可以在 issue 中提出来，我有空会根据大家的需求写上的。

代码是最好的老师，可以download，改成自己喜欢的。