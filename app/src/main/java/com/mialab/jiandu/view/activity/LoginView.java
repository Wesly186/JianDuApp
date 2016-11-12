package com.mialab.jiandu.view.activity;

/**
 * Created by Wesly186 on 2016/8/23.
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
