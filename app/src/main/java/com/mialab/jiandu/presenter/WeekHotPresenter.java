package com.mialab.jiandu.presenter;

import android.content.Context;

import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.model.ArticleModel;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;
import com.mialab.jiandu.view.activity.WeekHotView;

import java.util.List;

/**
 * Created by Wesly186 on 2016/11/27.
 */

public class WeekHotPresenter extends BasePresenter {

    private Context context;
    private WeekHotView weekHotView;
    private ArticleModel articleModel;

    public WeekHotPresenter(Context context, WeekHotView weekHotView) {
        this.context = context;
        this.weekHotView = weekHotView;
        articleModel = new ArticleModel();
    }

    public void getArticleWeekHot(final int currentPage) {
        articleModel.setArticleWeekHotSubscriber(new HttpSubscriber<List<Article>>() {
            @Override
            public void onSuccess(BaseModel<List<Article>> response) {
                weekHotView.loadSuccess(currentPage, response.getData());
            }

            @Override
            public void onFailure(String message) {
                weekHotView.loadFailure(message);
            }

            @Override
            public void onBadNetwork() {
                weekHotView.onBadNetWork();
            }
        });
        addSubscription(articleModel.getArticleWeekHot(context, currentPage));
    }
}
