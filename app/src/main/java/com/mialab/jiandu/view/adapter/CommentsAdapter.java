package com.mialab.jiandu.view.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mialab.jiandu.R;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.ArticleComment;
import com.mialab.jiandu.entity.User;
import com.mialab.jiandu.utils.TimeUtils;

import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * 评论
 */

public class CommentsAdapter extends BaseQuickAdapter<ArticleComment, BaseViewHolder> {

    public CommentsAdapter(int layoutResId, List<ArticleComment> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ArticleComment articleComment) {
        User user = articleComment.getUser();
        baseViewHolder.setText(R.id.tv_name, user.getUsername())
                .setText(R.id.tv_job, user.getJob())
                .setText(R.id.tv_publish_time, TimeUtils.time2Now(new Date(articleComment.getPublishTime())))
                .setText(R.id.tv_content, articleComment.getComment());
        Glide.with(mContext)
                .load(GlobalConf.BASE_PIC_URL + user.getHeadPic())
                .placeholder(R.drawable.pic_default_user_head)
                .error(R.drawable.pic_default_user_head)
                .bitmapTransform(new CenterCrop(mContext), new RoundedCornersTransformation(mContext, 8, 0))
                .crossFade()
                .into((ImageView) baseViewHolder.getView(R.id.iv_head));
    }
}
