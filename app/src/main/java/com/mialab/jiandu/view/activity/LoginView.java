package com.mialab.jiandu.view.activity;

/**
 * 登陆
 */
public interface LoginView {

    void onLogin();

    void onLoginFailed(String message);

    void onBadNetWork();

    void onLoginSuccess();

    String getPhone();

    String getPassword();

    void IllegalInput(String message);
}
