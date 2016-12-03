package com.mialab.jiandu.presenter;

import android.content.Context;

import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.model.ArticleModel;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;
import com.mialab.jiandu.view.fragment.RankView;

import java.util.List;

/**
 * Created by Wesly186 on 2016/11/27.
 */

public class RankPresenter extends BasePresenter {

    private Context context;
    private RankView rankView;
    private ArticleModel articleModel;

    public RankPresenter(Context context, RankView rankView) {
        this.context = context;
        this.rankView = rankView;
        articleModel = new ArticleModel();
    }

    public void getArticleSynthetically(final int currentPage) {
        articleModel.setArticleSyntheticallySubscriber(new HttpSubscriber<List<Article>>() {
            @Override
            public void onSuccess(BaseModel<List<Article>> response) {
                rankView.loadSuccess(currentPage, response.getData());
            }

            @Override
            public void onFailure(String message) {
                rankView.loadFailure(message);
            }

            @Override
            public void onBadNetwork() {
                rankView.onBadNetWork();
            }
        });
        addSubscription(articleModel.getArticleSynthetically(context, currentPage));
    }
}
