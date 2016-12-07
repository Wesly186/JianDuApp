package com.mialab.jiandu.presenter;

import android.content.Context;

import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.entity.Rank;
import com.mialab.jiandu.model.RankModel;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;
import com.mialab.jiandu.view.fragment.RankView;

import java.util.List;

/**
 * Created by Wesly186 on 2016/12/7.
 */
public class RankPresenter extends BasePresenter {

    private Context mContext;
    private RankView rankView;
    private RankModel rankModel;

    public RankPresenter(Context mContext, RankView rankView) {
        this.mContext = mContext;
        this.rankView = rankView;
        rankModel = new RankModel();
    }

    public void getRankByReads() {
        rankModel.setRankByReadsSubscriber(
                new HttpSubscriber<List<Rank>>() {
                    @Override
                    public void onSuccess(BaseModel<List<Rank>> response) {
                        rankView.loadSuccess(response.getData());
                    }

                    @Override
                    public void onFailure(String message) {
                        rankView.loadFailure(message);
                    }

                    @Override
                    public void onBadNetwork() {
                        rankView.onBadNetWork();
                    }
                }
        );
        addSubscription(rankModel.getRankByReads(mContext));
    }

    public void getRankByContributions() {
        rankModel.setRankByContributionsSubscriber(new HttpSubscriber<List<Rank>>() {
            @Override
            public void onSuccess(BaseModel<List<Rank>> response) {
                rankView.loadSuccess(response.getData());
            }

            @Override
            public void onFailure(String message) {
                rankView.loadFailure(message);
            }

            @Override
            public void onBadNetwork() {
                rankView.onBadNetWork();
            }
        });
        addSubscription(rankModel.getRankByContributions(mContext));
    }
}
