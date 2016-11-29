package com.mialab.jiandu.entity;

/**
 * Created by Wesly186 on 2016/11/19.
 */

public class Article {

    private int id;
    private String title;
    private String briefIntro;
    private User writer;
    private long publishTime;
    private String picUrl;
    private String articleUrl;

    public Article() {

    }

    public Article(int id, String title, String briefIntro, User writer, long publishTime) {
        this.id = id;
        this.title = title;
        this.briefIntro = briefIntro;
        this.writer = writer;
        this.publishTime = publishTime;
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
}
