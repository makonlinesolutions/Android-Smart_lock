package com.smartlock.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponseDetails {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("guest_id")
    @Expose
    public int guestId;
    @SerializedName("smo_id")
    @Expose
    public int smoId;
    @SerializedName("order_id")
    @Expose
    public int orderId;
    @SerializedName("Response")
    @Expose
    public String response;
    @SerializedName("statusCode")
    @Expose
    public int statusCode;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("token")
    @Expose
    public String token;
}
