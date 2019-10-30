package com.nova_smartlock.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.nova_smartlock.R;
import com.nova_smartlock.app.SmartLockApp;
import com.nova_smartlock.constant.Config;
import com.nova_smartlock.dao.DbService;
import com.nova_smartlock.db.DatabaseHelper;
import com.nova_smartlock.db.LockDetails;
import com.nova_smartlock.model.Key;
import com.nova_smartlock.model.KeyObj;
import com.nova_smartlock.net.ResponseService;
import com.nova_smartlock.sp.MyPreference;
import com.nova_smartlock.utils.Const;
import com.nova_smartlock.utils.DisplayUtil;
import com.nova_smartlock.utils.NetworkUtils;
import com.nova_smartlock.utils.SharePreferenceUtility;
import com.ttlock.bl.sdk.util.GsonUtil;
import com.ttlock.bl.sdk.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.nova_smartlock.utils.Const.KEY_VALUE;
import static com.nova_smartlock.utils.Const.USER_KEY_VALUE;

public class NearbyLockActivity extends BaseActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    TextView tv_no_locks;
    List<Nearby_model> nearby_models;

    private List<Key> keys;
    private List<LockDetails> arrLockDetails;
    private Context mContext;
    public static Key curKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_lock);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recylerview);
        tv_no_locks = findViewById(R.id.tv_no_locks);

        mContext = NearbyLockActivity.this;
        layoutManager = new LinearLayoutManager(mContext);

      /*  Nearby_model list = new Nearby_model();
        list.setName("Cottage-CTH");
        nearby_models.add(list);*/
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        nearby_models = new ArrayList<>();


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void init() {
        //turn on bluetooth
        SmartLockApp.mTTLockAPI.requestBleEnable(this);
        LogUtil.d("start bluetooth service", DBG);
        SmartLockApp.mTTLockAPI.startBleService(this);
        //It need location permission to start bluetooth scan,or it can not scan device
        if (requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            SmartLockApp.mTTLockAPI.startBTDeviceScan();
        }

//        accessToken = MyPreference.getStr(this, MyPreference.ACCESS_TOKEN);
        keys = new ArrayList<>();
        if (NetworkUtils.isNetworkConnected(mContext)) {
            syncData();
        } else {
            DisplayUtil.showMessageDialog(mContext, "Please check Mobile network connection", getDrawable(R.drawable.ic_no_internet));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.add_door_lock:
                startActivity(new Intent(mContext, AddLockActivity.class));
                return true;
            case R.id.logout:

                new AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DbService.deleteAllKey();
                                Toast.makeText(mContext, "Logout Successfully", Toast.LENGTH_SHORT).show();
                                MyPreference.putStr(mContext, MyPreference.ACCESS_TOKEN, "");
                                MyPreference.putStr(mContext, MyPreference.OPEN_ID, "");
                                SharePreferenceUtility.saveBooleanPreferences(mContext, Config.IS_ADMIN_LOGIN, false);
                                SharePreferenceUtility.saveObjectPreferences(mContext, KEY_VALUE, null);
                                SharePreferenceUtility.saveObjectPreferences(mContext, USER_KEY_VALUE, null);
                                SharePreferenceUtility.saveBooleanPreferences(mContext, Const.IS_LOGIN, false);
                                Intent intent = new Intent(mContext, SplashScreenActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void syncData() {
        boolean is_admin_login = (boolean) SharePreferenceUtility.getPreferences(mContext, Config.IS_ADMIN_LOGIN, SharePreferenceUtility.PREFTYPE_BOOLEAN);
        if (is_admin_login) {
            new AsyncTask<Void, String, String>() {

                @Override
                protected String doInBackground(Void... params) {
                    //you can synchronizes all key datas when lastUpdateDate is 0
                    String json = ResponseService.syncData(0);
                    LogUtil.d("json:" + json, DBG);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.has("errcode")) {
//                        toast(jsonObject.getString("description"));
                            Intent intent = new Intent(NearbyLockActivity.this, LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                            finish();
                            return json;
                        }
                        //use lastUpdateDate you can get the newly added key and data after the time
                        long lastUpdateDate = jsonObject.getLong("lastUpdateDate");
                        String keyList = jsonObject.getString("keyList");
//                    JSONArray jsonArray = jsonObject.getJSONArray("keyList");
                        keys.clear();
                        ArrayList<KeyObj> list = GsonUtil.toObject(keyList, new TypeToken<ArrayList<KeyObj>>() {
                        });
                        keys.addAll(convert2DbModel(list));
                        //clear local keys and save new keys
//                        DbService.deleteAllKey();
//                        DbService.saveKeyList(keys);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return json;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
//                    progressDialog.cancel();

                    if (keys.size() == 0) {
                        tv_no_locks.setVisibility(View.VISIBLE);
                        SharePreferenceUtility.saveObjectPreferences(NearbyLockActivity.this, KEY_VALUE, null);
                    } else {
                        tv_no_locks.setVisibility(View.INVISIBLE);
                        adapter = new Nearby_Adapter(NearbyLockActivity.this, keys);
//                keyAdapter = new KeyAdapter(MainActivity.this, keys);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setOnCreateContextMenuListener(NearbyLockActivity.this);
                    }
                }
            }.execute();
        } else {
            DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
            arrLockDetails = databaseHelper.getAllLock();
            adapter = new Nearby_Adapter(NearbyLockActivity.this, arrLockDetails, true);
//                keyAdapter = new KeyAdapter(MainActivity.this, keys);
            recyclerView.setAdapter(adapter);
            recyclerView.setOnCreateContextMenuListener(NearbyLockActivity.this);
        }
    }


    private static ArrayList<Key> convert2DbModel(ArrayList<KeyObj> list) {
        ArrayList<Key> keyList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (KeyObj key : list) {
                Key DbKey = new Key();
                DbKey.setUserType(key.userType);
                DbKey.setKeyStatus(key.keyStatus);
                DbKey.setLockId(key.lockId);
                DbKey.setKeyId(key.keyId);
                DbKey.setLockVersion(GsonUtil.toJson(key.lockVersion));
                DbKey.setLockName(key.lockName);
                DbKey.setLockAlias(key.lockAlias);
                DbKey.setLockMac(key.lockMac);
                DbKey.setElectricQuantity(key.electricQuantity);
                DbKey.setLockFlagPos(key.lockFlagPos);
                DbKey.setAdminPwd(key.adminPwd);
                DbKey.setLockKey(key.lockKey);
                DbKey.setNoKeyPwd(key.noKeyPwd);
                DbKey.setDeletePwd(key.deletePwd);
                DbKey.setPwdInfo(key.pwdInfo);
                DbKey.setTimestamp(key.timestamp);
                DbKey.setAesKeyStr(key.aesKeyStr);
                DbKey.setStartDate(key.startDate);
                DbKey.setEndDate(key.endDate);
                DbKey.setSpecialValue(key.specialValue);
                DbKey.setTimezoneRawOffset(key.timezoneRawOffset);
                DbKey.setKeyRight(key.keyRight);
                DbKey.setKeyboardPwdVersion(key.keyboardPwdVersion);
                DbKey.setRemoteEnable(key.remoteEnable);
                DbKey.setRemarks(key.remarks);

                keyList.add(DbKey);
            }
        }
        return keyList;
    }

    @Override
    public void onBackPressed() {
        if (keys.size() > 1) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NearbyLockActivity.this.finishAffinity();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        } else {
            startActivity(new Intent(NearbyLockActivity.this, MainActivity.class).putExtra("from_near_by_activity", true));
            finish();
        }
    }
}
