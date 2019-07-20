package com.smartlock.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KeyDetailsResponseData {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("Response")
    @Expose
    public List<KeyDetails> response = null;
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("message")
    @Expose
    public String message;
}
