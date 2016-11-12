package com.mialab.jiandu.presenter;

import android.content.Context;
import android.net.Uri;

import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.entity.User;
import com.mialab.jiandu.model.UserModel;
import com.mialab.jiandu.utils.ImageUtils;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;
import com.mialab.jiandu.view.activity.UserProfileView;

import java.io.File;

/**
 * Created by Wesly186 on 2016/11/12.
 */

public class UserProfilePresenter {
    private Context context;
    private UserProfileView userProfileView;
    private UserModel userModel;

    public UserProfilePresenter(Context context, UserProfileView userProfileView) {
        this.context = context;
        this.userProfileView = userProfileView;
        userModel = new UserModel();
    }

    public void updateUserProfile(File headFile, String name, String blog, String intro, String sex, String job) {
        userModel.setUpdateProfileSubscribe(new HttpSubscriber<User>(context) {
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
        userModel.updateProfile(headFile, name, blog, intro, sex, job, context);
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
        /*if (null == uri){
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null){
            data = uri.getPath();
        }else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA},null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(
                            MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;*/
    }
}
