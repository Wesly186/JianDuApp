package com.mialab.jiandu.entity;

/**
 * Created by Wesly186 on 2016/11/19.
 */

public class Article {

    private int id;
    private String title;
    private String briefIntro;
    private User author;
    private long publishTime;

    public Article() {

    }

    public Article(int id, String title, String briefIntro, User author, long publishTime) {
        this.id = id;
        this.title = title;
        this.briefIntro = briefIntro;
        this.author = author;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }
}
