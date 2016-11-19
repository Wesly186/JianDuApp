package com.mialab.jiandu.presenter;

import android.content.Context;
import android.content.pm.PackageManager;

import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.AppVersion;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.model.SettingModel;
import com.mialab.jiandu.model.UserModel;
import com.mialab.jiandu.utils.http.helper.DownloadUtils;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;
import com.mialab.jiandu.view.activity.SettingView;

import okhttp3.Call;

/**
 * Created by Wesly186 on 2016/11/12.
 */

public class SettingPresenter {
    private Context context;
    private SettingView settingView;
    private UserModel userModel;
    private SettingModel settingModel;
    private int lastpercentage;

    public SettingPresenter(Context context, SettingView settingView) {
        this.context = context;
        this.settingView = settingView;
        userModel = new UserModel();
        settingModel = new SettingModel();
    }

    public void checkUpdate() {
        settingModel.setVersionSubscriber(new HttpSubscriber<AppVersion>() {
            @Override
            public void onSuccess(BaseModel<AppVersion> response) {
                settingView.showUpdateDialog(response.getData());
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

    public void downloadNewVersion(AppVersion data) {
        DownloadUtils.getInstance().download(GlobalConf.BASE_PIC_URL + data.getDownloadUrl(), "/sdcard/JianDu", "update.apk", new DownloadUtils.RequestCallBack() {
            @Override
            public void onProgress(long progress, long total, boolean done) {

                int percentage = (int) ((double) progress / total * 100);
                if (percentage - lastpercentage > 3) {
                    lastpercentage = percentage;
                    settingView.updateProgress(percentage);
                }


                if (done) {
                    settingView.downloadComplete();
                }
            }

            @Override
            public void onFailure(Call call, Exception e) {
                settingView.updateFailed("新版本下载失败");
            }
        });
    }

    public void loginOut() {
        userModel.eraseLoginInfo(context);
    }
}
