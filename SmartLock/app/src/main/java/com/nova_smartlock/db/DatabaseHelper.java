package com.nova_smartlock.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nova_smartlock.model.KeyDetails;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "lock_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(LockDetails.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + LockDetails.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertLock(KeyDetails keyDetails) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(LockDetails.master_id, keyDetails.masterId);
        values.put(LockDetails.room_id, keyDetails.roomId);
        values.put(LockDetails.userType, keyDetails.userType);
        values.put(LockDetails.keyId, keyDetails.keyId);
        values.put(LockDetails.lockversion, keyDetails.lockversion);
        values.put(LockDetails.lockname, keyDetails.lockname);
        values.put(LockDetails.lockAlis, keyDetails.lockAlis);
        values.put(LockDetails.lockMac, keyDetails.lockMac);
        values.put(LockDetails.electricQuantity, keyDetails.electricQuantity);
        values.put(LockDetails.lockFlagPos, keyDetails.lockFlagPos);
        values.put(LockDetails.adminPwd, keyDetails.adminPwd);
        values.put(LockDetails.lockkey, keyDetails.lockkey);
        values.put(LockDetails.noKeyPwd, keyDetails.noKeyPwd);
        values.put(LockDetails.deletePwd, keyDetails.deletePwd);
        values.put(LockDetails.pwdInfo, keyDetails.pwdInfo);
        values.put(LockDetails.timestamp, keyDetails.timestamp);
        values.put(LockDetails.aesKeyStr, keyDetails.aesKeyStr);
        values.put(LockDetails.startDate, keyDetails.startDate);
        values.put(LockDetails.endDate, keyDetails.endDate);
        values.put(LockDetails.specialValue, keyDetails.specialValue);
        values.put(LockDetails.timezoneRawOffset, keyDetails.timezoneRawOffset);
        values.put(LockDetails.keyRight, keyDetails.keyRight);
        values.put(LockDetails.keyboardPwdVersion, keyDetails.keyboardPwdVersion);
        values.put(LockDetails.remoteEnable, keyDetails.remoteEnable);
        values.put(LockDetails.remarks, keyDetails.remarks);
        values.put(LockDetails.id, keyDetails.id);
        values.put(LockDetails.floor_id, String.valueOf(keyDetails.floorId));
        values.put(LockDetails.room_no, keyDetails.roomNo);
        values.put(LockDetails.room_type_id, keyDetails.roomTypeId);
        values.put(LockDetails.total_beds, String.valueOf(keyDetails.totalBeds));
        values.put(LockDetails.housekeeping_status, keyDetails.housekeepingStatus);
        values.put(LockDetails.availability, String.valueOf(keyDetails.availability));
        values.put(LockDetails.fd_remark, String.valueOf(keyDetails.fdRemark));
        values.put(LockDetails.hk_remark, keyDetails.hkRemark);
        values.put(LockDetails.assigned_to, String.valueOf(keyDetails.assignedTo));
        values.put(LockDetails.room_desc, String.valueOf(keyDetails.roomDesc));
        values.put(LockDetails.minimum_stay, String.valueOf(keyDetails.minimumStay));
        values.put(LockDetails.short_code, keyDetails.shortCode);
        values.put(LockDetails.sort_key, String.valueOf(keyDetails.sortKey));
        values.put(LockDetails.bed_type_id, String.valueOf(keyDetails.bedTypeId));
        values.put(LockDetails.phone_extension, keyDetails.phoneExtension);
        values.put(LockDetails.key_card_alias, keyDetails.keyCardAlias);
        values.put(LockDetails.room_property, String.valueOf(keyDetails.roomProperty));
        values.put(LockDetails.count_paymaster_room_inventory, String.valueOf(keyDetails.countPaymasterRoomInventory));
        values.put(LockDetails.room_as, String.valueOf(keyDetails.roomAs));
        values.put(LockDetails.image, String.valueOf(keyDetails.image));
        values.put(LockDetails.created_by, keyDetails.createdBy);
        values.put(LockDetails.modified_by, keyDetails.modifiedBy);
        values.put(LockDetails.status, keyDetails.status);

        long id = db.insert(LockDetails.TABLE_NAME, null, values);

        db.close();
        return id;
    }


    public List<LockDetails> getAllLock() {
        List<LockDetails> arrLock = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + LockDetails.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LockDetails lockDetails = new LockDetails();
                lockDetails.setMasterId(cursor.getInt(cursor.getColumnIndex(LockDetails.master_id)));
                lockDetails.setRoomId(cursor.getInt(cursor.getColumnIndex(LockDetails.room_id)));
                lockDetails.setUserType_value(cursor.getString(cursor.getColumnIndex(LockDetails.userType)));
                lockDetails.setKeyId_value(cursor.getString(cursor.getColumnIndex(LockDetails.keyId)));
                lockDetails.setLockversion_value(cursor.getString(cursor.getColumnIndex(LockDetails.lockversion)));
                lockDetails.setLockname_value(cursor.getString(cursor.getColumnIndex(LockDetails.lockname)));
                lockDetails.setLockAlis_value(cursor.getString(cursor.getColumnIndex(LockDetails.lockAlis)));
                lockDetails.setLockMac_value(cursor.getString(cursor.getColumnIndex(LockDetails.lockMac)));
                lockDetails.setElectricQuantity_value(cursor.getString(cursor.getColumnIndex(LockDetails.electricQuantity)));
                lockDetails.setLockFlagPos_value(cursor.getString(cursor.getColumnIndex(LockDetails.lockFlagPos)));
                lockDetails.setAdminPwd_value(cursor.getString(cursor.getColumnIndex(LockDetails.adminPwd)));
                lockDetails.setLockkey_value(cursor.getString(cursor.getColumnIndex(LockDetails.lockkey)));
                lockDetails.setNoKeyPwd_value(cursor.getString(cursor.getColumnIndex(LockDetails.noKeyPwd)));
                lockDetails.setUserType_value(cursor.getString(cursor.getColumnIndex(LockDetails.userType)));
                lockDetails.setDeletePwd_value(cursor.getString(cursor.getColumnIndex(LockDetails.deletePwd)));
                lockDetails.setPwdInfo_value(cursor.getString(cursor.getColumnIndex(LockDetails.pwdInfo)));
                lockDetails.setTimestamp_value(cursor.getString(cursor.getColumnIndex(LockDetails.timestamp)));
                lockDetails.setAesKeyStr_value(cursor.getString(cursor.getColumnIndex(LockDetails.aesKeyStr)));
                lockDetails.setStartDate_value(cursor.getString(cursor.getColumnIndex(LockDetails.startDate)));
                lockDetails.setEndDate_value(cursor.getString(cursor.getColumnIndex(LockDetails.endDate)));
                lockDetails.setSpecialValue_value(cursor.getString(cursor.getColumnIndex(LockDetails.specialValue)));
                lockDetails.setTimezoneRawOffset_value(cursor.getString(cursor.getColumnIndex(LockDetails.timezoneRawOffset)));
                lockDetails.setKeyRight_value(cursor.getString(cursor.getColumnIndex(LockDetails.keyRight)));
                lockDetails.setKeyboardPwdVersion_value(cursor.getString(cursor.getColumnIndex(LockDetails.keyboardPwdVersion)));
                lockDetails.setRemoteEnable_value(cursor.getString(cursor.getColumnIndex(LockDetails.remoteEnable)));
                lockDetails.setRemarks_value(cursor.getString(cursor.getColumnIndex(LockDetails.remarks)));
                lockDetails.setId_value(cursor.getInt(cursor.getColumnIndex(LockDetails.id)));
                lockDetails.setRoomNo(cursor.getString(cursor.getColumnIndex(LockDetails.room_no)));
                lockDetails.setRoomTypeId(cursor.getInt(cursor.getColumnIndex(LockDetails.room_type_id)));
                lockDetails.setTotalBeds(cursor.getString(cursor.getColumnIndex(LockDetails.total_beds)));
                lockDetails.setHousekeepingStatus(cursor.getInt(cursor.getColumnIndex(LockDetails.housekeeping_status)));
                lockDetails.setAvailability_value(cursor.getString(cursor.getColumnIndex(LockDetails.availability)));
                lockDetails.setFdRemark(cursor.getString(cursor.getColumnIndex(LockDetails.fd_remark)));
                lockDetails.setHkRemark(cursor.getString(cursor.getColumnIndex(LockDetails.hk_remark)));
                lockDetails.setAssignedTo(cursor.getString(cursor.getColumnIndex(LockDetails.assigned_to)));
                lockDetails.setRoomDesc(cursor.getString(cursor.getColumnIndex(LockDetails.room_desc)));
                lockDetails.setMinimumStay(cursor.getString(cursor.getColumnIndex(LockDetails.minimum_stay)));
                lockDetails.setShortCode(cursor.getString(cursor.getColumnIndex(LockDetails.short_code)));
                lockDetails.setSortKey(cursor.getString(cursor.getColumnIndex(LockDetails.sort_key)));
                lockDetails.setBedTypeId(cursor.getString(cursor.getColumnIndex(LockDetails.bed_type_id)));
                lockDetails.setPhoneExtension(cursor.getString(cursor.getColumnIndex(LockDetails.phone_extension)));
                lockDetails.setKeyCardAlias(cursor.getString(cursor.getColumnIndex(LockDetails.key_card_alias)));
                lockDetails.setRoomProperty(cursor.getString(cursor.getColumnIndex(LockDetails.room_property)));
                lockDetails.setCountPaymasterRoomInventory(cursor.getString(cursor.getColumnIndex(LockDetails.count_paymaster_room_inventory)));
                lockDetails.setRoomAs(cursor.getString(cursor.getColumnIndex(LockDetails.room_as)));
                lockDetails.setImage_value(cursor.getString(cursor.getColumnIndex(LockDetails.image)));
                lockDetails.setCreatedBy(cursor.getString(cursor.getColumnIndex(LockDetails.created_by)));
                lockDetails.setModifiedBy(cursor.getString(cursor.getColumnIndex(LockDetails.modified_by)));
                lockDetails.setStatus_value(cursor.getInt(cursor.getColumnIndex(LockDetails.status)));

                arrLock.add(lockDetails);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return arrLock;
    }


    public void deleteLock(LockDetails lockDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LockDetails.TABLE_NAME, LockDetails.master_id + " = ?",
                new String[]{String.valueOf(lockDetails.getMaster_id())});
        db.close();
    }

    public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + LockDetails.TABLE_NAME);
        db.close();
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + LockDetails.TABLE_NAME);
    }

}
