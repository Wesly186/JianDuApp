package com.mialab.jiandu.model;

import android.content.Context;

import com.mialab.jiandu.api.IArticle;
import com.mialab.jiandu.api.IBanner;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.utils.PrefUtils;
import com.mialab.jiandu.utils.http.helper.RetrofitHelper;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Wesly186 on 2016/11/26.
 */

public class ArticleModel {

    private HttpSubscriber<List<Article>> articleByTimeSubscriber;
    private HttpSubscriber<List<Article>> articleSyntheticallySubscriber;
    private HttpSubscriber<String> publishArticleSubscriber;
    private HttpSubscriber<List<Article>> articleWeekHotSubscriber;
    private HttpSubscriber<List<Article>> searchArticleSubscriber;
    private HttpSubscriber<List<Article>> articleCollectionSubscriber;
    private HttpSubscriber<List<Article>> getBannersSubscriber;

    /**
     * 获取banner列表
     *
     * @param context
     * @return
     */
    public Subscription getBanners(Context context) {

        IBanner iBanner = RetrofitHelper.getProxy(IBanner.class, context);
        Observable<BaseModel<List<Article>>> observable = iBanner.getBanners(PrefUtils.getString(context, GlobalConf.ACCESS_TOKEN, ""));
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getBannersSubscriber);
    }

    public void setGetBannersSubscriber(HttpSubscriber<List<Article>> getBannersSubscriber) {
        this.getBannersSubscriber = getBannersSubscriber;
    }

    /**
     * 时间顺序
     *
     * @param context
     * @param currentPage
     * @return
     */
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

    /**
     * 综合排序
     *
     * @param context
     * @param currentPage
     * @return
     */
    public Subscription getArticleSynthetically(Context context, int currentPage) {

        IArticle iArticle = RetrofitHelper.getProxy(IArticle.class, context);
        Observable<BaseModel<List<Article>>> observable = iArticle.getArticleSynthetically(PrefUtils.getString(context, GlobalConf.ACCESS_TOKEN, ""), currentPage);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articleSyntheticallySubscriber);
        return subscription;
    }

    public void setArticleSyntheticallySubscriber(HttpSubscriber<List<Article>> articleSyntheticallySubscriber) {
        this.articleSyntheticallySubscriber = articleSyntheticallySubscriber;
    }

    /**
     * 本周热门
     *
     * @param context
     * @param currentPage
     * @return
     */
    public Subscription getArticleWeekHot(Context context, int currentPage) {

        IArticle iArticle = RetrofitHelper.getProxy(IArticle.class, context);
        Observable<BaseModel<List<Article>>> observable = iArticle.getArticleWeekHot(PrefUtils.getString(context, GlobalConf.ACCESS_TOKEN, ""), currentPage);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articleWeekHotSubscriber);
        return subscription;
    }

    public void setArticleWeekHotSubscriber(HttpSubscriber<List<Article>> articleWeekHotSubscriber) {
        this.articleWeekHotSubscriber = articleWeekHotSubscriber;
    }

    public Subscription searchArticle(Context context, String keyword, int currentPage) {

        IArticle iArticle = RetrofitHelper.getProxy(IArticle.class, context);
        Observable<BaseModel<List<Article>>> observable = iArticle.searchArticle(PrefUtils.getString(context, GlobalConf.ACCESS_TOKEN, ""), keyword, currentPage);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchArticleSubscriber);
        return subscription;
    }

    public void setSearchArticleSubscriber(HttpSubscriber<List<Article>> searchArticleSubscriber) {
        this.searchArticleSubscriber = searchArticleSubscriber;
    }

    /**
     * 我的收藏
     *
     * @param context
     * @param currentPage
     * @return
     */
    public Subscription getArticleCollection(Context context, int currentPage) {
        IArticle iArticle = RetrofitHelper.getProxy(IArticle.class, context);
        Observable<BaseModel<List<Article>>> observable = iArticle.getArticleCollection(PrefUtils.getString(context, GlobalConf.ACCESS_TOKEN, ""), currentPage);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articleCollectionSubscriber);
        return subscription;
    }

    public void setArticleCollectionSubscriber(HttpSubscriber<List<Article>> articleCollectionSubscriber) {
        this.articleCollectionSubscriber = articleCollectionSubscriber;
    }

    /**
     * 我的阅历
     *
     * @param context
     * @param currentPage
     * @return
     */
    public Subscription getArticleReads(Context context, int currentPage) {
        IArticle iArticle = RetrofitHelper.getProxy(IArticle.class, context);
        Observable<BaseModel<List<Article>>> observable = iArticle.getArticleReads(PrefUtils.getString(context, GlobalConf.ACCESS_TOKEN, ""), currentPage);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articleCollectionSubscriber);
        return subscription;
    }

    public void getArticleReads(HttpSubscriber<List<Article>> articleCollectionSubscriber) {
        this.articleCollectionSubscriber = articleCollectionSubscriber;
    }

    public Subscription publishArticle(Context context, String title, String desc, String address, File articleImg) {
        RequestBody picBody = null;
        MultipartBody.Part articlePic = null;
        RequestBody accessToken = null;
        RequestBody articleTitle = null;
        RequestBody articleDesc = null;
        RequestBody articleAddress = null;
        //普通key/value
        accessToken = RequestBody.create(MediaType.parse("multipart/form-data"), PrefUtils.getString(context, GlobalConf.ACCESS_TOKEN, ""));
        //文章图片
        if (articleImg != null) {
            picBody = RequestBody.create(MediaType.parse("multipart/form-data"), articleImg);
            articlePic = MultipartBody.Part.createFormData("articlePic", articleImg.getName(), picBody);
        }
        if (title != null) {
            articleTitle = RequestBody.create(MediaType.parse("multipart/form-data"), title);
        }
        if (desc != null) {
            articleDesc = RequestBody.create(MediaType.parse("multipart/form-data"), desc);
        }
        if (address != null) {
            articleAddress = RequestBody.create(MediaType.parse("multipart/form-data"), address);
        }
        IArticle iArticle = RetrofitHelper.getProxy(IArticle.class, context);
        Observable<BaseModel<String>> observable = iArticle.publishArticle(accessToken, articlePic, articleTitle, articleDesc, articleAddress);
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(publishArticleSubscriber);
    }

    public void setPublishArticleSubscriber(HttpSubscriber<String> publishArticleSubscriber) {
        this.publishArticleSubscriber = publishArticleSubscriber;
    }
}
