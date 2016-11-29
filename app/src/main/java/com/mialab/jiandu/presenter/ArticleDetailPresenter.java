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
import com.mialab.jiandu.model.ArticleModel;
import com.mialab.jiandu.utils.share.ShareHelper;
import com.mialab.jiandu.view.activity.ArticleDetailView;

/**
 * Created by Wesly186 on 2016/11/26.
 */

public class ArticleDetailPresenter extends BasePresenter {

    private Context context;
    private ShareHelper mShare;
    private ArticleDetailView articleDetailView;
    private ArticleModel articleModel;

    public ArticleDetailPresenter(Context context, ArticleDetailView articleDetailView) {
        this.context = context;
        this.articleDetailView = articleDetailView;
        articleModel = new ArticleModel();
    }

    /**
     * 显示新闻详情
     *
     * @param webView
     */
    public void getNewsDetail(WebView webView, Article article) {
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
    public void ShareNews(@Nullable View anchor, boolean isWindowFullScreen) {
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
}
