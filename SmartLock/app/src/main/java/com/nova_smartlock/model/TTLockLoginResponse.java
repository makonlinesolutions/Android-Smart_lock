package com.nova_smartlock.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TTLockLoginResponse {
    @SerializedName("access_token")
    @Expose
    public String accessToken;
    @SerializedName("uid")
    @Expose
    public Integer uid;
    @SerializedName("refresh_token")
    @Expose
    public String refreshToken;
    @SerializedName("openid")
    @Expose
    public Integer openid;
    @SerializedName("scope")
    @Expose
    public String scope;
    @SerializedName("token_type")
    @Expose
    public String tokenType;
    @SerializedName("expires_in")
    @Expose
    public Integer expiresIn;
}
