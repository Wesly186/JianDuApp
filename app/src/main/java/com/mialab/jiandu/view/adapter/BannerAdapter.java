package com.mialab.jiandu.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mialab.jiandu.R;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.Banner;

import java.util.List;

/**
 * Created by Wesly186 on 2016/8/17.
 */
public class BannerAdapter extends PagerAdapter {

    private Context context;
    private List<Banner> banners;

    public BannerAdapter(Context context, List<Banner> banners) {
        this.context = context;
        this.banners = banners;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(context, R.layout.pager_item_banner, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_news_pic);
        Banner banner = banners.get(position % banners.size());
        //占位符和渐现动画
        Glide.with(context)
                .load(GlobalConf.BASE_PIC_URL + banner.getPicUrl())
                .placeholder(R.drawable.pic_holder_banner)
                .error(R.drawable.pic_holder_banner)
                .centerCrop()
                .crossFade()
                .into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}