package com.smartlock.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetails {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("check_in")
    @Expose
    public String checkIn;
    @SerializedName("check_out")
    @Expose
    public String checkOut;
    @SerializedName("adults")
    @Expose
    public Integer adults;
    @SerializedName("kids")
    @Expose
    public Integer kids;
    @SerializedName("room_type_id")
    @Expose
    public String roomTypeId;
    @SerializedName("room_id")
    @Expose
    public Integer roomId;
    @SerializedName("rate_type")
    @Expose
    public Integer rateType;
    @SerializedName("ordered_on")
    @Expose
    public String orderedOn;
    @SerializedName("arrive_time")
    @Expose
    public String arriveTime;
    @SerializedName("departure_time")
    @Expose
    public String departureTime;
    @SerializedName("group_code")
    @Expose
    public String groupCode;
    @SerializedName("group_name")
    @Expose
    public String groupName;
    @SerializedName("room_no")
    @Expose
    public String roomNo;
    @SerializedName("short_code")
    @Expose
    public String shortCode;
    @SerializedName("hk_remark")
    @Expose
    public String hkRemark;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("shortcode")
    @Expose
    public String shortcode;

}
