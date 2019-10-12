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
    @SerializedName("checkInDate")
    @Expose
    public String checkInDate;
    @SerializedName("checkInTime")
    @Expose
    public String checkInTime;
    @SerializedName("checkOutDate")
    @Expose
    public String checkOutDate;
    @SerializedName("checkOutTime")
    @Expose
    public String checkOutTime;
    @SerializedName("orderStatus")
    @Expose
    public Integer orderStatus;
    @SerializedName("orderType")
    @Expose
    public String orderType;
    @SerializedName("groupName")
    @Expose
    public String groupName;
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("message")
    @Expose
    public String message;


}
