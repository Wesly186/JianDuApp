package com.mialab.jiandu.view.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mialab.jiandu.R;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.User;
import com.mialab.jiandu.utils.TimeUtils;

import java.util.Date;
import java.util.List;


/**
 * Created by Wesly186 on 2016/11/19.
 */

public class HotFragmentAdapter extends BaseQuickAdapter<Article, BaseViewHolder> {

    public HotFragmentAdapter(int layoutResId, List<Article> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Article article) {
        User writer = article.getWriter();
        baseViewHolder.setText(R.id.tv_author, writer.getUsername() + "  ·")
                .setText(R.id.tv_title, article.getTitle())
                .setText(R.id.tv_publishtime, TimeUtils.time2Now(new Date(article.getPublishTime())))
                .setText(R.id.tv_collection, article.getCollectionNum() + "人收藏  ·");

        Glide.with(mContext)
                .load(GlobalConf.BASE_PIC_URL + article.getPicUrl())
                .placeholder(R.drawable.image_article_default)
                .error(R.drawable.image_article_default)
                .centerCrop()
                .crossFade()
                .into((ImageView) baseViewHolder.getView(R.id.iv_article_pic));
    }
}
