package com.mialab.jiandu.api;

import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.entity.Rank;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 贡献
 */

public interface IRank {

    @GET("rank/getRankByReads")
    Observable<BaseModel<List<Rank>>> getRankByReads(@Query("accessToken") String accessToken);

    @GET("rank/getRankByContributions")
    Observable<BaseModel<List<Rank>>> getRankByContributions(@Query("accessToken") String accessToken);
}
