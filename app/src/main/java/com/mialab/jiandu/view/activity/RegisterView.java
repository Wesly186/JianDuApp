package com.mialab.jiandu.view.activity;

/**
 * 注册
 */

public interface RegisterView {

    void beginCount();

    void getCodeSuccess();

    void getCodeFailed(String message);

    void onBadNetWork();

    String getPhone();

    void illegalInput(String message);

    String getPassword();

    int getValidationCode();

    void registerSuccess();

    void registerFailure(String message);

    void registing();
}
