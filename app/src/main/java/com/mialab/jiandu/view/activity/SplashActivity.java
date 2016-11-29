package com.mialab.jiandu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mialab.jiandu.R;
import com.mialab.jiandu.entity.AppVersion;
import com.mialab.jiandu.presenter.SplashPresenter;
import com.mialab.jiandu.utils.StatusBarUtil;
import com.mialab.jiandu.view.base.MvpActivity;

import org.greenrobot.eventbus.EventBus;

public class SplashActivity extends MvpActivity<SplashPresenter> implements SplashView {

    private AppVersion mAppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected SplashPresenter initPresenter() {
        return new SplashPresenter(this, this);
    }

    @Override
    protected void initView() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 0);
    }

    @Override
    public void initData() {
        //检查更新
        mvpPresenter.checkUpdate();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                jump2NextPage();
            }
        }, 1200);
    }

    private void jump2NextPage() {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void getNewVersion(AppVersion appVersion) {
        mAppVersion = appVersion;
    }

    @Override
    protected void onStop() {
        if (mAppVersion != null) {
            EventBus.getDefault().post(mAppVersion);
        }
        super.onStop();
    }
}
