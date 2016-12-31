package com.mialab.jiandu.model;

import android.content.Context;

import com.mialab.jiandu.api.IVersion;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.AppVersion;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.utils.PrefUtils;
import com.mialab.jiandu.utils.http.helper.RetrofitHelper;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 设置界面自动更新 检查更新
 */
public class SettingModel {

    private HttpSubscriber<AppVersion> versionSubscriber;

    public boolean AutoUpdateSet(Context context) {
        return PrefUtils.getBoolean(context, GlobalConf.AUTO_UPDATE_SET, false);
    }

    public void setAutoUpdateSet(Context context, boolean autoUpdateSet) {
        PrefUtils.setBoolean(context, GlobalConf.AUTO_UPDATE_SET, autoUpdateSet);
    }

    public Subscription checkUpdate(Context context, int versionCode) {
        IVersion version = RetrofitHelper.getProxy(IVersion.class, context);
        Observable<BaseModel<AppVersion>> observable = version.checkUpdate(versionCode);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(versionSubscriber);
        return subscription;
    }

    public void setVersionSubscriber(HttpSubscriber<AppVersion> versionSubscriber) {
        this.versionSubscriber = versionSubscriber;
    }
}
