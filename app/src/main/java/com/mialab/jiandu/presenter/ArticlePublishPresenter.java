package com.mialab.jiandu.presenter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.model.ArticleModel;
import com.mialab.jiandu.utils.ImageUtils;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;
import com.mialab.jiandu.view.activity.ArticlePublishView;

import java.io.File;

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

    public void publish(String articleTitle, String articleDesc, String articleAddress, File articlePic) {
        if (TextUtils.isEmpty(articleTitle)) {
            articlePublishView.illegalInput("标题不能为空");
            return;
        }
        if (TextUtils.isEmpty(articleDesc)) {
            articlePublishView.illegalInput("描述不能为空");
            return;
        }
        if (TextUtils.isEmpty(articleAddress)) {
            articlePublishView.illegalInput("地址不能为空");
            return;
        }

        articleModel.setPublishArticleSubscriber(new HttpSubscriber<String>() {
            @Override
            public void onStart() {
                articlePublishView.onRequestStart();
            }

            @Override
            public void onSuccess(BaseModel<String> response) {
                articlePublishView.success(response.getData());
            }

            @Override
            public void onFailure(String message) {
                articlePublishView.failure(message);
            }

            @Override
            public void onBadNetwork() {
                articlePublishView.badNetWork();
            }
        });
        addSubscription(articleModel.publishArticle(context, articleTitle, articleDesc, articleAddress, articlePic));
    }

    /**
     * 根据uri得到图片的绝对路径
     *
     * @param uri
     * @return
     */
    public File getAbsolutePath(final Uri uri) {
        return ImageUtils.scal(uri);
    }
}
