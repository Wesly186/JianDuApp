package com.mialab.jiandu.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.entity.LoginInfo;
import com.mialab.jiandu.model.UserModel;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;
import com.mialab.jiandu.view.activity.LoginView;

/**
 * Created by Wesly186 on 2016/8/22.
 */
public class LoginPresenter {

    private Context context;
    private LoginView loginView;
    private UserModel userModel;

    public LoginPresenter(Context context, LoginView loginView) {
        this.context = context;
        this.loginView = loginView;
        userModel = new UserModel();
    }

    public void getOAuth() {
        //输入格式校验
        String phone = loginView.getPhone();
        String password = loginView.getPassword();
        if (phone.length() != 11 || TextUtils.isEmpty(password.trim())) {
            loginView.IllegalInput("手机号格式不正确");
            return;
        }

        userModel.setoAuthSubscribe(new HttpSubscriber<LoginInfo>(context) {

            @Override
            public void onStart() {
                loginView.onLogin();
            }

            @Override
            public void onSuccess(BaseModel<LoginInfo> response) {
                userModel.saveLoginInfo(response.getData(), context);
                loginView.onLoginSuccess();
            }

            @Override
            public void onFailure(String message) {
                loginView.onLoginFailed(message);
            }

            @Override
            public void onBadNetwork() {
                loginView.onBadNetWork();
            }
        });
        userModel.getOAuthInfo(loginView.getPhone(), loginView.getPassword(), context);
    }
}
