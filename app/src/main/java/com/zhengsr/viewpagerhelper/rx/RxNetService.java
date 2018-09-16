package com.zhengsr.viewpagerhelper.rx;


import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by zhengshaorui
 * Time on 2018/8/14
 * retrofit 不能穿泛型，所以这用国际通用类型 String
 */

public interface RxNetService {

    @GET
    Observable<String> get(@Url String url, @QueryMap Map<String, Object> map);

    @FormUrlEncoded
    @POST
    Observable<String> poat(@Url String url, @FieldMap Map<String, Object> map);

    @POST
    Observable<String> postRaw(@Url String url, @Body RequestBody body);

    @DELETE
    Observable<String> delete(@Url String url, @QueryMap Map<String, Object> map);

    @Streaming
    @GET
    Observable<String> download(@Url String url, @QueryMap Map<String, Object> map);

    @Multipart
    @POST
    Observable<String> upload(@Url String url, @Part MultipartBody.Part file);

    //表单
    @Multipart
    @POST
    Observable<String> uploadForm(@Url String url, @Part List<MultipartBody.Part> files);

    //多图上传
    @Multipart
    @POST
    Observable<String> uploadImages(@Url String url, @Part MultipartBody.Part[] files);

}
