package com.mialab.jiandu.api;

import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.entity.Message;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Wesly186 on 2016/12/8.
 */

public interface INotify {
    @GET("notify/getMessages")
    Observable<BaseModel<List<Message>>> getMessages(@Query("accessToken") String accessToken, @Query("currentPage") int currentPage);
}
