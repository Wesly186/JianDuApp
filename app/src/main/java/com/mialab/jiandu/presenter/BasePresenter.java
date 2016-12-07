package com.mialab.jiandu.presenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Wesly186 on 2016/11/26.
 */

public class BasePresenter {

    private CompositeSubscription mCompositeSubscription;

    public void onDetachView() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscription(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }
}
