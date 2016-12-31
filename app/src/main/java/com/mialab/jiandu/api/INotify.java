package com.mialab.jiandu.api;

import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.entity.Message;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 通知界面
 */

public interface INotify {
    @GET("notify/getMessages")
    Observable<BaseModel<List<Message>>> getMessages(@Query("accessToken") String accessToken, @Query("currentPage") int currentPage);
}
