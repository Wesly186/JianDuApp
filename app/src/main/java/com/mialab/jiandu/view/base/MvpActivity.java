package com.mialab.jiandu.view.base;

import android.os.Bundle;

import com.mialab.jiandu.presenter.BasePresenter;

/**
 * mvp
 */

public abstract class MvpActivity<P extends BasePresenter> extends BaseActivity {

    protected P mvpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpPresenter = initPresenter();
    }

    protected abstract P initPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mvpPresenter != null) {
            mvpPresenter.onDetachView();
        }
    }
}
