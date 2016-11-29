package com.mialab.jiandu.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mialab.jiandu.R;
import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.Banner;
import com.mialab.jiandu.entity.User;
import com.mialab.jiandu.presenter.RankPresenter;
import com.mialab.jiandu.view.activity.ContributionActivity;
import com.mialab.jiandu.view.activity.DiscussActivity;
import com.mialab.jiandu.view.activity.SearchActivity;
import com.mialab.jiandu.view.activity.WeekHotActivity;
import com.mialab.jiandu.view.adapter.BannerAdapter;
import com.mialab.jiandu.view.adapter.RankFragmentAdapter;
import com.mialab.jiandu.view.base.MvpFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RankFragment extends MvpFragment<RankPresenter> implements RankView, View.OnClickListener {

    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_rank)
    RecyclerView mRecyclerView;
    ViewPager vpBanner;
    LinearLayout llDiscuss;
    LinearLayout llContribution;
    LinearLayout llWeekHot;

    private BannerAdapter bannerAdapter;
    private RankFragmentAdapter mAdapter;
    private List<Banner> banners = new ArrayList<>();
    private List<Article> articles = new ArrayList<>();

    private static final int PAGE_SIZE = 15;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentItem = vpBanner.getCurrentItem() + 1;
            vpBanner.setCurrentItem(currentItem);
            handler.sendEmptyMessageDelayed(0, 4000);
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_rank;
    }

    @Override
    protected RankPresenter initPresenter(Context context) {
        return new RankPresenter(context, this);
    }

    @Override
    protected void initView() {
        //refreshLayout.setRefreshing(true);
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.red);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new RankFragmentAdapter(R.layout.recycler_item_article_rank, articles);
        mAdapter.openLoadAnimation();

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(mContext, "" + Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });

        View headView = LayoutInflater.from(mContext).inflate(R.layout.recycler_header_rank, mRecyclerView, false);
        mAdapter.addHeaderView(headView);

        vpBanner = (ViewPager) headView.findViewById(R.id.vp_banner);
        llDiscuss = (LinearLayout) headView.findViewById(R.id.ll_discuss);
        llContribution = (LinearLayout) headView.findViewById(R.id.ll_contribution);
        llWeekHot = (LinearLayout) headView.findViewById(R.id.ll_week_hot);
        bannerAdapter = new BannerAdapter(mContext, banners);
    }

    @Override
    public void initData() {

        rlSearch.setOnClickListener(this);
        llDiscuss.setOnClickListener(this);
        llContribution.setOnClickListener(this);
        llWeekHot.setOnClickListener(this);

        vpBanner.setAdapter(bannerAdapter);
        mRecyclerView.setAdapter(mAdapter);

        for (int i = 0; i < 40; i++) {
            articles.add(new Article(i, "JavaScript闯关记", "JavaScript闯关记", new User(), 265356436543646l));
        }

        banners.add(new Banner(1, "JavaScript闯关记", new User(), 265356436543646l, "120495-11f3030ace9594ae.jpg", "http://dev.qq.com/topic/57bec216d81f2415515d3e9c"));
        banners.add(new Banner(2, "JavaScript闯关记", new User(), 265356436543646l, "120495-97b57d0cabc2a9ff.jpg", "http://dev.qq.com/topic/57bec216d81f2415515d3e9c"));
        banners.add(new Banner(3, "JavaScript闯关记", new User(), 265356436543646l, "120495-2977b17c9a0708a0.png", "http://dev.qq.com/topic/57bec216d81f2415515d3e9c"));

        bannerAdapter.notifyDataSetChanged();
        mAdapter.notifyDataSetChanged();
        handler.sendEmptyMessageDelayed(0, 4000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_search:
                startActivity(new Intent(mContext, SearchActivity.class));
                break;
            case R.id.ll_discuss:
                startActivity(new Intent(mContext, DiscussActivity.class));
                break;
            case R.id.ll_contribution:
                startActivity(new Intent(mContext, ContributionActivity.class));
                break;
            case R.id.ll_week_hot:
                startActivity(new Intent(mContext, WeekHotActivity.class));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
