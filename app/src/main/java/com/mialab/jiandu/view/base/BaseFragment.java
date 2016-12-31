package com.mialab.jiandu.view.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * 基础
 */
public abstract class BaseFragment extends Fragment {

    protected Context mContext;//内容
    protected View mRootView;//根布局

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,//fragment的形成
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentViewId(), container, false);
        ButterKnife.bind(this, mRootView);
        this.mContext = getActivity();
        initView();//布局
        initData();//数据
        return mRootView;
    }

    protected abstract int getContentViewId();

    protected void initView() {

    }

    public void initData() {

    }
}
