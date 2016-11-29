package com.mialab.jiandu.view.activity;

import android.os.Bundle;

import com.mialab.jiandu.R;
import com.mialab.jiandu.presenter.ArticlePublishPresenter;
import com.mialab.jiandu.utils.StatusBarUtil;
import com.mialab.jiandu.view.base.MvpActivity;

public class ArticlePublishActivity extends MvpActivity<ArticlePublishPresenter> implements ArticlePublishView {

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
    }

    @Override
    public void initData() {

    }
}
