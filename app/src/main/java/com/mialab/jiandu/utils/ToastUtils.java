package com.mialab.jiandu.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Wesly186 on 2016/8/17.
 */
public class ToastUtils {

    private static Toast mToast;

    public static void showToast(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }
}