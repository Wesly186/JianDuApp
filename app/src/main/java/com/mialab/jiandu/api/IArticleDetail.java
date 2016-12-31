package com.mialab.jiandu.api;

import com.mialab.jiandu.entity.ArticleComment;
import com.mialab.jiandu.entity.BaseModel;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 文章细节
 */
public interface IArticleDetail {

    /**
     * 获取评论
     *
     * @param articleId
     * @param currentPage
     * @return
     */
    @FormUrlEncoded
    @POST("article/getComments")
    Observable<BaseModel<List<ArticleComment>>> getComments(@Field("articleId") int articleId, @Field("currentPage") int currentPage);

    /**
     * 评论文章
     *
     * @param accessToken
     * @param articleId
     * @param comment
     * @return
     */
    @FormUrlEncoded
    @POST("article/doComment")
    Observable<BaseModel<String>> doComment(@Field("accessToken") String accessToken, @Field("articleId") int articleId, @Field("comment") String comment);

    /**
     * 收藏文章
     *
     * @param accessToken
     * @param articleId
     * @return
     */
    @FormUrlEncoded
    @POST("article/collectArticle")
    Observable<BaseModel<String>> collectArticle(@Field("accessToken") String accessToken, @Field("articleId") int articleId,
                                                 @Field("collect") boolean collect);

    /**
     * 添加到已经阅读的列表
     *
     * @param accessToken
     * @param articleId
     * @return
     */
    @FormUrlEncoded
    @POST("article/add2Read")
    Observable<BaseModel<String>> add2Read(@Field("accessToken") String accessToken, @Field("articleId") int articleId);
}
