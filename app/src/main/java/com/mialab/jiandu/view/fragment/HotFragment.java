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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mialab.jiandu.R;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.presenter.HotPresenter;
import com.mialab.jiandu.utils.DensityUtils;
import com.mialab.jiandu.utils.ToastUtils;
import com.mialab.jiandu.view.activity.ArticleDetailActivity;
import com.mialab.jiandu.view.activity.ContributionActivity;
import com.mialab.jiandu.view.activity.MainActivity;
import com.mialab.jiandu.view.activity.SearchActivity;
import com.mialab.jiandu.view.activity.WeekHotActivity;
import com.mialab.jiandu.view.adapter.BannerAdapter;
import com.mialab.jiandu.view.adapter.HotFragmentAdapter;
import com.mialab.jiandu.view.base.MvpFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HotFragment extends MvpFragment<HotPresenter> implements HotView, View.OnClickListener {

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

    LinearLayout llDot;

    private BannerAdapter bannerAdapter;
    private HotFragmentAdapter mAdapter;
    private List<Article> mBanners = new ArrayList<>();
    private List<Article> mArticles = new ArrayList<>();

    private Article clickArticle;

    private int mCurrentPage = 0;

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
        return R.layout.fragment_hot;
    }

    @Override
    protected HotPresenter initPresenter(Context context) {
        return new HotPresenter(context, this);
    }

    @Override
    protected void initView() {
        refreshLayout.setRefreshing(true);
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.red);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new HotFragmentAdapter(R.layout.recycler_item_article_rank, mArticles);
        mAdapter.openLoadAnimation();

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mvpPresenter.getArticleSynthetically(mCurrentPage + 1);
                    }
                }, 800);
            }
        });

        View headView = LayoutInflater.from(mContext).inflate(R.layout.recycler_header_hot, mRecyclerView, false);
        mAdapter.addHeaderView(headView);

        vpBanner = (ViewPager) headView.findViewById(R.id.vp_banner);
        llDiscuss = (LinearLayout) headView.findViewById(R.id.ll_discuss);
        llContribution = (LinearLayout) headView.findViewById(R.id.ll_contribution);
        llWeekHot = (LinearLayout) headView.findViewById(R.id.ll_week_hot);
        llDot = (LinearLayout) headView.findViewById(R.id.ll_dot);
        bannerAdapter = new BannerAdapter(mContext, mBanners);
    }

    @Override
    public void initData() {

        rlSearch.setOnClickListener(this);
        llDiscuss.setOnClickListener(this);
        llContribution.setOnClickListener(this);
        llWeekHot.setOnClickListener(this);

        vpBanner.setAdapter(bannerAdapter);
        mRecyclerView.setAdapter(mAdapter);
        vpBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateDot();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bannerAdapter.setOnItemClickListener(new BannerAdapter.OnItemClickListener() {
            @Override
            public void onclick(int position) {
                clickArticle = mBanners.get(position % mBanners.size());
                startActivity(new Intent(mContext, ArticleDetailActivity.class));
            }
        });
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                clickArticle = mArticles.get(position);
                startActivity(new Intent(mContext, ArticleDetailActivity.class));
            }
        });

        mvpPresenter.getArticleSynthetically(0);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mvpPresenter.getBanners();
                mvpPresenter.getArticleSynthetically(0);
            }
        });


        mvpPresenter.getBanners();

        handler.sendEmptyMessageDelayed(0, 4000);
    }

    /**
     * 初始化dot
     */
    private void initDots() {
        llDot.removeAllViews();
        for (int i = 0; i < mBanners.size(); i++) {
            View view = new View(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtils.dp2px(mContext, 7), DensityUtils.dp2px(mContext, 7));
            if (i != 0) {
                params.leftMargin = DensityUtils.dp2px(mContext, 7);
            }
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.dot_bg_selector);
            llDot.addView(view);
        }
    }

    private void updateDot() {
        if (mBanners.size() == 0) return;
        int currentPage = vpBanner.getCurrentItem() % mBanners.size();
        for (int i = 0; i < llDot.getChildCount(); i++) {
            if (i == currentPage) {
                llDot.getChildAt(i).setEnabled(true);
            } else {
                llDot.getChildAt(i).setEnabled(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_search:
                startActivity(new Intent(mContext, SearchActivity.class));
                break;
            case R.id.ll_discuss:
                ToastUtils.showToast(mContext, "开发中，尽请期待！");
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
    public void loadSuccess(int currentPage, List<Article> articles) {
        refreshLayout.setRefreshing(false);
        if (currentPage == 0) {
            this.mArticles.clear();
            this.mArticles.addAll(articles);
            this.mCurrentPage = 0;
        } else {
            this.mCurrentPage++;
            this.mArticles.addAll(articles);
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
    public void loadBannerSuccess(List<Article> data) {
        mBanners.clear();
        mBanners.addAll(data);
        bannerAdapter.notifyDataSetChanged();
        initDots();
        updateDot();
    }

    @Override
    public void onStop() {
        if (clickArticle != null) {
            if (((MainActivity) mContext).getCurrentSelect() == MainActivity.RANK_FRAGMENT) {
                EventBus.getDefault().post(clickArticle);
            }
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
