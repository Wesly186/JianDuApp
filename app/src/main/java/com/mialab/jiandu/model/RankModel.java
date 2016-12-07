package com.mialab.jiandu.model;

import android.content.Context;

import com.mialab.jiandu.api.IRank;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.entity.Rank;
import com.mialab.jiandu.utils.PrefUtils;
import com.mialab.jiandu.utils.http.helper.RetrofitHelper;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Wesly186 on 2016/12/7.
 */

public class RankModel {

    private HttpSubscriber<List<Rank>> rankByReadsSubscriber;
    private HttpSubscriber<List<Rank>> rankByContributionsSubscriber;

    /**
     * 根据阅读数排名
     *
     * @param context
     * @return
     */
    public Subscription getRankByReads(Context context) {
        IRank iRank = RetrofitHelper.getProxy(IRank.class, context);
        Observable<BaseModel<List<Rank>>> observable = iRank.getRankByReads(PrefUtils.getString(context, GlobalConf.ACCESS_TOKEN, ""));
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rankByReadsSubscriber);
    }

    public void setRankByReadsSubscriber(HttpSubscriber<List<Rank>> rankByReadsSubscriber) {
        this.rankByReadsSubscriber = rankByReadsSubscriber;
    }

    /**
     * 根据贡献文章数排名
     *
     * @param context
     * @return
     */
    public Subscription getRankByContributions(Context context) {
        IRank iRank = RetrofitHelper.getProxy(IRank.class, context);
        Observable<BaseModel<List<Rank>>> observable = iRank.getRankByContributions(PrefUtils.getString(context, GlobalConf.ACCESS_TOKEN, ""));
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rankByContributionsSubscriber);
    }

    public void setRankByContributionsSubscriber(HttpSubscriber<List<Rank>> rankByContributionsSubscriber) {
        this.rankByContributionsSubscriber = rankByContributionsSubscriber;
    }
}
