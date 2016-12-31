package com.mialab.jiandu.view.activity;

import com.mialab.jiandu.entity.Article;

import java.util.List;

/**
 * 每周热榜
 */

public interface WeekHotView {
    void loadSuccess(int currentPage, List<Article> data);

    void loadFailure(String message);

    void onBadNetWork();
}
