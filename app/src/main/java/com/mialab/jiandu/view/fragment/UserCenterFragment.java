package com.mialab.jiandu.view.fragment;


import android.content.Context;
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
import com.mialab.jiandu.utils.AuthenticateUtils;
import com.mialab.jiandu.utils.PrefUtils;
import com.mialab.jiandu.view.activity.ArticlePublishActivity;
import com.mialab.jiandu.view.activity.CollectionsActivity;
import com.mialab.jiandu.view.activity.ContributionActivity;
import com.mialab.jiandu.view.activity.LoginActivity;
import com.mialab.jiandu.view.activity.SettingActivity;
import com.mialab.jiandu.view.activity.UserProfileActivity;
import com.mialab.jiandu.view.base.MvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.mialab.jiandu.utils.AuthenticateUtils.hasLogin;

/**
 * Created by Wesly186 on 2016/8/17.
 */
public class UserCenterFragment extends MvpFragment<UserCenterPresenter> implements UserCenterView, View.OnClickListener {

    @BindView(R.id.rl_user_profile)
    RelativeLayout rlUserProfile;
    @BindView(R.id.rl_collections)
    RelativeLayout rlCollections;
    @BindView(R.id.rl_reads)
    RelativeLayout rlReads;
    @BindView(R.id.tv_collection_num)
    TextView tvCollectionNum;
    @BindView(R.id.tv_read_num)
    TextView tvReadNum;
    @BindView(R.id.rl_publish_article)
    RelativeLayout rlPublishArticle;
    @BindView(R.id.rl_rank)
    RelativeLayout rlRank;
    @BindView(R.id.rl_settings)
    RelativeLayout rlSettings;

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_job)
    TextView tvJob;

    private User mUser;

    public static final int collection = 0;
    public static final int reads = 1;

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
    protected UserCenterPresenter initPresenter(Context context) {
        return new UserCenterPresenter(context, this);
    }

    @Override
    protected void initView() {
        if (AuthenticateUtils.hasLogin()) {
            mUser = mvpPresenter.getUserInfoFormCache(PrefUtils.getString(mContext, GlobalConf.PHONE, ""));
            tvName.setText(mUser.getUsername());
            tvJob.setText(TextUtils.isEmpty(mUser.getJob()) ? "我的职位" : mUser.getJob());
            Glide.with(this)
                    .load(GlobalConf.BASE_PIC_URL + mUser.getHeadPic())
                    .placeholder(R.drawable.pic_default_user_head)
                    .error(R.drawable.pic_default_user_head)
                    .bitmapTransform(new CenterCrop(mContext), new CropCircleTransformation(mContext))
                    .crossFade()
                    .into(ivHead);
            tvCollectionNum.setText(mUser.getCollectionNum() + "篇");
            tvReadNum.setText(mUser.getReadNum() + "篇");
        }
    }

    @Override
    public void initData() {
        rlUserProfile.setOnClickListener(this);
        rlCollections.setOnClickListener(this);
        rlReads.setOnClickListener(this);
        rlPublishArticle.setOnClickListener(this);
        rlRank.setOnClickListener(this);
        rlSettings.setOnClickListener(this);
        if (AuthenticateUtils.hasLogin()) {
            mvpPresenter.getUserInfo();
        }
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
            case R.id.rl_collections:
                if (!hasLogin()) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                    return;
                }
                Intent collectionIntent = new Intent(mContext, CollectionsActivity.class);
                collectionIntent.putExtra("type", UserCenterFragment.collection);
                startActivity(collectionIntent);
                break;
            case R.id.rl_reads:
                if (!hasLogin()) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                    return;
                }
                Intent readsIntent = new Intent(mContext, CollectionsActivity.class);
                readsIntent.putExtra("type", UserCenterFragment.reads);
                startActivity(readsIntent);
                break;
            case R.id.rl_publish_article:
                if (!hasLogin()) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                    return;
                }
                startActivity(new Intent(mContext, ArticlePublishActivity.class));
                break;
            case R.id.rl_rank:
                if (!hasLogin()) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                    return;
                }
                startActivity(new Intent(mContext, ContributionActivity.class));
                break;
            case R.id.rl_settings:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
        }
    }

    @Override
    public void getUserInfoSuccess(User data) {
        mUser = data;
        initView();
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
