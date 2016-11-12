package com.mialab.jiandu.presenter;

import android.content.Context;

import com.mialab.jiandu.entity.User;
import com.mialab.jiandu.model.UserModel;
import com.mialab.jiandu.view.fragment.UserCenterView;

/**
 * Created by Wesly186 on 2016/11/12.
 */

public class UserCenterPresenter {
    private Context context;
    private UserCenterView userCenterView;
    private UserModel userModel;

    public UserCenterPresenter(Context context, UserCenterView userCenterView) {
        this.context = context;
        this.userCenterView = userCenterView;
        userModel = new UserModel();
    }

    public User getUserInfoFormCache(String phone) {
        return userModel.getFromDB(phone, context);
    }
}
