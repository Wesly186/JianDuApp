package com.mialab.jiandu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Wesly186 on 2016/11/12.
 */

public class ImageUtils {
    public static File saveImage(Context context, Bitmap bitmap, String directory, String fileName) throws Exception {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            ToastUtils.showToast(context, "未检测到SD卡！");
            return null;
        }
        File dir = new File(Environment.getExternalStorageDirectory(), directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File image = new File(dir, fileName);
        if (!image.exists()) {
            image.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(image));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();

        return image;
    }
}
