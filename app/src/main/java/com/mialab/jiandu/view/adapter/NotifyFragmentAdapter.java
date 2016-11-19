package com.mialab.jiandu.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Wesly186 on 2016/11/15.
 */

public class NotifyFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;                         //fragment列表
    private List<String> titles;                              //tab名的列表

    public NotifyFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titles) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position % titles.size());
    }

}
