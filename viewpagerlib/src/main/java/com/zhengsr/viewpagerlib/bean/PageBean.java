package com.zhengsr.viewpagerlib.bean;

import android.view.View;

import com.zhengsr.viewpagerlib.type.BannerTransType;

import java.util.List;

/**
 * created by zhengshaorui on 2019/8/12
 * Describe: 用来构建banneer 的一些数据
 */

public class PageBean {

    private Builder mBuilder;
    private PageBean(Builder builder) {
       mBuilder = builder;
    }

    public Builder getParams() {
        return mBuilder;
    }

    public static class Builder<T>{
        private View indicator;
        private View openview;
        private List<T> datas;
        private boolean useCode ;
        private boolean isAutoLoop;
        private int loopTime = -1;
        private int pagerSwitchTime = -1;
        private int cardHeight = -1;
        private int loopMaxCount = -1;
        private boolean isCycle;
        private BannerTransType bannerTransformer = BannerTransType.UNKNOWN;

        public Builder indicator(View bottomLayout){
            this.indicator = bottomLayout;
            return this;
        }
        public Builder openView(View openView){
            this.openview = openView;
            return this;
        }

        public Builder cycle(boolean isCycle){
            this.isCycle = isCycle;
            return this;
        }

        public Builder data(List<T> datas){
            this.datas = datas;
            return this;
        }

        public Builder useCode(boolean useCode){
            this.useCode = useCode;
            return this;
        }

        public Builder autoLoop(boolean isAutoLoop){
            this.isAutoLoop = isAutoLoop;
            return this;
        }

        public Builder loopTime(int loopTime){
            this.loopTime = loopTime;
            return this;
        }

        public Builder pagerSwitchTime(int pagerSwitchTime){
            this.pagerSwitchTime = pagerSwitchTime;
            return this;
        }

        public Builder cardHeight(int cardHeight){
            this.cardHeight = cardHeight;
            return this;
        }
        public Builder loopMaxCount(int loopMaxCount){
            this.loopMaxCount = loopMaxCount;
            return this;
        }

        public Builder bannerTransformer(BannerTransType bannerTransformer){
            this.bannerTransformer = bannerTransformer;
            return this;
        }


        public PageBean builder(){
            return new PageBean(this);
        }


        public View getIndicator() {
            return indicator;
        }

        public View getOpenview() {
            return openview;
        }

        public List<T> getDatas() {
            return datas;
        }

        public boolean isAutoLoop() {
            return isAutoLoop;
        }

        public int getLoopTime() {
            return loopTime;
        }

        public int getPagerSwitchTime() {
            return pagerSwitchTime;
        }

        public int getLoopMaxCount() {
            return loopMaxCount;
        }

        public BannerTransType getBannerTransformer() {
            return bannerTransformer;
        }

        public boolean isUseCode() {
            return useCode;
        }

        public int getCardHeight() {
            return cardHeight;
        }

        public boolean isCycle() {
            return isCycle;
        }
    }

}
