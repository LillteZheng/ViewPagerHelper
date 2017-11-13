
你是否有遇到这样的问题，每次开发一个新的项目，在 viewpager 这一块上，总是在做重复的东西，比如app引导页，轮播图，
viewpager+fragment 的 tab 指示器等等，这些虽然简单，但却是每个app都要的，而且很耗时，有没有每次在写这个，都很无语的感觉呢？

基于上面这种情况，ViewPagerHelper 就诞生了。ViewPagerHelper 是一个能快速帮你的完成 app引导页，轮播，和viewpager 指示器的工具，
内面内置了常用属性，满足你日常对油腻的师姐的一切幻想，解放你的双手，释放你的灵魂。。。。。

详细内容可以参考这篇博客：
http://blog.csdn.net/u011418943/article/details/78493002

## **How to use**
这里用的是 jitpack 这个网站，所以：

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
然后在你的 module 中添加：

```
compile 'com.github.LillteZheng:ViewPagerHelper:v0.1'
```

##  **效果图**

首先，大家最常用到的就是轮播图了，这里给大家提供了 4 中常用的 Indicator

### **1、轮播图**

**第一个，仿魅族的轮播图，加底部放大圆点：**

![image](https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/loop_mz.gif)

**第二个，现在比较流行的弧形图片**

![image](https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/loop_arc.gif)


**第三个，底部指示器是移动的，看起来比较舒服**

![image](https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/loop_tran.gif)


**第四个，底部指示器文字版本**

![image](https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/loop_text.gif )


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

## **2、Tab指示器**


**第一种**

![image](https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/tab_tri.gif)



**第二种，条形状的版本**

![image](https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/tab_rect.gif)

**第三种，文字颜色渐变的方式**

![image](https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/tab_color.gif)

这里的配置就更简单了，viewpager 就是普通的，关键就是这个 TabIndicator 了，因为我把这三种效果都装进去了.

**xml 的配置**
```
<com.zhengsr.viewpagerlib.indicator.TabIndicator
        android:id="@+id/line_indicator"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:background="@color/black_ff"
        app:tab_color="@color/white"
        app:tab_width="25dp"
        app:tab_height="5dp"
        app:tab_text_default_color="@color/white_ff"
        app:tab_text_change_color="@color/white"
        app:tab_show="true"
        app:tab_text_type="normaltext"
        app:tab_textsize="16sp"
        app:visiabel_size="3"
        app:tap_type="tri"
        >
    </com.zhengsr.viewpagerlib.indicator.TabIndicator>
```
然后在代码中这样配置：
```
    /**
     * 把 TabIndicator 跟viewpager关联起来
     */
    TabIndicator tabIndecator = (TabIndicator) findViewById(R.id.line_indicator);
    // 设置 viewpager的切换速度，这样点击的时候比较自然
    tabIndecator.setViewPagerSwitchSpeed(viewPager,600);
    tabIndecator.setTabData(viewPager,mTitle, new TabIndicator.TabClickListener() {
        @Override
        public void onClick(int position) {
            //顶部点击的方法公布出来
            viewPager.setCurrentItem(position);
        }
    });
```
这样，一个比较好用的viewpager 加指示器的方式就完成了。

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
<?xml version="1.0" encoding="utf-8"?>
<resources>

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

    <!--移动indicator indicator-->
    <declare-styleable name="TransIndicator">
        <attr name="trans_width" format="dimension"/> <!--指示器的大小-->
        <attr name="trans_height" format="dimension"/> <!--指示器的大小-->
        <attr name="trans_defaultcolor" format="color|reference"/> <!--指示器默认的颜色-->
        <attr name="trans_leftmargin" format="dimension"/>
        <attr name="trans_movecolor" format="color|reference"/> <!--指示器移动的颜色-->
        <attr name="trans_dismiss_open" format="boolean"/>
        <attr name="trans_round_radius" format="dimension"/>
        <attr name="trans_type">
            <enum name="circle" value="0"/>
            <enum name="round" value="1"/>
        </attr>
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

    <!--顶部viewpager指示器-->
    <declare-styleable name="TabIndicator">
        <attr name="visiabel_size" format="integer"/> <!--可视化个数，比如有一排，我们就只要显示4个-->
        <attr name="tab_color" format="color|reference"/> <!--指示器的颜色-->
        <attr name="tab_width" format="dimension"/>  <!--指示器的宽度-->
        <attr name="tab_height" format="dimension"/> <!--指示器的高度-->
        <attr name="tab_textsize" format="dimension"/> <!--顶部文字的大小-->
        <attr name="tab_text_default_color" format="color|reference"/> <!--顶部文字默认的颜色-->
        <attr name="tab_text_change_color" format="color|reference"/> <!--移动时，顶部文字的颜色-->
        <attr name="tab_show" format="boolean"/>  <!--是否显示指示器-->
        <attr name="tab_text_type">               <!--顶部文字的类型，nromaltext为普通的textview，
                                                       corlortext为文件渐变-->
            <enum name="normaltext" value="0"/>     
            <enum name="colortext" value="1"/>
        </attr>
        <attr name="tap_type" >           <!--指示器类型，有三角形或者圆条-->
            <enum name="tri" value="0"/>
            <enum name="rect" value="1"/>
        </attr>
    </declare-styleable>

    <!--文件渐变属性-->
    <declare-styleable name="ColorTextView">
        <attr name="colortext_size" format="dimension"/>
        <attr name="colortext_default_color" format="color|reference"/> <!--默认颜色-->
        <attr name="colortext_change_color" format="color|reference"/> <!--渐变颜色-->
    </declare-styleable>

</resources>
```

如果你有想要的效果，而本项目中没有的，你可以在 issue 中提出来，作者看到了，会抽空去实现的，
如果有发现问题了或者需要提供哪些接口出来，也可以在 issue 中提出来。当然，喜欢请 start 或 fork 来一波。

代码是最好的老师，可以download，改成自己喜欢的。