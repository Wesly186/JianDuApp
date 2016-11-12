package com.mialab.jiandu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.mialab.jiandu.R;
import com.mialab.jiandu.app.JianDuApplication;
import com.mialab.jiandu.presenter.SettingPresenter;
import com.mialab.jiandu.utils.StatusBarUtil;
import com.mialab.jiandu.view.base.BaseActivity;

import butterknife.BindView;

public class SettingActivity extends BaseActivity implements View.OnClickListener, SettingView {

    @BindView(R.id.rl_login_out)
    RelativeLayout rlLoginOut;

    private SettingPresenter settingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        if (android.os.Build.BRAND.equals("Xiaomi")) {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
            StatusBarUtil.setMiuiStatusBarDarkMode(this, true);
        } else if (android.os.Build.BRAND.equals("Meizu")) {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
            StatusBarUtil.setMeizuStatusBarDarkIcon(this, true);
        } else {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.statusbar_light), 0);
        }
    }

    @Override
    public void initData() {
        settingPresenter = new SettingPresenter(this, this);

        rlLoginOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_login_out:
                settingPresenter.loginOut();
                JianDuApplication.finishAll();
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                break;
        }
    }
}
