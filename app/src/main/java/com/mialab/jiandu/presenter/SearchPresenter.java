package com.mialab.jiandu.presenter;

import android.content.Context;

import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.model.ArticleModel;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;
import com.mialab.jiandu.view.activity.SearchView;

import java.util.List;

/**
 * Created by Wesly186 on 2016/12/8.
 */
public class SearchPresenter extends BasePresenter {

    private Context context;
    private SearchView searchView;
    private ArticleModel articleModel;

    public SearchPresenter(Context context, SearchView searchView) {
        this.context = context;
        this.searchView = searchView;
        articleModel = new ArticleModel();
    }

    public void searchArticle(String keyword, final int currentPage) {
        articleModel.setSearchArticleSubscriber(new HttpSubscriber<List<Article>>() {
            @Override
            public void onSuccess(BaseModel<List<Article>> response) {
                searchView.loadSuccess(currentPage, response.getData());
            }

            @Override
            public void onFailure(String message) {
                searchView.loadFailure(message);
            }

            @Override
            public void onBadNetwork() {
                searchView.onBadNetWork();
            }
        });
        addSubscription(articleModel.searchArticle(context, keyword, currentPage));
    }
}
