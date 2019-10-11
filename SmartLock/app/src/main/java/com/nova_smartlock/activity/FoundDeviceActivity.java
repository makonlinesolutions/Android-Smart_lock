package com.nova_smartlock.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.nova_smartlock.R;
import com.nova_smartlock.adapter.FoundDeviceAdapter;
import com.nova_smartlock.app.SmartLockApp;
import com.nova_smartlock.constant.BleConstant;
import com.nova_smartlock.enumtype.Operation;
import com.nova_smartlock.model.AddLockResponse;
import com.nova_smartlock.model.Key;
import com.nova_smartlock.model.KeyObj;
import com.nova_smartlock.net.ResponseService;
import com.nova_smartlock.retrofit.ApiServiceProvider;
import com.nova_smartlock.retrofit.ApiServices;
import com.ttlock.bl.sdk.scanner.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.util.GsonUtil;
import com.ttlock.bl.sdk.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoundDeviceActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private List<ExtendedBluetoothDevice> devices;
    private ListView listView;
    private FoundDeviceAdapter foundDeviceAdapter;
    private List<Key> keys;
    private Context mContext;
    public static Key curKey;
    private ExtendedBluetoothDevice device;
    private Toolbar toolbar;
    private ArrayList<KeyObj> list;
    private ApiServices services;
    private int i;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BleConstant.ACTION_BLE_DEVICE)) {
                Bundle bundle = intent.getExtras();
                device = bundle.getParcelable(BleConstant.DEVICE);
                foundDeviceAdapter.updateDevice(device);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_device);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mContext = FoundDeviceActivity.this;
        init();
    }

    private void init() {
        devices = new ArrayList<>();
        listView = getView(R.id.list);
        foundDeviceAdapter = new FoundDeviceAdapter(this, devices);
        listView.setAdapter(foundDeviceAdapter);
        listView.setOnItemClickListener(this);
        mContext = FoundDeviceActivity.this;
        services = new ApiServiceProvider(getApplicationContext()).apiServices;
        registerReceiver(mReceiver, getIntentFilter());
    }

    private IntentFilter getIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleConstant.ACTION_BLE_DEVICE);
        intentFilter.addAction(BleConstant.ACTION_BLE_DISCONNECTED);
        return intentFilter;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SmartLockApp.bleSession.setOperation(Operation.ADD_ADMIN);
        SmartLockApp.mTTLockAPI.connect((ExtendedBluetoothDevice) foundDeviceAdapter.getItem(position));
        showProgressDialog();
        syncData(((ExtendedBluetoothDevice) foundDeviceAdapter.getItem(position)).getName());
    }

    private void getRequestToAddLockToPMSServer(KeyObj keyObj) {

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


    private void syncData(final String name) {
        new AsyncTask<Void, String, String>() {

            @Override
            protected String doInBackground(Void... params) {
                String json = ResponseService.syncData(0);
                LogUtil.d("json:" + json, DBG);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.has("errcode")) {
                        return json;
                    }
                    long lastUpdateDate = jsonObject.getLong("lastUpdateDate");
                    String keyList = jsonObject.getString("keyList");
                    list = GsonUtil.toObject(keyList, new TypeToken<ArrayList<KeyObj>>() {
                    });

                    for (i = 0; i < list.size(); i++) {
                        if (name.equals(list.get(i).lockName))
                            getRequestToAddLockToPMSServer(list.get(i));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

            }
        }.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
