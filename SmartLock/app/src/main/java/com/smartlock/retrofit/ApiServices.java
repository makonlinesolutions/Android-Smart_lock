package com.smartlock.retrofit;

import com.smartlock.model.KeyDetailsResponse;
import com.smartlock.model.LoginResponse;

import java.util.Observable;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServices {

    @FormUrlEncoded
    @POST("login-user")
    Call<LoginResponse> LOGIN_RESPONSE_OBSERVABLE(
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("key-details/{order_id}")
    Call<KeyDetailsResponse> KEY_DETAILS_OBSERVABLE(
            @Path("order_id") String order_id
    );
}
