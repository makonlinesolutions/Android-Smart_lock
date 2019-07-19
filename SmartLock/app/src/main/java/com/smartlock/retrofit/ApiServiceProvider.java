package com.smartlock.retrofit;

import android.content.Context;

public class ApiServiceProvider extends RetrofitBase {
    private static ApiServiceProvider apiServiceProvider;
    public ApiServices apiServices;

    public ApiServiceProvider(Context context) {
        super(context, true);
        apiServices = retrofit.create(ApiServices.class);
    }

    public static ApiServiceProvider getInstance(Context context) {
        if (apiServiceProvider == null) {
            apiServiceProvider = new ApiServiceProvider(context);
        }
        return apiServiceProvider;
    }
}
