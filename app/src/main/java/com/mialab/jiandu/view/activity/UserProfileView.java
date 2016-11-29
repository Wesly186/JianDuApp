package com.mialab.jiandu.view.activity;

import com.mialab.jiandu.entity.User;

/**
 * Created by Wesly186 on 2016/11/12.
 */

public interface UserProfileView {
    void updateSuccess(User user);

    void updateFailure(String message);

    void onBadNetWork();

    void requestCameraSuccess();

    void requestCameraFailure();
}
