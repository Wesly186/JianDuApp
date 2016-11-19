package com.mialab.jiandu.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mialab.jiandu.entity.Article;

import java.util.List;


/**
 * Created by Wesly186 on 2016/11/19.
 */

public class HomeFragmentAdapter extends BaseQuickAdapter<Article, BaseViewHolder> {

    public HomeFragmentAdapter(int layoutResId, List<Article> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Article article) {

    }
}
