package com.nova_smartlock.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.nova_smartlock.db.LockDetails;
import com.nova_smartlock.model.Key;

public class SharePreferenceUtility {

    public static final int PREFTYPE_BOOLEAN = 0;
    public static final int PREFTYPE_INT = 1;
    public static final int PREFTYPE_STRING = 2;
    public static final int PREFTYPE_LONG = 3;
    public static final int PREFTYPE_OBJECT = 4;
    public static final int PREFTYPE_USER_LOCK_OBJECT = 5;
    private static final String TAG = SharePreferenceUtility.class.getName();

    public static void saveStringPreferences(Context context, String strKey, String strValue) {
        try {
            if (context != null) {
                Log.d(TAG, strKey);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(strKey, strValue);
                editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveIntPreferences(Context context, String strKey, int intValue) {
        try {
            if (context != null) {
                Log.d(TAG, strKey);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(strKey, intValue);
                editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveObjectPreferences(Context context, String strKey, Key objectValue) {
        try {
            if (context != null) {
                Log.d(TAG, strKey);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(objectValue);
                prefsEditor.putString(strKey, json);
                prefsEditor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveUserLock(Context context, String strKey, LockDetails lockDetails) {
        try {
            if (context != null) {
                Log.d(TAG, strKey);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(lockDetails);
                prefsEditor.putString(strKey, json);
                prefsEditor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void saveBooleanPreferences(Context context, String strKey, boolean boolValue) {
        try {
            if (context != null) {
                Log.d(TAG, strKey);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(strKey, boolValue);
                editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveLongPreferences(Context context, String strKey, long ll_steps) {
        try {
            if (context != null) {
                Log.d(TAG, strKey);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong(strKey, ll_steps);
                editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getPreferences(Context context, String key, int preferenceDataType) {
        Object value = null;
        SharedPreferences sharedPreferences;
        try {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            switch (preferenceDataType) {
                case PREFTYPE_BOOLEAN:
                    value = sharedPreferences.getBoolean(key, false);
                    break;
                case PREFTYPE_INT:
                    value = sharedPreferences.getInt(key, 0);
                    break;
                case PREFTYPE_STRING:
                    value = sharedPreferences.getString(key, "");
                    break;
                case PREFTYPE_LONG:
                    value = sharedPreferences.getLong(key, 0L);
                    break;
                case PREFTYPE_OBJECT:
                    Gson gson = new Gson();
                    String json = sharedPreferences.getString(key, "");
                    value = gson.fromJson(json, Key.class);
                    break;
                case PREFTYPE_USER_LOCK_OBJECT:
                    Gson gson_user = new Gson();
                    String json_user = sharedPreferences.getString(key, "");
                    value = gson_user.fromJson(json_user, LockDetails.class);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            switch (preferenceDataType) {
                case PREFTYPE_BOOLEAN:
                    value = false;
                    break;
                case PREFTYPE_INT:
                    value = 0;
                    break;
                case PREFTYPE_STRING:
                    value = "";
                    break;
                case PREFTYPE_LONG:
                    value = 0L;
                    break;
                case PREFTYPE_OBJECT:
                    value = "";
                    break;

                case PREFTYPE_USER_LOCK_OBJECT:
                    value = "";
                    break;
            }
        }
        return value;
    }

    public static void removeStringPreferences(Context context, String strKey){
        try {
            if (context != null) {
                Log.d(TAG, strKey);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(strKey);
                editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeIntPreferences(Context context, String strKey){
        try {
            if (context != null) {
                Log.d(TAG, strKey);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(strKey);
                editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeLongPreferences(Context context, String strKey){
        try {
            if (context != null) {
                Log.d(TAG, strKey);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(strKey);
                editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeBooleanPreferences(Context context, String strKey){
        try {
            if (context != null) {
                Log.d(TAG, strKey);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(strKey);
                editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
