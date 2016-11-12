package com.mialab.jiandu.utils.http.subscriber;

import android.content.Context;
import android.content.Intent;

import com.mialab.jiandu.app.JianDuApplication;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.utils.ToastUtils;
import com.mialab.jiandu.utils.http.exception.InvalidAccessTokenException;
import com.mialab.jiandu.view.activity.LoginActivity;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by Wesly186 on 2016/8/23.
 */
public abstract class HttpSubscriber<T> extends Subscriber<BaseModel<T>> {

    private Context mContext;

    public HttpSubscriber(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * onNext结束被调用
     */
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof IOException) {
            onBadNetwork();
        } else if (e instanceof HttpException) {
            int statusCode = ((HttpException) e).code();
            if (statusCode == 404) {
                onFailure("你访问的资源找不到了");
            } else {
                onFailure("服务器出错");
            }
        } else {
            onFailure(e.getMessage());
        }
    }

    @Override
    public void onNext(BaseModel<T> response) {
        if (response.getCode() == 200) {
            onSuccess(response);
        } else if (response.getCode() == 402) {
            onError(new InvalidAccessTokenException("accessToken过期"));
        } else if (response.getCode() == 403) {
            onRefreshTokenExpire();
        } else {
            onFailure(response.getMessage());
        }
    }

    /**
     * 业务成功，正确返回结果
     *
     * @param response
     */
    public abstract void onSuccess(BaseModel<T> response);

    /**
     * 网络连接正常，业务失败（code!=200||statusCode!=200）
     *
     * @param message
     */
    public abstract void onFailure(String message);

    /**
     * 网络连接失败，网络原因，如：连接超时，无网络等
     */
    public abstract void onBadNetwork();

    /**
     * refreshToken过期，跳转到登陆界面
     */
    private void onRefreshTokenExpire() {
        ToastUtils.showToast(JianDuApplication.getContext(), "登陆授权失效，请重新登陆");
        JianDuApplication.finishAll();
        mContext.startActivity(new Intent(mContext, LoginActivity.class));
    }
}
