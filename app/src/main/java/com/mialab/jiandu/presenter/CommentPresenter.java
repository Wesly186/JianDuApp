package com.mialab.jiandu.presenter;

import android.content.Context;

import com.mialab.jiandu.entity.ArticleComment;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.entity.User;
import com.mialab.jiandu.model.ArticleDetailModel;
import com.mialab.jiandu.model.UserModel;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;
import com.mialab.jiandu.view.activity.CommentView;

import java.util.List;

/**
 * Created by Wesly186 on 2016/8/27.
 */
public class CommentPresenter extends BasePresenter {

    private Context context;
    private CommentView commentView;
    private ArticleDetailModel articleDetailModel;
    private UserModel userModel;

    public CommentPresenter(Context context, CommentView commentView) {
        this.context = context;
        this.commentView = commentView;
        articleDetailModel = new ArticleDetailModel();
        userModel = new UserModel();
    }

    public void getComments(int articleId, final int currentPage) {
        articleDetailModel.setGetCommentSubscriber(new HttpSubscriber<List<ArticleComment>>() {
            @Override
            public void onSuccess(BaseModel<List<ArticleComment>> response) {
                commentView.getCommentsSuccess(currentPage, response.getData());
            }

            @Override
            public void onFailure(String message) {
                commentView.getCommentsFailure(message);
            }

            @Override
            public void onBadNetwork() {
                commentView.onBadNetwork();
            }
        });
        addSubscription(articleDetailModel.getComments(context, articleId, currentPage));
    }

    /**
     * 发表评论
     */
    public void doComment(int articleId, String comment) {
        articleDetailModel.setDoCommentSubscriber(new HttpSubscriber<String>() {
            @Override
            public void onSuccess(BaseModel<String> response) {
                commentView.doCommentSuccess();
            }

            @Override
            public void onFailure(String message) {
                commentView.doCommentFailure(message);
            }

            @Override
            public void onBadNetwork() {
                commentView.commentBadNetwork();
            }
        });
        articleDetailModel.doComments(context, articleId, comment);
    }

    public User getUserFromCache() {
        return userModel.getFromDB(context);
    }
}
