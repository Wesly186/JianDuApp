package com.mialab.jiandu.api;

import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.BaseModel;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Wesly186 on 2016/12/7.
 */

public interface IBanner {

    @FormUrlEncoded
    @POST("article/getBanners")
    Observable<BaseModel<List<Article>>> getBanners(@Field("accessToken") String accessToken);
}
