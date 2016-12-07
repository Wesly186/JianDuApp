package com.mialab.jiandu.view.fragment;

import com.mialab.jiandu.entity.Rank;

import java.util.List;

/**
 * Created by Wesly186 on 2016/12/7.
 */

public interface RankView {
    void loadSuccess(List<Rank> data);

    void loadFailure(String message);

    void onBadNetWork();
}
