package com.zhengsr.viewpagerlib.bean;

import com.zhengsr.viewpagerlib.type.CircleIndicatorType;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: CircleIndicator 的自定义属性
 */
public class CirBean {

    /**
     * CircleIndicator 的类型，支持三种,NOAMAL,CIRTORECT,SCALE
     */
    public CircleIndicatorType type;
    /**
     * 圆点大小
     */
    public int cirSize;
    /**
     * 圆点默认颜色
     */
    public int normalColor = -2;
    /**
     * 圆点选中颜色
     */
    public int selectedColor = -2;
    /**
     * 圆点之间的margin
     */
    public int horizonMargin ;
    /**
     * 是否可以移动的，默认为true
     */
    public boolean isCanMove = true;
    /**
     * 当圆点变成矩形，矩形的宽度，高度就是 cirSize
     */
    public int rectWidth;
    /**
     * 放大缩小的倍速，默认不放大
     */
    public float scaleFactor =1;
}
