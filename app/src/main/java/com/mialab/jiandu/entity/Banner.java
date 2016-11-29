package com.mialab.jiandu.entity;

/**
 * Created by Wesly186 on 2016/11/27.
 */

public class Banner {
    private int id;
    private String title;
    private User writer;
    private long publishTime;
    private String picUrl;
    private String articleUrl;

    public Banner() {

    }

    public Banner(int id, String title, User writer, long publishTime, String picUrl, String articleUrl) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.publishTime = publishTime;
        this.picUrl = picUrl;
        this.articleUrl = articleUrl;
    }

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
}
