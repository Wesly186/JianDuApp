package com.mialab.jiandu.conf;

import com.bilibili.socialize.share.core.SharePlatformConfig;
import com.bilibili.socialize.share.util.SharePlatformConfigHelper;

public class GlobalConf {

    //网络相关
    public static final int DEFAULT_TIMEOUT = 10;
    public static final String BASE_URL = "http://192.168.1.172/JianDu/";
    public static final String BASE_PIC_URL = "http://192.168.1.172/pic/";

    //bilibilishare-appId
    public static final String QQ_APPID = "1105648886";
    public static final String QQ_APPKEY = "vrwdz0cu6vL9crd7";
    public static final String WECHAT_APPID = "1105648886";
    public static final String WECHAT_APPSECRET = "vrwdz0cu6vL9crd7";
    public static final String SINA_APPKEY = "977861245";
    public static final String SHARE_TITLE = " 简读";
    public static final String SHARE_CONTENT = "【简读】";

    //sharepreference的key
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String PHONE = "phone";
    public static final String AUTO_UPDATE_SET = "auto_update_set";

    public static void configPlatformsIfNeed() {
        if (SharePlatformConfig.hasAlreadyConfig()) {
            return;
        }
        SharePlatformConfigHelper.configQQPlatform(QQ_APPID, QQ_APPKEY);
        SharePlatformConfigHelper.configWeixinPlatform(WECHAT_APPID, WECHAT_APPSECRET);
        SharePlatformConfigHelper.configSina(SINA_APPKEY);
    }
}
