package com.smartlock.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.smartlock.R;
import com.smartlock.app.SmartLockApp;
import com.smartlock.constant.Config;
import com.smartlock.dao.DbService;
import com.smartlock.model.AddLockResponse;
import com.smartlock.model.Key;
import com.smartlock.model.KeyObj;
import com.smartlock.net.ResponseService;
import com.smartlock.retrofit.ApiServiceProvider;
import com.smartlock.retrofit.ApiServices;
import com.smartlock.sp.MyPreference;
import com.smartlock.utils.Const;
import com.smartlock.utils.SharePreferenceUtility;
import com.ttlock.bl.sdk.api.TTLockAPI;
import com.ttlock.bl.sdk.util.GsonUtil;
import com.ttlock.bl.sdk.util.LogUtil;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.smartlock.constant.Config.IS_ADMIN_LOGIN;
import static com.smartlock.utils.Const.KEY_VALUE;
import static com.smartlock.utils.Const.USER_KEY_VALUE;

public class MainActivity extends BaseActivity implements DrawerAdapter.OnItemSelectedListener {
    private static final int POS_DASHBOARD = 0;
    private static final int POS_ADDLOCK = 1;
    private static final int POS_CUSTOMER_SERVICE = 3;
    private static final int POS_SETTINGS = 4;
    private static final int POS_LOGOUT = 2;
    private static final int ADD_LOCK = 6;
    private String[] screenTitles;
    private Drawable[] screenIcons;
    SlidingRootNav slidingRootNav;
    private ImageView mIvLock;
    private List<Key> keys;
    private Context mContext;
    private boolean IS_ADMIN = false;
    private boolean IS_FROM_NEAR_BY_ACTIVITY = false;
    public static Key curKey;
    private Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Smart Lock");
        setSupportActionBar(toolbar);
        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();
        intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("isAdmin")) {
                IS_ADMIN = intent.getBooleanExtra("isAdmin", false);
                IS_FROM_NEAR_BY_ACTIVITY = intent.getBooleanExtra("from_near_by_activity", false);
            }

        }

        mContext = MainActivity.this;
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuLayout(R.layout.menulayout)
                .withMenuOpened(false) //Initial menu opened/closed state. Default == false
                .withMenuLocked(false) //If true, a user can't open or close the menu. Default == false.
                .withGravity(SlideGravity.LEFT) //If LEFT you can swipe a menu from left to right, if RIGHT - the direction is opposite.
                .withSavedState(savedInstanceState) //If you call the method, layout will restore its opened/closed state
                .inject();

        mIvLock = findViewById(R.id.ivLockLock);
        DrawerAdapter adapter;
        boolean is_admin_login = (boolean) SharePreferenceUtility.getPreferences(mContext, Config.IS_ADMIN_LOGIN, SharePreferenceUtility.PREFTYPE_BOOLEAN);

        if (is_admin_login) {
            adapter = new DrawerAdapter(Arrays.asList(
                    createItemFor(POS_DASHBOARD).setChecked(true),
                    createItemFor(POS_ADDLOCK).setChecked(true),
                    createItemFor(POS_LOGOUT).setChecked(true)));

        } else {
            adapter = new DrawerAdapter(Arrays.asList(
                    createItemFor(POS_DASHBOARD).setChecked(true),
                    createItemFor(POS_LOGOUT).setChecked(true)));
        }
        adapter.setListener(this);
        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
//        adapter.setSelected(POS_DASHBOARD);

        mIvLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NearbyLockActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        String token = MyPreference.getStr(mContext, MyPreference.ACCESS_TOKEN);

        if (token.isEmpty() || token.equals("")) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
            return;
        }
        init();
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.white))
                .withTextTint(color(R.color.white))
                .withSelectedIconTint(color(R.color.white))
                .withSelectedTextTint(color(R.color.white));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.d("", DBG);
//        syncData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TTLockAPI.REQUEST_ENABLE_BT) {
                //start bluetooth scan
                SmartLockApp.mTTLockAPI.startBTDeviceScan();
            }
        }

    }

    /**
     * synchronizes the data of key
     */
    private void syncData() {
        showProgressDialog();
        new AsyncTask<Void, String, String>() {

            @Override
            protected String doInBackground(Void... params) {
                //you can synchronizes all key datas when lastUpdateDate is 0
                String json = ResponseService.syncData(0);
                LogUtil.d("json:" + json, DBG);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.has("errcode")) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
                    DbService.deleteAllKey();
                    DbService.saveKeyList(keys);
                    boolean is_admin_login = (boolean) SharePreferenceUtility.getPreferences(mContext, Config.IS_ADMIN_LOGIN, SharePreferenceUtility.PREFTYPE_BOOLEAN);

                    if (is_admin_login) {
                        for (int i = 0; i < list.size(); i++) {
                            getRequestToAddLockToPMSServer(list.get(i));
                        }
                    }

                    Bundle bundle = new Bundle();
                    bundle.putInt("key_data", keys.size());

                    Fragment fragment = new Fragment_home();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.cancel();

            }
        }.execute();
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
    protected void onStop() {
        super.onStop();
//        drawer.closeDrawer(GravityCompat.START);
        LogUtil.d("", DBG);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    @Override
    public void onItemSelected(int position) {
        if (position == POS_LOGOUT) {
            MyPreference.putStr(mContext, MyPreference.ACCESS_TOKEN, "");
            MyPreference.putStr(mContext, MyPreference.OPEN_ID, "");
            SharePreferenceUtility.saveBooleanPreferences(mContext, Config.IS_ADMIN_LOGIN, false);
            SharePreferenceUtility.saveObjectPreferences(mContext, KEY_VALUE, null);
            SharePreferenceUtility.saveObjectPreferences(mContext, USER_KEY_VALUE, null);
            SharePreferenceUtility.saveBooleanPreferences(mContext, Const.IS_LOGIN, false);
            Intent intent = new Intent(MainActivity.this, SplashScreenActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
        if (position == POS_ADDLOCK) {
            Intent intent;
            boolean is_admin_login = (boolean) SharePreferenceUtility.getPreferences(mContext, Config.IS_ADMIN_LOGIN, SharePreferenceUtility.PREFTYPE_BOOLEAN);

            if (is_admin_login) {
                intent = new Intent(MainActivity.this, AddLockActivity.class);
            } else {
                MyPreference.putStr(mContext, MyPreference.ACCESS_TOKEN, "");
                MyPreference.putStr(mContext, MyPreference.OPEN_ID, "");
                SharePreferenceUtility.saveBooleanPreferences(mContext, Config.IS_ADMIN_LOGIN, false);
                SharePreferenceUtility.saveObjectPreferences(mContext, KEY_VALUE, null);
                SharePreferenceUtility.saveBooleanPreferences(mContext, Const.IS_LOGIN, false);
                intent = new Intent(MainActivity.this, SplashScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        if (position == POS_DASHBOARD) {
            Fragment home_fragment = new Fragment_home();
            FragmentManager fragmentManager_home = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager_home.beginTransaction();
            fragmentTransaction.replace(R.id.container, home_fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        if (position == POS_SETTINGS) {
            Intent intent = new Intent(MainActivity.this, SettingsNavActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        /*if (position == POS_MESSAGES) {
            Intent intent = new Intent(MainActivity.this, MessagesActivity.class);
            startActivity(intent);
        }*/
        if (position == POS_CUSTOMER_SERVICE) {
            Intent intent = new Intent(MainActivity.this, CustomerServiceActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        slidingRootNav.closeMenu();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);

        if (fragment instanceof Fragment_home) {
            new AlertDialog.Builder(this)
                    .setMessage("Are You Sure You Want To Exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finishAffinity();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        } else {

            Fragment home_fragment = new Fragment_home();
            FragmentManager fragmentManager_home = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager_home.beginTransaction();
            fragmentTransaction.replace(R.id.container, home_fragment);
            fragmentTransaction.commit();
        }
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
        if (intent.getBooleanExtra("from_near_by_activity", false)) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("from_near_by_activity", true);
            Fragment home_fragment = new Fragment_home();
            home_fragment.setArguments(bundle);
            FragmentManager fragmentManager_home = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager_home.beginTransaction();
            fragmentTransaction.replace(R.id.container, home_fragment);
            fragmentTransaction.commit();
        } else {
            syncData();
        }
    }


    private ApiServices services;

    private void getRequestToAddLockToPMSServer(KeyObj keyObj) {
        services = new ApiServiceProvider(getApplicationContext()).apiServices;

        Call<AddLockResponse> addLockResponseCall = services.ADD_LOCK_RESPONSE_CALL("", keyObj.userType, keyObj.keyStatus, String.valueOf(keyObj.lockId),
                String.valueOf(keyObj.keyId), keyObj.lockVersion.protocolVersion, keyObj.lockName, keyObj.lockAlias, keyObj.lockMac, String.valueOf(keyObj.electricQuantity),
                String.valueOf(keyObj.lockFlagPos), keyObj.adminPwd, keyObj.lockKey, keyObj.noKeyPwd, "000", keyObj.pwdInfo, String.valueOf(keyObj.timestamp), keyObj.aesKeyStr,
                String.valueOf(keyObj.startDate), String.valueOf(keyObj.endDate), String.valueOf(keyObj.specialValue), String.valueOf(keyObj.timezoneRawOffset),
                String.valueOf(keyObj.keyRight), String.valueOf(keyObj.keyboardPwdVersion), String.valueOf(keyObj.remoteEnable), keyObj.remarks);

        addLockResponseCall.enqueue(new Callback<AddLockResponse>() {
            @Override
            public void onResponse(Call<AddLockResponse> call, Response<AddLockResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().response.statusCode == 200) {
                        Log.d("Lock add", "successfully");

                    }
                } else {
                    Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddLockResponse> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
