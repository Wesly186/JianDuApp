package com.mialab.jiandu.view.activity;

import com.mialab.jiandu.entity.Article;

import java.util.List;

/**
 * Created by Wesly186 on 2016/12/2.
 */

public interface WeekHotView {
    void loadSuccess(int currentPage, List<Article> data);

    void loadFailure(String message);

    void onBadNetWork();
}
