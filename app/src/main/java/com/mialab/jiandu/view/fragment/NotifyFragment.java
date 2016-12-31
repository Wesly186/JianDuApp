package com.mialab.jiandu.view.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.mialab.jiandu.R;
import com.mialab.jiandu.view.adapter.NotifyFragmentAdapter;
import com.mialab.jiandu.view.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NotifyFragment extends BaseFragment {

    @BindView(R.id.tl_title)
    TabLayout tlTitle;
    @BindView(R.id.pager)
    ViewPager viewPager;

    private FragmentPagerAdapter fAdapter;
    private List<Fragment> fragments;
    private List<String> titles;

    private MessageFragment messageFragment;
    private ActivityFragment activityFragment;//动态页，未实现

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_notify;
    }

    @Override
    protected void initView() {
        //初始化各fragment
        messageFragment = new MessageFragment();
        activityFragment = new ActivityFragment();
        //将fragment装进列表中
        fragments = new ArrayList<Fragment>();
        fragments.add(messageFragment);
        fragments.add(activityFragment);
        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        titles = new ArrayList<>();
        titles.add("消息");
        titles.add("动态");
        //设置TabLayout的模式，MODE_SCROLLABLE，tab较多时使用
        tlTitle.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        //也可以在xml中设置tab：<android.support.design.widget.TabItem android:text="@string/tab_text"/>
        tlTitle.addTab(tlTitle.newTab().setText(titles.get(0)));
        tlTitle.addTab(tlTitle.newTab().setText(titles.get(1)));
        //viewpager加载adapter
        fAdapter = new NotifyFragmentAdapter(getActivity().getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(fAdapter);
        //TabLayout加载viewpager
        tlTitle.setupWithViewPager(viewPager);
    }
}
