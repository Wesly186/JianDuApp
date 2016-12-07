package com.mialab.jiandu.view.activity;

/**
 * Created by Wesly186 on 2016/11/26.
 */

public interface ArticleDetailView {
    void setLoadingProgress(int progress, boolean loadComplete);

    void onBadNetWork();

    void disableInput();

    void collectSuccess(boolean collect);

    void collectFailure(String message);

    void add2ReadSuccess();
}
