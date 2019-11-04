package com.nova_smartlock.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdminDetails {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("user_name")
    @Expose
    public String userName;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("lock_id")
    @Expose
    public String lockId;
    @SerializedName("secret_key")
    @Expose
    public String secretKey;
}
