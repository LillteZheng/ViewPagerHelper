package com.zhengsr.viewpagerhelper.rx;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author by  zhengshaorui on 2019/10/9
 * Describe: 网络生成类
 */
public class HttpCreate {

    public static HttpServerApi getServer(){
        Retrofit retrofit = new Retrofit.Builder()
                //这里采用这个，因为有多个baseurl
                .baseUrl("https://www.wanandroid.com/")
                //转字符串
                .addConverterFactory(ScalarsConverterFactory.create())
                //fastjson
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkHttpHolder.BUILDER)
                .build();
        return retrofit.create(HttpServerApi.class);
    }

    /**
     * 配置okhttp3 client
     */

    private static class OkHttpHolder{
        static OkHttpClient BUILDER = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
    }
}
