package com.nova_smartlock.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdminGetDetailsItems {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("details")
    @Expose
    public AdminDetails details;
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
}
