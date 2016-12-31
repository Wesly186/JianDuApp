package com.mialab.jiandu.view.activity;

/**
 * 主界面
 */

public interface MainView {
    void updateProgress(int percentage);

    void downloadComplete();

    void updateFailed(String message);

    void requestWriteSuccess();

    void requestWriteFailure();
}
