package com.mialab.jiandu.presenter;

import android.content.Context;

import com.mialab.jiandu.model.UserModel;
import com.mialab.jiandu.view.activity.SettingView;

/**
 * Created by Wesly186 on 2016/11/12.
 */

public class SettingPresenter {
    private Context context;
    private SettingView settingView;
    private UserModel userModel;

    public SettingPresenter(Context context, SettingView settingView) {
        this.context = context;
        this.settingView = settingView;
        userModel = new UserModel();
    }

    public void loginOut() {
        userModel.eraseLoginInfo(context);
    }
}
