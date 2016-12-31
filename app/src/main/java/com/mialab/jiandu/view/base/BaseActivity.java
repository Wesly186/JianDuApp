package com.mialab.jiandu.view.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mialab.jiandu.app.JianDuApplication;

import butterknife.ButterKnife;

/**
 * 基础
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        JianDuApplication.addActivity(this);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
    }

    protected abstract int getContentViewId();

    protected void initView() {

    }

    public void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JianDuApplication.removeActivity(this);
    }
}
