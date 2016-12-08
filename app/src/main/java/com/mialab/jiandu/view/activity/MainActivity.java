package com.mialab.jiandu.view.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat.Builder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mialab.jiandu.R;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.AppVersion;
import com.mialab.jiandu.presenter.MainPresenter;
import com.mialab.jiandu.utils.PrefUtils;
import com.mialab.jiandu.utils.StatusBarUtil;
import com.mialab.jiandu.utils.ToastUtils;
import com.mialab.jiandu.view.base.MvpActivity;
import com.mialab.jiandu.view.fragment.HomeFragment;
import com.mialab.jiandu.view.fragment.HotFragment;
import com.mialab.jiandu.view.fragment.NotifyFragment;
import com.mialab.jiandu.view.fragment.UserCenterFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by Wesly186 on 2016/8/16.
 */
public class MainActivity extends MvpActivity<MainPresenter> implements MainView {

    @BindView(R.id.fl_content)
    FrameLayout frameLayout;
    @BindView(R.id.rg_bottom_tab)
    RadioGroup rgTab;

    private HomeFragment mHomeFragment = new HomeFragment();
    private HotFragment mHotFragment = new HotFragment();
    private NotifyFragment mNotifyFragment = new NotifyFragment();
    private UserCenterFragment mUserCenterFragment = new UserCenterFragment();

    public static final int HOME_FRAGMENT = 0;
    public static final int RANK_FRAGMENT = 1;
    public static final int NOTIFY_FRAGMENT = 2;
    public static final int USERCENTER_FRAGMENT = 3;

    private int currentSelect;

    private long lastPressBackTime;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private NotificationManager notifyManager;
    private Builder notifyBuilder;

    private AppVersion mAppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter initPresenter() {
        return new MainPresenter(this, this);
    }

    @Override
    protected void initView() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimaryDark), 0);
    }

    @Override
    public void initData() {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fl_content, mHomeFragment);
        transaction.add(R.id.fl_content, mHotFragment);
        transaction.add(R.id.fl_content, mNotifyFragment);
        transaction.add(R.id.fl_content, mUserCenterFragment);
        transaction.commit();

        rgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                hideAllFragment(transaction);
                switch (checkedId) {
                    case R.id.rb_home:
                        currentSelect = HOME_FRAGMENT;
                        transaction.show(mHomeFragment);
                        break;
                    case R.id.rb_rank:
                        currentSelect = RANK_FRAGMENT;
                        transaction.show(mHotFragment);
                        break;
                    case R.id.rb_notify:
                        currentSelect = NOTIFY_FRAGMENT;
                        transaction.show(mNotifyFragment);
                        break;
                    case R.id.rb_usercenter:
                        currentSelect = USERCENTER_FRAGMENT;
                        transaction.show(mUserCenterFragment);
                        break;
                }
                transaction.commit();
            }
        });
        rgTab.check(R.id.rb_home);
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction transaction) {
        transaction.hide(mHomeFragment);
        transaction.hide(mHotFragment);
        transaction.hide(mNotifyFragment);
        transaction.hide(mUserCenterFragment);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void handleEvent(AppVersion appVersion) {
        mAppVersion = appVersion;
        mvpPresenter.requestWritePermission();
    }

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
                ToastUtils.showToast(MainActivity.this, "后台下载更新中...");
                showUpdateNotification(false);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showUpdateNotification(boolean auto) {
        notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifyBuilder = (Builder) new Builder(MainActivity.this)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle(auto ? "WIFI自动更新：简读" : "新版本：简读")
                .setContentText("正在下载...");
        notifyBuilder.setAutoCancel(false);
        notifyBuilder.setProgress(100, 0, false);
        notifyManager.notify(0, notifyBuilder.build());
    }

    @Override
    public void updateProgress(int percentage) {
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
    public void updateFailed(String message) {
        notifyManager.cancel(0);
        ToastUtils.showToast(MainActivity.this, message);
    }

    @Override
    public void requestWriteSuccess() {
        if (PrefUtils.getBoolean(this, GlobalConf.SETTING_WIFI_AUTO_UPDATE, false)) {
            mvpPresenter.downloadNewVersion(mAppVersion);
            showUpdateNotification(true);
        } else {
            showUpdateDialog(mAppVersion);
        }
    }

    @Override
    public void requestWriteFailure() {
        Toast.makeText(this, "没有写文件权限，请通过应用市场更新或者在设置中授权！", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onBackPressed() {
        long pressTime = new Date().getTime() / 1000;
        if (pressTime - lastPressBackTime < 2) {
            super.onBackPressed();
        } else {
            lastPressBackTime = pressTime;
            ToastUtils.showToast(MainActivity.this, "再点一次退出");
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public int getCurrentSelect() {
        return currentSelect;
    }
}
