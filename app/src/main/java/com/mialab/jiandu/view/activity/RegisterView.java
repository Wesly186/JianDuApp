package com.mialab.jiandu.view.activity;

/**
 * Created by Wesly186 on 2016/11/12.
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
