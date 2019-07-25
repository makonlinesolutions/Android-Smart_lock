package com.smartlock.db;

public class LockDetails {
    public static final String TABLE_NAME = "lock";
    public static final String master_id = "master_id";
    public static final String room_id = "room_id";
    public static final String userType = "userType";
    public static final String keyId = "keyId";
    public static final String lockversion = "lockversion";
    public static final String lockname = "lockname";
    public static final String lockAlis = "lockAlis";
    public static final String lockMac = "lockMac";
    public static final String electricQuantity = "electricQuantity";
    public static final String lockFlagPos = "lockFlagPos";
    public static final String adminPwd = "adminPwd";
    public static final String lockkey = "lockkey";
    public static final String noKeyPwd = "noKeyPwd";
    public static final String deletePwd = "deletePwd";
    public static final String pwdInfo = "pwdInfo";
    public static final String timestamp = "timestamp";
    public static final String aesKeyStr = "aesKeyStr";
    public static final String startDate = "startDate";
    public static final String endDate = "endDate";
    public static final String specialValue = "specialValue";
    public static final String timezoneRawOffset = "timezoneRawOffset";
    public static final String keyRight = "keyRight";
    public static final String keyboardPwdVersion = "keyboardPwdVersion";
    public static final String remoteEnable = "remoteEnable";
    public static final String remarks = "remarks";
    public static final String id = "id";
    public static final String floor_id = "floor_id";
    public static final String room_no = "room_no";
    public static final String room_type_id = "room_type_id";
    public static final String total_beds = "total_beds";
    public static final String housekeeping_status = "housekeeping_status";
    public static final String availability = "availability";
    public static final String fd_remark = "fd_remark";
    public static final String hk_remark = "hk_remark";
    public static final String assigned_to = "assigned_to";
    public static final String room_desc = "room_desc";
    public static final String minimum_stay = "minimum_stay";
    public static final String short_code = "short_code";
    public static final String sort_key = "sort_key";
    public static final String bed_type_id = "bed_type_id";
    public static final String phone_extension = "phone_extension";
    public static final String key_card_alias = "key_card_alias";
    public static final String room_property = "room_property";
    public static final String count_paymaster_room_inventory = "count_paymaster_room_inventory";
    public static final String room_as = "room_as";
    public static final String image = "image";
    public static final String created_by = "created_by";
    public static final String modified_by = "modified_by";
    public static final String status = "status";


    private int masterId;
    private int roomId;
    private String userType_value;
    private String keyId_value;
    private String lockversion_value;
    private String lockname_value;
    private String lockAlis_value;
    private String lockMac_value;
    private String electricQuantity_value;
    private String lockFlagPos_value;
    private String adminPwd_value;
    private String lockkey_value;
    private String noKeyPwd_value;
    private String deletePwd_value;
    private String pwdInfo_value;
    private String timestamp_value;
    private String aesKeyStr_value;
    private String startDate_value;
    private String endDate_value;
    private String specialValue_value;
    private String timezoneRawOffset_value;
    private String keyRight_value;
    private String keyboardPwdVersion_value;
    private String remoteEnable_value;
    private String remarks_value;
    private int id_value;
    private Object floorId;
    private String roomNo;
    private int roomTypeId;
    private Object totalBeds;
    private int housekeepingStatus;
    private Object availability_value;
    private Object fdRemark;
    private String hkRemark;
    private Object assignedTo;
    private Object roomDesc;
    private Object minimumStay;
    private String shortCode;
    private Object sortKey;
    private Object bedTypeId;
    private String phoneExtension;
    private String keyCardAlias;
    private Object roomProperty;
    private Object countPaymasterRoomInventory;
    private Object roomAs;
    private Object image_value;
    private String createdBy;
    private String modifiedBy;
    private int status_value;


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + master_id + " INTEGER ,"
                    + room_id + " INTEGER ,"
                    + userType + " TEXT ,"
                    + keyId + " TEXT ,"
                    + lockversion + " TEXT ,"
                    + lockname + " TEXT ,"
                    + lockAlis + " TEXT ,"
                    + lockMac + " TEXT ,"
                    + electricQuantity + " TEXT ,"
                    + lockFlagPos + " TEXT ,"
                    + adminPwd + " TEXT ,"
                    + lockkey + " TEXT ,"
                    + noKeyPwd + " TEXT ,"
                    + deletePwd + " TEXT ,"
                    + pwdInfo + " TEXT ,"
                    + timestamp + " TEXT ,"
                    + aesKeyStr + " TEXT ,"
                    + startDate + " TEXT ,"
                    + endDate + " TEXT ,"
                    + specialValue + " TEXT ,"
                    + timezoneRawOffset + " TEXT ,"
                    + keyRight + " TEXT ,"
                    + keyboardPwdVersion + " TEXT ,"
                    + remoteEnable + " TEXT ,"
                    + remarks + " TEXT ,"
                    + id + " INTEGER ,"
                    + floor_id + " TEXT ,"
                    + room_no + " TEXT ,"
                    + room_type_id + " INTEGER ,"
                    + total_beds + " TEXT ,"
                    + housekeeping_status + " INTEGER ,"
                    + availability + " TEXT ,"
                    + fd_remark + " TEXT ,"
                    + hk_remark + " TEXT ,"
                    + assigned_to + " TEXT ,"
                    + room_desc + " TEXT ,"
                    + minimum_stay + " TEXT ,"
                    + short_code + " TEXT ,"
                    + sort_key + " TEXT ,"
                    + bed_type_id + " TEXT ,"
                    + phone_extension + " TEXT ,"
                    + key_card_alias + " TEXT ,"
                    + room_property + " TEXT ,"
                    + count_paymaster_room_inventory + " TEXT ,"
                    + room_as + " TEXT ,"
                    + image + " TEXT ,"
                    + created_by + " TEXT ,"
                    + modified_by + " TEXT ,"
                    + status + " INTEGER "
                    + ")";


    public LockDetails(int masterId, int roomId, String userType_value, String keyId_value, String lockversion_value, String lockname_value, String lockAlis_value, String lockMac_value, String electricQuantity_value, String lockFlagPos_value, String adminPwd_value, String lockkey_value, String noKeyPwd_value, String deletePwd_value, String pwdInfo_value, String timestamp_value, String aesKeyStr_value, String startDate_value, String endDate_value, String specialValue_value, String timezoneRawOffset_value, String keyRight_value, String keyboardPwdVersion_value, String remoteEnable_value, String remarks_value, int id_value, Object floorId, String roomNo, int roomTypeId, Object totalBeds, int housekeepingStatus, Object availability_value, Object fdRemark, String hkRemark, Object assignedTo, Object roomDesc, Object minimumStay, String shortCode, Object sortKey, Object bedTypeId, String phoneExtension, String keyCardAlias, Object roomProperty, Object countPaymasterRoomInventory, Object roomAs, Object image_value, String createdBy, String modifiedBy, int status_value) {
        this.masterId = masterId;
        this.roomId = roomId;
        this.userType_value = userType_value;
        this.keyId_value = keyId_value;
        this.lockversion_value = lockversion_value;
        this.lockname_value = lockname_value;
        this.lockAlis_value = lockAlis_value;
        this.lockMac_value = lockMac_value;
        this.electricQuantity_value = electricQuantity_value;
        this.lockFlagPos_value = lockFlagPos_value;
        this.adminPwd_value = adminPwd_value;
        this.lockkey_value = lockkey_value;
        this.noKeyPwd_value = noKeyPwd_value;
        this.deletePwd_value = deletePwd_value;
        this.pwdInfo_value = pwdInfo_value;
        this.timestamp_value = timestamp_value;
        this.aesKeyStr_value = aesKeyStr_value;
        this.startDate_value = startDate_value;
        this.endDate_value = endDate_value;
        this.specialValue_value = specialValue_value;
        this.timezoneRawOffset_value = timezoneRawOffset_value;
        this.keyRight_value = keyRight_value;
        this.keyboardPwdVersion_value = keyboardPwdVersion_value;
        this.remoteEnable_value = remoteEnable_value;
        this.remarks_value = remarks_value;
        this.id_value = id_value;
        this.floorId = floorId;
        this.roomNo = roomNo;
        this.roomTypeId = roomTypeId;
        this.totalBeds = totalBeds;
        this.housekeepingStatus = housekeepingStatus;
        this.availability_value = availability_value;
        this.fdRemark = fdRemark;
        this.hkRemark = hkRemark;
        this.assignedTo = assignedTo;
        this.roomDesc = roomDesc;
        this.minimumStay = minimumStay;
        this.shortCode = shortCode;
        this.sortKey = sortKey;
        this.bedTypeId = bedTypeId;
        this.phoneExtension = phoneExtension;
        this.keyCardAlias = keyCardAlias;
        this.roomProperty = roomProperty;
        this.countPaymasterRoomInventory = countPaymasterRoomInventory;
        this.roomAs = roomAs;
        this.image_value = image_value;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.status_value = status_value;
    }

    public LockDetails() {
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getMaster_id() {
        return master_id;
    }

    public static String getRoom_id() {
        return room_id;
    }

    public static String getUserType() {
        return userType;
    }

    public static String getKeyId() {
        return keyId;
    }

    public static String getLockversion() {
        return lockversion;
    }

    public static String getLockname() {
        return lockname;
    }

    public static String getLockAlis() {
        return lockAlis;
    }

    public static String getLockMac() {
        return lockMac;
    }

    public static String getElectricQuantity() {
        return electricQuantity;
    }

    public static String getLockFlagPos() {
        return lockFlagPos;
    }

    public static String getAdminPwd() {
        return adminPwd;
    }

    public static String getLockkey() {
        return lockkey;
    }

    public static String getNoKeyPwd() {
        return noKeyPwd;
    }

    public static String getDeletePwd() {
        return deletePwd;
    }

    public static String getPwdInfo() {
        return pwdInfo;
    }

    public static String getTimestamp() {
        return timestamp;
    }

    public static String getAesKeyStr() {
        return aesKeyStr;
    }

    public static String getStartDate() {
        return startDate;
    }

    public static String getEndDate() {
        return endDate;
    }

    public static String getSpecialValue() {
        return specialValue;
    }

    public static String getTimezoneRawOffset() {
        return timezoneRawOffset;
    }

    public static String getKeyRight() {
        return keyRight;
    }

    public static String getKeyboardPwdVersion() {
        return keyboardPwdVersion;
    }

    public static String getRemoteEnable() {
        return remoteEnable;
    }

    public static String getRemarks() {
        return remarks;
    }

    public static String getId() {
        return id;
    }

    public static String getFloor_id() {
        return floor_id;
    }

    public static String getRoom_no() {
        return room_no;
    }

    public static String getRoom_type_id() {
        return room_type_id;
    }

    public static String getTotal_beds() {
        return total_beds;
    }

    public static String getHousekeeping_status() {
        return housekeeping_status;
    }

    public static String getAvailability() {
        return availability;
    }

    public static String getFd_remark() {
        return fd_remark;
    }

    public static String getHk_remark() {
        return hk_remark;
    }

    public static String getAssigned_to() {
        return assigned_to;
    }

    public static String getRoom_desc() {
        return room_desc;
    }

    public static String getMinimum_stay() {
        return minimum_stay;
    }

    public static String getShort_code() {
        return short_code;
    }

    public static String getSort_key() {
        return sort_key;
    }

    public static String getBed_type_id() {
        return bed_type_id;
    }

    public static String getPhone_extension() {
        return phone_extension;
    }

    public static String getKey_card_alias() {
        return key_card_alias;
    }

    public static String getRoom_property() {
        return room_property;
    }

    public static String getCount_paymaster_room_inventory() {
        return count_paymaster_room_inventory;
    }

    public static String getRoom_as() {
        return room_as;
    }

    public static String getImage() {
        return image;
    }

    public static String getCreated_by() {
        return created_by;
    }

    public static String getModified_by() {
        return modified_by;
    }

    public static String getStatus() {
        return status;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getUserType_value() {
        return userType_value;
    }

    public void setUserType_value(String userType_value) {
        this.userType_value = userType_value;
    }

    public String getKeyId_value() {
        return keyId_value;
    }

    public void setKeyId_value(String keyId_value) {
        this.keyId_value = keyId_value;
    }

    public String getLockversion_value() {
        return lockversion_value;
    }

    public void setLockversion_value(String lockversion_value) {
        this.lockversion_value = lockversion_value;
    }

    public String getLockname_value() {
        return lockname_value;
    }

    public void setLockname_value(String lockname_value) {
        this.lockname_value = lockname_value;
    }

    public String getLockAlis_value() {
        return lockAlis_value;
    }

    public void setLockAlis_value(String lockAlis_value) {
        this.lockAlis_value = lockAlis_value;
    }

    public String getLockMac_value() {
        return lockMac_value;
    }

    public void setLockMac_value(String lockMac_value) {
        this.lockMac_value = lockMac_value;
    }

    public String getElectricQuantity_value() {
        return electricQuantity_value;
    }

    public void setElectricQuantity_value(String electricQuantity_value) {
        this.electricQuantity_value = electricQuantity_value;
    }

    public String getLockFlagPos_value() {
        return lockFlagPos_value;
    }

    public void setLockFlagPos_value(String lockFlagPos_value) {
        this.lockFlagPos_value = lockFlagPos_value;
    }

    public String getAdminPwd_value() {
        return adminPwd_value;
    }

    public void setAdminPwd_value(String adminPwd_value) {
        this.adminPwd_value = adminPwd_value;
    }

    public String getLockkey_value() {
        return lockkey_value;
    }

    public void setLockkey_value(String lockkey_value) {
        this.lockkey_value = lockkey_value;
    }

    public String getNoKeyPwd_value() {
        return noKeyPwd_value;
    }

    public void setNoKeyPwd_value(String noKeyPwd_value) {
        this.noKeyPwd_value = noKeyPwd_value;
    }

    public String getDeletePwd_value() {
        return deletePwd_value;
    }

    public void setDeletePwd_value(String deletePwd_value) {
        this.deletePwd_value = deletePwd_value;
    }

    public String getPwdInfo_value() {
        return pwdInfo_value;
    }

    public void setPwdInfo_value(String pwdInfo_value) {
        this.pwdInfo_value = pwdInfo_value;
    }

    public String getTimestamp_value() {
        return timestamp_value;
    }

    public void setTimestamp_value(String timestamp_value) {
        this.timestamp_value = timestamp_value;
    }

    public String getAesKeyStr_value() {
        return aesKeyStr_value;
    }

    public void setAesKeyStr_value(String aesKeyStr_value) {
        this.aesKeyStr_value = aesKeyStr_value;
    }

    public String getStartDate_value() {
        return startDate_value;
    }

    public void setStartDate_value(String startDate_value) {
        this.startDate_value = startDate_value;
    }

    public String getEndDate_value() {
        return endDate_value;
    }

    public void setEndDate_value(String endDate_value) {
        this.endDate_value = endDate_value;
    }

    public String getSpecialValue_value() {
        return specialValue_value;
    }

    public void setSpecialValue_value(String specialValue_value) {
        this.specialValue_value = specialValue_value;
    }

    public String getTimezoneRawOffset_value() {
        return timezoneRawOffset_value;
    }

    public void setTimezoneRawOffset_value(String timezoneRawOffset_value) {
        this.timezoneRawOffset_value = timezoneRawOffset_value;
    }

    public String getKeyRight_value() {
        return keyRight_value;
    }

    public void setKeyRight_value(String keyRight_value) {
        this.keyRight_value = keyRight_value;
    }

    public String getKeyboardPwdVersion_value() {
        return keyboardPwdVersion_value;
    }

    public void setKeyboardPwdVersion_value(String keyboardPwdVersion_value) {
        this.keyboardPwdVersion_value = keyboardPwdVersion_value;
    }

    public String getRemoteEnable_value() {
        return remoteEnable_value;
    }

    public void setRemoteEnable_value(String remoteEnable_value) {
        this.remoteEnable_value = remoteEnable_value;
    }

    public String getRemarks_value() {
        return remarks_value;
    }

    public void setRemarks_value(String remarks_value) {
        this.remarks_value = remarks_value;
    }

    public int getId_value() {
        return id_value;
    }

    public void setId_value(int id_value) {
        this.id_value = id_value;
    }

    public Object getFloorId() {
        return floorId;
    }

    public void setFloorId(Object floorId) {
        this.floorId = floorId;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public Object getTotalBeds() {
        return totalBeds;
    }

    public void setTotalBeds(Object totalBeds) {
        this.totalBeds = totalBeds;
    }

    public int getHousekeepingStatus() {
        return housekeepingStatus;
    }

    public void setHousekeepingStatus(int housekeepingStatus) {
        this.housekeepingStatus = housekeepingStatus;
    }

    public Object getAvailability_value() {
        return availability_value;
    }

    public void setAvailability_value(Object availability_value) {
        this.availability_value = availability_value;
    }

    public Object getFdRemark() {
        return fdRemark;
    }

    public void setFdRemark(Object fdRemark) {
        this.fdRemark = fdRemark;
    }

    public String getHkRemark() {
        return hkRemark;
    }

    public void setHkRemark(String hkRemark) {
        this.hkRemark = hkRemark;
    }

    public Object getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Object assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Object getRoomDesc() {
        return roomDesc;
    }

    public void setRoomDesc(Object roomDesc) {
        this.roomDesc = roomDesc;
    }

    public Object getMinimumStay() {
        return minimumStay;
    }

    public void setMinimumStay(Object minimumStay) {
        this.minimumStay = minimumStay;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public Object getSortKey() {
        return sortKey;
    }

    public void setSortKey(Object sortKey) {
        this.sortKey = sortKey;
    }

    public Object getBedTypeId() {
        return bedTypeId;
    }

    public void setBedTypeId(Object bedTypeId) {
        this.bedTypeId = bedTypeId;
    }

    public String getPhoneExtension() {
        return phoneExtension;
    }

    public void setPhoneExtension(String phoneExtension) {
        this.phoneExtension = phoneExtension;
    }

    public String getKeyCardAlias() {
        return keyCardAlias;
    }

    public void setKeyCardAlias(String keyCardAlias) {
        this.keyCardAlias = keyCardAlias;
    }

    public Object getRoomProperty() {
        return roomProperty;
    }

    public void setRoomProperty(Object roomProperty) {
        this.roomProperty = roomProperty;
    }

    public Object getCountPaymasterRoomInventory() {
        return countPaymasterRoomInventory;
    }

    public void setCountPaymasterRoomInventory(Object countPaymasterRoomInventory) {
        this.countPaymasterRoomInventory = countPaymasterRoomInventory;
    }

    public Object getRoomAs() {
        return roomAs;
    }

    public void setRoomAs(Object roomAs) {
        this.roomAs = roomAs;
    }

    public Object getImage_value() {
        return image_value;
    }

    public void setImage_value(Object image_value) {
        this.image_value = image_value;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public int getStatus_value() {
        return status_value;
    }

    public void setStatus_value(int status_value) {
        this.status_value = status_value;
    }

    public static String getCreateTable() {
        return CREATE_TABLE;
    }
}
