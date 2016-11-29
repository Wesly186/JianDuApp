package com.mialab.jiandu.view.fragment;

import com.mialab.jiandu.entity.Article;

import java.util.List;

/**
 * Created by Wesly186 on 2016/11/26.
 */

public interface HomeView {
    void loadSuccess(int currentPage, List<Article> articles);

    void loadFailure(String message);

    void onBadNetWork();
}
