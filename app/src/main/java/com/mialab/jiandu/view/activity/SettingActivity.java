package com.mialab.jiandu.view.activity;

import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;

import butterknife.BindView;

public class SettingActivity extends MvpActivity<SettingPresenter> implements View.OnClickListener, SettingView, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.ib_back)
    ImageButton ibBavk;
    @BindView(R.id.rl_update_password)
    RelativeLayout rlUpdatePassword;
    @BindView(R.id.rl_check_update)
    RelativeLayout rlCheckUpdate;
    @BindView(R.id.tv_version_name)
    TextView tvVersionName;
    @BindView(R.id.tv_cache_num)
    TextView tvCacheNum;
    @BindView(R.id.rl_clean_cache)
    RelativeLayout rlCleanCache;
    @BindView(R.id.switch_wifi_auto)
    SwitchCompat switchWiFiAuto;
    @BindView(R.id.switch_push_article)
    SwitchCompat switchPushArticle;
    @BindView(R.id.rl_star)
    RelativeLayout rlStar;
    @BindView(R.id.rl_feedback)
    RelativeLayout rlFeedBack;
    @BindView(R.id.rl_login_out)
    RelativeLayout rlLoginOut;

    private NotificationManager notifyManager;
    private NotificationCompat.Builder notifyBuilder;

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
        try {
            tvVersionName.setText("V" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            tvVersionName.setText("版本未知");
        }
        try {
            tvCacheNum.setText(mvpPresenter.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        switchWiFiAuto.setChecked(PrefUtils.getBoolean(this, GlobalConf.SETTING_WIFI_AUTO_UPDATE, false));
        switchPushArticle.setChecked(PrefUtils.getBoolean(this, GlobalConf.SETTING_PUSH_ARTICLE, false));
    }

    @Override
    public void initData() {
        ibBavk.setOnClickListener(this);
        rlUpdatePassword.setOnClickListener(this);
        rlCheckUpdate.setOnClickListener(this);
        rlCleanCache.setOnClickListener(this);
        rlStar.setOnClickListener(this);
        rlFeedBack.setOnClickListener(this);
        rlLoginOut.setOnClickListener(this);

        switchWiFiAuto.setOnCheckedChangeListener(this);
        switchPushArticle.setOnCheckedChangeListener(this);
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
            case R.id.rl_check_update:
                mvpPresenter.requestWritePermission();
                break;
            case R.id.rl_clean_cache:
                mvpPresenter.clearAllCache(this);
                tvCacheNum.setText("0KB");
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
    public void showUpdateDialog(final AppVersion data) {
        View dialogView = View.inflate(this, R.layout.dialog_update, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        TextView tvVersionName = (TextView) dialogView.findViewById(R.id.tv_version_name);
        TextView tvChangeLog = (TextView) dialogView.findViewById(R.id.tv_changelog);
        TextView tvCancel = (TextView) dialogView.findViewById(R.id.btn_cancel);
        TextView tvConfirm = (TextView) dialogView.findViewById(R.id.btn_confirm);

        tvVersionName.setText("发现新版本：" + data.getVersionName());
        tvChangeLog.setText(data.getUpdateMsg());
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mvpPresenter.downloadNewVersion(data);
                ToastUtils.showToast(SettingActivity.this, "后台下载更新中...");
                showUpdateNotification();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void updateDownloadProgress(int percentage) {
        notifyBuilder.setProgress(100, percentage, false);
        notifyManager.notify(0, notifyBuilder.build());
    }

    @Override
    public void downloadComplete() {
        notifyManager.cancel(0);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(new File("/sdcard/JianDu/update.apk")),
                "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent, 0);
    }

    @Override
    public void updateVersionFailure(String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public void downloadFailed(String message) {
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

    @Override
    public void requestWriteSuccess() {
        mvpPresenter.checkUpdate();
    }

    @Override
    public void requestWriteFailure() {
        Toast.makeText(this, "没有写文件权限，请通过应用市场更新或者在设置中授权！", Toast.LENGTH_LONG).show();
    }

    public void showUpdateNotification() {
        notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifyBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle("新版本：简读")
                .setContentText("正在下载...");
        notifyBuilder.setAutoCancel(false);
        notifyBuilder.setProgress(100, 0, false);
        notifyManager.notify(0, notifyBuilder.build());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_wifi_auto:
                PrefUtils.setBoolean(this, GlobalConf.SETTING_WIFI_AUTO_UPDATE, isChecked);
                break;
            case R.id.switch_push_article:
                PrefUtils.setBoolean(this, GlobalConf.SETTING_PUSH_ARTICLE, isChecked);
                break;
        }
    }
}
