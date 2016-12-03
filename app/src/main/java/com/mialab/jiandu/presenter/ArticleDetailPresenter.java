package com.mialab.jiandu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.entity.User;
import com.mialab.jiandu.model.ArticleDetailModel;
import com.mialab.jiandu.model.UserModel;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;
import com.mialab.jiandu.utils.share.ShareHelper;
import com.mialab.jiandu.view.activity.ArticleDetailView;

/**
 * Created by Wesly186 on 2016/11/26.
 */

public class ArticleDetailPresenter extends BasePresenter {

    private Context context;
    private ShareHelper mShare;
    private ArticleDetailView articleDetailView;
    private ArticleDetailModel articleDetailModel;
    private UserModel userModel;

    public ArticleDetailPresenter(Context context, ArticleDetailView articleDetailView) {
        this.context = context;
        this.articleDetailView = articleDetailView;
        articleDetailModel = new ArticleDetailModel();
        userModel = new UserModel();
    }

    /**
     * 显示新闻详情
     *
     * @param webView
     */
    public void showArticleDetail(WebView webView, Article article) {
        WebSettings settings = webView.getSettings();
        //JS和缓存
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //使用webView加载
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //网页加载进度显示
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                    articleDetailView.setLoadingProgress(100, true);
                } else {
                    // 加载中
                    articleDetailView.setLoadingProgress(newProgress * 5, false);
                }
            }
        });
        webView.loadUrl(article.getArticleUrl());
    }

    /**
     * 分享新闻
     *
     * @param anchor
     * @param isWindowFullScreen
     */
    public void ShareArticle(@Nullable View anchor, boolean isWindowFullScreen) {
        if (mShare == null) {
            mShare = ShareHelper.instance((AppCompatActivity) (context), (ShareHelper.Callback) articleDetailView);
        }
        if (anchor == null) {
            mShare.showShareDialog();
        } else {
            if (isWindowFullScreen)
                mShare.showShareFullScreenWindow(anchor);
            else
                mShare.showShareWarpWindow(anchor);
        }
    }

    @Override
    public void onDetachView() {
        super.onDetachView();
        if (mShare != null) {
            mShare.reset();
            mShare = null;
        }
    }

    public void collectArticle(int id, final boolean collect) {
        articleDetailModel.setCollectArticleSubscribe(new HttpSubscriber<String>() {
            @Override
            public void onStart() {
                articleDetailView.disableInput();
            }

            @Override
            public void onSuccess(BaseModel<String> response) {
                User user = userModel.getFromDB(context);
                if (collect) {
                    user.setCollectionNum(user.getCollectionNum() + 1);
                    userModel.updateUserInfoCache(context, user);
                } else {
                    user.setCollectionNum(user.getCollectionNum() - 1);
                    userModel.updateUserInfoCache(context, user);
                }
                articleDetailView.collectSuccess(collect);
            }

            @Override
            public void onFailure(String message) {
                articleDetailView.collectFailure(message);
            }

            @Override
            public void onBadNetwork() {
                articleDetailView.onBadNetWork();
            }
        });
        addSubscription(articleDetailModel.collectArticle(context, id, collect));
    }
}
