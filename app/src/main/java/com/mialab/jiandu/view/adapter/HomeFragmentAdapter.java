package com.mialab.jiandu.view.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mialab.jiandu.R;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.User;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * Created by Wesly186 on 2016/11/19.
 */

public class HomeFragmentAdapter extends BaseQuickAdapter<Article, BaseViewHolder> {

    public HomeFragmentAdapter(int layoutResId, List<Article> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Article article) {
        User writer = article.getWriter();
        baseViewHolder.setText(R.id.tv_writer, writer.getUsername())
                .setText(R.id.tv_breif_intro, article.getBriefIntro())
                .setText(R.id.tv_title, article.getTitle());

        Glide.with(mContext)
                .load(GlobalConf.BASE_PIC_URL + writer.getHeadPic())
                .placeholder(R.drawable.pic_default_user_head)
                .error(R.drawable.pic_default_user_head)
                .bitmapTransform(new CenterCrop(mContext), new RoundedCornersTransformation(mContext, 8, 0))
                .crossFade()
                .into((ImageView) baseViewHolder.getView(R.id.iv_head));

        Glide.with(mContext)
                .load(GlobalConf.BASE_PIC_URL + article.getPicUrl())
                .placeholder(R.drawable.image_article_default)
                .error(R.drawable.image_article_default)
                .centerCrop()
                .crossFade()
                .into((ImageView) baseViewHolder.getView(R.id.iv_article_pic));
    }
}
