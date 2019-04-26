package com.smartlock.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.smartlock.R;
import com.smartlock.enumtype.Operation;
import com.smartlock.model.Key;
import com.smartlock.net.ResponseService;
import com.smartlock.sp.MyPreference;
import com.smartlock.utils.SharePreferenceUtility;

import org.json.JSONException;
import org.json.JSONObject;

import static com.smartlock.activity.NearbyLockActivity.curKey;
import static com.smartlock.app.SmartLockApp.bleSession;
import static com.smartlock.app.SmartLockApp.mTTLockAPI;
import static com.smartlock.utils.Const.KEY_VALUE;

public class Fragment_home extends Fragment implements View.OnClickListener {
    ImageView img_lock, img_circular, mIvLockName;
    CircularProgressView progressView;
    LinearLayout ll_records, ll_settings, ll_send_key, ll_generate_passcode, ll_ekeys, ll_passcode;
    RelativeLayout mRlUnLock;
    private Key mKey;
    private TextView mTvLockName;
    private int openid;
    String name_lock;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        img_lock = view.findViewById(R.id.img_lock);
        img_circular = view.findViewById(R.id.img_circular);
        mTvLockName = view.findViewById(R.id.tvLockName);
        progressView = (CircularProgressView) view.findViewById(R.id.progress_view);
        ll_records = view.findViewById(R.id.ll_records);
        ll_settings = view.findViewById(R.id.ll_settings);
        ll_send_key = view.findViewById(R.id.ll_send_key);
        ll_generate_passcode = view.findViewById(R.id.ll_generate_passcode);
        ll_passcode = view.findViewById(R.id.ll_passcode);
        ll_ekeys = view.findViewById(R.id.ll_ekeys);
        mIvLockName = view.findViewById(R.id.iv_lock_name);
        mKey = (Key) SharePreferenceUtility.getPreferences(getContext(), KEY_VALUE, SharePreferenceUtility.PREFTYPE_OBJECT);
        if (mKey != null) {
            curKey = mKey;
            mTvLockName.setText("CONNECTION WITH : " + mKey.getLockAlias());
        }

        if (mKey != null && mKey.isAdmin()) {
            ll_generate_passcode.setVisibility(View.VISIBLE);
            ll_ekeys.setVisibility(View.VISIBLE);
            ll_send_key.setVisibility(View.VISIBLE);
            ll_passcode.setVisibility(View.VISIBLE);
        } else {
            ll_generate_passcode.setVisibility(View.GONE);
            ll_ekeys.setVisibility(View.GONE);
            ll_send_key.setVisibility(View.GONE);
            ll_passcode.setVisibility(View.GONE);
        }
        openid = MyPreference.getOpenid(getActivity(), MyPreference.OPEN_ID);
        img_lock.setOnClickListener(this);

        ll_records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecordsActivity.class);
                startActivity(intent);
            }
        });

        ll_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        ll_send_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SendeKeyActivity.class);
                startActivity(intent);
            }
        });

        ll_generate_passcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GeneratePasscodeActivity.class);
                startActivity(intent);
            }
        });

        ll_ekeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EkeysActivity.class);
                startActivity(intent);
            }
        });

        ll_passcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PasscodesActivity.class);
                startActivity(intent);
            }
        });

        mIvLockName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogForChangeKeyName();
            }
        });
        return view;
    }

    private void openDialogForChangeKeyName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dia_view = layoutInflater.inflate(R.layout.custom_edit_text_view, null);
        builder.setView(dia_view);
        final EditText edt_lock_name = dia_view.findViewById(R.id.edt_lockName);
        FlatButton button = dia_view.findViewById(R.id.fb_ChangeName);
//        final String name = "ADJ";


        final Dialog dialog = builder.create();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_lock = edt_lock_name.getText().toString().trim();
                if (TextUtils.isEmpty(name_lock)) {
                    Toast.makeText(getContext(), "Please enter name", Toast.LENGTH_SHORT).show();
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
        token = MyPreference.getStr(getActivity(), MyPreference.ACCESS_TOKEN);
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
                            mTvLockName.setText("CONNECTION WITH : " + name_lock);
                        } else {
                            msg = "Something went wrong";
                        }
                    } else {
                        msg = "Something went wrong";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_lock) {
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation();
            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            progressView.stopAnimation();
                            progressView.setVisibility(View.GONE);
                        }
                    }, 5000);

            if (mKey != null && mKey.getLockMac() != null && mTTLockAPI.isConnected(mKey.getLockMac())) {//If the lock is connected, you can call interface directly
                if (mKey.isAdmin())
                    mTTLockAPI.unlockByAdministrator(null, openid, mKey.getLockVersion(), mKey.getAdminPwd(), mKey.getLockKey(), mKey.getLockFlagPos(), System.currentTimeMillis(), mKey.getAesKeyStr(), mKey.getTimezoneRawOffset());
                else
                    mTTLockAPI.unlockByUser(null, openid, mKey.getLockVersion(), mKey.getStartDate(), mKey.getEndDate(), mKey.getLockKey(), mKey.getLockFlagPos(), mKey.getAesKeyStr(), mKey.getTimezoneRawOffset());
            } else {//to connect the lock
                progressView.setVisibility(View.VISIBLE);
                progressView.startAnimation();
                mTTLockAPI.connect(mKey.getLockMac());
                bleSession.setOperation(Operation.CLICK_UNLOCK);
                bleSession.setLockmac(mKey.getLockMac());
            }
        } else {

        }
    }
}
