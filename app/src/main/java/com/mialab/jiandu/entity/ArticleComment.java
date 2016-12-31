package com.mialab.jiandu.entity;

/**
 * 文章评论
 */
public class ArticleComment {

    private Integer id;

    private String phone;

    private Integer articleId;

    private long publishTime;

    private String comment;

    private User user;

    public ArticleComment() {

    }

    public ArticleComment(Integer articleId, long publishTime, String comment, User user) {
        this.articleId = articleId;
        this.publishTime = publishTime;
        this.comment = comment;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
