package com.mialab.jiandu.view.activity;


import com.mialab.jiandu.entity.ArticleComment;

import java.util.List;

/**
 * 评论
 */
public interface CommentView {
    void getCommentsSuccess(int currentPage, List<ArticleComment> articleComments);

    void getCommentsFailure(String message);

    void onBadNetwork();

    void doCommentSuccess();

    void doCommentFailure(String message);

    void commentBadNetwork();
}
