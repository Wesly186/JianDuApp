package com.mialab.jiandu.view.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.mialab.jiandu.R;
import com.mialab.jiandu.view.activity.SearchActivity;
import com.mialab.jiandu.view.base.BaseFragment;

import butterknife.BindView;

public class RankFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_rank;
    }

    @Override
    public void initData() {
        rlSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_search:
                startActivity(new Intent(context, SearchActivity.class));
                break;
        }
    }


}
