package com.mialab.jiandu.presenter;

import android.content.Context;

import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.model.ArticleModel;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;
import com.mialab.jiandu.view.fragment.HomeView;

import java.util.List;

/**
 * Created by Wesly186 on 2016/11/12.
 */

public class HomeArticlePresenter extends BasePresenter {

    private Context context;
    private HomeView homeView;
    private ArticleModel articleModel;

    public HomeArticlePresenter(Context context, HomeView homeView) {
        this.context = context;
        this.homeView = homeView;
        articleModel = new ArticleModel();
    }

    public void getArticleByTime(final int currentPage) {
        articleModel.setArticleByTimeSubscriber(new HttpSubscriber<List<Article>>() {
            @Override
            public void onSuccess(BaseModel<List<Article>> response) {
                homeView.loadSuccess(currentPage, response.getData());
            }

            @Override
            public void onFailure(String message) {
                homeView.loadFailure(message);
            }

            @Override
            public void onBadNetwork() {
                homeView.onBadNetWork();
            }
        });
        addSubscription(articleModel.getArticleByTime(context, currentPage));
    }
}
