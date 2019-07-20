package com.smartlock.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KeyDetails {
    @SerializedName("master_id")
    @Expose
    public Integer masterId;
    @SerializedName("room_id")
    @Expose
    public Integer roomId;
    @SerializedName("room_no")
    @Expose
    public String roomNo;
    @SerializedName("room_type")
    @Expose
    public String roomType;
    @SerializedName("check_in_time")
    @Expose
    public String checkInTime;
    @SerializedName("check_out_time")
    @Expose
    public String checkOutTime;
    @SerializedName("number_of_persons")
    @Expose
    public Integer numberOfPersons;
    @SerializedName("userType")
    @Expose
    public String userType;
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
    public Integer id;
    @SerializedName("floor_id")
    @Expose
    public String floorId;
    @SerializedName("room_type_id")
    @Expose
    public Integer roomTypeId;
    @SerializedName("total_beds")
    @Expose
    public String totalBeds;
    @SerializedName("housekeeping_status")
    @Expose
    public Integer housekeepingStatus;
    @SerializedName("availability")
    @Expose
    public String availability;
    @SerializedName("fd_remark")
    @Expose
    public String fdRemark;
    @SerializedName("hk_remark")
    @Expose
    public String hkRemark;
    @SerializedName("assigned_to")
    @Expose
    public Integer assignedTo;
    @SerializedName("room_desc")
    @Expose
    public String roomDesc;
    @SerializedName("minimum_stay")
    @Expose
    public String minimumStay;
    @SerializedName("short_code")
    @Expose
    public String shortCode;
    @SerializedName("sort_key")
    @Expose
    public Integer sortKey;
    @SerializedName("bed_type_id")
    @Expose
    public Integer bedTypeId;
    @SerializedName("phone_extension")
    @Expose
    public String phoneExtension;
    @SerializedName("key_card_alias")
    @Expose
    public String keyCardAlias;
    @SerializedName("room_property")
    @Expose
    public String roomProperty;
    @SerializedName("count_paymaster_room_inventory")
    @Expose
    public Object countPaymasterRoomInventory;
    @SerializedName("room_as")
    @Expose
    public String roomAs;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("created_by")
    @Expose
    public String createdBy;
    @SerializedName("modified_by")
    @Expose
    public String modifiedBy;
    @SerializedName("status")
    @Expose
    public Integer status;
}
