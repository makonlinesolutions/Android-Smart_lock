package com.nova_smartlock.model;

public class PasscodeListItem {

    public int lockId;
    public int keyboardPwdVersion;
    public long endDate;
    public long sendDate;
    public int keyboardPwdId;
    public String keyboardPwd;
    public int keyboardPwdType;
    public long startDate;
    public String receiverUsername;
    public int status;

    public PasscodeListItem(int lockId, int keyboardPwdVersion, long endDate, long sendDate, int keyboardPwdId, String keyboardPwd, int keyboardPwdType, long startDate, String receiverUsername, int status) {
        this.lockId = lockId;
        this.keyboardPwdVersion = keyboardPwdVersion;
        this.endDate = endDate;
        this.sendDate = sendDate;
        this.keyboardPwdId = keyboardPwdId;
        this.keyboardPwd = keyboardPwd;
        this.keyboardPwdType = keyboardPwdType;
        this.startDate = startDate;
        this.receiverUsername = receiverUsername;
        this.status = status;
    }

    public PasscodeListItem() {
    }

    public int getLockId() {
        return lockId;
    }

    public void setLockId(int lockId) {
        this.lockId = lockId;
    }

    public int getKeyboardPwdVersion() {
        return keyboardPwdVersion;
    }

    public void setKeyboardPwdVersion(int keyboardPwdVersion) {
        this.keyboardPwdVersion = keyboardPwdVersion;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public long getSendDate() {
        return sendDate;
    }

    public void setSendDate(long sendDate) {
        this.sendDate = sendDate;
    }

    public int getKeyboardPwdId() {
        return keyboardPwdId;
    }

    public void setKeyboardPwdId(int keyboardPwdId) {
        this.keyboardPwdId = keyboardPwdId;
    }

    public String getKeyboardPwd() {
        return keyboardPwd;
    }

    public void setKeyboardPwd(String keyboardPwd) {
        this.keyboardPwd = keyboardPwd;
    }

    public int getKeyboardPwdType() {
        return keyboardPwdType;
    }

    public void setKeyboardPwdType(int keyboardPwdType) {
        this.keyboardPwdType = keyboardPwdType;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
