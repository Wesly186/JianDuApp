package com.mialab.jiandu.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.model.UserModel;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;
import com.mialab.jiandu.view.activity.RegisterView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Wesly186 on 2016/11/12.
 */

public class RegisterPresenter {

    private Context context;
    private RegisterView registerView;
    private UserModel userModel;

    public RegisterPresenter(Context context, RegisterView registerView) {
        this.context = context;
        this.registerView = registerView;
        userModel = new UserModel();
    }

    /**
     * business:register,retrievePassword
     */
    public void getValidationCode() {

        String phone = registerView.getPhone();
        if (TextUtils.isEmpty(phone.trim()) || phone.length() != 11) {
            registerView.illegalInput("手机号格式不正确");
            return;
        }

        userModel.setValidationCodeSubscribe(new HttpSubscriber<String>(context) {
            @Override
            public void onStart() {
                registerView.beginCount();
            }

            @Override
            public void onSuccess(BaseModel<String> response) {
                registerView.getCodeSuccess();
            }

            @Override
            public void onFailure(String message) {
                registerView.getCodeFailed(message);
            }

            @Override
            public void onBadNetwork() {
                registerView.onBadNetWork();
            }
        });
        userModel.getValidationCode(phone, "register", context);
    }

    public void register() {
        String phone = registerView.getPhone();
        if (TextUtils.isEmpty(phone.trim()) || phone.length() != 11) {
            registerView.illegalInput("手机号格式不正确");
            return;
        }
        String password = registerView.getPassword();
        Pattern pattern = Pattern
                .compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            registerView.illegalInput("密码6-20位，包含字母和数字！");
            return;
        }
        int validationCode = registerView.getValidationCode();
        userModel.setRegisterSubscribe(new HttpSubscriber<String>(context) {
            @Override
            public void onStart() {
                registerView.registing();
            }

            @Override
            public void onSuccess(BaseModel<String> response) {
                registerView.registerSuccess();
            }

            @Override
            public void onFailure(String message) {
                registerView.registerFailure(message);
            }

            @Override
            public void onBadNetwork() {
                registerView.onBadNetWork();
            }
        });
        userModel.register(phone, password, validationCode, context);
    }
}
