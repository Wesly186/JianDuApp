package com.mialab.jiandu.view.activity;

import com.mialab.jiandu.entity.Article;

import java.util.List;

/**
 * 收藏
 */

public interface CollectionsView {
    void loadSuccess(int currentPage, List<Article> data);

    void loadFailure(String message);

    void onBadNetWork();
}
