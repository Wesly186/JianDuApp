package com.mialab.jiandu.entity;

/**
 * 文章
 */

public class Article {

    private int id;
    private String title;
    private String briefIntro;
    private User writer;
    private long publishTime;
    private String picUrl;
    private String articleUrl;
    private boolean isBanner;
    private int collectionNum;
    private int commentNum;
    private boolean hasCollected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBriefIntro() {
        return briefIntro;
    }

    public void setBriefIntro(String briefIntro) {
        this.briefIntro = briefIntro;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public int getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(int collectionNum) {
        this.collectionNum = collectionNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public boolean isHasCollected() {
        return hasCollected;
    }

    public void setHasCollected(boolean hasCollected) {
        this.hasCollected = hasCollected;
    }

    public boolean isBanner() {
        return isBanner;
    }

    public void setBanner(boolean banner) {
        isBanner = banner;
    }
}
