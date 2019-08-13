## APP 启动引导页

<table  align="center">
 <tr>
    <th>正常版</th>
    <th>移动版</th>
     <th>缩放版</th>
  </tr>
   <tr>
    <td><a href="url"><img src="https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/glidenormal.gif" align="left" height="480" width="340"></a></td>
    <td><a href="url"><img src="https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/glidetrans.gif" align="left" height="480" width="340" ></a></td>
        <td><a href="url"><img src="https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/glidezoom.gif" align="left" height="480" width="340" ></a></td>
  </tr>
</table>

## 使用

如果你要使用 引导页，也是非常简单，只需要使用 **GlideViewPager** 即可。配置与上面基本一致：

### 布局
```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:zsr="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:clickable="false"
    android:clipToPadding="false"
    android:orientation="vertical">

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

    <com.zhengsr.viewpagerlib.indicator.TransIndicator
        android:id="@+id/splase_bottom_layout"
        android:layout_alignParentBottom="true"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        zsr:trans_leftmargin="10dp"
        zsr:trans_movecolor="@color/colorAccent"
        zsr:trans_defaultcolor="@color/gray_cccc"
        zsr:trans_type="circle"
        zsr:trans_width="5dp"
        zsr:trans_height="5dp"
        zsr:trans_dismiss_open="true"
        android:orientation="horizontal"/>

</RelativeLayout>
```
Button 为启动按钮，这个自行设计即可。
### 初始化
```
public void initView() {

    GlideViewPager viewPager = (GlideViewPager) findViewById(R.id.splase_viewpager);
    TransIndicator linearLayout = (TransIndicator) findViewById(R.id.splase_bottom_layout);
    //点击跳转的按钮
    Button button = (Button) findViewById(R.id.splase_start_btn);


    //先把本地的图片 id 装进 list 容器中
    List<Integer> images = new ArrayList<>();
    for (int i = 0; i < RES.length; i++) {
        images.add(RES[i]);

    }
    //配置pagerbean，这里主要是为了viewpager的指示器的作用，注意记得写上泛型
    PageBean bean = new PageBean.Builder<Integer>()
            .data(images)
            .indicator(linearLayout)
            .openView(button)
            .builder();

    // 把数据添加到 viewpager中，并把view提供出来，这样除了方便调试，也不会出现一个view，多个
    // parent的问题，这里在轮播图比较明显
    viewPager.setPageListener(bean, R.layout.image_layout, new PageHelperListener() {
        @Override
        public void getItemView(View view, Object data) {
            ImageView imageView = view.findViewById(R.id.icon);
            imageView.setImageResource((Integer) data);
        }
    });



    //点击实现跳转功能
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(GlideTransActivity.this,MainActivity.class));
            Toast.makeText(GlideTransActivity.this, "回到首页", Toast.LENGTH_SHORT).show();
            finish();
        }
    });
}
```



## 代码帮助
可以通过代码去查看怎么配置

[移动版xml](https://github.com/LillteZheng/ViewPagerHelper/blob/master/app/src/main/res/layout/activity_glide_trans.xml)
[移动版Activity](https://github.com/LillteZheng/ViewPagerHelper/blob/master/app/src/main/java/com/zhengsr/viewpagerhelper/activity/guide/GlideTransActivity.java)
