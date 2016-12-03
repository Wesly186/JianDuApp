package com.mialab.jiandu.view.activity;

/**
 * Created by Wesly186 on 2016/11/29.
 */

public interface ArticlePublishView {
    void success(String data);

    void failure(String message);

    void badNetWork();

    void illegalInput(String message);

    void onRequestStart();
}
