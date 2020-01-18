package com.zhengsr.viewpagerlib.bean;

import android.support.annotation.Nullable;
import android.view.View;

import com.zhengsr.viewpagerlib.type.BannerTransType;

import java.util.List;

/**
 * created by zhengshaorui on 2017/8/12
 * Describe: 用来构建banneer 的一些数据
 */

public class PageBean {

    /**
     * 轮播的时间，即每一个 item 停留的时间
     */
    public int loopTime;
    /**
     * viewpager 的切换时间
     */
    public int smoothScrollTime;
    /**
     * 支持循环轮播的最大个数，比如设置为3，当你的数据只有2个时，不支持轮播
     */
    public int loopMaxCount = -1;
    /**
     * 是否一开始就轮播
     */
    public boolean isAutoLoop;

    /**
     * 是否循环填充数据，当 isAutoLoop 为true，则isAutoCycle 也为true，
     * 当数据大于 loopMaxCount 时，也为true，如果不需要自动轮播，只需要循环，可填这个为true
     */
    public boolean isAutoCycle;
    /**
     *  卡片高度
     */
    public int cardHeight;
    /**
     * viewpager 的 transFormer 效果，默认支持4中
     */
    public BannerTransType transFormer;





}
