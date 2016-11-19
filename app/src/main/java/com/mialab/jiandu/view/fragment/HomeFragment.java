package com.mialab.jiandu.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mialab.jiandu.R;
import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.User;
import com.mialab.jiandu.view.adapter.HomeFragmentAdapter;
import com.mialab.jiandu.view.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by Wesly186 on 2016/8/17.
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.recycler_home)
    RecyclerView mRecyclerView;

    private HomeFragmentAdapter mAdapter;
    private List<Article> articles = new ArrayList<>();

    private static final int PAGE_SIZE = 15;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new HomeFragmentAdapter(R.layout.recycler_item_article_home, articles);
        mAdapter.openLoadAnimation();

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(context, "" + Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void initData() {

        mRecyclerView.setAdapter(mAdapter);

        for (int i = 0; i < 15; i++) {
            articles.add(new Article(i, "JavaScript闯关记", "JavaScript闯关记", new User(), 265356436543646l));
        }

        mAdapter.notifyDataSetChanged();
    }
}