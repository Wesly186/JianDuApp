package com.mialab.jiandu.model;

import android.content.Context;

import com.mialab.jiandu.api.IArticleDetail;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.ArticleComment;
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
 * Created by Wesly186 on 2016/8/27.
 */
public class ArticleDetailModel {

    private HttpSubscriber<List<ArticleComment>> getCommentSubscriber;
    private HttpSubscriber<String> doCommentSubscriber;
    private HttpSubscriber<String> collectArticleSubscribe;
    private HttpSubscriber<String> add2ReadSubscribe;

    /**
     * 从网络加载评论
     *
     * @param context
     */
    public Subscription getComments(Context context, int articleId, int currentage) {
        IArticleDetail iArticleDetail = RetrofitHelper.getProxy(IArticleDetail.class, context);
        Observable<BaseModel<List<ArticleComment>>> observable = iArticleDetail.getComments(articleId, currentage);
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getCommentSubscriber);
    }

    public void setGetCommentSubscriber(HttpSubscriber<List<ArticleComment>> getCommentSubscriber) {
        this.getCommentSubscriber = getCommentSubscriber;
    }

    /**
     * 评论
     *
     * @param context
     * @param articleId
     * @param comment
     * @return
     */
    public Subscription doComments(Context context, int articleId, String comment) {
        IArticleDetail iArticleDetail = RetrofitHelper.getProxy(IArticleDetail.class, context);
        Observable<BaseModel<String>> observable = iArticleDetail.doComment(PrefUtils.getString(context, GlobalConf.ACCESS_TOKEN, ""), articleId, comment);
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(doCommentSubscriber);
    }

    public void setDoCommentSubscriber(HttpSubscriber<String> doCommentSubscriber) {
        this.doCommentSubscriber = doCommentSubscriber;
    }

    /**
     * 收藏文章
     *
     * @param id
     * @return
     */
    public Subscription collectArticle(Context context, int id, boolean collect) {
        IArticleDetail iArticleDetail = RetrofitHelper.getProxy(IArticleDetail.class, context);
        Observable<BaseModel<String>> observable = iArticleDetail.collectArticle(PrefUtils.getString(context, GlobalConf.ACCESS_TOKEN, ""), id, collect);
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(collectArticleSubscribe);
    }

    public void setCollectArticleSubscribe(HttpSubscriber<String> collectArticleSubscribe) {
        this.collectArticleSubscribe = collectArticleSubscribe;
    }

    /**
     * 添加到已经阅读列表中
     *
     * @param context
     * @param id
     * @return
     */
    public Subscription add2Read(Context context, int id) {
        IArticleDetail iArticleDetail = RetrofitHelper.getProxy(IArticleDetail.class, context);
        Observable<BaseModel<String>> observable = iArticleDetail.add2Read(PrefUtils.getString(context, GlobalConf.ACCESS_TOKEN, ""), id);
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(add2ReadSubscribe);
    }

    public void setAdd2ReadSubscribe(HttpSubscriber<String> add2ReadSubscribe) {
        this.add2ReadSubscribe = add2ReadSubscribe;
    }
}
