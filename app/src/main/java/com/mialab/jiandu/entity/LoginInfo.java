package com.mialab.jiandu.entity;

/**
 * Created by Wesly186 on 2016/11/11.
 */

public class LoginInfo {

    private User userRsp;
    private OauthToken oauthToken;

    public User getUserRsp() {
        return userRsp;
    }

    public void setUserRsp(User userRsp) {
        this.userRsp = userRsp;
    }

    public OauthToken getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(OauthToken oauthToken) {
        this.oauthToken = oauthToken;
    }
}
