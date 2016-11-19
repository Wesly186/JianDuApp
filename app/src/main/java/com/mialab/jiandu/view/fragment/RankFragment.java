package com.mialab.jiandu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mialab.jiandu.R;
import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.User;
import com.mialab.jiandu.view.activity.SearchActivity;
import com.mialab.jiandu.view.adapter.RankFragmentAdapter;
import com.mialab.jiandu.view.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RankFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;
    @BindView(R.id.recycler_rank)
    RecyclerView mRecyclerView;

    private RankFragmentAdapter mAdapter;
    private List<Article> articles = new ArrayList<>();

    private static final int PAGE_SIZE = 15;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_rank;
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new RankFragmentAdapter(R.layout.recycler_item_article_rank, articles);
        mAdapter.openLoadAnimation();

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(context, "" + Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });

        View headView = LayoutInflater.from(context).inflate(R.layout.recycler_header_rank, mRecyclerView, false);
        mAdapter.addHeaderView(headView);
    }

    @Override
    public void initData() {
        rlSearch.setOnClickListener(this);

        mRecyclerView.setAdapter(mAdapter);

        for (int i = 0; i < 40; i++) {
            articles.add(new Article(i, "JavaScript闯关记", "JavaScript闯关记", new User(), 265356436543646l));
        }

        mAdapter.notifyDataSetChanged();
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
