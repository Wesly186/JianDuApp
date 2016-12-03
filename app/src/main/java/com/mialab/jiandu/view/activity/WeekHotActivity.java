package com.mialab.jiandu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mialab.jiandu.R;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.presenter.WeekHotPresenter;
import com.mialab.jiandu.utils.StatusBarUtil;
import com.mialab.jiandu.utils.ToastUtils;
import com.mialab.jiandu.view.adapter.RankFragmentAdapter;
import com.mialab.jiandu.view.base.MvpActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WeekHotActivity extends MvpActivity<WeekHotPresenter> implements WeekHotView, View.OnClickListener {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_rank)
    RecyclerView mRecyclerView;

    private RankFragmentAdapter mAdapter;
    private List<Article> articles = new ArrayList<>();

    private int clickPosition = 0;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_week_hot;
    }

    @Override
    protected WeekHotPresenter initPresenter() {
        return new WeekHotPresenter(this, this);
    }

    @Override
    protected void initView() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimaryDark), 0);
        refreshLayout.setRefreshing(true);
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.red);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RankFragmentAdapter(R.layout.recycler_item_article_rank, articles);
        mAdapter.openLoadAnimation();

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mvpPresenter.getArticleWeekHot(currentPage + 1);
                    }
                }, 800);
            }
        });
    }

    @Override
    public void initData() {
        ibBack.setOnClickListener(this);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                clickPosition = position;
                startActivity(new Intent(WeekHotActivity.this, ArticleDetailActivity.class));
            }
        });

        mvpPresenter.getArticleWeekHot(0);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mvpPresenter.getArticleWeekHot(0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
        }
    }

    @Override
    public void loadSuccess(int currentPage, List<Article> articles) {
        refreshLayout.setRefreshing(false);
        if (currentPage == 0) {
            this.articles.clear();
            this.articles.addAll(articles);
            this.currentPage = 0;
        } else {
            this.currentPage++;
            this.articles.addAll(articles);
        }
        if (articles.size() < GlobalConf.PAGE_SIZE) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadFailure(String message) {
        refreshLayout.setRefreshing(false);
        mAdapter.loadMoreFail();
        ToastUtils.showToast(this, message);
    }

    @Override
    public void onBadNetWork() {
        mAdapter.loadMoreFail();
        refreshLayout.setRefreshing(false);
        ToastUtils.showToast(this, "网络异常");
    }

    @Override
    public void onStop() {
        if (clickPosition < articles.size()) {
            EventBus.getDefault().post(articles.get(clickPosition));
        }
        super.onStop();
    }
}
