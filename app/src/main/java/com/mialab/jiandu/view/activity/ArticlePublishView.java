package com.mialab.jiandu.view.activity;

/**
 * 文章发布
 */

public interface ArticlePublishView {
    void success(String data);

    void failure(String message);

    void badNetWork();

    void illegalInput(String message);

    void onRequestStart();
}
