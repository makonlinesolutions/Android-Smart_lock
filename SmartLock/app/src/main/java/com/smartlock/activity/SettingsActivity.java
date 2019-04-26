package com.smartlock.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;
import com.smartlock.R;
import com.smartlock.model.Key;
import com.smartlock.net.ResponseService;
import com.smartlock.sp.MyPreference;

import org.json.JSONException;
import org.json.JSONObject;

import static com.smartlock.activity.NearbyLockActivity.curKey;

public class SettingsActivity extends AppCompatActivity {
    Toolbar toolbar;
    private Context mContext;
    private Key mKey;
    private TextView mTvLockNumber, mTvMacId, mTvBattery, mTvValidty, mTvLockName, mTvLockGroup;
    private RelativeLayout rl_lockname, rl_lockgroup, rl_lockclock, rl_diagnosis, rl_firmware_update, rl_unlock_remotely, rl_read_operations, rl_adminpasscode, rl_locksound;

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
            @Override
            public void onClick(View v) {
                openDialogForChangeKeyName();
            }
        });

        rl_lockgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, LockgroupActivity.class);
                startActivity(intent);
            }
        });

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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        rl_lockname = findViewById(R.id.rl_lockname);
        rl_lockgroup = findViewById(R.id.rl_lockgroup);
        rl_lockclock = findViewById(R.id.rl_lockclock);
        rl_diagnosis = findViewById(R.id.rl_diagnosis);
        rl_firmware_update = findViewById(R.id.rl_firmware_update);
        rl_unlock_remotely = findViewById(R.id.rl_unlock_remotely);
        rl_read_operations = findViewById(R.id.rl_read_operations);
        rl_adminpasscode = findViewById(R.id.rl_adminpasscode);
        rl_locksound = findViewById(R.id.rl_locksound);

        mTvLockNumber = findViewById(R.id.tv_lock_number);
        mTvMacId = findViewById(R.id.tv_mac_address);
        mTvBattery = findViewById(R.id.tv_battery);
        mTvValidty = findViewById(R.id.tv_validity_period);
        mTvLockName = findViewById(R.id.tv_lock_name);
        mTvLockGroup = findViewById(R.id.tv_lock_group);

        mContext = SettingsActivity.this;
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
            @Override
            public void onClick(View v) {
                name_lock = edt_lock_name.getText().toString().trim();
                if (TextUtils.isEmpty(name_lock)) {
                    Toast.makeText(mContext, "Please enter name", Toast.LENGTH_SHORT).show();
                } else {
                    getRequestToChangeName(name_lock);
                    dialog.dismiss();
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

            @Override
            protected void onPostExecute(String json) {
                String msg = getString(R.string.words_authorize_successed);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.has("errcode")) {
                        if (jsonObject.getString("errcode").equals("0")) {
                            msg = "Lock Name changed successfully";
                            mTvLockName.setText(name_lock);
                        } else {
                            msg = "Something went wrong";
                        }
                    } else {
                        msg = "Something went wrong";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
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

                @Override
                protected void onPostExecute(String json) {
                    String msg = getString(R.string.words_authorize_successed);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.has("errcode")) {
                            msg = "Something went wrong";
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            mTvLockNumber.setText(jsonObject.getString("lockName"));
                            mTvMacId.setText(jsonObject.getString("lockMac"));
                            mTvBattery.setText(jsonObject.getString("electricQuantity"));
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
}
