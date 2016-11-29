package com.mialab.jiandu.presenter;

import android.content.Context;

import com.mialab.jiandu.model.ArticleModel;
import com.mialab.jiandu.view.activity.ArticlePublishView;

/**
 * Created by Wesly186 on 2016/11/29.
 */

public class ArticlePublishPresenter extends BasePresenter {

    private Context context;
    private ArticlePublishView articlePublishView;
    private ArticleModel articleModel;

    public ArticlePublishPresenter(Context context, ArticlePublishView articlePublishView) {
        this.context = context;
        this.articlePublishView = articlePublishView;
        articleModel = new ArticleModel();
    }
}
