package com.mialab.jiandu.view.activity;


import com.mialab.jiandu.entity.ArticleComment;

import java.util.List;

/**
 * Created by Wesly186 on 2016/8/27.
 */
public interface CommentView {
    void getCommentsSuccess(int currentPage, List<ArticleComment> articleComments);

    void getCommentsFailure(String message);

    void onBadNetwork();

    void doCommentSuccess();

    void doCommentFailure(String message);

    void commentBadNetwork();
}
