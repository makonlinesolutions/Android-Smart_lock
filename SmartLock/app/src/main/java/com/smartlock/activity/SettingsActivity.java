package com.smartlock.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;
import com.smartlock.R;
import com.smartlock.model.Key;
import com.smartlock.model.UnlockKeyNameResponse;
import com.smartlock.net.ResponseService;
import com.smartlock.retrofit.ApiServiceProvider;
import com.smartlock.retrofit.ApiServices;
import com.smartlock.sp.MyPreference;
import com.smartlock.utils.DisplayUtil;
import com.smartlock.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.smartlock.activity.NearbyLockActivity.curKey;

public class SettingsActivity extends AppCompatActivity {
    Toolbar toolbar;
    private Context mContext;
    private Key mKey;
    private TextView mTvLockNumber, mTvMacId, mTvBattery, mTvValidty, mTvLockName, mTvLockGroup;
    private RelativeLayout rl_lockname, rl_lockgroup, rl_lockclock, rl_diagnosis, rl_firmware_update, rl_unlock_remotely, rl_read_operations, rl_adminpasscode, rl_locksound;
    private Button mBtLock;
    private ApiServices services;
    private android.app.AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initUi();

        if (curKey != null) {
            mKey = curKey;
        } else {
            startActivity(new Intent(mContext, NearbyLockActivity.class));
        }

        rl_lockname.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkConnected(mContext)) {
                    openDialogForChangeKeyName();
                } else {
                    DisplayUtil.showMessageDialog(mContext, "Please check internet connection", getDrawable(R.drawable.ic_no_internet));
                }
            }
        });

       /* rl_lockgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, LockgroupActivity.class);
                startActivity(intent);
            }
        });*/

        rl_lockclock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, LockClockActivity.class);
                startActivity(intent);
            }
        });

        rl_diagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, DiagnosisActivity.class);
                startActivity(intent);
            }
        });


        rl_firmware_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, FirmwareUpdateActivity.class);
                startActivity(intent);
            }
        });

        rl_unlock_remotely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, UnlockRemotelyActivity.class);
                startActivity(intent);
            }
        });

        rl_read_operations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ReadOperationRecordsActivity.class);
                startActivity(intent);
            }
        });

        rl_adminpasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, AdminPasscodeActivity.class);
                startActivity(intent);
            }
        });

        rl_locksound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, LockSoundActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initUi() {
        mContext = SettingsActivity.this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        services = new ApiServiceProvider(mContext).apiServices;
        rl_lockname = findViewById(R.id.rl_lockname);
        rl_lockgroup = findViewById(R.id.rl_lockgroup);
        rl_lockclock = findViewById(R.id.rl_lockclock);
        rl_diagnosis = findViewById(R.id.rl_diagnosis);
        rl_firmware_update = findViewById(R.id.rl_firmware_update);
        rl_unlock_remotely = findViewById(R.id.rl_unlock_remotely);
        rl_read_operations = findViewById(R.id.rl_read_operations);
        rl_adminpasscode = findViewById(R.id.rl_adminpasscode);
        rl_locksound = findViewById(R.id.rl_locksound);
        mBtLock = findViewById(R.id.btn_delete);
        mTvLockNumber = findViewById(R.id.tv_lock_number);
        mTvMacId = findViewById(R.id.tv_mac_address);
        mTvBattery = findViewById(R.id.tv_battery);
        mTvValidty = findViewById(R.id.tv_validity_period);
        mTvLockName = findViewById(R.id.tv_lock_name);
        mTvLockGroup = findViewById(R.id.tv_lock_group);
        alertDialog = new SpotsDialog.Builder().setContext(mContext).setMessage("Loading").build();


        mBtLock.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkConnected(mContext)) {
                    deleteLock();
                } else {
                    DisplayUtil.showMessageDialog(mContext, "Please check internet connection", getDrawable(R.drawable.ic_no_internet));
                }
            }
        });
    }

    String name_lock;
    private void openDialogForChangeKeyName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dia_view = layoutInflater.inflate(R.layout.custom_edit_text_view, null);
        builder.setView(dia_view);
        final EditText edt_lock_name = dia_view.findViewById(R.id.edt_lockName);
        FlatButton button = dia_view.findViewById(R.id.fb_ChangeName);


        final Dialog dialog = builder.create();
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                name_lock = edt_lock_name.getText().toString().trim();
                if (TextUtils.isEmpty(name_lock)) {
                    DisplayUtil.showMessageDialog(SettingsActivity.this, "Please enter name", getDrawable(R.drawable.ic_iconfinder_143_attention_183267));
                } else {
                    dialog.dismiss();

                    if (NetworkUtils.isNetworkConnected(mContext)) {
                        alertDialog.show();
                        Call<UnlockKeyNameResponse> unlockKeyNameResponseCall = services.UNLOCK_KEY_NAME_RESPONSE_CALL(String.valueOf(mKey.getLockId()), name_lock);
                        unlockKeyNameResponseCall.enqueue(new Callback<UnlockKeyNameResponse>() {
                            @Override
                            public void onResponse(Call<UnlockKeyNameResponse> call, Response<UnlockKeyNameResponse> response) {
                                alertDialog.dismiss();
                                if (response.isSuccessful()) {
                                    if (response.body().response.statusCode == 200) {
                                        if (TextUtils.isEmpty(name_lock)) {
                                            DisplayUtil.showMessageDialog(mContext, "Please enter name", getDrawable(R.drawable.ic_iconfinder_143_attention_183267));
                                            //Toast.makeText(mContext, "Please enter name", Toast.LENGTH_SHORT).show();
                                        } else {
                                            getRequestToChangeName(name_lock);
                                            dialog.dismiss();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<UnlockKeyNameResponse> call, Throwable t) {
                                alertDialog.dismiss();
                            }
                        });
                    } else {
                        DisplayUtil.showMessageDialog(mContext, "Please check internet connection", getDrawable(R.drawable.ic_no_internet));
                    }
                }
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    private void getRequestToChangeName(final String name) {
        final String token, lock_id;
        token = MyPreference.getStr(mContext, MyPreference.ACCESS_TOKEN);
        lock_id = String.valueOf(mKey.getLockId());
        new AsyncTask<Void, Integer, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return ResponseService.uploadLockName(name, token, lock_id);
            }
            @SuppressLint("NewApi")
            @Override
            protected void onPostExecute(String json) {
                String msg = getString(R.string.words_authorize_successed);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.has("errcode")) {
                        if (jsonObject.getString("errcode").equals("0")) {
                            msg = "Lock Name changed successfully";
                            mTvLockName.setText(name_lock);
                            DisplayUtil.showMessageDialog(SettingsActivity.this, msg, getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                        } else {
                            msg = "Error while changing lock name!";
                            DisplayUtil.showMessageDialog(SettingsActivity.this, msg, getDrawable(R.drawable.ic_iconfinder_ic_cancel_48px_352263));

                        }
                    } else {
                        msg = "Error while changing lock name!";
                        DisplayUtil.showMessageDialog(SettingsActivity.this, msg, getDrawable(R.drawable.ic_iconfinder_ic_cancel_48px_352263));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRequestLockDetails();
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

    private void getRequestLockDetails() {
        final String token, lock_id;
        token = MyPreference.getStr(mContext, MyPreference.ACCESS_TOKEN);
        lock_id = String.valueOf(mKey.getLockId());
        if (mKey != null) {
            new AsyncTask<Void, Integer, String>() {

                @Override
                protected String doInBackground(Void... params) {
                    return ResponseService.lockDetails(token, lock_id);
                }

                @SuppressLint("NewApi")
                @Override
                protected void onPostExecute(String json) {
                    String msg = getString(R.string.words_authorize_successed);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.has("errcode")) {
                            msg = "Couldn't get lock details!";
                            DisplayUtil.showMessageDialog(SettingsActivity.this, msg, getDrawable(R.drawable.ic_iconfinder_ic_cancel_48px_352263));
                            //Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            mTvLockNumber.setText(jsonObject.getString("lockName"));
                            mTvMacId.setText(jsonObject.getString("lockMac"));
                            mTvBattery.setText(jsonObject.getString("electricQuantity") + "%");
                            mTvLockName.setText(jsonObject.getString("lockAlias"));
                            JSONObject lockGroupDetails = jsonObject.getJSONObject("lockVersion");
                            mTvLockGroup.setText(lockGroupDetails.getString("groupId"));

//                        mTvLockNumber.setText(jsonObject.getString("lockName"));
//                        mTvLockNumber.setText(jsonObject.getString("lockName"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        }
    }

    private void deleteLock() {
        alertDialog.show();
        new AsyncTask<Void, Integer, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return ResponseService.deleteKey(mKey.getKeyId());
            }

            @SuppressLint("NewApi")
            @Override
            protected void onPostExecute(String json) {
                alertDialog.dismiss();
                Log.d("Settings Activity", json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.has("errcode")) {
                        Toast.makeText(mContext, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(mContext, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    } else {
                        Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute();

    }
}
