package com.mialab.jiandu.view.activity;

import com.mialab.jiandu.entity.AppVersion;

/**
 * Created by Wesly186 on 2016/9/15.
 */
public interface SettingView {

    void showUpdateDialog(AppVersion data);

    void updateProgress(int percentage);

    void downloadComplete();

    void updateFailed(String message);

    void updatePassSuccess();

    void updatePassFailure(String message);

    void badNetWork();

    void illegalInput(String message);
}
