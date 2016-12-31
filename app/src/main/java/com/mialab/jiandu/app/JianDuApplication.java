package com.mialab.jiandu.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import butterknife.BuildConfig;
import butterknife.ButterKnife;

/**
 * 一些静态方法 第三方框架初始化
 */
public class JianDuApplication extends Application {

    private static List<Activity> activities = new ArrayList<Activity>();
    private static Context mContext;

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static int getSize() {
        return activities.size();
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        ButterKnife.setDebug(BuildConfig.DEBUG);
    }
}