package com.mialab.jiandu.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mialab.jiandu.R;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.Message;
import com.mialab.jiandu.presenter.MessagePresenter;
import com.mialab.jiandu.utils.AuthenticateUtils;
import com.mialab.jiandu.utils.ToastUtils;
import com.mialab.jiandu.view.activity.ArticleDetailActivity;
import com.mialab.jiandu.view.activity.CommentsActivity;
import com.mialab.jiandu.view.adapter.MessageFragmentAdapter;
import com.mialab.jiandu.view.base.MvpFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by Wesly186 on 2016/8/17.
 */
public class MessageFragment extends MvpFragment<MessagePresenter> implements MessageView {

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_rank)
    RecyclerView mRecyclerView;

    private MessageFragmentAdapter mAdapter;
    private List<Message> messages = new ArrayList<>();
    private Message clickMessage;

    private int currentPage = 0;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_message;
    }

    @Override
    protected MessagePresenter initPresenter(Context context) {
        return new MessagePresenter(context, this);
    }

    @Override
    protected void initView() {
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.red);
        if (!AuthenticateUtils.hasLogin()) {
            refreshLayout.setEnabled(false);
        } else {
            refreshLayout.setRefreshing(true);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new MessageFragmentAdapter(R.layout.recycler_item_message, messages);
        mAdapter.openLoadAnimation();

        View emptyView = LayoutInflater.from(mContext).inflate(R.layout.recycler_empty_message, mRecyclerView, false);
        mAdapter.setEmptyView(emptyView);
    }

    @Override
    public void initData() {

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mvpPresenter.getMessages(currentPage + 1);
                    }
                }, 800);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                clickMessage = messages.get(position);
                if (clickMessage.getType() == 0) {
                    startActivity(new Intent(mContext, CommentsActivity.class));
                } else {
                    startActivity(new Intent(mContext, ArticleDetailActivity.class));
                }
            }
        });
        if (AuthenticateUtils.hasLogin()) {
            mvpPresenter.getMessages(0);
        }


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mvpPresenter.getMessages(0);
            }
        });
    }

    @Override
    public void loadSuccess(int currentPage, List<Message> articles) {
        refreshLayout.setRefreshing(false);
        if (currentPage == 0) {
            this.messages.clear();
            this.messages.addAll(articles);
            this.currentPage = 0;
        } else {
            this.currentPage++;
            this.messages.addAll(articles);
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
        ToastUtils.showToast(mContext, "网络异常");
    }

    @Override
    public void onStop() {
        if (clickMessage != null) {
            EventBus.getDefault().post(clickMessage);
        }
        super.onStop();
    }
}