package com.mialab.jiandu.entity;

/**
 * Created by Wesly186 on 2016/12/8.
 */

public class Message extends Article {
    // 0:comment,1:collect
    private int type;
    private long time;
    private User user;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
