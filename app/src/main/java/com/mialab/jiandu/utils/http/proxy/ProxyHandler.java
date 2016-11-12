package com.mialab.jiandu.utils.http.proxy;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.mialab.jiandu.api.Iuser;
import com.mialab.jiandu.app.JianDuApplication;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.entity.OauthToken;
import com.mialab.jiandu.utils.PrefUtils;
import com.mialab.jiandu.utils.http.exception.InvalidAccessTokenException;
import com.mialab.jiandu.utils.http.helper.RetrofitHelper;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import retrofit2.http.Query;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Wesly186 on 2016/8/27.
 */
public class ProxyHandler implements InvocationHandler {

    private final static String ACCESS_TOKEN = "accessToken";

    private boolean mRefreshTokenError = true;
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
                        if (throwable instanceof InvalidAccessTokenException) {
                            return refreshTokenWhenTokenInvalid();
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
            Iuser iUser = RetrofitHelper.create(Iuser.class);
            Observable<BaseModel<OauthToken>> observable = iUser.getAccessTokenByRefresh(PrefUtils.getString(
                    JianDuApplication.getContext(), "refresh_token", ""));
            observable.subscribe(new HttpSubscriber<OauthToken>(mContext) {
                @Override
                public void onSuccess(BaseModel<OauthToken> response) {
                    mRefreshTokenError = false;
                    PrefUtils.setString(JianDuApplication.getContext(), "access_token", response.getData().getAccessToken());
                }

                @Override
                public void onFailure(String message) {
                    mRefreshTokenError = true;
                }

                @Override
                public void onBadNetwork() {
                    mRefreshTokenError = true;
                }
            });

            if (mRefreshTokenError) {
                return Observable.error(new Throwable());
            } else {
                return Observable.just(true);
            }
        }
    }


    /**
     * 更新AccessToken参数
     */

    private void updateMethodToken(Method method, Object[] args) {
        Log.e("ProxyHandler:", "updateMethodToken");
        if (mIsTokenNeedRefresh && !TextUtils.isEmpty(PrefUtils.getString(JianDuApplication.getContext(), "accessToken", ""))) {
            Annotation[][] annotationsArray = method.getParameterAnnotations();
            Annotation[] annotations;
            if (annotationsArray != null && annotationsArray.length > 0) {
                for (int i = 0; i < annotationsArray.length; i++) {
                    annotations = annotationsArray[i];
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof Query) {
                            if (ACCESS_TOKEN.equals(((Query) annotation).value())) {
                                args[i] = PrefUtils.getString(JianDuApplication.getContext(), "accessToken", "");
                            }
                        }
                    }
                }
            }
            mIsTokenNeedRefresh = false;
        }
    }
}