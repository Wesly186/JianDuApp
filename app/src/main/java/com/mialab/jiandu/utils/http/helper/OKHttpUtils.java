package com.mialab.jiandu.utils.http.helper;


import com.mialab.jiandu.conf.GlobalConf;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Wesly186 on 2016/8/18.
 */
public class OKHttpUtils {

    public static OkHttpClient getClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        HttpUrl httpUrl = request.url().newBuilder()
                                .build();
                        request = request.newBuilder().url(httpUrl).build();
                        return chain.proceed(request);
                    }
                })
                .connectTimeout(GlobalConf.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(GlobalConf.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(GlobalConf.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        return okHttpClient;
    }
}
