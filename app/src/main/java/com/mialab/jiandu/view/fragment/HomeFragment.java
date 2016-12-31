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

public class HomeFragment extends MvpFragment<HomeArticlePresenter> implements HomeView, View.OnClickListener {

    @BindView(R.id.recycler_home)
    RecyclerView mRecyclerView;//页面根布局
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout refreshLayout;//刷新后的布局情况
    @BindView(R.id.rl_bad_network)
    RelativeLayout rlBadNetwork;//无网络时显示的布局
    @BindView(R.id.btn_reload)
    Button btnReload;//按键：重新加载

    private HomeFragmentAdapter mAdapter;//适配器
    private List<Article> articles = new ArrayList<>();//存放文章
    private Article clickArticle;

    private int currentPage = 0;//首页为0号页面

    @Override
    protected int getContentViewId() {//抽象方法，返回内容视图ID
        return R.layout.fragment_home;
    }//获取文章布局的位置信息

    @Override
    protected HomeArticlePresenter initPresenter(Context context) {//显示首页文章
        return new HomeArticlePresenter(context, this);
    }

    @Override
    protected void initView() {//初始化视图页面
        refreshLayout.setRefreshing(true);//刷新
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.red);//刷新图像

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));//用recycleview的方法，流畅刷新
        mAdapter = new HomeFragmentAdapter(R.layout.recycler_item_article_home, articles);//获取文章信息
        mAdapter.openLoadAnimation();

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mvpPresenter.getArticleByTime(currentPage + 1);//给后面加载页面，返回首页做准备
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
                mvpPresenter.getArticleByTime(0);//获取第0页内容
            }
        });
        btnReload.setOnClickListener(this);
    }

    @Override
    public void loadSuccess(int currentPage, List<Article> articles) {
        rlBadNetwork.setVisibility(View.INVISIBLE);
        refreshLayout.setVisibility(View.VISIBLE);
        refreshLayout.setRefreshing(false);
        if (currentPage == 0) {//下拉
            this.articles.clear();
            this.articles.addAll(articles);
            this.currentPage = 0;
        } else {//上拉
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
        switch (v.getId()) {//获取点击的位置id
            case R.id.btn_reload://重载
                mvpPresenter.getArticleByTime(0);//默认加载第一页
                break;
        }
    }

    @Override
    public void onStop() {//跳转，不是onpause
        if (clickArticle != null) {
            if (((MainActivity) mContext).getCurrentSelect() == MainActivity.HOME_FRAGMENT) {//在第一个FRAGEMENT里
                EventBus.getDefault().post(clickArticle);//链接
            }
        }
        super.onStop();
    }
}