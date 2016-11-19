package com.mialab.jiandu.presenter;

import android.content.Context;

import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.AppVersion;
import com.mialab.jiandu.utils.http.helper.DownloadUtils;
import com.mialab.jiandu.view.activity.MainView;

import okhttp3.Call;

/**
 * Created by Wesly186 on 2016/11/12.
 */

public class MainPresenter {
    private Context context;
    private MainView mainView;
    private int lastpercentage;

    public MainPresenter(Context context, MainView mainView) {
        this.context = context;
        this.mainView = mainView;
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
}
