package com.zhengsr.viewpagerhelper.rx;





import com.zhengsr.viewpagerhelper.bean.ArticleData;
import com.zhengsr.viewpagerhelper.bean.BannerBean;
import com.zhengsr.viewpagerhelper.bean.BaseResponse;
import com.zhengsr.viewpagerhelper.bean.PageDataInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * @author by  zhengshaorui on 2019/10/9
 * Describe: 统一网络服务接口类
 */
public interface HttpServerApi {
    @GET
    Observable<String> getJson(@Url String url);


    /**
     * https://www.wanandroid.com/banner/json
     * 获取 Banner 数据
     * @return
     */
    @GET("banner/json")
    Observable<BaseResponse<List<BannerBean>>> getBanner();

    /**
     * 获取文章
     * https://www.wanandroid.com/article/list/num/json
     * @param num 页码
     * @return
     */
    @GET("article/list/{num}/json")
    Observable<BaseResponse<PageDataInfo<List<ArticleData>>>> getArticle(@Path("num") int num);


}
