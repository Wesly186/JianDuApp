package com.mialab.jiandu.presenter;

import android.content.Context;
import android.content.pm.PackageManager;

import com.mialab.jiandu.entity.AppVersion;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.model.SettingModel;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;
import com.mialab.jiandu.view.activity.SplashView;

/**
 * Created by Wesly186 on 2016/11/12.
 */

public class SplashPresenter extends BasePresenter {

    private Context context;
    private SplashView splashView;
    private SettingModel settingModel;

    public SplashPresenter(Context context, SplashView splashView) {
        this.context = context;
        this.splashView = splashView;
        settingModel = new SettingModel();
    }

    public void checkUpdate() {
        settingModel.setVersionSubscriber(new HttpSubscriber<AppVersion>() {
            @Override
            public void onSuccess(BaseModel<AppVersion> response) {
                splashView.getNewVersion(response.getData());
            }

            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onBadNetwork() {

            }
        });
        int versionCode = 1;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // 包名错误
            e.printStackTrace();
        }
        settingModel.checkUpdate(context, versionCode);
    }
}
