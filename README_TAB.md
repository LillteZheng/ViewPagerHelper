## Tab 指示器效果图

<table  align="center">
 <tr>
    <th>三角形版本</th>
    <th>条形状版本</th>
     <th>文字颜色渐变方式，加了滚动效果</th>
  </tr>
   <tr>
    <td><a href="url"><img src="https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/tri.gif" align="left" height="480" width="340"></a></td>
    <td><a href="url"><img src="https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/rect2.gif" align="left" height="480" width="340" ></a></td>
        <td><a href="url"><img src="https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/color.gif" align="left" height="480" width="340" ></a></td>
  </tr>

</table>

## 使用

主要为 **TabIndicator** 这个类，在布局中，把它放在viewpager的上面即可，如下：

### 布局
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/activity_fragment_page"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="com.zhengsr.viewpagerhelper.tab.TabActivity">

    <com.zhengsr.viewpagerlib.indicator.TabIndicator
        android:id="@+id/line_indicator"
        android:layout_width="wrap_content"
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

    <android.support.v4.view.ViewPager
        android:id="@+id/viewpager"
        android:layout_width="match_parent"
        android:background="@color/gray_cccc"
        android:layout_height="wrap_content"/>
    
</LinearLayout>
```
你可以通过这些属性，自由配置三角形，条状，或者只使用文字颜色变化，自定义属性的名字跟它的功能一样。
TabIndicator 默认支持滚动，也可以设置不可滚动；

当然也可以动态配置，接着看看数据这么初始化：
### 初始化
```
for (String string : mTitle) {
    CusFragment fragment = CusFragment.newInStance(string);
    mFragments.add(fragment);
}
final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
viewPager.setAdapter(new CusAdapter(getSupportFragmentManager()));
/**
 * 把 TabIndicator 跟viewpager关联起来
 */
TabIndicator tabIndecator = (TabIndicator) findViewById(R.id.line_indicator);
//设置viewpager滑动速度
tabIndecator.setViewPagerSwitchSpeed(viewPager,600);
tabIndecator.setTabData(viewPager,mTitle, new TabIndicator.TabClickListener() {
    @Override
    public void onClick(int position) {
        //顶部点击的方法公布出来
        viewPager.setCurrentItem(position);
    }
});
```



## 代码帮助
可以通过代码去查看怎么配置

[三角形xml](https://github.com/LillteZheng/ViewPagerHelper/blob/master/app/src/main/res/layout/activity_tritab_page.xml)
[三角形Activity](https://github.com/LillteZheng/ViewPagerHelper/blob/master/app/src/main/java/com/zhengsr/viewpagerhelper/activity/tab/TriTabActivity.java)
