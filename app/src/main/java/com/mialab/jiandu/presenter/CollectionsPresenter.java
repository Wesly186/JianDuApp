package com.mialab.jiandu.presenter;

import android.content.Context;

import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.model.ArticleModel;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;
import com.mialab.jiandu.view.activity.CollectionsView;

import java.util.List;

/**
 * Created by Wesly186 on 2016/11/27.
 */

public class CollectionsPresenter extends BasePresenter {

    private Context context;
    private CollectionsView collectionsView;
    private ArticleModel articleModel;

    public CollectionsPresenter(Context context, CollectionsView collectionsView) {
        this.context = context;
        this.collectionsView = collectionsView;
        articleModel = new ArticleModel();
    }

    public void getArticleCollection(final int currentPage) {
        articleModel.setArticleCollectionSubscriber(new HttpSubscriber<List<Article>>() {
            @Override
            public void onSuccess(BaseModel<List<Article>> response) {
                collectionsView.loadSuccess(currentPage, response.getData());
            }

            @Override
            public void onFailure(String message) {
                collectionsView.loadFailure(message);
            }

            @Override
            public void onBadNetwork() {
                collectionsView.onBadNetWork();
            }
        });
        addSubscription(articleModel.getArticleCollection(context, currentPage));
    }
}
