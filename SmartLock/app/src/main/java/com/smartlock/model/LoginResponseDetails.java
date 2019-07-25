package com.smartlock.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponseDetails {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("guest_id")//smo_id
    @Expose
    public String user_id;
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("smo_id")//
    @Expose
    public String smo_id;
}
