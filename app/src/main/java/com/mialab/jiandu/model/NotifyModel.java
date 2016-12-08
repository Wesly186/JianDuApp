package com.mialab.jiandu.model;

import android.content.Context;

import com.mialab.jiandu.api.INotify;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.entity.Message;
import com.mialab.jiandu.utils.PrefUtils;
import com.mialab.jiandu.utils.http.helper.RetrofitHelper;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Wesly186 on 2016/12/8.
 */

public class NotifyModel {
    private HttpSubscriber<List<Message>> getMessagesSubscriber;

    public Subscription getMessages(Context context, int currentPage) {

        INotify iNotify = RetrofitHelper.getProxy(INotify.class, context);
        Observable<BaseModel<List<Message>>> observable = iNotify.getMessages(PrefUtils.getString(context, GlobalConf.ACCESS_TOKEN, ""), currentPage);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMessagesSubscriber);
        return subscription;
    }

    public void setGetMessagesSubscriber(HttpSubscriber<List<Message>> getMessagesSubscriber) {
        this.getMessagesSubscriber = getMessagesSubscriber;
    }
}
