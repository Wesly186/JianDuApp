package com.mialab.jiandu.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mialab.jiandu.R;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.presenter.HomeArticlePresenter;
import com.mialab.jiandu.utils.ToastUtils;
import com.mialab.jiandu.view.activity.ArticleDetailActivity;
import com.mialab.jiandu.view.activity.MainActivity;
import com.mialab.jiandu.view.adapter.HomeFragmentAdapter;
import com.mialab.jiandu.view.base.MvpFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by Wesly186 on 2016/8/17.
 */
public class HomeFragment extends MvpFragment<HomeArticlePresenter> implements HomeView, View.OnClickListener {

    @BindView(R.id.recycler_home)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.rl_bad_network)
    RelativeLayout rlBadNetwork;
    @BindView(R.id.btn_reload)
    Button btnReload;

    private HomeFragmentAdapter mAdapter;
    private List<Article> articles = new ArrayList<>();
    private Article clickArticle;

    private int currentPage = 0;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected HomeArticlePresenter initPresenter(Context context) {
        return new HomeArticlePresenter(context, this);
    }

    @Override
    protected void initView() {
        refreshLayout.setRefreshing(true);
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.red);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new HomeFragmentAdapter(R.layout.recycler_item_article_home, articles);
        mAdapter.openLoadAnimation();

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mvpPresenter.getArticleByTime(currentPage + 1);
                    }
                }, 800);
            }
        });
    }

    @Override
    public void initData() {

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                clickArticle = articles.get(position);
                startActivity(new Intent(mContext, ArticleDetailActivity.class));
            }
        });

        mvpPresenter.getArticleByTime(0);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mvpPresenter.getArticleByTime(0);
            }
        });
        btnReload.setOnClickListener(this);
    }

    @Override
    public void loadSuccess(int currentPage, List<Article> articles) {
        rlBadNetwork.setVisibility(View.INVISIBLE);
        refreshLayout.setVisibility(View.VISIBLE);
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
        ToastUtils.showToast(mContext, message);
    }

    @Override
    public void onBadNetWork() {
        mAdapter.loadMoreFail();
        refreshLayout.setRefreshing(false);
        rlBadNetwork.setVisibility(View.VISIBLE);
        refreshLayout.setVisibility(View.INVISIBLE);
        ToastUtils.showToast(mContext, "网络异常");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reload:
                mvpPresenter.getArticleByTime(0);
                break;
        }
    }

    @Override
    public void onStop() {
        if (clickArticle != null) {
            if (((MainActivity) mContext).getCurrentSelect() == MainActivity.HOME_FRAGMENT) {
                EventBus.getDefault().post(clickArticle);
            }
        }
        super.onStop();
    }
}