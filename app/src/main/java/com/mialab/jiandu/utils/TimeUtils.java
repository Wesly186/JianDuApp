package com.mialab.jiandu.utils;

import java.util.Date;

/**
 * Created by Wesly186 on 2016/12/2.
 */

public class TimeUtils {

    /**
     * 计算给定时间距离当前时间的时间间隔，返回字符串，用来做时间线显示
     *
     * @param date
     * @return
     */
    public static String time2Now(Date date) {
        // 当前时间
        long nowTime = new Date().getTime();
        // 需要计算的时间
        long time = date.getTime();
        // 时间间隔，分钟
        long timeInterval = (nowTime - time) / 1000 / 60;
        String retval = null;

        if (timeInterval < 2) {
            retval = "刚刚";
        } else if (timeInterval < 60) {
            retval = timeInterval + "分钟前";
        } else if (timeInterval < 60 * 24) {
            retval = timeInterval / 60 + "小时前";
        } else if (timeInterval < 60 * 24 * 30) {
            retval = timeInterval / 60 / 24 + "天前";
        } else if (timeInterval < 60 * 24 * 30 * 12) {
            retval = timeInterval / 60 / 24 / 30 + "个月前";
        } else {
            retval = timeInterval / 60 / 24 / 30 / 12 + "年前";
        }
        return retval;
    }
}
