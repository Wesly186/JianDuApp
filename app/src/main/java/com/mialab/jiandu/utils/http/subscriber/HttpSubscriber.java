package com.mialab.jiandu.utils.http.subscriber;

import android.util.Log;

import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.utils.http.exception.TokenInvalidException;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by Wesly186 on 2016/8/23.
 */
public abstract class HttpSubscriber<T> extends Subscriber<BaseModel<T>> {

    /**
     * onNext结束被调用
     */
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Log.e("HttpSubscriber:", e.getMessage());
        e.printStackTrace();
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
            if (e instanceof TokenInvalidException) {
                onFailure(e.getMessage());
            } else {
                onFailure("未知错误");
            }
        }
    }

    @Override
    public void onNext(BaseModel<T> response) {
        if (response.getCode() == 200) {
            onSuccess(response);
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

}
