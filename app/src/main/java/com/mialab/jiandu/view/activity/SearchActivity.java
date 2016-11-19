package com.mialab.jiandu.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mialab.jiandu.R;
import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.User;
import com.mialab.jiandu.utils.StatusBarUtil;
import com.mialab.jiandu.view.adapter.RankFragmentAdapter;
import com.mialab.jiandu.view.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.recycler_rank)
    RecyclerView mRecyclerView;

    private RankFragmentAdapter mAdapter;
    private List<Article> articles = new ArrayList<>();

    private static final int PAGE_SIZE = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        if (android.os.Build.BRAND.equals("Xiaomi")) {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
            StatusBarUtil.setMiuiStatusBarDarkMode(this, true);
        } else if (android.os.Build.BRAND.equals("Meizu")) {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
            StatusBarUtil.setMeizuStatusBarDarkIcon(this, true);
        } else {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.statusbar_light), 0);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RankFragmentAdapter(R.layout.recycler_item_article_rank, articles);
        mAdapter.openLoadAnimation();

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(SearchActivity.this, "" + Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void initData() {
        mRecyclerView.setAdapter(mAdapter);

        for (int i = 0; i < 40; i++) {
            articles.add(new Article(i, "JavaScript闯关记", "JavaScript闯关记", new User(), 265356436543646l));
        }

        mAdapter.notifyDataSetChanged();
    }

}
