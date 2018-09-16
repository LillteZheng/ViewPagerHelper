package com.zhengsr.viewpagerhelper.rx;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by zhengshaorui
 * Time on 2018/8/14
 */

public class RxNetCreate {
    /**
     * 获取retrofit服务
     * @return
     */
    public static RxNetService getService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.wanandroid.com/")
                //转字符串
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkHttpHolder.BUILDER)
                .build();
        return retrofit.create(RxNetService.class);
    }

    /**
     * 配置okhttp3 client
     */
    private static class OkHttpHolder{
         static OkHttpClient BUILDER = new OkHttpClient.Builder()
                 .connectTimeout(20, TimeUnit.SECONDS)
                 .build();
    }
}
