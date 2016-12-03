package com.mialab.jiandu.view.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mialab.jiandu.R;
import com.mialab.jiandu.app.JianDuApplication;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.AppVersion;
import com.mialab.jiandu.presenter.SettingPresenter;
import com.mialab.jiandu.utils.EditDialogBuilder;
import com.mialab.jiandu.utils.PrefUtils;
import com.mialab.jiandu.utils.StatusBarUtil;
import com.mialab.jiandu.utils.ToastUtils;
import com.mialab.jiandu.view.base.MvpActivity;

import butterknife.BindView;

public class SettingActivity extends MvpActivity<SettingPresenter> implements View.OnClickListener, SettingView {

    @BindView(R.id.ib_back)
    ImageButton ibBavk;
    @BindView(R.id.rl_update_password)
    RelativeLayout rlUpdatePassword;
    @BindView(R.id.rl_star)
    RelativeLayout rlStar;
    @BindView(R.id.rl_feedback)
    RelativeLayout rlFeedBack;
    @BindView(R.id.rl_login_out)
    RelativeLayout rlLoginOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected SettingPresenter initPresenter() {
        return new SettingPresenter(this, this);
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
        ibBavk.setOnClickListener(this);
        rlUpdatePassword.setOnClickListener(this);
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
            case R.id.rl_update_password:
                if (!hasLogin()) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                View dialogView = View.inflate(this, R.layout.dialog_password, null);
                final EditText etOldPassword = (EditText) dialogView.findViewById(R.id.et_old_password);
                final EditText etNewPassword = (EditText) dialogView.findViewById(R.id.et_new_password);
                TextView btnConfirm = (TextView) dialogView.findViewById(R.id.btn_confirm);
                TextView btnCancel = (TextView) dialogView.findViewById(R.id.btn_cancel);
                AlertDialog.Builder builderPassword = new AlertDialog.Builder(this);
                builderPassword.setView(dialogView);
                final AlertDialog dialogChoice = builderPassword.create();

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mvpPresenter.updatePassword(etOldPassword.getText().toString(), etNewPassword.getText().toString());
                        dialogChoice.dismiss();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogChoice.dismiss();
                    }
                });
                dialogChoice.show();
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
                mvpPresenter.loginOut();
                JianDuApplication.finishAll();
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                break;
        }
    }

    private boolean hasLogin() {
        String accessToken = PrefUtils.getString(this, GlobalConf.ACCESS_TOKEN, "");
        return !TextUtils.isEmpty(accessToken);
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
        ToastUtils.showToast(this, message);
    }

    @Override
    public void updatePassSuccess() {
        ToastUtils.showToast(this, "修改成功！");
    }

    @Override
    public void updatePassFailure(String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public void badNetWork() {
        ToastUtils.showToast(this, "网络异常，请稍后重试！");
    }

    @Override
    public void illegalInput(String message) {
        ToastUtils.showToast(this, message);
    }
}
