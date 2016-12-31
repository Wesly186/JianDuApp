package com.mialab.jiandu.api;

import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.BaseModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 文章接口
 */

public interface IArticle {

    @FormUrlEncoded
    @POST("article/getArticleByTime")
    Observable<BaseModel<List<Article>>> getArticleByTime(@Field("accessToken") String accessToken, @Field("currentPage") int currentPage);

    @FormUrlEncoded
    @POST("article/getArticleSynthetically")
    Observable<BaseModel<List<Article>>> getArticleSynthetically(@Field("accessToken") String accessToken, @Field("currentPage") int currentPage);

    @FormUrlEncoded
    @POST("article/getArticleWeekHot")
    Observable<BaseModel<List<Article>>> getArticleWeekHot(@Field("accessToken") String accessToken, @Field("currentPage") int currentPage);

    @GET("article/searchArticle")
    Observable<BaseModel<List<Article>>> searchArticle(@Query("accessToken") String accessToken, @Query("keyword") String keyword, @Query("currentPage") int currentPage);

    @FormUrlEncoded
    @POST("article/getArticleCollection")
    Observable<BaseModel<List<Article>>> getArticleCollection(@Field("accessToken") String accessToken, @Field("currentPage") int currentPage);

    @FormUrlEncoded
    @POST("article/getArticleReads")
    Observable<BaseModel<List<Article>>> getArticleReads(@Field("accessToken") String accessToken, @Field("currentPage") int currentPage);

    @Multipart
    @POST("article/publishArticle")
    Observable<BaseModel<String>> publishArticle(@Part("accessToken") RequestBody accessToken, @Part MultipartBody.Part articlePic, @Part("articleTitle") RequestBody articleTitle,
                                                 @Part("articleDesc") RequestBody articleDesc, @Part("articleAddress") RequestBody articleAddress);
}