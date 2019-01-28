package com.ffm.network;

import android.util.Log;


import com.ffm.BuildConfig;
import com.ffm.preference.AppPreference;
import com.ffm.util.StringUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ffm.preference.AppPrefConstants.ACCESS_TOKEN;
import static com.ffm.preference.AppPrefConstants.SESSION_ID;


/**
 * Created by Raviteja on 19-07-2017 for HugFit
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class RetrofitService {
    private static final String BASE_URL = BuildConfig.WEB_SERVICE_URL;
    private static final long CONNECT_TIMEOUT = 5000;
    private static final long WRITE_TIMEOUT = 5000;
    private static final long READ_TIMEOUT = 5000;

    public static Retrofit getRestService(final boolean session, final String... accept) {
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        b.readTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
        b.writeTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        OkHttpClient okHttpClient = b.addInterceptor(logging).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                String sessionId = AppPreference.getInstance().getString(SESSION_ID);
                if (BuildConfig.DEBUG) {
                    if (session && StringUtil.isEmpty(sessionId)) {
                        Log.i("OkHttp", "-----Session not found-----");
                    } else if (session) {
                        Log.i("OkHttp", sessionId);
                    }
                }
                Request.Builder builder = original.newBuilder()
                        .header("Accept", accept.length == 0 ? "application/json" : accept[0])
                        .header("User-Agent", "android")
                        .header("PID", "hugfit")
                        .header("version-name", String.valueOf(BuildConfig.VERSION_NAME))
                        .header("version-code", String.valueOf(BuildConfig.VERSION_CODE));
                if (sessionId != null && session) {
                    builder.header(ACCESS_TOKEN, sessionId);
                }
                builder.method(original.method(), original.body());
                Request request = builder.build();
                return chain.proceed(request);
            }
        }).build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonUtil.getGson()))
                .client(okHttpClient)
                .build();
    }
}