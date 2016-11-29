package com.mialab.jiandu.presenter;

import android.app.Activity;
import android.content.Context;

import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.AppVersion;
import com.mialab.jiandu.model.PermissionModel;
import com.mialab.jiandu.utils.http.helper.DownloadUtils;
import com.mialab.jiandu.view.activity.MainView;

import okhttp3.Call;
import rx.Subscriber;

/**
 * Created by Wesly186 on 2016/11/12.
 */

public class MainPresenter extends BasePresenter {

    private Context context;
    private MainView mainView;
    private int lastpercentage;
    private PermissionModel permissionModel;

    public MainPresenter(Context context, MainView mainView) {
        this.context = context;
        this.mainView = mainView;
        permissionModel = new PermissionModel();
    }

    public void downloadNewVersion(AppVersion data) {
        DownloadUtils.getInstance().download(GlobalConf.BASE_PIC_URL + data.getDownloadUrl(), "/sdcard/JianDu", "update.apk", new DownloadUtils.RequestCallBack() {
            @Override
            public void onProgress(long progress, long total, boolean done) {

                int percentage = (int) ((double) progress / total * 100);
                if (percentage - lastpercentage > 3) {
                    lastpercentage = percentage;
                    mainView.updateProgress(percentage);
                }
                if (done) {
                    mainView.downloadComplete();
                }
            }

            @Override
            public void onFailure(Call call, Exception e) {
                mainView.updateFailed("新版本下载失败");
            }
        });
    }

    public void requestWritePermission() {
        permissionModel.setWriteSubscriber(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    mainView.requestWriteSuccess();
                } else {
                    mainView.requestWriteFailure();
                }
            }
        });
        addSubscription(permissionModel.requestWrite((Activity) context));
    }
}
