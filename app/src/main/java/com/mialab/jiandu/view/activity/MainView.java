package com.mialab.jiandu.view.activity;

/**
 * Created by Wesly186 on 2016/11/19.
 */

public interface MainView {
    void updateProgress(int percentage);

    void downloadComplete();

    void updateFailed(String message);
}
