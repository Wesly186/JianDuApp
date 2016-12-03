package com.mialab.jiandu.utils.http.proxy;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.mialab.jiandu.api.Iuser;
import com.mialab.jiandu.app.JianDuApplication;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.entity.OauthToken;
import com.mialab.jiandu.utils.PrefUtils;
import com.mialab.jiandu.utils.http.exception.TokenInvalidException;
import com.mialab.jiandu.utils.http.helper.RetrofitHelper;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;
import com.mialab.jiandu.view.activity.LoginActivity;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.http.Field;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Wesly186 on 2016/8/27.
 */
public class ProxyHandler implements InvocationHandler {

    private final static String ACCESS_TOKEN = "accessToken";

    private Throwable mRefreshTokenError;
    private boolean mIsTokenNeedRefresh;

    private Object mTarget;
    private Context mContext;

    public ProxyHandler(Object proxyObject, Context context) {
        mTarget = proxyObject;
        mContext = context;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        return Observable.just(null).flatMap(new Func1<Object, Observable<?>>() {
            @Override
            public Observable<?> call(Object o) {
                try {
                    try {
                        if (mIsTokenNeedRefresh) {
                            updateMethodToken(method, args);
                        }
                        return (Observable<?>) method.invoke(mTarget, args);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Throwable> observable) {
                return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(Throwable throwable) {
                        if (throwable instanceof HttpException) {
                            if (((HttpException) throwable).code() == 401) {
                                return refreshTokenWhenTokenInvalid();
                            } else {
                                return Observable.error(throwable);
                            }
                        } else {
                            return Observable.error(throwable);
                        }
                    }
                });
            }
        });
    }

    /**
     * Refresh the token when the current token is invalid.
     *
     * @return Observable
     */
    private Observable<?> refreshTokenWhenTokenInvalid() {
        synchronized (ProxyHandler.class) {
            mIsTokenNeedRefresh = true;
            Iuser iUser = RetrofitHelper.getProxy(Iuser.class, mContext);
            Observable<BaseModel<OauthToken>> observable = iUser.getAccessTokenByRefresh(PrefUtils.getString(
                    JianDuApplication.getContext(), GlobalConf.REFRESH_TOKEN, ""));
            observable.subscribe(new HttpSubscriber<OauthToken>() {

                @Override
                public void onSuccess(BaseModel<OauthToken> response) {
                    PrefUtils.setString(JianDuApplication.getContext(), GlobalConf.ACCESS_TOKEN, response.getData().getAccessToken());
                }

                @Override
                public void onFailure(String message) {
                    onRefreshTokenExpire();
                    mRefreshTokenError = new TokenInvalidException(message);
                }

                @Override
                public void onBadNetwork() {
                    mRefreshTokenError = new Throwable("网络故障");
                }
            });
            if (mRefreshTokenError != null) {
                return Observable.error(mRefreshTokenError);
            } else {
                return Observable.just(true);
            }
        }
    }


    /**
     * 更新AccessToken参数
     */

    private void updateMethodToken(Method method, Object[] args) {
        if (mIsTokenNeedRefresh && !TextUtils.isEmpty(PrefUtils.getString(JianDuApplication.getContext(), GlobalConf.ACCESS_TOKEN, ""))) {
            Annotation[][] annotationsArray = method.getParameterAnnotations();
            Annotation[] annotations;
            if (annotationsArray != null && annotationsArray.length > 0) {
                for (int i = 0; i < annotationsArray.length; i++) {
                    annotations = annotationsArray[i];
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof Query) {
                            if (ACCESS_TOKEN.equals(((Query) annotation).value())) {
                                args[i] = PrefUtils.getString(JianDuApplication.getContext(), GlobalConf.ACCESS_TOKEN, "");
                            }
                        }
                        if (annotation instanceof Part) {
                            if (ACCESS_TOKEN.equals(((Part) annotation).value())) {
                                args[i] = RequestBody.create(MediaType.parse("multipart/form-data"), PrefUtils.getString(mContext, GlobalConf.ACCESS_TOKEN, ""));
                            }
                        }
                        if (annotation instanceof Field) {
                            if (ACCESS_TOKEN.equals(((Field) annotation).value())) {
                                args[i] = PrefUtils.getString(JianDuApplication.getContext(), GlobalConf.ACCESS_TOKEN, "");
                            }
                        }
                    }
                }
            }
            mIsTokenNeedRefresh = false;
        }
    }

    /**
     * refreshToken过期，跳转到登陆界面
     */
    private void onRefreshTokenExpire() {
        mContext.startActivity(new Intent(mContext, LoginActivity.class));
    }
}