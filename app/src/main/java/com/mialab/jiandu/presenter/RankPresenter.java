package com.mialab.jiandu.presenter;

import android.content.Context;

import com.mialab.jiandu.view.fragment.RankView;

/**
 * Created by Wesly186 on 2016/11/27.
 */

public class RankPresenter extends BasePresenter {

    private Context context;
    private RankView rankView;

    public RankPresenter(Context context, RankView rankView) {
        this.context = context;
        this.rankView = rankView;
    }
}
