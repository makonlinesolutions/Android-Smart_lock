package com.smartlock.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KeyDetails {

    @SerializedName("master_id")
    @Expose
    public int masterId;
    @SerializedName("room_id")
    @Expose
    public int roomId;
    @SerializedName("userType")
    @Expose
    public String userType;
    @SerializedName("lockId")
    @Expose
    public String lockId;
    @SerializedName("keyId")
    @Expose
    public String keyId;
    @SerializedName("lockversion")
    @Expose
    public String lockversion;
    @SerializedName("lockname")
    @Expose
    public String lockname;
    @SerializedName("lockAlis")
    @Expose
    public String lockAlis;
    @SerializedName("lockMac")
    @Expose
    public String lockMac;
    @SerializedName("electricQuantity")
    @Expose
    public String electricQuantity;
    @SerializedName("lockFlagPos")
    @Expose
    public String lockFlagPos;
    @SerializedName("adminPwd")
    @Expose
    public String adminPwd;
    @SerializedName("lockkey")
    @Expose
    public String lockkey;
    @SerializedName("noKeyPwd")
    @Expose
    public String noKeyPwd;
    @SerializedName("deletePwd")
    @Expose
    public String deletePwd;
    @SerializedName("pwdInfo")
    @Expose
    public String pwdInfo;
    @SerializedName("timestamp")
    @Expose
    public String timestamp;
    @SerializedName("aesKeyStr")
    @Expose
    public String aesKeyStr;
    @SerializedName("startDate")
    @Expose
    public String startDate;
    @SerializedName("endDate")
    @Expose
    public String endDate;
    @SerializedName("specialValue")
    @Expose
    public String specialValue;
    @SerializedName("timezoneRawOffset")
    @Expose
    public String timezoneRawOffset;
    @SerializedName("keyRight")
    @Expose
    public String keyRight;
    @SerializedName("keyboardPwdVersion")
    @Expose
    public String keyboardPwdVersion;
    @SerializedName("remoteEnable")
    @Expose
    public String remoteEnable;
    @SerializedName("remarks")
    @Expose
    public String remarks;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("floor_id")
    @Expose
    public Object floorId;
    @SerializedName("room_no")
    @Expose
    public String roomNo;
    @SerializedName("room_type_id")
    @Expose
    public int roomTypeId;
    @SerializedName("total_beds")
    @Expose
    public Object totalBeds;
    @SerializedName("housekeeping_status")
    @Expose
    public int housekeepingStatus;
    @SerializedName("availability")
    @Expose
    public Object availability;
    @SerializedName("fd_remark")
    @Expose
    public Object fdRemark;
    @SerializedName("hk_remark")
    @Expose
    public String hkRemark;
    @SerializedName("assigned_to")
    @Expose
    public Object assignedTo;
    @SerializedName("room_desc")
    @Expose
    public Object roomDesc;
    @SerializedName("minimum_stay")
    @Expose
    public Object minimumStay;
    @SerializedName("short_code")
    @Expose
    public String shortCode;
    @SerializedName("sort_key")
    @Expose
    public Object sortKey;
    @SerializedName("bed_type_id")
    @Expose
    public Object bedTypeId;
    @SerializedName("phone_extension")
    @Expose
    public String phoneExtension;
    @SerializedName("key_card_alias")
    @Expose
    public String keyCardAlias;
    @SerializedName("room_property")
    @Expose
    public Object roomProperty;
    @SerializedName("count_paymaster_room_inventory")
    @Expose
    public Object countPaymasterRoomInventory;
    @SerializedName("room_as")
    @Expose
    public Object roomAs;
    @SerializedName("image")
    @Expose
    public Object image;
    @SerializedName("created_by")
    @Expose
    public String createdBy;
    @SerializedName("modified_by")
    @Expose
    public String modifiedBy;
    @SerializedName("status")
    @Expose
    public int status;
}
