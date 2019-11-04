package com.nova_smartlock.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TermsConditionItems {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message_body")
    @Expose
    public String messageBody;
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
}
