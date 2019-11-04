package com.nova_smartlock.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdminDataGetDetailsResponse {
    @SerializedName("response")
    @Expose
    public AdminGetDetailsItems response;
}
