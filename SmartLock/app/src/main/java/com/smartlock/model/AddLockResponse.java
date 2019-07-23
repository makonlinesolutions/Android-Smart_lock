package com.smartlock.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddLockResponse {

    @SerializedName("response")
    @Expose
    public AddLockResponseItem response;

}
