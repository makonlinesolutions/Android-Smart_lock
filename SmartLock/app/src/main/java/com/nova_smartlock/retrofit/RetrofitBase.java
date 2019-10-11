package com.nova_smartlock.retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nova_smartlock.BuildConfig;
import com.nova_smartlock.utils.AppUtil;
import com.nova_smartlock.utils.ConfigUtils;
import com.nova_smartlock.utils.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBase {
    protected Retrofit retrofit;
    protected Context context;
    private Logger logger;


    public RetrofitBase(Context context, boolean addTimeout) {
        this.context = context;


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient().newBuilder().addInterceptor(interceptor);
        if (addTimeout) {
            httpClientBuilder.readTimeout(Constants.TimeOut.SOCKET_TIME_OUT, TimeUnit.SECONDS);
            httpClientBuilder.connectTimeout(Constants.TimeOut.CONNECTION_TIME_OUT, TimeUnit.SECONDS);
        } else {
        }
        addVersioningHeaders(httpClientBuilder, context);
        OkHttpClient httpClient = httpClientBuilder.build();

//        logger = new Logger(RetrofitBase.class.getSimpleName());

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(ConfigUtils.APPLICATION_BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }

    private void addVersioningHeaders(OkHttpClient.Builder builder, Context context) {
        final String appVersion = "v.1.0.1";
        final int version = AppUtil.getApplicationVersionCode(context);
            final String appName = "RetroKit";
        final String name = "RetroKit";
        builder.interceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader(appVersion, String.valueOf(version))
                        .addHeader(appName, name)
                        .build();
                return chain.proceed(request);
            }
        });
    }
}
