package com.mialab.jiandu.presenter;

import android.content.Context;

import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.model.ArticleModel;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;
import com.mialab.jiandu.view.fragment.HotView;

import java.util.List;

/**
 * Created by Wesly186 on 2016/11/27.
 */

public class HotPresenter extends BasePresenter {

    private Context context;
    private HotView hotView;
    private ArticleModel articleModel;

    public HotPresenter(Context context, HotView hotView) {
        this.context = context;
        this.hotView = hotView;
        articleModel = new ArticleModel();
    }

    public void getArticleSynthetically(final int currentPage) {
        articleModel.setArticleSyntheticallySubscriber(new HttpSubscriber<List<Article>>() {
            @Override
            public void onSuccess(BaseModel<List<Article>> response) {
                hotView.loadSuccess(currentPage, response.getData());
            }

            @Override
            public void onFailure(String message) {
                hotView.loadFailure(message);
            }

            @Override
            public void onBadNetwork() {
                hotView.onBadNetWork();
            }
        });
        addSubscription(articleModel.getArticleSynthetically(context, currentPage));
    }

    public void getBanners() {
        articleModel.setGetBannersSubscriber(new HttpSubscriber<List<Article>>() {
            @Override
            public void onSuccess(BaseModel<List<Article>> response) {
                hotView.loadBannerSuccess(response.getData());
            }

            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onBadNetwork() {

            }
        });
        addSubscription(articleModel.getBanners(context));
    }
}
