package com.smartlock.model;

public class RecordListItems {
    private Integer lockId;
    private Integer serverDate;
    private Integer recordType;
    private Integer success;
    private String keyboardPwd;
    private Integer lockDate;
    private String username;

    public RecordListItems(Integer lockId, Integer serverDate, Integer recordType, Integer success, String keyboardPwd, Integer lockDate, String username) {
        this.lockId = lockId;
        this.serverDate = serverDate;
        this.recordType = recordType;
        this.success = success;
        this.keyboardPwd = keyboardPwd;
        this.lockDate = lockDate;
        this.username = username;
    }

    public RecordListItems() {
    }

    public Integer getLockId() {
        return lockId;
    }

    public void setLockId(Integer lockId) {
        this.lockId = lockId;
    }

    public Integer getServerDate() {
        return serverDate;
    }

    public void setServerDate(Integer serverDate) {
        this.serverDate = serverDate;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getKeyboardPwd() {
        return keyboardPwd;
    }

    public void setKeyboardPwd(String keyboardPwd) {
        this.keyboardPwd = keyboardPwd;
    }

    public Integer getLockDate() {
        return lockDate;
    }

    public void setLockDate(Integer lockDate) {
        this.lockDate = lockDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
