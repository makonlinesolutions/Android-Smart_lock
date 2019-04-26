package com.smartlock.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.smartlock.R;
import com.smartlock.adapter.FoundDeviceAdapter;
import com.smartlock.app.SmartLockApp;
import com.smartlock.constant.BleConstant;
import com.smartlock.enumtype.Operation;
import com.smartlock.model.Key;
import com.ttlock.bl.sdk.scanner.ExtendedBluetoothDevice;

import java.util.ArrayList;
import java.util.List;

import static com.smartlock.app.SmartLockApp.bleSession;
import static com.smartlock.app.SmartLockApp.mTTLockAPI;

public class FoundDeviceActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private List<ExtendedBluetoothDevice> devices;
    private ListView listView;
    private FoundDeviceAdapter foundDeviceAdapter;
    private List<Key> keys;
    private Context mContext;
    public static Key curKey;
    ExtendedBluetoothDevice device;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

             if (action.equals(BleConstant.ACTION_BLE_DEVICE)) {
                Bundle bundle = intent.getExtras();
                 device = bundle.getParcelable(BleConstant.DEVICE);
                 foundDeviceAdapter.updateDevice(device);
            }
//            else if(action.equals(BleConstant.ACTION_BLE_DISCONNECTED)) {
//                cancelProgressDialog();
//            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_device);
        init();
    }

    private void init() {
        devices = new ArrayList<>();
        listView = getView(R.id.list);
        foundDeviceAdapter = new FoundDeviceAdapter(this, devices);
        listView.setAdapter(foundDeviceAdapter);
        listView.setOnItemClickListener(this);
        mContext = FoundDeviceActivity.this;
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
     /*   SmartLockApp.bleSession.setOperation(Operation.ADD_ADMIN);
        SmartLockApp.mTTLockAPI.connect((ExtendedBluetoothDevice) foundDeviceAdapter.getItem(position));
        mTTLockAPI.connect(device.getAddress());
        bleSession.setOperation(Operation.CLICK_UNLOCK);
        bleSession.setLockmac(device.getAddress());
        showProgressDialog();
        startActivity(new Intent(mContext, MainActivity.class));
        finish();*/
        SmartLockApp.bleSession.setOperation(Operation.ADD_ADMIN);
        SmartLockApp.mTTLockAPI.connect((ExtendedBluetoothDevice) foundDeviceAdapter.getItem(position));
        showProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
