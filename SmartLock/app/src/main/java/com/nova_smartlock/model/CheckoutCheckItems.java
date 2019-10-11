package com.nova_smartlock.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckoutCheckItems {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("isCheckout")
    @Expose
    public Boolean isCheckout;
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
}
