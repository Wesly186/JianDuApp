package com.mialab.jiandu.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mialab.jiandu.R;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.presenter.SearchPresenter;
import com.mialab.jiandu.utils.StatusBarUtil;
import com.mialab.jiandu.utils.ToastUtils;
import com.mialab.jiandu.view.adapter.HotFragmentAdapter;
import com.mialab.jiandu.view.base.MvpActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchActivity extends MvpActivity<SearchPresenter> implements SearchView, View.OnClickListener {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.focus_view)
    View focusView;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_rank)
    RecyclerView mRecyclerView;

    private HotFragmentAdapter mAdapter;
    private List<Article> articles = new ArrayList<>();
    private Article clickArticle;

    private String keyword;
    private int currentPage = 0;

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
    protected SearchPresenter initPresenter() {
        return new SearchPresenter(this, this);
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
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.red);
        refreshLayout.setEnabled(false);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HotFragmentAdapter(R.layout.recycler_item_article_rank, articles);
        mAdapter.openLoadAnimation();

        View emptyView = LayoutInflater.from(this).inflate(R.layout.recycler_empty_search, mRecyclerView, false);
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
                        mvpPresenter.searchArticle(keyword, currentPage + 1);
                    }
                }, 800);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                clickArticle = articles.get(position);
                startActivity(new Intent(SearchActivity.this, ArticleDetailActivity.class));
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    refreshLayout.setRefreshing(true);
                    //隐藏软键盘和清空EditText
                    keyword = etSearch.getText().toString();
                    etSearch.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    focusView.requestFocus();
                    mvpPresenter.searchArticle(keyword, 0);
                    return true;
                }
                return false;
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
