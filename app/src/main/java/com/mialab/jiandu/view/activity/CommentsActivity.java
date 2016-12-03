package com.mialab.jiandu.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mialab.jiandu.R;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.ArticleComment;
import com.mialab.jiandu.entity.User;
import com.mialab.jiandu.presenter.CommentPresenter;
import com.mialab.jiandu.utils.AuthenticateUtils;
import com.mialab.jiandu.utils.StatusBarUtil;
import com.mialab.jiandu.utils.ToastUtils;
import com.mialab.jiandu.view.adapter.CommentsAdapter;
import com.mialab.jiandu.view.base.MvpActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class CommentsActivity extends MvpActivity<CommentPresenter> implements CommentView, View.OnClickListener {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_comments)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.btn_send_comment)
    Button btnSendComment;

    View headView;
    ImageView ivNewsPic;
    TextView tvNewsTitle;
    TextView tvBriefIntro;
    TextView tvTime;
    TextView tvCollectNum;

    private CommentsAdapter mAdapter;
    private Article mArticle;
    private List<ArticleComment> mArticleComments = new ArrayList<ArticleComment>();

    private int mCurrentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    @Override
    protected CommentPresenter initPresenter() {
        return new CommentPresenter(this, this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_comments;
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

        refreshLayout.setRefreshing(true);
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.red);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommentsAdapter(R.layout.recycler_item_comment, mArticleComments);
        mAdapter.openLoadAnimation();

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mvpPresenter.getComments(mArticle.getId(), mCurrentPage + 1);
                    }
                }, 800);
            }
        });

        headView = LayoutInflater.from(this).inflate(R.layout.recycler_header_comments, mRecyclerView, false);
        mAdapter.addHeaderView(headView);

        ivNewsPic = (ImageView) headView.findViewById(R.id.iv_news_pic);
        tvNewsTitle = (TextView) headView.findViewById(R.id.tv_title);
        tvBriefIntro = (TextView) headView.findViewById(R.id.tv_brief_intro);
        tvTime = (TextView) headView.findViewById(R.id.tv_time);
        tvCollectNum = (TextView) headView.findViewById(R.id.tv_collect_num);

    }

    @Override
    public void initData() {
        mRecyclerView.setAdapter(mAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mvpPresenter.getComments(mArticle.getId(), 0);
            }
        });

        ibBack.setOnClickListener(this);
        btnSendComment.setOnClickListener(this);
    }

    @Override
    public void getCommentsSuccess(int currentPage, List<ArticleComment> articleComments) {
        refreshLayout.setRefreshing(false);
        if (currentPage == 0) {
            mArticleComments.clear();
            mArticleComments.addAll(articleComments);
            mCurrentPage = 0;
        } else {
            mCurrentPage++;
            mArticleComments.addAll(articleComments);
        }
        if (articleComments.size() < GlobalConf.PAGE_SIZE) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getCommentsFailure(String message) {
        refreshLayout.setRefreshing(false);
        mAdapter.loadMoreFail();
        ToastUtils.showToast(this, message);
    }

    @Override
    public void onBadNetwork() {
        mAdapter.loadMoreFail();
        refreshLayout.setRefreshing(false);
        ToastUtils.showToast(this, "网络异常");
    }

    @Override
    public void doCommentSuccess() {
        ToastUtils.showToast(this, "评论成功");
    }

    @Override
    public void doCommentFailure(String message) {
        mArticleComments.remove(mArticleComments.size() - 1);
        mAdapter.notifyDataSetChanged();
        ToastUtils.showToast(this, message);
    }

    @Override
    public void commentBadNetwork() {
        mArticleComments.remove(mArticleComments.size() - 1);
        mAdapter.notifyDataSetChanged();
        ToastUtils.showToast(this, "网络异常");
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void handleEvent(Article article) {
        mArticle = article;
        Glide.with(this)
                .load(GlobalConf.BASE_PIC_URL + article.getPicUrl())
                .placeholder(R.drawable.pic_holder_banner)
                .error(R.drawable.pic_holder_banner)
                .crossFade()
                .into(ivNewsPic);
        tvNewsTitle.setText(article.getTitle());
        tvBriefIntro.setText(article.getBriefIntro());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String lastEditTime = format.format(new Date(article.getPublishTime()));
        tvTime.setText(lastEditTime.substring(0, 10));
        tvCollectNum.setText(article.getCollectionNum() + "");

        mvpPresenter.getComments(mArticle.getId(), 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_comment:
                if (!AuthenticateUtils.hasLogin()) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                User user = mvpPresenter.getUserFromCache();
                ArticleComment comment = new ArticleComment(mArticle.getId(), new Date().getTime(), etComment.getText().toString(), user);
                mArticleComments.add(comment);
                mAdapter.notifyDataSetChanged();
                mvpPresenter.doComment(mArticle.getId(), etComment.getText().toString());
                //隐藏软键盘和清空EditText
                etComment.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etComment.getWindowToken(), 0);
                break;
            case R.id.ib_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
