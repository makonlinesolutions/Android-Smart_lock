package com.nova_smartlock.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LockTimeUpdateItems {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("Response")
    @Expose
    public String response;
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;

}
