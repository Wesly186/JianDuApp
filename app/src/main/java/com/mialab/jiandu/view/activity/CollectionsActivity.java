package com.mialab.jiandu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mialab.jiandu.R;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.presenter.CollectionsPresenter;
import com.mialab.jiandu.utils.StatusBarUtil;
import com.mialab.jiandu.utils.ToastUtils;
import com.mialab.jiandu.view.adapter.HotFragmentAdapter;
import com.mialab.jiandu.view.base.MvpActivity;
import com.mialab.jiandu.view.fragment.UserCenterFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CollectionsActivity extends MvpActivity<CollectionsPresenter> implements CollectionsView, View.OnClickListener {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_rank)
    RecyclerView mRecyclerView;

    private HotFragmentAdapter mAdapter;
    private List<Article> articles = new ArrayList<>();
    private Article clickArticle;

    private int currentPage = 0;

    private Intent mIntent;
    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_collections;
    }

    @Override
    protected CollectionsPresenter initPresenter() {
        mIntent = getIntent();
        mType = mIntent.getIntExtra("type", 0);
        return new CollectionsPresenter(this, this);
    }

    @Override
    protected void initView() {
        if (mType == UserCenterFragment.collection) {
            tvTitle.setText("我的收藏");
        } else {
            tvTitle.setText("我的阅历");
        }
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimaryDark), 0);
        refreshLayout.setRefreshing(true);
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.red);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HotFragmentAdapter(R.layout.recycler_item_article_rank, articles);
        mAdapter.openLoadAnimation();

        View emptyView = null;
        if (mType == UserCenterFragment.collection) {
            emptyView = LayoutInflater.from(this).inflate(R.layout.recycler_empty_collection, mRecyclerView, false);
        } else {
            emptyView = LayoutInflater.from(this).inflate(R.layout.recycler_empty_reads, mRecyclerView, false);
        }
        mAdapter.setEmptyView(emptyView);
    }

    @Override
    public void initData() {
        ibBack.setOnClickListener(this);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mType == UserCenterFragment.collection) {
                            mvpPresenter.getArticleCollection(currentPage + 1);
                        } else {
                            mvpPresenter.getArticleReads(currentPage + 1);
                        }
                    }
                }, 800);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                clickArticle = articles.get(position);
                startActivity(new Intent(CollectionsActivity.this, ArticleDetailActivity.class));
            }
        });

        if (mType == UserCenterFragment.collection) {
            mvpPresenter.getArticleCollection(0);
        } else {
            mvpPresenter.getArticleReads(0);
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mType == UserCenterFragment.collection) {
                    mvpPresenter.getArticleCollection(0);
                } else {
                    mvpPresenter.getArticleReads(0);
                }
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
        if (clickArticle != null) {
            EventBus.getDefault().post(clickArticle);
        }
        super.onStop();
    }
}
