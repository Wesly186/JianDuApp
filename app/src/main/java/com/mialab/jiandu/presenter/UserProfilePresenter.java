package com.mialab.jiandu.presenter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.entity.User;
import com.mialab.jiandu.model.PermissionModel;
import com.mialab.jiandu.model.UserModel;
import com.mialab.jiandu.utils.ImageUtils;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;
import com.mialab.jiandu.view.activity.UserProfileView;

import java.io.File;

import rx.Subscriber;

/**
 * Created by Wesly186 on 2016/11/12.
 */

public class UserProfilePresenter extends BasePresenter {
    private Context context;
    private UserProfileView userProfileView;
    private UserModel userModel;
    private PermissionModel permissionModel;

    public UserProfilePresenter(Context context, UserProfileView userProfileView) {
        this.context = context;
        this.userProfileView = userProfileView;
        userModel = new UserModel();
        permissionModel = new PermissionModel();
    }

    public void updateUserProfile(File headFile, String name, String blog, String intro, String sex, String job) {
        userModel.setUpdateProfileSubscribe(new HttpSubscriber<User>() {
            @Override
            public void onSuccess(BaseModel<User> response) {
                userModel.save2DB(response.getData(), context);
                userProfileView.updateSuccess(response.getData());
            }

            @Override
            public void onFailure(String message) {
                userProfileView.updateFailure(message);
            }

            @Override
            public void onBadNetwork() {
                userProfileView.onBadNetWork();
            }
        });
        addSubscription(userModel.updateProfile(headFile, name, blog, intro, sex, job, context));
    }

    public User getUserInfoFormCache(String phone) {
        return userModel.getFromDB(phone, context);
    }

    /**
     * 根据uri得到图片的绝对路径
     *
     * @param uri
     * @return
     */
    public File getAbsolutePath(final Uri uri) {
        return ImageUtils.scal(uri);
    }

    public void requestCameraPermission() {
        permissionModel.setCameraSubscriber(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    userProfileView.requestCameraSuccess();
                } else {
                    userProfileView.requestCameraFailure();
                }
            }
        });
        addSubscription(permissionModel.requestCamera((Activity) context));
    }
}
