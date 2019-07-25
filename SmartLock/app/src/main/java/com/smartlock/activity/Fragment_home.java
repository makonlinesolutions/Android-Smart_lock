package com.smartlock.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import com.dd.processbutton.FlatButton;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.smartlock.R;
import com.smartlock.constant.Config;
import com.smartlock.dao.DbService;
import com.smartlock.db.DatabaseHelper;
import com.smartlock.db.LockDetails;
import com.smartlock.enumtype.Operation;
import com.smartlock.model.Key;
import com.smartlock.net.ResponseService;
import com.smartlock.sp.MyPreference;
import com.smartlock.utils.DisplayUtil;
import com.smartlock.utils.SharePreferenceUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.smartlock.activity.NearbyLockActivity.curKey;
import static com.smartlock.app.SmartLockApp.bleSession;
import static com.smartlock.app.SmartLockApp.mTTLockAPI;
import static com.smartlock.utils.Const.KEY_VALUE;
import static com.smartlock.utils.Const.USER_KEY_VALUE;

public class Fragment_home extends Fragment implements View.OnClickListener {
    ImageView img_lock, img_circular, mIvLockName;
    CircularProgressView progressView;
    LinearLayout ll_records, ll_settings, ll_send_key, ll_generate_passcode, ll_ekeys, ll_passcode, ll_options;
    RelativeLayout mRlUnLock;
    private Key mKey;
    private TextView mTvLockName, mTvNoLockFound;
    private int openid;
    String name_lock;
    private View viewLine;
    Dialog dialog;
    private static Fragment_home instance;
    private List<Key> arrKey;
    private List<LockDetails> arrKeyDetails;
    private LockDetails keyDetails;
    private boolean from_near_by_activity = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        instance = this;
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
        ll_options = view.findViewById(R.id.linearlayout);
        mTvNoLockFound = view.findViewById(R.id.tv_no_lock_found);
        viewLine = view.findViewById(R.id.view1);

        ll_ekeys.setVisibility(View.GONE);
        ll_passcode.setVisibility(View.GONE);
        ll_generate_passcode.setVisibility(View.GONE);
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        arrKeyDetails = databaseHelper.getAllLock();

        Bundle bundle = getArguments();

        if (bundle != null) {
            from_near_by_activity = bundle.getBoolean("from_near_by_activity", false);
        }
        if (!from_near_by_activity) {
            boolean is_admin_login = (boolean) SharePreferenceUtility.getPreferences(getContext(), Config.IS_ADMIN_LOGIN, SharePreferenceUtility.PREFTYPE_BOOLEAN);
            if (is_admin_login) {
                arrKey = DbService.getKeyListKey();
                if (arrKey.size() > 0) {
                    if (arrKey.size() == 1) {
                        mKey = arrKey.get(0);
                        img_lock.setBackgroundResource(R.drawable.ic_lock_black_24dp);
                        curKey = mKey;
                        mTvLockName.setText(mKey.getLockAlias());
                        mTvLockName.setVisibility(View.VISIBLE);
                        mIvLockName.setVisibility(View.VISIBLE);
                        ll_options.setVisibility(View.VISIBLE);
                        mTvNoLockFound.setVisibility(View.INVISIBLE);
                        viewLine.setVisibility(View.VISIBLE);
                    } else {
                        SharePreferenceUtility.saveObjectPreferences(getContext(), KEY_VALUE, null);
                        startActivity(new Intent(getContext(), NearbyLockActivity.class));
                        getActivity().finish();
                    }

                } else {
                    img_lock.setBackgroundResource(R.drawable.ic_add_black_24dp);
                    mTvLockName.setVisibility(View.INVISIBLE);
                    mIvLockName.setVisibility(View.INVISIBLE);
                    ll_options.setVisibility(View.INVISIBLE);
                    mTvNoLockFound.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.INVISIBLE);
//            startActivity(new Intent(getContext(), NearbyLockActivity.class));
//            ((Activity)getContext()).finish();
                }


            } else {
                if (arrKeyDetails.size() > 0) {
                    if (arrKeyDetails.size() == 1) {
                        keyDetails = arrKeyDetails.get(0);
                        mKey = new Key(Long.parseLong(String.valueOf(keyDetails.getId_value())), keyDetails.getUserType_value(), String.valueOf(keyDetails.getStatus_value()), keyDetails.getId_value(), Integer.parseInt(keyDetails.getKeyId_value()), keyDetails.getLockversion_value(), keyDetails.getLockname_value(),
                                keyDetails.getLockAlis_value(), keyDetails.getLockMac_value(), Integer.parseInt(keyDetails.getElectricQuantity_value()), Integer.parseInt(keyDetails.getLockFlagPos_value()), keyDetails.getAdminPwd_value(),
                                keyDetails.getLockkey_value(), keyDetails.getNoKeyPwd_value(), keyDetails.getDeletePwd_value(), keyDetails.getPwdInfo_value(), Long.parseLong(keyDetails.getTimestamp_value()), keyDetails.getAesKeyStr_value(), Long.parseLong(keyDetails.getStartDate_value()), Long.parseLong(keyDetails.getStartDate_value()),
                                Integer.parseInt(keyDetails.getSpecialValue_value()), Integer.parseInt(keyDetails.getTimezoneRawOffset_value()), Integer.parseInt(keyDetails.getKeyRight_value()), Integer.parseInt(keyDetails.getKeyboardPwdVersion_value()),
                                Integer.parseInt(keyDetails.getRemoteEnable_value()), keyDetails.getRemarks_value(), "", "", "");


                        img_lock.setBackgroundResource(R.drawable.ic_lock_black_24dp);
                        curKey = mKey;
                        mTvLockName.setText(keyDetails.getLockAlis_value());
                        mTvLockName.setVisibility(View.VISIBLE);
                        mIvLockName.setVisibility(View.VISIBLE);
                        ll_options.setVisibility(View.VISIBLE);
                        mTvNoLockFound.setVisibility(View.INVISIBLE);
                        viewLine.setVisibility(View.VISIBLE);
                    } else {
                        SharePreferenceUtility.saveObjectPreferences(getContext(), KEY_VALUE, null);
                        startActivity(new Intent(getContext(), NearbyLockActivity.class));
                        getActivity().finish();
                    }

                } else {
                    img_lock.setBackgroundResource(R.drawable.ic_add_black_24dp);
                    mTvLockName.setVisibility(View.INVISIBLE);
                    mIvLockName.setVisibility(View.INVISIBLE);
                    ll_options.setVisibility(View.INVISIBLE);
                    mTvNoLockFound.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.INVISIBLE);
//            startActivity(new Intent(getContext(), NearbyLockActivity.class));
//            ((Activity)getContext()).finish();
                }
            }
        } else {
            boolean is_admin_login = (boolean) SharePreferenceUtility.getPreferences(getContext(), Config.IS_ADMIN_LOGIN, SharePreferenceUtility.PREFTYPE_BOOLEAN);

            if (!is_admin_login) {
                keyDetails = (LockDetails) SharePreferenceUtility.getPreferences(getContext(), USER_KEY_VALUE, SharePreferenceUtility.PREFTYPE_USER_LOCK_OBJECT);
                mKey = new Key(Long.parseLong(String.valueOf(keyDetails.getId_value())), keyDetails.getUserType_value(), String.valueOf(keyDetails.getStatus_value()), keyDetails.getId_value(), Integer.parseInt(keyDetails.getKeyId_value()), keyDetails.getLockversion_value(), keyDetails.getLockname_value(),
                        keyDetails.getLockAlis_value(), keyDetails.getLockMac_value(), Integer.parseInt(keyDetails.getElectricQuantity_value()), Integer.parseInt(keyDetails.getLockFlagPos_value()), keyDetails.getAdminPwd_value(),
                        keyDetails.getLockkey_value(), keyDetails.getNoKeyPwd_value(), keyDetails.getDeletePwd_value(), keyDetails.getPwdInfo_value(), Long.parseLong(keyDetails.getTimestamp_value()), keyDetails.getAesKeyStr_value(), Long.parseLong(keyDetails.getStartDate_value()), Long.parseLong(keyDetails.getStartDate_value()),
                        Integer.parseInt(keyDetails.getSpecialValue_value()), Integer.parseInt(keyDetails.getTimezoneRawOffset_value()), Integer.parseInt(keyDetails.getKeyRight_value()), Integer.parseInt(keyDetails.getKeyboardPwdVersion_value()),
                        Integer.parseInt(keyDetails.getRemoteEnable_value()), keyDetails.getRemarks_value(), "", "", "");

                img_lock.setBackgroundResource(R.drawable.ic_lock_black_24dp);
                curKey = mKey;
                DbService.saveKey(mKey);
                mTvLockName.setText(keyDetails.getLockAlis_value());
                mTvLockName.setVisibility(View.VISIBLE);
                mIvLockName.setVisibility(View.VISIBLE);
                ll_options.setVisibility(View.VISIBLE);
                mTvNoLockFound.setVisibility(View.INVISIBLE);
                viewLine.setVisibility(View.VISIBLE);

            } else {
                mKey = (Key) SharePreferenceUtility.getPreferences(getContext(), KEY_VALUE, SharePreferenceUtility.PREFTYPE_OBJECT);

                if (mKey != null) {

                    img_lock.setBackgroundResource(R.drawable.ic_lock_black_24dp);
                    curKey = mKey;
                    mTvLockName.setText(mKey.getLockAlias());
                    mTvLockName.setVisibility(View.VISIBLE);
                    mIvLockName.setVisibility(View.VISIBLE);
                    ll_options.setVisibility(View.VISIBLE);
                    mTvNoLockFound.setVisibility(View.INVISIBLE);
                    viewLine.setVisibility(View.VISIBLE);
                } else {
                    img_lock.setBackgroundResource(R.drawable.ic_add_black_24dp);
                    mTvLockName.setVisibility(View.INVISIBLE);
                    mIvLockName.setVisibility(View.INVISIBLE);
                    ll_options.setVisibility(View.INVISIBLE);
                    mTvNoLockFound.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.INVISIBLE);
                }
            }
        }

        boolean is_admin_login = (boolean) SharePreferenceUtility.getPreferences(getContext(), Config.IS_ADMIN_LOGIN, SharePreferenceUtility.PREFTYPE_BOOLEAN);


        if (mKey != null && mKey.isAdmin()) {
            ll_send_key.setVisibility(View.VISIBLE);
        } else {
            ll_send_key.setVisibility(View.GONE);
        }
        openid = MyPreference.getOpenid(getActivity(), MyPreference.OPEN_ID);
        img_lock.setOnClickListener(this);

        ll_records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecordsActivity.class);
                startActivity(intent);
                ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        ll_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        ll_send_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SendeKeyActivity.class);
                startActivity(intent);
                ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
                ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        ll_passcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PasscodesActivity.class);
                startActivity(intent);
                ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        mIvLockName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogForChangeKeyName();
            }
        });

        if (!is_admin_login) {
            ll_send_key.setVisibility(View.GONE);
            ll_records.setVisibility(View.GONE);
            ll_settings.setVisibility(View.GONE);
        }

        if (!is_admin_login) {
            mIvLockName.setVisibility(View.GONE);
        }
        return view;
    }

    public static Fragment_home getInstance() {
        return instance;
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
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                name_lock = edt_lock_name.getText().toString().trim();
                if (TextUtils.isEmpty(name_lock)) {
                    DisplayUtil.showMessageDialog(getContext(), "Please enter name", getActivity().getDrawable(R.drawable.ic_iconfinder_143_attention_183267));
                    //Toast.makeText(getContext(), "Please enter name", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_lock) {

            if (mKey == null) {
                startActivity(new Intent(getContext(), AddLockActivity.class));
            } else {
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
            }
        } else {


        }
    }

    public void showDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DisplayUtil.showDialog(getContext());
            }
        });
    }

    public void showMessageDialog(final String message, final Drawable drawable) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DisplayUtil.showMessageDialog(getContext(), message, drawable);
            }
        });
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
