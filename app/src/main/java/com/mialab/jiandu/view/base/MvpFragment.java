package com.mialab.jiandu.view.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mialab.jiandu.presenter.BasePresenter;

/**
 * Created by Wesly186 on 2016/11/26.
 */

public abstract class MvpFragment<P extends BasePresenter> extends BaseFragment {

    protected P mvpPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mvpPresenter = initPresenter(getActivity());
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    protected abstract P initPresenter(Context context);

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mvpPresenter != null) {
            mvpPresenter.onDetachView();
        }
    }
}
