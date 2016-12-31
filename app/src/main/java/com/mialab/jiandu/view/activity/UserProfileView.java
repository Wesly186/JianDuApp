package com.mialab.jiandu.view.activity;

import com.mialab.jiandu.entity.User;

/**
 * 用户信息
 */

public interface UserProfileView {
    void updateSuccess(User user);

    void updateFailure(String message);

    void onBadNetWork();

    void requestCameraSuccess();

    void requestCameraFailure();
}
