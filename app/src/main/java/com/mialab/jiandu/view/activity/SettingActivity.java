package com.mialab.jiandu.view.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.mialab.jiandu.R;
import com.mialab.jiandu.app.JianDuApplication;
import com.mialab.jiandu.entity.AppVersion;
import com.mialab.jiandu.presenter.SettingPresenter;
import com.mialab.jiandu.utils.EditDialogBuilder;
import com.mialab.jiandu.utils.StatusBarUtil;
import com.mialab.jiandu.utils.ToastUtils;
import com.mialab.jiandu.view.base.BaseActivity;

import butterknife.BindView;

public class SettingActivity extends BaseActivity implements View.OnClickListener, SettingView {

    @BindView(R.id.ib_back)
    ImageButton ibBavk;
    @BindView(R.id.rl_star)
    RelativeLayout rlStar;
    @BindView(R.id.rl_feedback)
    RelativeLayout rlFeedBack;
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

        ibBavk.setOnClickListener(this);
        rlStar.setOnClickListener(this);
        rlFeedBack.setOnClickListener(this);
        rlLoginOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.rl_star:
                try {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    ToastUtils.showToast(this, "没有找到应用市场");
                }
                break;
            case R.id.rl_feedback:
                EditDialogBuilder builder = new EditDialogBuilder(SettingActivity.this);
                builder.setDialogTitle("告诉我们你的想法：").setPositiveButtonListener(new EditDialogBuilder.PositiveButtonClickListener() {
                    @Override
                    public void onClick(String content) {
                        if (TextUtils.isEmpty(content.trim())) {
                            ToastUtils.showToast(SettingActivity.this, "内容不能为空");
                        } else {
                            ToastUtils.showToast(SettingActivity.this, "感谢您的反馈！");
                        }
                    }
                }).build().show();
                break;
            case R.id.rl_login_out:
                settingPresenter.loginOut();
                JianDuApplication.finishAll();
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                break;
        }
    }

    @Override
    public void showUpdateDialog(AppVersion data) {

    }

    @Override
    public void updateProgress(int percentage) {

    }

    @Override
    public void downloadComplete() {

    }

    @Override
    public void updateFailed(String message) {

    }
}
