package com.mialab.jiandu.entity;

public class OauthToken {

    private String phone;

    private String accessToken;

    private long accessTokenExpires;

    private String refreshToken;

    private long refreshTokenExpires;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken == null ? null : accessToken.trim();
    }

    public long getAccessTokenExpires() {
        return accessTokenExpires;
    }

    public void setAccessTokenExpires(long accessTokenExpires) {
        this.accessTokenExpires = accessTokenExpires;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken == null ? null : refreshToken.trim();
    }

    public long getRefreshTokenExpires() {
        return refreshTokenExpires;
    }

    public void setRefreshTokenExpires(long refreshTokenExpires) {
        this.refreshTokenExpires = refreshTokenExpires;
    }
}