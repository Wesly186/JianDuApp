package com.mialab.jiandu.view.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mialab.jiandu.R;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.Rank;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.mialab.jiandu.R.id.tv_rank;


/**
 * Created by Wesly186 on 2016/11/19.
 */

public class RankFragmentAdapter extends BaseQuickAdapter<Rank, BaseViewHolder> {

    public RankFragmentAdapter(int layoutResId, List<Rank> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Rank rank) {

        baseViewHolder.setText(tv_rank, rank.getRankNum() + "")
                .setText(R.id.tv_name, rank.getUsername())
                .setText(R.id.tv_job, rank.getJob())
                .setText(R.id.tv_desc, "阅读文章" + rank.getReadNum() + "篇，发布文章" + rank.getPublishNum() + "篇");

        Glide.with(mContext)
                .load(GlobalConf.BASE_PIC_URL + rank.getHeadPic())
                .placeholder(R.drawable.pic_default_user_head)
                .error(R.drawable.pic_default_user_head)
                .bitmapTransform(new CenterCrop(mContext), new CropCircleTransformation(mContext))
                .crossFade()
                .into((ImageView) baseViewHolder.getView(R.id.iv_head));
    }
}
