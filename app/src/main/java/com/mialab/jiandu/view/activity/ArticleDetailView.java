package com.mialab.jiandu.view.activity;

import com.mialab.jiandu.entity.Article;

/**
 * Created by Wesly186 on 2016/11/26.
 */

public interface ArticleDetailView {
    void setLoadingProgress(int progress, boolean loadComplete);

    void praiseNewsFailed(String message);

    void onBadNetWork();

    void praiseNewsStart();

    void praiseNewsSuccess(Article data);

    void needLogin();
}
