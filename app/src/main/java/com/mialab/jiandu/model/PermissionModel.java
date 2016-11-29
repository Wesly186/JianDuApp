package com.mialab.jiandu.model;

import android.Manifest;
import android.app.Activity;

import com.tbruyelle.rxpermissions.RxPermissions;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Wesly186 on 2016/11/28.
 */

public class PermissionModel {

    private Subscriber<Boolean> cameraSubscriber;
    private Subscriber<Boolean> writeSubscriber;
    private RxPermissions rxPermissions;

    public Subscription requestCamera(Activity activity) {
        rxPermissions = new RxPermissions(activity);
        return rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe(cameraSubscriber);
    }

    public Subscription requestWrite(Activity activity) {
        rxPermissions = new RxPermissions(activity);
        return rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(writeSubscriber);
    }


    public void setCameraSubscriber(Subscriber<Boolean> cameraSubscriber) {
        this.cameraSubscriber = cameraSubscriber;
    }

    public void setWriteSubscriber(Subscriber<Boolean> writeSubscriber) {
        this.writeSubscriber = writeSubscriber;
    }
}
