package com.nova_smartlock.listeners;


import com.nova_smartlock.utils.ErrorObject;

public interface RetrofitListener {
    void onResponseSuccess(Object responseBody, int apiFlag);

    void onResponseError(ErrorObject errorObject, Throwable throwable, int apiFlag);

}
