package com.mialab.jiandu.utils.http.helper;

import android.content.Context;

import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.utils.http.proxy.ProxyHandler;

import java.lang.reflect.Proxy;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Wesly186 on 2016/8/17.
 */
public class RetrofitHelper {

    private static Retrofit getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalConf.BASE_URL)
                .client(OKHttpUtils.getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit;
    }


    public static <T> T create(Class<T> target) {
        return getRetrofit().create(target);
    }

    public static <T> T getProxy(Class<T> clazz, Context context) {
        T t = getRetrofit().create(clazz);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new ProxyHandler(t, context));
    }
}
