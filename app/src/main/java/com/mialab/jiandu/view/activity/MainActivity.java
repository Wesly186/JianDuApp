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

import com.mialab.jiandu.R;
import com.mialab.jiandu.entity.AppVersion;
import com.mialab.jiandu.presenter.MainPresenter;
import com.mialab.jiandu.utils.StatusBarUtil;
import com.mialab.jiandu.utils.ToastUtils;
import com.mialab.jiandu.view.base.BaseActivity;
import com.mialab.jiandu.view.fragment.HomeFragment;
import com.mialab.jiandu.view.fragment.NotifyFragment;
import com.mialab.jiandu.view.fragment.RankFragment;
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
public class MainActivity extends BaseActivity implements MainView {

    @BindView(R.id.fl_content)
    FrameLayout frameLayout;
    @BindView(R.id.rg_bottom_tab)
    RadioGroup rgTab;

    private MainPresenter mainPresenter;

    private HomeFragment mHomeFragment = new HomeFragment();
    private RankFragment mRankFragment = new RankFragment();
    private NotifyFragment mNotifyFragment = new NotifyFragment();
    private UserCenterFragment mUserCenterFragment = new UserCenterFragment();

    private long lastPressBackTime;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private NotificationManager notifyManager;
    private Builder notifyBuilder;

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
    protected void initView() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimaryDark), 0);
    }

    @Override
    public void initData() {

        mainPresenter = new MainPresenter(this, this);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fl_content, mHomeFragment);
        transaction.add(R.id.fl_content, mRankFragment);
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
                        transaction.show(mHomeFragment);
                        break;
                    case R.id.rb_rank:
                        transaction.show(mRankFragment);
                        break;
                    case R.id.rb_notify:
                        transaction.show(mNotifyFragment);
                        break;
                    case R.id.rb_usercenter:
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
        transaction.hide(mRankFragment);
        transaction.hide(mNotifyFragment);
        transaction.hide(mUserCenterFragment);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void handleEvent(AppVersion appVersion) {
        showUpdateDialog(appVersion);
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
                mainPresenter.downloadNewVersion(data);
                ToastUtils.showToast(MainActivity.this, "后台下载更新中...");
                showUpdateNotification();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showUpdateNotification() {
        notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifyBuilder = (Builder) new Builder(MainActivity.this)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle("新版本：简读")
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
}
