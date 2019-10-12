package com.nova_smartlock.retrofit;

import com.nova_smartlock.model.AddLockResponse;
import com.nova_smartlock.model.CheckoutCheckResponse;
import com.nova_smartlock.model.KeyDetailsResponse;
import com.nova_smartlock.model.LoginResponse;
import com.nova_smartlock.model.UnlockKeyNameResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiServices {

    @FormUrlEncoded
    @POST("login-user")
    Call<LoginResponse> LOGIN_RESPONSE_OBSERVABLE(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("update-master-key-details")
    Call<UnlockKeyNameResponse> UNLOCK_KEY_NAME_RESPONSE_CALL(
            @Field("lockId") String lockId,
            @Field("lockAlis") String lockAlis
    );

    @FormUrlEncoded
    @POST("add-master-key")
    Call<AddLockResponse> ADD_LOCK_RESPONSE_CALL(
            @Field("room_id") String room_id,
            @Field("userType") String userType,
            @Field("keyStatus") String keyStatus,
            @Field("lockId") String lockId,
            @Field("keyId") String keyId,
            @Field("lockversion") String lockversion,
            @Field("lockname") String lockname,
            @Field("lockAlis") String lockAlis,
            @Field("lockMac") String lockMac,
            @Field("electricQuantity") String electricQuantity,
            @Field("lockFlagPos") String lockFlagPos,
            @Field("adminPwd") String adminPwd,
            @Field("lockkey") String lockkey,
            @Field("noKeyPwd") String noKeyPwd,
            @Field("deletePwd") String deletePwd,
            @Field("pwdInfo") String pwdInfo,
            @Field("timestamp") String timestamp,
            @Field("aesKeyStr") String aesKeyStr,
            @Field("startDate") String startDate,
            @Field("endDate") String endDate,
            @Field("specialValue") String specialValue,
            @Field("timezoneRawOffset") String timezoneRawOffset,
            @Field("keyRight") String keyRight,
            @Field("keyboardPwdVersion") String keyboardPwdVersion,
            @Field("remoteEnable") String remoteEnable,
            @Field("remarks") String remarks
    );

    @GET("key-details/{smo_id}")
    Call<KeyDetailsResponse> KEY_DETAILS_OBSERVABLE(
            @Path("smo_id") String order_id, @Header("Authorization") String authorization
    );

    @GET("check-is-guest-checkout/{order_id}/{guest_id}")
    Call<CheckoutCheckResponse> CHECKOUT_CHECK_RESPONSE_CALL(
            @Path("order_id") String order_id, @Path("guest_id") String guest_id,@Header("Authorization") String authorization
    );
}
