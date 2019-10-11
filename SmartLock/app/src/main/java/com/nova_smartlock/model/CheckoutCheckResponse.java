package com.nova_smartlock.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckoutCheckResponse {

    @SerializedName("response")
    @Expose
    public CheckoutCheckItems response;
}
