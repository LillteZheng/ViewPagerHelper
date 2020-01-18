
你是否有遇到这样的问题，每次开发一个新的项目，在 viewpager 这一块上，总是在做重复的东西，比如app引导页，轮播图，
viewpager+fragment 的 tab 指示器等等，这些虽然简单，但却是每个app都要的，而且很耗时，有没有每次在写这个，都很无语的感觉呢？
基于这个，ViewPagerHleper 就诞生了，它可以快速帮你搞定 banner 轮播图，实现高级定制化，内置多种指示器，满足你的日常需求，妈妈再也不用担心我不会复制粘贴了。

[![](https://jitpack.io/v/LillteZheng/ViewPagerHelper.svg)](https://jitpack.io/#LillteZheng/ViewPagerHelpe)
![](https://img.shields.io/github/stars/LillteZheng/ViewPagerHelper.svg) 
![](https://img.shields.io/github/forks/LillteZheng/ViewPagerHelper.svg)
[![](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-%E9%83%91%E5%B0%91%E9%94%90-green.svg)](https://blog.csdn.net/u011418943)



详细内容可以参考这篇博客：
http://blog.csdn.net/u011418943/article/details/78493002

## **使用**
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
implementation 'com.github.LillteZheng:ViewPagerHelper:v1.9'
```


##  **效果图**

首先，大家最常用到的就是轮播图了，这里给大家提供了 4 中常用的 Indicator

### **轮播图**

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


[轮播使用说明](https://github.com/LillteZheng/ViewPagerHelper/blob/master/README_Banner.md)

## ~~Tab指示器~~

> TabIndicator 已经不再维护；推荐大家使用新库：https://github.com/LillteZheng/FlowHelper
  支持更多效果，功能更强大，且支持自己自定义

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

**https://github.com/LillteZheng/FlowHelper   的效果如下：**

<table  align="center">
 <tr>
    <th>没有结合ViewPager</th>
    <th>结合ViewPager</th>
  </tr>
   <tr>
    <td><a href="url"><img src="https://github.com/LillteZheng/FlowHelper/raw/master/gif/tab_click.gif" align="left"height="789" width="479"></a></td>
    <td><a href="url"><img src="https://github.com/LillteZheng/FlowHelper/raw/master/gif/tab_viewpager.gif" align="left" height="789" width="479" ></a></td>
  </tr>

</table>

[Tab指示器使用说明](https://github.com/LillteZheng/ViewPagerHelper/blob/master/README_TAB.md)

## **弧形图片，支持高斯模糊**

<table  align="center">
 <tr>
    <th>弧形图片</th>
  </tr>
   <tr>
    <td><a href="url"><img src="https://github.com/LillteZheng/ViewPagerHelper/raw/master/gif/arc.png" align="left" height="480" width="340"></a></td>
  </tr>

</table>


### app 启动引导页

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

[引导页使用说明](https://github.com/LillteZheng/ViewPagerHelper/blob/master/README_GUIDE.md)


## 版本说明    

**v1.9**
- 处理TabIndicator第一次没加载数据报错和优化部分代码

**v1.8**
- 修改ArcImageView 
- 增加缩放因子
- 增加缩放中心点坐标

**v1.7**
- 修改ArcImageView 
- arcHeight 支持负数，凹进去，方便在user界面使用
- 增加高斯模糊，arc_blur ，对应参数0到25
- 支持使用背景色，采用arc_use_color


**v1.6**
- ArcImageView 去掉scaleType中的 matrix 属性


**v1.5**
- 修改了BannerViewpager 的一些自定说明，避免歧义，小伙伴记得更新呀
- 自定义控件，全部增加动态设置参数接口，写得好累啊
- BannerViewpager 增加了卡片式布局的效果
- 优化Readme说明

**其他版本**
- **v1.4  --> 添加ScaleImageView可缩放控件，可支持在viewpager等滑动控件中使用**
- **v1.3  --> TabIndicator 支持滚动，app:tab_iscanscroll 默认true滚动，可设置不滚动**
- **v0.9  --> 处理第一次轮播图 index 不对问题，优化代码**
- **v0.8  --> 退出时自动关轮播，isOutVisiableWindow()方法，用于有滚动时，判断是否停止轮播**
- **v0.5  --> 增加 banner_loop_max_count 变量，当数据大于这个数值时，才会填充多个数据和轮播**
- **v0.4  --> 解决app引导页，快速滑动时，“立即体验”按钮会不显示问题,并修改自定义属性，防止干扰**
- **v0.3  --> 从lib中移除glide的依赖，防止干扰其他项目，去掉和优化一些代码，谢谢各位的提醒**
- **v0.2  --> 修复TabIndicator的宽度，不是 match_parent时，通过SetTabData添加数据，却显示不全的问题**
- **v0.1  --> 发布稳定版**

下面可以看一下自定义的属性，方便大家在用的时候调用：

**一些自定义属性：**

**BannerViewPager**


| 名称 | 类型 |说明 |
|---|---|---|
|banner_isAutoLoop|boolean|是否自动轮播|
|banner_looptime|integer|轮播的时间|
|banner_switchtime|integer|viewpager的切换速度|
|banner_loop_max_count|integer|超过这个数字时，才会轮播效果|
|banner_card_height|integer,dimension|卡片的高度|
|banner_iscycle|boolean|是否循环|
|banner_transformer|card,mz,zoom,depath|transformer的效果|

**ZoomIndicator**

| 名称 | 类型 |说明 |
|---|---|---|
|zoom_selector|reference|selecotr，一般就一个圆点即可|
|zoom_leftmargin|dimension|两个圆点之间的间距|
|zoom_max|float|小圆点放大的倍数|
|zoom_alpha_min|float|小圆点缩小的倍数|
|zoom_dismiss_open|boolean|做引导页的最后一页，是否隐藏圆点|

**NormalIndicator**

| 名称 | 类型 |说明 |
|---|---|---|
|normal_selector|reference|selecotr，一般就一个圆点即可-|
|normal_leftmargin|dimension|两个圆点之间的间距|
|normal_dismiss_open|boolean|做引导页的最后一页，是否隐藏圆点|

**TransIndicator**

| 名称 | 类型 |说明 |
|---|---|---|
|trans_width|dimension|指示器宽度|
|trans_height|dimension|指示器高度|
|trans_defaultcolor|color,reference|指示器默认的颜色|
|trans_leftmargin|dimension|两个圆点之间的间距|
|trans_movecolor|color,reference|指示器移动的颜色|
|trans_dismiss_open|boolean|做引导页的最后一页，是否隐藏圆点|
|trans_round_radius|dimension|矩形圆角|
|trans_type|round,circle|矩形圆角|


**TextIndicator**

| 名称 | 类型 |说明 |
|---|---|---|
|word_show_circle|boolean|是否显示外援的圆圈|
|word_circle_color|reference,color|圆圈的颜色|
|word_text_color|reference,color|文字颜色|
|word_text_size|reference|文字大小|

**弧形图片 ArcImageView**

| 名称 | 类型 |说明 |
|---|---|---|
|arc_height|dimension|弧度的高度|
|arc_blur|integer|图片的模糊度，0到25|
|arc_use_color|color，reference|是否使用背景色|
|arc_scaleX|dimension|缩放中心点X方向|
|arc_scaleY|dimension|缩放中心点Y方向|
|arc_scaleFactor|float|缩放比例|
|arc_auto_fix|boolean|自动适配大小，如果用glide，可以忽略,默认为true|


**顶部viewpager指示器 TabIndicator**

| 名称 | 类型 |说明 |
|---|---|---|
|visiabel_size|integer|可视化个数，比如有一排，我们就只要显示4个|
|tab_color|color,reference|指示器的颜色|
|tab_show|boolean|是否显示指示器|
|tab_text_type|normaltext,colortext|顶部文字的类型，nromaltext为普通的textview，corlortext为文件渐变|
|tab_width|dimension|指示器的宽度|
|tab_height|dimension|指示器的高度|
|tab_textsize|dimension|顶部文字的大小|
|tab_text_default_color|color,reference|顶部文字默认的颜色|
|tab_text_change_color|color,reference|移动时，顶部文字的颜色|
|tap_type|tri,rect|指示器类型，有三角形或者圆条|
|tab_iscanscroll|boolean|指示器是否支持滚动|



**渐变文字 TextIndicator**

| 名称 | 类型 |说明 |
|---|---|---|
|colortext_size|dimension|文字大小|
|colortext_default_color|reference,color|默认颜色|
|colortext_change_color|reference,color|渐变颜色|


**缩放控件 ScaleImageView**

| 名称 | 类型 |说明 |
|---|---|---|
|scale_auto_time|reference,integer|双击时，达到放大的时间|
|scale_limit_board|boolean|是否限制边界，即不能缩放到比控件小|
|scale_autofit|boolean|自动适配缩放值，有些图片是正方形，如果你的高度没设定好，建议设置为false，不能会变形|
|scale_double_factor|integer|双击时放大倍数|
|scale_max_factor|integer|可放大的最大倍数|
|scale_interrupt_parent_touch|boolean|是否截获父控件触摸事件，放大时，需要截取，不然无法移动|


如果你有想要的效果，而本项目中没有的，你可以在 issue 中提出来，作者看到了，会抽空去实现的，
如果有发现问题了或者需要提供哪些接口出来，也可以在 issue 中提出来。当然，喜欢请 start 或 fork 来一波。

代码是最好的老师，可以download，改成自己喜欢的。
