package com.mialab.jiandu.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.mialab.jiandu.R;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.User;
import com.mialab.jiandu.event.UserInfoUpdate;
import com.mialab.jiandu.presenter.UserCenterPresenter;
import com.mialab.jiandu.utils.PrefUtils;
import com.mialab.jiandu.view.activity.ArticlePublishActivity;
import com.mialab.jiandu.view.activity.LoginActivity;
import com.mialab.jiandu.view.activity.SettingActivity;
import com.mialab.jiandu.view.activity.UserProfileActivity;
import com.mialab.jiandu.view.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Wesly186 on 2016/8/17.
 */
public class UserCenterFragment extends BaseFragment implements UserCenterView, View.OnClickListener {

    @BindView(R.id.rl_user_profile)
    RelativeLayout rlUserProfile;
    @BindView(R.id.rl_contributions)
    RelativeLayout rlContributions;
    @BindView(R.id.rl_settings)
    RelativeLayout rlSettings;

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_job)
    TextView tvJob;

    private User user;
    private UserCenterPresenter userCenterPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_usercenter;
    }

    @Override
    protected void initView() {
        if (userCenterPresenter == null) {
            userCenterPresenter = new UserCenterPresenter(mContext, this);
        }
        if (hasLogin()) {
            user = userCenterPresenter.getUserInfoFormCache(PrefUtils.getString(mContext, GlobalConf.PHONE, ""));
            tvName.setText(user.getUsername());
            tvJob.setText(TextUtils.isEmpty(user.getJob()) ? "我的职位" : user.getJob());
            Glide.with(this)
                    .load(GlobalConf.BASE_PIC_URL + user.getHeadPic())
                    .placeholder(R.drawable.pic_default_user_head)
                    .error(R.drawable.pic_default_user_head)
                    .bitmapTransform(new CenterCrop(mContext), new CropCircleTransformation(mContext))
                    .crossFade()
                    .into(ivHead);
        }
    }

    @Override
    public void initData() {
        rlUserProfile.setOnClickListener(this);
        rlContributions.setOnClickListener(this);
        rlSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_user_profile:
                if (!hasLogin()) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                    return;
                }
                startActivity(new Intent(mContext, UserProfileActivity.class));
                break;
            case R.id.rl_contributions:
                if (!hasLogin()) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                    return;
                }
                startActivity(new Intent(mContext, ArticlePublishActivity.class));
                break;
            case R.id.rl_settings:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
        }
    }

    private boolean hasLogin() {
        String accessToken = PrefUtils.getString(mContext, GlobalConf.ACCESS_TOKEN, "");
        return !TextUtils.isEmpty(accessToken);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void handleEvent(UserInfoUpdate event) {
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
