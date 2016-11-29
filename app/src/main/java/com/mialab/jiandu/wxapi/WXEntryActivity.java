package com.mialab.jiandu.wxapi;

import com.bilibili.socialize.share.core.ui.BaseWXEntryActivity;
import com.mialab.jiandu.conf.GlobalConf;

public class WXEntryActivity extends BaseWXEntryActivity {

    @Override
    protected String getAppId() {
        return GlobalConf.WECHAT_APPID;
    }

}