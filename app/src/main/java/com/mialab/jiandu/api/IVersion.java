package com.mialab.jiandu.api;

import com.mialab.jiandu.entity.AppVersion;
import com.mialab.jiandu.entity.BaseModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * App版本更新
 */
public interface IVersion {

    @GET("appVersion/checkUpdate")
    Observable<BaseModel<AppVersion>> checkUpdate(@Query("versionCode") int versionCode);
}
