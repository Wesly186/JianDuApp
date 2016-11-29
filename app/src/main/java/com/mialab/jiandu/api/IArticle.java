package com.mialab.jiandu.api;

import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.BaseModel;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Wesly186 on 2016/11/26.
 */

public interface IArticle {
    @FormUrlEncoded
    @POST("article/getArticleByTime")
    Observable<BaseModel<List<Article>>> getArticleByTime(@Field("accessToken") String accessToken, @Field("currentPage") int currentPage);
}
