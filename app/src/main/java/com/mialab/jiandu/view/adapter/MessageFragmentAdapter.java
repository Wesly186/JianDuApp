package com.mialab.jiandu.view.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mialab.jiandu.R;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.Message;
import com.mialab.jiandu.entity.User;
import com.mialab.jiandu.utils.TimeUtils;

import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * Created by Wesly186 on 2016/11/19.
 */

public class MessageFragmentAdapter extends BaseQuickAdapter<Message, BaseViewHolder> {

    public MessageFragmentAdapter(int layoutResId, List<Message> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Message message) {
        int type = message.getType();
        User user = message.getUser();
        baseViewHolder.setText(R.id.tv_user, user.getUsername())
                .setText(R.id.tv_title, message.getTitle())
                .setText(R.id.tv_message, type == 0 ? "评论了你的文章" : "收藏了你的文章")
                .setText(R.id.tv_time, TimeUtils.time2Now(new Date(message.getTime())));

        Glide.with(mContext)
                .load(GlobalConf.BASE_PIC_URL + message.getPicUrl())
                .placeholder(R.drawable.image_article_default)
                .error(R.drawable.image_article_default)
                .centerCrop()
                .crossFade()
                .into((ImageView) baseViewHolder.getView(R.id.iv_article_pic));
        Glide.with(mContext)
                .load(GlobalConf.BASE_PIC_URL + user.getHeadPic())
                .placeholder(R.drawable.pic_default_user_head)
                .error(R.drawable.pic_default_user_head)
                .bitmapTransform(new CenterCrop(mContext), new RoundedCornersTransformation(mContext, 8, 0))
                .crossFade()
                .into((ImageView) baseViewHolder.getView(R.id.iv_head));
    }
}
