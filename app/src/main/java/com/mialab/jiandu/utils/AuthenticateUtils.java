package com.mialab.jiandu.utils;

import android.text.TextUtils;

import com.mialab.jiandu.app.JianDuApplication;
import com.mialab.jiandu.conf.GlobalConf;

/**
 * Created by Wesly186 on 2016/12/3.
 */

public class AuthenticateUtils {

    public static boolean hasLogin() {
        String accessToken = PrefUtils.getString(JianDuApplication.getContext(), GlobalConf.ACCESS_TOKEN, "");
        return !TextUtils.isEmpty(accessToken);
    }
}
