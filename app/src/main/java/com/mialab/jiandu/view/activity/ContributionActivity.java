package com.mialab.jiandu.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.mialab.jiandu.R;
import com.mialab.jiandu.utils.StatusBarUtil;
import com.mialab.jiandu.view.adapter.NotifyFragmentAdapter;
import com.mialab.jiandu.view.base.BaseActivity;
import com.mialab.jiandu.view.fragment.RankFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ContributionActivity extends BaseActivity implements ContributionView, View.OnClickListener {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tl_title)
    TabLayout tlTitle;
    @BindView(R.id.pager)
    ViewPager viewPager;

    private FragmentPagerAdapter fAdapter;
    private List<Fragment> fragments;
    private List<String> titles;

    private RankFragment readFragment;
    private RankFragment contributionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_contribution;
    }

    @Override
    protected void initView() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimaryDark), 0);
        //初始化各fragment
        readFragment = new RankFragment(RankFragment.FACTOR_READS);
        contributionFragment = new RankFragment(RankFragment.FACTOR_CONTRIBUTION);
        //将fragment装进列表中
        fragments = new ArrayList<Fragment>();
        fragments.add(readFragment);
        fragments.add(contributionFragment);
        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        titles = new ArrayList<>();
        titles.add("阅读排名");
        titles.add("贡献排名");
        //设置TabLayout的模式，MODE_SCROLLABLE，tab较多时使用
        tlTitle.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        //也可以在xml中设置tab：<android.support.design.widget.TabItem android:text="@string/tab_text"/>
        tlTitle.addTab(tlTitle.newTab().setText(titles.get(0)));
        tlTitle.addTab(tlTitle.newTab().setText(titles.get(1)));
        //viewpager加载adapter
        fAdapter = new NotifyFragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(fAdapter);
        //TabLayout加载viewpager
        tlTitle.setupWithViewPager(viewPager);
    }

    @Override
    public void initData() {
        ibBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
        }
    }
}
