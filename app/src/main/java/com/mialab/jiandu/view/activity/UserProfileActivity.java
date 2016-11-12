package com.mialab.jiandu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mialab.jiandu.R;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.User;
import com.mialab.jiandu.event.UserInfoUpdate;
import com.mialab.jiandu.presenter.UserProfilePresenter;
import com.mialab.jiandu.utils.EditDialogBuilder;
import com.mialab.jiandu.utils.ImageUtils;
import com.mialab.jiandu.utils.PrefUtils;
import com.mialab.jiandu.utils.StatusBarUtil;
import com.mialab.jiandu.utils.ToastUtils;
import com.mialab.jiandu.view.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class UserProfileActivity extends BaseActivity implements UserProfileView, View.OnClickListener {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    @BindView(R.id.rl_sex)
    RelativeLayout rlSex;
    @BindView(R.id.rl_job)
    RelativeLayout rlJob;
    @BindView(R.id.rl_username)
    RelativeLayout rlUsername;
    @BindView(R.id.rl_blog)
    RelativeLayout rlBlog;
    @BindView(R.id.rl_introduction)
    RelativeLayout rlIntroduction;

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_job)
    TextView tvJob;
    @BindView(R.id.tv_username)
    TextView tvName;
    @BindView(R.id.tv_blog)
    TextView tvBlog;
    @BindView(R.id.tv_intro)
    TextView tvIntro;

    public static final int REQ_THUMB = 10;

    private UserProfilePresenter userProfilePresenter;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userProfilePresenter = new UserProfilePresenter(this, this);
        initView();
        initData();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_profile;
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

        user = userProfilePresenter.getUserInfoFormCache(PrefUtils.getString(UserProfileActivity.this, GlobalConf.PHONE, ""));
        Glide.with(this)
                .load(GlobalConf.BASE_PIC_URL + user.getHeadPic())
                .placeholder(R.drawable.pic_default_user_head)
                .error(R.drawable.pic_default_user_head)
                .centerCrop()
                .bitmapTransform(new CropCircleTransformation(UserProfileActivity.this))
                .crossFade()
                .into(ivHead);
        tvSex.setText(user.getSex());
        tvJob.setText(TextUtils.isEmpty(user.getJob()) ? "如：工程师" : user.getJob());
        tvName.setText(user.getUsername());
        tvBlog.setText(TextUtils.isEmpty(user.getBlogAddress()) ? "你的博客" : user.getBlogAddress());
        tvIntro.setText(TextUtils.isEmpty(user.getIntroduction()) ? "这个人很懒，什么都没留下！" : user.getIntroduction());
    }

    @Override
    public void initData() {

        ibBack.setOnClickListener(this);
        rlHead.setOnClickListener(this);
        rlSex.setOnClickListener(this);
        rlJob.setOnClickListener(this);
        rlUsername.setOnClickListener(this);
        rlBlog.setOnClickListener(this);
        rlIntroduction.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditDialogBuilder builder = new EditDialogBuilder(UserProfileActivity.this);
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.rl_head:
                View view = View.inflate(UserProfileActivity.this, R.layout.dialog_head, null);
                RelativeLayout rlCamera = (RelativeLayout) view.findViewById(R.id.rl_camera);
                RelativeLayout rlAlbum = (RelativeLayout) view.findViewById(R.id.rl_album);
                AlertDialog.Builder builderHead = new AlertDialog.Builder(UserProfileActivity.this);
                builderHead.setView(view);
                final AlertDialog dialogChoice = builderHead.create();

                rlCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //判断是否有相机应用
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, REQ_THUMB);
                        } else {
                            ToastUtils.showToast(UserProfileActivity.this, "无法启动相机");
                        }
                        dialogChoice.dismiss();
                    }
                });
                rlAlbum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogChoice.dismiss();
                    }
                });

                dialogChoice.show();
                break;
            case R.id.rl_sex:
                View dialogView = View.inflate(UserProfileActivity.this, R.layout.dialog_sex, null);
                RelativeLayout tvMale = (RelativeLayout) dialogView.findViewById(R.id.rl_male);
                RelativeLayout tvFemale = (RelativeLayout) dialogView.findViewById(R.id.rl_female);
                ImageView ivMale = (ImageView) dialogView.findViewById(R.id.iv_male);
                ImageView ivFemale = (ImageView) dialogView.findViewById(R.id.iv_female);
                if (user.getSex().equals("男")) {
                    ivMale.setVisibility(View.VISIBLE);
                } else {
                    ivFemale.setVisibility(View.VISIBLE);
                }
                AlertDialog.Builder builderSex = new AlertDialog.Builder(UserProfileActivity.this);
                builderSex.setView(dialogView);
                final AlertDialog dialog = builderSex.create();

                tvMale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvSex.setText("男");
                        userProfilePresenter.updateUserProfile(null, null, null, null, "男", null);
                        dialog.dismiss();
                    }
                });
                tvFemale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvSex.setText("女");
                        userProfilePresenter.updateUserProfile(null, null, null, null, "女", null);
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;
            case R.id.rl_job:
                builder.setDialogTitle("修改职位").setPositiveButtonListener(new EditDialogBuilder.PositiveButtonClickListener() {
                    @Override
                    public void onClick(String content) {
                        if (TextUtils.isEmpty(content.trim())) {
                            ToastUtils.showToast(UserProfileActivity.this, "职位不能为空");
                        } else {
                            tvJob.setText(content);
                            userProfilePresenter.updateUserProfile(null, null, null, null, null, content);
                        }
                    }
                }).build().show();
                break;
            case R.id.rl_username:
                builder.setDialogTitle("修改用户名").setPositiveButtonListener(new EditDialogBuilder.PositiveButtonClickListener() {
                    @Override
                    public void onClick(String content) {
                        if (TextUtils.isEmpty(content.trim())) {
                            ToastUtils.showToast(UserProfileActivity.this, "用户名不能为空");
                        } else {
                            tvName.setText(content);
                            userProfilePresenter.updateUserProfile(null, content, null, null, null, null);
                        }
                    }
                }).build().show();
                break;
            case R.id.rl_blog:
                builder.setDialogTitle("修改博客地址").setPositiveButtonListener(new EditDialogBuilder.PositiveButtonClickListener() {
                    @Override
                    public void onClick(String content) {
                        if (TextUtils.isEmpty(content.trim())) {
                            ToastUtils.showToast(UserProfileActivity.this, "博客地址不能为空");
                        } else {
                            tvBlog.setText(content);
                            userProfilePresenter.updateUserProfile(null, null, content, null, null, null);
                        }
                    }
                }).build().show();
                break;
            case R.id.rl_introduction:
                builder.setDialogTitle("修改个人介绍").setPositiveButtonListener(new EditDialogBuilder.PositiveButtonClickListener() {
                    @Override
                    public void onClick(String content) {
                        if (TextUtils.isEmpty(content.trim())) {
                            ToastUtils.showToast(UserProfileActivity.this, "个人介绍不能为空");
                        } else {
                            tvIntro.setText(content);
                            userProfilePresenter.updateUserProfile(null, null, null, content, null, null);
                        }
                    }
                }).build().show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_THUMB:
                if (resultCode != Activity.RESULT_OK) {
                    return;
                }
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                File headFile = null;
                try {
                    headFile = ImageUtils.saveImage(UserProfileActivity.this, imageBitmap, "JianDu", "head.png");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Glide.with(this)
                        .load(headFile)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .placeholder(R.drawable.pic_default_user_head)
                        .error(R.drawable.pic_default_user_head)
                        .centerCrop()
                        .bitmapTransform(new CropCircleTransformation(UserProfileActivity.this))
                        .crossFade()
                        .into(ivHead);
                userProfilePresenter.updateUserProfile(headFile, user.getUsername(), null, null, null, null);
                break;
        }
    }

    @Override
    public void updateSuccess(User user) {
        this.user = user;
        EventBus.getDefault().post(new UserInfoUpdate());
        ToastUtils.showToast(this, "更新成功");
    }

    @Override
    public void updateFailure(String message) {
        initView();
        ToastUtils.showToast(this, message);
    }

    @Override
    public void onBadNetWork() {
        initView();
        ToastUtils.showToast(this, "网络异常");
    }
}