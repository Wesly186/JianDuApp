package com.mialab.jiandu.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.AppVersion;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.model.PermissionModel;
import com.mialab.jiandu.model.SettingModel;
import com.mialab.jiandu.model.UserModel;
import com.mialab.jiandu.utils.http.helper.DownloadUtils;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;
import com.mialab.jiandu.view.activity.SettingView;

import java.io.File;
import java.math.BigDecimal;

import okhttp3.Call;
import rx.Subscriber;

/**
 * Created by Wesly186 on 2016/11/12.
 */

public class SettingPresenter extends BasePresenter {
    private Context context;
    private SettingView settingView;
    private UserModel userModel;
    private SettingModel settingModel;
    private PermissionModel permissionModel;
    private int lastpercentage;

    public SettingPresenter(Context context, SettingView settingView) {
        this.context = context;
        this.settingView = settingView;
        userModel = new UserModel();
        settingModel = new SettingModel();
        permissionModel = new PermissionModel();
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
                    settingView.requestWriteSuccess();
                } else {
                    settingView.requestWriteFailure();
                }
            }
        });
        addSubscription(permissionModel.requestWrite((Activity) context));
    }

    public void checkUpdate() {
        settingModel.setVersionSubscriber(new HttpSubscriber<AppVersion>() {
            @Override
            public void onSuccess(BaseModel<AppVersion> response) {
                settingView.showUpdateDialog(response.getData());
            }

            @Override
            public void onFailure(String message) {
                settingView.updateVersionFailure(message);
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
                    settingView.updateDownloadProgress(percentage);
                }


                if (done) {
                    settingView.downloadComplete();
                }
            }

            @Override
            public void onFailure(Call call, Exception e) {
                settingView.downloadFailed("新版本下载失败");
            }
        });
    }

    public void loginOut() {
        userModel.eraseLoginInfo(context);
    }

    public void updatePassword(String oldPassword, String newpassword) {
        if (oldPassword.equals(newpassword)) {
            settingView.illegalInput("新密码和旧密码不能相同！");
            return;
        }
        userModel.setUpdatePassSubscribe(new HttpSubscriber<String>() {
            @Override
            public void onSuccess(BaseModel<String> response) {
                settingView.updatePassSuccess();
            }

            @Override
            public void onFailure(String message) {
                settingView.updatePassFailure(message);
            }

            @Override
            public void onBadNetwork() {
                settingView.badNetWork();
            }
        });
        addSubscription(userModel.updatePassword(context, userModel.getFromDB(context).getPhone(), oldPassword, newpassword));
    }

    public String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    public void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
}
