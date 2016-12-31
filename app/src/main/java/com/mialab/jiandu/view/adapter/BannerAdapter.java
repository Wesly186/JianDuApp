package com.mialab.jiandu.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mialab.jiandu.R;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.Article;

import java.util.List;

/**
 * 轮播图
 */
public class BannerAdapter extends PagerAdapter {

    private Context context;
    private List<Article> banners;

    private OnItemClickListener listener;

    public BannerAdapter(Context context, List<Article> banners) {
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
    public Object instantiateItem(ViewGroup container, final int position) {
        if (banners.size() == 0) return null;
        View view = View.inflate(context, R.layout.pager_item_banner, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_news_pic);
        Article banner = banners.get(position % banners.size());
        //占位符和渐现动画
        Glide.with(context)
                .load(GlobalConf.BASE_PIC_URL + banner.getPicUrl())
                .placeholder(R.drawable.pic_holder_banner)
                .error(R.drawable.pic_holder_banner)
                .centerCrop()
                .crossFade()
                .into(imageView);
        container.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onclick(position);
                }
            }
        });
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

    public interface OnItemClickListener {
        void onclick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
