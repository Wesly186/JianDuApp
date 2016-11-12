package com.mialab.jiandu.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.mialab.jiandu.R;
import com.mialab.jiandu.utils.StatusBarUtil;
import com.mialab.jiandu.utils.ToastUtils;
import com.mialab.jiandu.view.base.BaseActivity;
import com.mialab.jiandu.view.fragment.HomeFragment;
import com.mialab.jiandu.view.fragment.NotifyFragment;
import com.mialab.jiandu.view.fragment.RankFragment;
import com.mialab.jiandu.view.fragment.UserCenterFragment;

import java.util.Date;

import butterknife.BindView;

/**
 * Created by Wesly186 on 2016/8/16.
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.fl_content)
    FrameLayout frameLayout;
    @BindView(R.id.rg_bottom_tab)
    RadioGroup rgTab;

    private HomeFragment mHomeFragment = new HomeFragment();
    private RankFragment mRankFragment = new RankFragment();
    private NotifyFragment mNotifyFragment = new NotifyFragment();
    private UserCenterFragment mUserCenterFragment = new UserCenterFragment();

    private long lastPressBackTime;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
