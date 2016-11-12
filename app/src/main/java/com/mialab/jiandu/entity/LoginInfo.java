package com.mialab.jiandu.entity;

/**
 * Created by Wesly186 on 2016/11/11.
 */

public class LoginInfo {
    private User user;
    private OauthToken oauthToken;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OauthToken getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(OauthToken oauthToken) {
        this.oauthToken = oauthToken;
    }
}
