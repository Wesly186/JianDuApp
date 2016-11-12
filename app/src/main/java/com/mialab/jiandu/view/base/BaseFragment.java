package com.mialab.jiandu.view.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Wesly186 on 2016/8/16.
 */
public abstract class BaseFragment extends Fragment {

    protected Context context;
    protected View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentViewId(), container, false);
        ButterKnife.bind(this, mRootView);
        this.context = getActivity();
        initView();
        initData();
        return mRootView;
    }

    protected abstract int getContentViewId();

    protected void initView() {

    }

    public void initData() {

    }
}
