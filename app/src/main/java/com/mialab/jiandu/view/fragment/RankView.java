package com.mialab.jiandu.view.fragment;

import com.mialab.jiandu.entity.Article;

import java.util.List;

/**
 * Created by Wesly186 on 2016/11/27.
 */

public interface RankView {
    void loadSuccess(int currentPage, List<Article> data);

    void loadFailure(String message);

    void onBadNetWork();
}
