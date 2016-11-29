package com.mialab.jiandu.model;

import android.content.Context;

import com.mialab.jiandu.api.IArticle;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.utils.PrefUtils;
import com.mialab.jiandu.utils.http.helper.RetrofitHelper;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Wesly186 on 2016/11/26.
 */

public class ArticleModel {

    private HttpSubscriber<List<Article>> articleByTimeSubscriber;

    public Subscription getArticleByTime(Context context, int currentPage) {

        IArticle iArticle = RetrofitHelper.getProxy(IArticle.class, context);
        Observable<BaseModel<List<Article>>> observable = iArticle.getArticleByTime(PrefUtils.getString(context, GlobalConf.ACCESS_TOKEN, ""), currentPage);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articleByTimeSubscriber);
        return subscription;
    }

    public void setArticleByTimeSubscriber(HttpSubscriber<List<Article>> articleByTimeSubscriber) {
        this.articleByTimeSubscriber = articleByTimeSubscriber;
    }
}
