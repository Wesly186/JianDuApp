package com.mialab.jiandu.view.fragment;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mialab.jiandu.R;
import com.mialab.jiandu.entity.Rank;
import com.mialab.jiandu.presenter.RankPresenter;
import com.mialab.jiandu.utils.ToastUtils;
import com.mialab.jiandu.view.adapter.RankFragmentAdapter;
import com.mialab.jiandu.view.base.MvpFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

//所有排序界面

public class RankFragment extends MvpFragment<RankPresenter> implements RankView {

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_rank)
    RecyclerView mRecyclerView;

    private RankFragmentAdapter mAdapter;
    private List<Rank> ranks = new ArrayList<>();
    private Rank clickRank;

    private int rankFactor;
    public static final int FACTOR_READS = 0;
    public static final int FACTOR_CONTRIBUTION = 1;

    @Override
    protected RankPresenter initPresenter(Context context) {
        return new RankPresenter(context, this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_rank;
    }

    @Override
    protected void initView() {
        refreshLayout.setRefreshing(true);
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.red);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new RankFragmentAdapter(R.layout.recycler_item_rank, ranks);
        mAdapter.openLoadAnimation();

        View footerView = LayoutInflater.from(mContext).inflate(R.layout.recycler_footer_rank, mRecyclerView, false);
        mAdapter.addFooterView(footerView);
    }

    @Override
    public void initData() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                clickRank = ranks.get(position);
            }
        });

        if (rankFactor == RankFragment.FACTOR_READS) {
            mvpPresenter.getRankByReads();
        } else if (rankFactor == RankFragment.FACTOR_CONTRIBUTION) {
            mvpPresenter.getRankByContributions();
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (rankFactor == RankFragment.FACTOR_READS) {
                    mvpPresenter.getRankByReads();
                } else if (rankFactor == RankFragment.FACTOR_CONTRIBUTION) {
                    mvpPresenter.getRankByContributions();
                }
            }
        });
    }

    @Override
    public void loadSuccess(List<Rank> articles) {
        refreshLayout.setRefreshing(false);
        this.ranks.clear();
        this.ranks.addAll(articles);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadFailure(String message) {
        refreshLayout.setRefreshing(false);
        ToastUtils.showToast(mContext, message);
    }

    @Override
    public void onBadNetWork() {
        refreshLayout.setRefreshing(false);
        ToastUtils.showToast(mContext, "网络异常");
    }

    @Override
    public void onStop() {
        if (clickRank != null) {
            EventBus.getDefault().post(clickRank);
        }
        super.onStop();
    }

    public void setRankFactor(int rankFactor) {
        this.rankFactor = rankFactor;
    }
}
