package com.mialab.jiandu.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mialab.jiandu.R;
import com.mialab.jiandu.presenter.ArticlePublishPresenter;
import com.mialab.jiandu.utils.StatusBarUtil;
import com.mialab.jiandu.utils.ToastUtils;
import com.mialab.jiandu.view.base.MvpActivity;

import java.io.File;

import butterknife.BindView;

public class ArticlePublishActivity extends MvpActivity<ArticlePublishPresenter> implements ArticlePublishView, View.OnClickListener {

    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_description)
    EditText etDesc;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.btn_publish)
    Button btnPublish;

    public static final int REQ_SELECT_PHOTOS = 11;
    private File articlePic = null;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_article_publish;
    }

    @Override
    protected ArticlePublishPresenter initPresenter() {
        return new ArticlePublishPresenter(this, this);
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
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
    }

    @Override
    public void initData() {
        ivPic.setOnClickListener(this);
        btnPublish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.iv_pic:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQ_SELECT_PHOTOS);
                break;
            case R.id.btn_publish:
                mvpPresenter.publish(etTitle.getText().toString(), etDesc.getText().toString(), etAddress.getText().toString(), articlePic);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("登陆中...");
                progressDialog.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_SELECT_PHOTOS:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    articlePic = mvpPresenter.getAbsolutePath(uri);
                }
                break;
        }
        if (articlePic == null) {
            return;
        }
        Glide.with(this)
                .load(articlePic)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.image_article_default)
                .error(R.drawable.image_article_default)
                .centerCrop()
                .crossFade()
                .into(ivPic);
    }

    @Override
    public void success(String data) {
        progressDialog.dismiss();
        btnPublish.setEnabled(true);
        ToastUtils.showToast(this, "发布成功，请耐心等待审核。");
        finish();
    }

    @Override
    public void failure(String message) {
        progressDialog.dismiss();
        btnPublish.setEnabled(true);
        ToastUtils.showToast(this, message);
    }

    @Override
    public void badNetWork() {
        progressDialog.dismiss();
        btnPublish.setEnabled(true);
        ToastUtils.showToast(this, "网络错误，请检查网络");
    }

    @Override
    public void illegalInput(String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public void onRequestStart() {
        btnPublish.setEnabled(false);
    }
}
