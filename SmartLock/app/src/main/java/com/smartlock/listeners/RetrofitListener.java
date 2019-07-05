package com.smartlock.listeners;


import com.smartlock.utils.ErrorObject;

public interface RetrofitListener {
    void onResponseSuccess(Object responseBody, int apiFlag);

    void onResponseError(ErrorObject errorObject, Throwable throwable, int apiFlag);

}
