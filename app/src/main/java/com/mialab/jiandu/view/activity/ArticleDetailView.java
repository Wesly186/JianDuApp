package com.mialab.jiandu.view.activity;

/**
 * 文章细节
 */

public interface ArticleDetailView {
    void setLoadingProgress(int progress, boolean loadComplete);

    void onBadNetWork();

    void disableInput();

    void collectSuccess(boolean collect);

    void collectFailure(String message);

    void add2ReadSuccess();
}
