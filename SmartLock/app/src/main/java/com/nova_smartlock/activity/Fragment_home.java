package com.nova_smartlock.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.card.MaterialCardView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
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
import com.nova_smartlock.R;
import com.nova_smartlock.constant.BleConstant;
import com.nova_smartlock.constant.Config;
import com.nova_smartlock.constant.UnlockTimeInterface;
import com.nova_smartlock.dao.DbService;
import com.nova_smartlock.db.DatabaseHelper;
import com.nova_smartlock.db.LockDetails;
import com.nova_smartlock.enumtype.Operation;
import com.nova_smartlock.model.CheckoutCheckResponse;
import com.nova_smartlock.model.Key;
import com.nova_smartlock.model.LockTimeUpdateResponse;
import com.nova_smartlock.model.UnlockKeyNameResponse;
import com.nova_smartlock.net.ResponseService;
import com.nova_smartlock.retrofit.ApiServiceProvider;
import com.nova_smartlock.retrofit.ApiServices;
import com.nova_smartlock.sp.MyPreference;
import com.nova_smartlock.utils.CommonUtils;
import com.nova_smartlock.utils.Const;
import com.nova_smartlock.utils.Constants;
import com.nova_smartlock.utils.DisplayUtil;
import com.nova_smartlock.utils.NetworkUtils;
import com.nova_smartlock.utils.SharePreferenceUtility;
import com.ttlock.bl.sdk.scanner.ExtendedBluetoothDevice;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nova_smartlock.activity.NearbyLockActivity.curKey;
import static com.nova_smartlock.app.SmartLockApp.bleSession;
import static com.nova_smartlock.app.SmartLockApp.mContext;
import static com.nova_smartlock.app.SmartLockApp.mTTLockAPI;
import static com.nova_smartlock.utils.Const.KEY_VALUE;
import static com.nova_smartlock.utils.Const.USER_KEY_VALUE;
import static com.nova_smartlock.utils.Constants.AppConst.ADULTS;
import static com.nova_smartlock.utils.Constants.AppConst.ARRIVE_TIME;
import static com.nova_smartlock.utils.Constants.AppConst.CHECK_IN;
import static com.nova_smartlock.utils.Constants.AppConst.CHECK_IN_DATE;
import static com.nova_smartlock.utils.Constants.AppConst.CHECK_IN_TIME;
import static com.nova_smartlock.utils.Constants.AppConst.CHECK_OUT;
import static com.nova_smartlock.utils.Constants.AppConst.CHECK_OUT_DATE;
import static com.nova_smartlock.utils.Constants.AppConst.CHECK_OUT_TIME;
import static com.nova_smartlock.utils.Constants.AppConst.DEPARTURE_TIME;
import static com.nova_smartlock.utils.Constants.AppConst.GROUP_CODE;
import static com.nova_smartlock.utils.Constants.AppConst.GROUP_NAME;
import static com.nova_smartlock.utils.Constants.AppConst.GUEST_TYPE;
import static com.nova_smartlock.utils.Constants.AppConst.IS_FIRST_TIME_LOGIN;
import static com.nova_smartlock.utils.Constants.AppConst.KIDS;
import static com.nova_smartlock.utils.Constants.AppConst.ORDER_ON;
import static com.nova_smartlock.utils.Constants.AppConst.ROOM_NO;
import static com.nova_smartlock.utils.Constants.AppConst.ROOM_SHORT;
import static com.nova_smartlock.utils.Constants.AppConst.ROOM_TYPE;
import static com.nova_smartlock.utils.Constants.AppConst.TOKEN;

public class Fragment_home extends Fragment implements View.OnClickListener, UnlockTimeInterface {
    private ImageView img_lock, img_circular, mIvLockName, mIvUnLock;
    private CircularProgressView progressView;
    private LinearLayout ll_records, ll_settings, ll_send_key, ll_generate_passcode, ll_ekeys, ll_passcode, ll_options;
    private RelativeLayout mRlUnLock;
    private Key mKey;
    private TextView mTvLockName, mTvNoLockFound, mTvRoomNumber;
    private int openid;
    String name_lock;
    private View viewLine;
    Dialog dialog;
    private static Fragment_home instance;
    private List<Key> arrKey;
    private List<LockDetails> arrKeyDetails;
    private LockDetails keyDetails;
    private boolean from_near_by_activity = false;
    private ApiServices services;
    private BluetoothAdapter mBluetoothAdapter;
    private List<ExtendedBluetoothDevice> mLeDevices ;
    private ExtendedBluetoothDevice device ;
    private android.app.AlertDialog alertDialog;
    private View view;
    private boolean is_first_time_login;
    private BottomSheetBehavior behavior ;
    private MaterialCardView linearBottomLayout ;
    private ImageView iv_arrow ;
    private TextView tv_room_no, tv_check_in, tv_check_out, tv_adults, tv_kids, tv_ordered_on, tv_arrive_time, tv_departure_time, tv_group_name_code, tv_room_id;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BleConstant.ACTION_BLE_DEVICE)) {
                Bundle bundle = intent.getExtras();
                device = bundle.getParcelable(BleConstant.DEVICE);
                mLeDevices.add(device);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        instance = this;
        mLeDevices = new ArrayList<>();
        img_lock = view.findViewById(R.id.img_lock);
        mIvUnLock = view.findViewById(R.id.img_un_lock);
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
        mTvRoomNumber = view.findViewById(R.id.tv_room_number);
        linearBottomLayout = view.findViewById(R.id.bottom_sheet_room_details);
        behavior = BottomSheetBehavior.from(linearBottomLayout);
        iv_arrow = view.findViewById(R.id.iv_arrow);
        tv_room_no = view.findViewById(R.id.tv_room_no);
        tv_check_in = view.findViewById(R.id.tv_check_in);
        tv_check_out = view.findViewById(R.id.tv_check_out);
        tv_adults = view.findViewById(R.id.tv_adults);
        tv_kids = view.findViewById(R.id.tv_kids);
        tv_ordered_on = view.findViewById(R.id.tv_ordered_on);
        tv_arrive_time = view.findViewById(R.id.tv_arrive_time);
        tv_departure_time = view.findViewById(R.id.tv_departure_time);
        tv_group_name_code = view.findViewById(R.id.tv_group_name_code);
        tv_room_id = view.findViewById(R.id.tv_room_id);

        is_first_time_login = (boolean) SharePreferenceUtility.getPreferences(getContext(), IS_FIRST_TIME_LOGIN, SharePreferenceUtility.PREFTYPE_BOOLEAN);

        if (is_first_time_login) {
            alertDialog = new SpotsDialog.Builder().setContext(getActivity()).setMessage("Loading").build();
            alertDialog.setCanceledOnTouchOutside(false);
        }
        init();
        ll_ekeys.setVisibility(View.GONE);
        boolean is_admin = (boolean) SharePreferenceUtility.getPreferences(getContext(), Config.IS_ADMIN_LOGIN, SharePreferenceUtility.PREFTYPE_BOOLEAN);
        if (!is_admin) {
            ll_passcode.setVisibility(View.GONE);
            ll_generate_passcode.setVisibility(View.GONE);
        }
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        arrKeyDetails = databaseHelper.getAllLock();
        services = new ApiServiceProvider(mContext).apiServices;

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        iv_arrow.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        setRoomDetails();
                        iv_arrow.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp));
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {
            }
        });

        iv_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((boolean) SharePreferenceUtility.getPreferences(getContext(), IS_FIRST_TIME_LOGIN, SharePreferenceUtility.PREFTYPE_BOOLEAN)) {
                    if (behavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    } else {
                        setRoomDetails();
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                } else {
                    CommonUtils.showProgressDialog(getContext());
                }
            }
        });

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
                        mTvLockName.setText("Lock Name: " + mKey.getLockAlias());
                        mTvLockName.setVisibility(View.VISIBLE);
                        mIvLockName.setVisibility(View.VISIBLE);
                        ll_options.setVisibility(View.VISIBLE);
                        mTvNoLockFound.setVisibility(View.INVISIBLE);
                        viewLine.setVisibility(View.VISIBLE);
                    } else {
                        SharePreferenceUtility.saveObjectPreferences(getContext(), KEY_VALUE, null);
                        startActivity(new Intent(getContext(), NearbyLockActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        getActivity().finish();
                    }
                } else {
                    img_lock.setBackgroundResource(R.drawable.ic_add_black_24dp);
                    mTvLockName.setVisibility(View.INVISIBLE);
                    mIvLockName.setVisibility(View.INVISIBLE);
                    ll_options.setVisibility(View.INVISIBLE);
                    mTvNoLockFound.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.INVISIBLE);
                    img_circular.setBackgroundResource(R.drawable.circular_gray_shape);

                    ((TextView) view.findViewById(R.id.tv_battery)).setVisibility(View.GONE);
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
                        mTvLockName.setText("Lock Name: " + keyDetails.getLockAlis_value());
                        mTvLockName.setVisibility(View.VISIBLE);
                        mIvLockName.setVisibility(View.VISIBLE);
                        ll_options.setVisibility(View.VISIBLE);
                        mTvNoLockFound.setVisibility(View.INVISIBLE);
                        viewLine.setVisibility(View.VISIBLE);
                    } else {
                        SharePreferenceUtility.saveObjectPreferences(getContext(), KEY_VALUE, null);
                        startActivity(new Intent(getContext(), NearbyLockActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        getActivity().finish();
                    }

                } else {
                    img_lock.setBackgroundResource(R.drawable.ic_add_black_24dp);
                    mTvLockName.setVisibility(View.INVISIBLE);
                    mIvLockName.setVisibility(View.INVISIBLE);
                    ll_options.setVisibility(View.INVISIBLE);
                    mTvNoLockFound.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.INVISIBLE);
                    img_circular.setBackgroundResource(R.drawable.circular_gray_shape);
                }
            }
        } else {
            boolean is_admin_login = (boolean) SharePreferenceUtility.getPreferences(getContext(), Config.IS_ADMIN_LOGIN, SharePreferenceUtility.PREFTYPE_BOOLEAN);
            if (!is_admin_login) {
                keyDetails = (LockDetails) SharePreferenceUtility.getPreferences(getContext(), USER_KEY_VALUE, SharePreferenceUtility.PREFTYPE_USER_LOCK_OBJECT);
                if (keyDetails != null) {
                    mKey = new Key(Long.parseLong(String.valueOf(keyDetails.getId_value())), keyDetails.getUserType_value(), String.valueOf(keyDetails.getStatus_value()), keyDetails.getId_value(), Integer.parseInt(keyDetails.getKeyId_value()), keyDetails.getLockversion_value(), keyDetails.getLockname_value(),
                            keyDetails.getLockAlis_value(), keyDetails.getLockMac_value(), Integer.parseInt(keyDetails.getElectricQuantity_value()), Integer.parseInt(keyDetails.getLockFlagPos_value()), keyDetails.getAdminPwd_value(),
                            keyDetails.getLockkey_value(), keyDetails.getNoKeyPwd_value(), keyDetails.getDeletePwd_value(), keyDetails.getPwdInfo_value(), Long.parseLong(keyDetails.getTimestamp_value()), keyDetails.getAesKeyStr_value(), Long.parseLong(keyDetails.getStartDate_value()), Long.parseLong(keyDetails.getStartDate_value()),
                            Integer.parseInt(keyDetails.getSpecialValue_value()), Integer.parseInt(keyDetails.getTimezoneRawOffset_value()), Integer.parseInt(keyDetails.getKeyRight_value()), Integer.parseInt(keyDetails.getKeyboardPwdVersion_value()),
                            Integer.parseInt(keyDetails.getRemoteEnable_value()), keyDetails.getRemarks_value(), "", "", "");
                }
                img_lock.setBackgroundResource(R.drawable.ic_lock_black_24dp);
                if (mKey != null) {
                    curKey = mKey;
                    DbService.saveKey(mKey);
                    mTvLockName.setText("Lock Name: " + keyDetails.getLockAlis_value());
                    mTvLockName.setVisibility(View.VISIBLE);
                    mIvLockName.setVisibility(View.VISIBLE);
                    ll_options.setVisibility(View.VISIBLE);
                    mTvNoLockFound.setVisibility(View.INVISIBLE);
                    viewLine.setVisibility(View.VISIBLE);
                } else {
                    showMessageDialog("Please select the key", getActivity().getDrawable(R.drawable.ic_iconfinder_143_attention_183267));
                }
            } else {
                mKey = (Key) SharePreferenceUtility.getPreferences(getContext(), KEY_VALUE, SharePreferenceUtility.PREFTYPE_OBJECT);
                arrKey = DbService.getKeyListKey();
                if (mKey != null) {
                    img_lock.setBackgroundResource(R.drawable.ic_lock_black_24dp);
                    curKey = mKey;
                    mTvLockName.setText("Lock Name: " + mKey.getLockAlias());
                    mTvLockName.setVisibility(View.VISIBLE);
                    mIvLockName.setVisibility(View.VISIBLE);
                    ll_options.setVisibility(View.VISIBLE);
                    mTvNoLockFound.setVisibility(View.INVISIBLE);
                    viewLine.setVisibility(View.VISIBLE);
                }else if (arrKey.size()>0) {
                    if (arrKey.size() == 1) {
                        mKey = arrKey.get(0);
                        img_lock.setBackgroundResource(R.drawable.ic_lock_black_24dp);
                        curKey = mKey;
                        mTvLockName.setText("Lock Name: " + mKey.getLockAlias());
                        mTvLockName.setVisibility(View.VISIBLE);
                        mIvLockName.setVisibility(View.VISIBLE);
                        ll_options.setVisibility(View.VISIBLE);
                        mTvNoLockFound.setVisibility(View.INVISIBLE);
                        viewLine.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getContext(), "Please select key", Toast.LENGTH_SHORT).show();
                        SharePreferenceUtility.saveObjectPreferences(getContext(), KEY_VALUE, null);
                        startActivity(new Intent(getContext(), NearbyLockActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        getActivity().finish();
                    }
                }else {
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
        if(!is_admin_login){
            linearBottomLayout.setVisibility(View.VISIBLE);
            setRoomDetails();
        }else {
            linearBottomLayout.setVisibility(View.GONE);
        }
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
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });

        ll_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        ll_send_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SendeKeyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        ll_generate_passcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_first_time_login = (boolean) SharePreferenceUtility.getPreferences(getContext(), IS_FIRST_TIME_LOGIN, SharePreferenceUtility.PREFTYPE_BOOLEAN);
                if (is_first_time_login) {
                    Intent intent = new Intent(getActivity(), GeneratePasscodeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    CommonUtils.showProgressDialog(getContext());
                }
            }
        });

        ll_ekeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EkeysActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        ll_passcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PasscodesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        mIvLockName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if ((boolean) SharePreferenceUtility.getPreferences(getContext(), IS_FIRST_TIME_LOGIN, SharePreferenceUtility.PREFTYPE_BOOLEAN)) {
                openDialogForChangeKeyName();
                /*}else {
                    CommonUtils.showProgressDialog(getContext());
                }*/
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
        ll_send_key.setVisibility(View.GONE);
        viewLine.setVisibility(View.GONE);
        if (!is_admin_login) {
            if (keyDetails != null) {
//                mTvRoomNumber.setText("Your Room Number is : " + keyDetails.getRoomNo());
//                mTvRoomNumber.setVisibility(View.VISIBLE);
                linearBottomLayout.setVisibility(View.VISIBLE);
                setRoomDetails();
            }
        }
        return view;
    }

    private void setRoomDetails() {
        String room_no = (String) SharePreferenceUtility.getPreferences(getContext(), ROOM_NO, SharePreferenceUtility.PREFTYPE_STRING);
        String room_short_name = (String) SharePreferenceUtility.getPreferences(getContext(), ROOM_SHORT, SharePreferenceUtility.PREFTYPE_STRING);
        if(room_no.equalsIgnoreCase("")){
            tv_room_no.setVisibility(View.GONE);
        }else {
            if(room_short_name.equalsIgnoreCase("")){
                tv_room_no.setText(room_no);
            }else {
                tv_room_no.setText(room_no + " (" + room_short_name + ")");
            }
//            tv_room_no.setVisibility(View.VISIBLE);
        }
        String check_in = (String) SharePreferenceUtility.getPreferences(getContext(), CHECK_IN, SharePreferenceUtility.PREFTYPE_STRING);
        if(check_in.equalsIgnoreCase("")){
            tv_check_in.setVisibility(View.GONE);
        }else {
            tv_check_in.setText(check_in);
            tv_check_in.setVisibility(View.VISIBLE);
        }
        String check_out = (String) SharePreferenceUtility.getPreferences(getContext(), CHECK_OUT, SharePreferenceUtility.PREFTYPE_STRING);
        if(check_out.equalsIgnoreCase("")){
            tv_check_out.setVisibility(View.GONE);
        }else {
            tv_check_out.setText(check_out);
            tv_check_out.setVisibility(View.VISIBLE);
        }
        int adults = (Integer) SharePreferenceUtility.getPreferences(getContext(), ADULTS, SharePreferenceUtility.PREFTYPE_INT);
        if(adults == 0){
            tv_adults.setVisibility(View.GONE);
        }else {
            tv_adults.setText(""+adults);
            tv_adults.setVisibility(View.VISIBLE);
        }
        int kids = (Integer) SharePreferenceUtility.getPreferences(getContext(), KIDS, SharePreferenceUtility.PREFTYPE_INT);
        if (String.valueOf(kids).isEmpty() || kids == 0) {
            tv_kids.setText(String.valueOf(0));
        }else {
            tv_kids.setText(String.valueOf(kids));
        }
        String order_on = (String) SharePreferenceUtility.getPreferences(getContext(), ORDER_ON, SharePreferenceUtility.PREFTYPE_STRING);
        if(order_on.equalsIgnoreCase("")){
            tv_ordered_on.setVisibility(View.GONE);
        }else {
            tv_ordered_on.setText(order_on);
            tv_ordered_on.setVisibility(View.VISIBLE);
        }
        String arrive_on = (String) SharePreferenceUtility.getPreferences(getContext(), ARRIVE_TIME, SharePreferenceUtility.PREFTYPE_STRING);
        if(arrive_on.equalsIgnoreCase("")){
            tv_arrive_time.setVisibility(View.GONE);
        }else {
            tv_arrive_time.setText(arrive_on);
            tv_arrive_time.setVisibility(View.VISIBLE);
        }
        String departure_time = (String) SharePreferenceUtility.getPreferences(getContext(), DEPARTURE_TIME, SharePreferenceUtility.PREFTYPE_STRING);
        if(departure_time.equalsIgnoreCase("")){
            tv_departure_time.setVisibility(View.GONE);
        }else {
            tv_departure_time.setText(departure_time);
            tv_departure_time.setVisibility(View.VISIBLE);
        }
        String group_name = (String) SharePreferenceUtility.getPreferences(getContext(), GROUP_NAME, SharePreferenceUtility.PREFTYPE_STRING);
        String group_code = (String) SharePreferenceUtility.getPreferences(getContext(), GROUP_CODE, SharePreferenceUtility.PREFTYPE_STRING);
        if (group_name.isEmpty()) {
            view.findViewById(R.id.ll_group_name).setVisibility(View.GONE);
        }else{
            view.findViewById(R.id.ll_group_name).setVisibility(View.VISIBLE);
            if (group_code.isEmpty()) {
                tv_group_name_code.setText(group_name);
            }else {
                tv_group_name_code.setText(group_name + " (" + group_code + ")");
            }
        }
        String room_type = (String) SharePreferenceUtility.getPreferences(getContext(), ROOM_TYPE, SharePreferenceUtility.PREFTYPE_STRING);
        if (room_type.equalsIgnoreCase("")) {
            tv_room_id.setVisibility(View.GONE);
        } else {
            tv_room_id.setText(room_type);
            tv_room_id.setVisibility(View.VISIBLE);
        }
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
                    DisplayUtil.showMessageDialog(getContext(), "Please Rename the lock", getActivity().getDrawable(R.drawable.ic_iconfinder_143_attention_183267));
                    //Toast.makeText(getContext(), "Please enter name", Toast.LENGTH_SHORT).show();
                } else {
                    if (NetworkUtils.isNetworkConnected(getContext())) {
                        Call<UnlockKeyNameResponse> unlockKeyNameResponseCall = services.UNLOCK_KEY_NAME_RESPONSE_CALL(String.valueOf(mKey.getLockId()), name_lock);
                        unlockKeyNameResponseCall.enqueue(new Callback<UnlockKeyNameResponse>() {
                            @Override
                            public void onResponse(Call<UnlockKeyNameResponse> call, Response<UnlockKeyNameResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().response.statusCode == 200) {
                                        if (TextUtils.isEmpty(name_lock)) {
                                            DisplayUtil.showMessageDialog(getContext(), "Please Rename the lock\n", getActivity().getDrawable(R.drawable.ic_iconfinder_143_attention_183267));
                                            //Toast.makeText(getContext(), "Please enter name", Toast.LENGTH_SHORT).show();
                                        } else {
                                            getRequestToChangeName(name_lock);
                                            dialog.dismiss();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure
                                    (Call<UnlockKeyNameResponse> call, Throwable t) {

                            }
                        });
                    } else {
                        Fragment_home.getInstance().showMessageDialog("Please check Mobile network connection", getActivity().getDrawable(R.drawable.ic_no_internet));
                    }
                /*}else {
                    CommonUtils.showProgressDialog(getContext());
                }*/
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
                            mTvLockName.setText("Lock Name: " + name_lock);
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_lock) {
            is_first_time_login = (boolean) SharePreferenceUtility.getPreferences(getContext(), IS_FIRST_TIME_LOGIN, SharePreferenceUtility.PREFTYPE_BOOLEAN);
            if (is_first_time_login) {
                if (!NetworkUtils.isNetworkConnected(mContext)) {
                    boolean is_admin_login = (boolean) SharePreferenceUtility.getPreferences(getContext(), Config.IS_ADMIN_LOGIN, SharePreferenceUtility.PREFTYPE_BOOLEAN);
                    if (is_admin_login) {

//                        Fragment_home.getInstance().showMessageDialog("Please check Mobile network connection", getActivity().getDrawable(R.drawable.ic_no_internet));
                        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (mBluetoothAdapter == null) {
                            // Device does not support Bluetooth
                        } else {
                            if (!mBluetoothAdapter.isEnabled()) {
                                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                                if (mBluetoothAdapter.disable()) {
                                    mBluetoothAdapter.enable();
                                    showMessageDialog("Bluetooth  is successfully enabled / ON", getActivity().getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                                }
                            } else {

                                if (mKey == null) {
                                    startActivity(new Intent(getContext(), AddLockActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                } else {

                                    if (mKey != null && mKey.getLockMac() != null && mTTLockAPI.isConnected(mKey.getLockMac())) {//If the lock is connected, you can call interface directly
                                        img_lock.setBackgroundResource(R.drawable.ic_unlock_color);
                                        if (mKey.isAdmin())
                                            mTTLockAPI.unlockByAdministrator(null, openid, mKey.getLockVersion(), mKey.getAdminPwd(), mKey.getLockKey(), mKey.getLockFlagPos(), System.currentTimeMillis(), mKey.getAesKeyStr(), mKey.getTimezoneRawOffset());
                                        else
                                            mTTLockAPI.unlockByUser(null, openid, mKey.getLockVersion(), mKey.getStartDate(), mKey.getEndDate(), mKey.getLockKey(), mKey.getLockFlagPos(), mKey.getAesKeyStr(), mKey.getTimezoneRawOffset());
                                    } else {//to connect the lock
                                        if (isDeviceNearBy(mKey.getLockMac())) {
                                            progressView.setVisibility(View.VISIBLE);
                                            progressView.startAnimation();
                                            new Handler().postDelayed(
                                                    new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            progressView.stopAnimation();
                                                            progressView.setVisibility(View.GONE);
                                                            img_lock.setVisibility(View.GONE);
                                                            mIvUnLock.setVisibility(View.VISIBLE);
                                                        }
                                                    }, 5000);
                                            new Handler().postDelayed(
                                                    new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            img_lock.setVisibility(View.VISIBLE);
                                                            mIvUnLock.setVisibility(View.GONE);
                                                        }
                                                    }, 8000);
                                            progressView.setVisibility(View.VISIBLE);
                                            progressView.startAnimation();
                                            mTTLockAPI.connect(mKey.getLockMac());
                                            bleSession.setOperation(Operation.CLICK_UNLOCK);
                                            bleSession.setLockmac(mKey.getLockMac());
                                        } else {
                                            Log.d("operation failed", "operation  684");
                                            showMessageDialog(mContext.getString(R.string.operation_failed), getContext().getDrawable(R.drawable.ic_iconfinder_ic_cancel_48px_352263));
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (isCheckInDate()) {
                            if (isCheckInTime()) {
//                        Fragment_home.getInstance().showMessageDialog("Please check Mobile network connection", getActivity().getDrawable(R.drawable.ic_no_internet));
                                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                                if (mBluetoothAdapter == null) {
                                    // Device does not support Bluetooth
                                } else {
                                    if (!mBluetoothAdapter.isEnabled()) {
                                        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                                        if (mBluetoothAdapter.disable()) {
                                            mBluetoothAdapter.enable();
                                            showMessageDialog("Bluetooth  is successfully enabled / ON", getActivity().getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                                        }
                                    } else {

                                        if (mKey == null) {
                                            startActivity(new Intent(getContext(), AddLockActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                        } else {

                                            if (mKey != null && mKey.getLockMac() != null && mTTLockAPI.isConnected(mKey.getLockMac())) {//If the lock is connected, you can call interface directly
                                                img_lock.setBackgroundResource(R.drawable.ic_unlock_color);
                                                if (mKey.isAdmin())
                                                    mTTLockAPI.unlockByAdministrator(null, openid, mKey.getLockVersion(), mKey.getAdminPwd(), mKey.getLockKey(), mKey.getLockFlagPos(), System.currentTimeMillis(), mKey.getAesKeyStr(), mKey.getTimezoneRawOffset());
                                                else
                                                    mTTLockAPI.unlockByUser(null, openid, mKey.getLockVersion(), mKey.getStartDate(), mKey.getEndDate(), mKey.getLockKey(), mKey.getLockFlagPos(), mKey.getAesKeyStr(), mKey.getTimezoneRawOffset());
                                            } else {//to connect the lock
                                                if (isDeviceNearBy(mKey.getLockMac())) {
                                                    progressView.setVisibility(View.VISIBLE);
                                                    progressView.startAnimation();
                                                    new Handler().postDelayed(
                                                            new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    progressView.stopAnimation();
                                                                    progressView.setVisibility(View.GONE);
                                                                    img_lock.setVisibility(View.GONE);
                                                                    mIvUnLock.setVisibility(View.VISIBLE);
                                                                }
                                                            }, 5000);
                                                    new Handler().postDelayed(
                                                            new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    img_lock.setVisibility(View.VISIBLE);
                                                                    mIvUnLock.setVisibility(View.GONE);
                                                                }
                                                            }, 8000);
                                                    progressView.setVisibility(View.VISIBLE);
                                                    progressView.startAnimation();
                                                    mTTLockAPI.connect(mKey.getLockMac());
                                                    bleSession.setOperation(Operation.CLICK_UNLOCK);
                                                    bleSession.setLockmac(mKey.getLockMac());
                                                } else {
                                                    Log.d("operation failed", "operation  684");
                                                    showMessageDialog(mContext.getString(R.string.operation_failed), getContext().getDrawable(R.drawable.ic_iconfinder_ic_cancel_48px_352263));
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                Fragment_home.getInstance().showMessageDialog("Please check your checkIn/checkOut Time", getActivity().getDrawable(R.drawable.ic_warning_black_48dp));
                            }
                        } else {
                            Fragment_home.getInstance().showMessageDialog("Please check your checkIn/checkOut Date", getActivity().getDrawable(R.drawable.ic_warning_black_48dp));
                        }
                    }
                } else {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        // Device does not support Bluetooth
                    } else {
                        if (!mBluetoothAdapter.isEnabled()) {
                            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                            if (mBluetoothAdapter.disable()) {
                                mBluetoothAdapter.enable();
                                showMessageDialog("Bluetooth  is successfully enabled / ON", getActivity().getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                            }
                        } else {
                            boolean is_admin_login = (boolean) SharePreferenceUtility.getPreferences(getContext(), Config.IS_ADMIN_LOGIN, SharePreferenceUtility.PREFTYPE_BOOLEAN);
                            if (is_admin_login) {
                                if (mKey == null) {
                                    startActivity(new Intent(getContext(), AddLockActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                } else {

                                    if (mKey != null && mKey.getLockMac() != null && mTTLockAPI.isConnected(mKey.getLockMac())) {//If the lock is connected, you can call interface directly
                                        img_lock.setBackgroundResource(R.drawable.ic_unlock_color);
                                        if (mKey.isAdmin())
                                            mTTLockAPI.unlockByAdministrator(null, openid, mKey.getLockVersion(), mKey.getAdminPwd(), mKey.getLockKey(), mKey.getLockFlagPos(), System.currentTimeMillis(), mKey.getAesKeyStr(), mKey.getTimezoneRawOffset());
                                        else
                                            mTTLockAPI.unlockByUser(null, openid, mKey.getLockVersion(), mKey.getStartDate(), mKey.getEndDate(), mKey.getLockKey(), mKey.getLockFlagPos(), mKey.getAesKeyStr(), mKey.getTimezoneRawOffset());
                                    } else {//to connect the lock

                                        if (isDeviceNearBy(mKey.getLockMac())) {


                                            progressView.setVisibility(View.VISIBLE);
                                            progressView.startAnimation();
                                            new Handler().postDelayed(
                                                    new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            progressView.stopAnimation();
                                                            progressView.setVisibility(View.GONE);
                                                            img_lock.setVisibility(View.GONE);
                                                            mIvUnLock.setVisibility(View.VISIBLE);
                                                        }
                                                    }, 5000);

                                            new Handler().postDelayed(
                                                    new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            img_lock.setVisibility(View.VISIBLE);
                                                            mIvUnLock.setVisibility(View.GONE);
                                                        }
                                                    }, 8000);


                                            progressView.setVisibility(View.VISIBLE);
                                            progressView.startAnimation();
                                            mTTLockAPI.connect(mKey.getLockMac());
                                            bleSession.setOperation(Operation.CLICK_UNLOCK);
                                            bleSession.setLockmac(mKey.getLockMac());
                                        } else {
                                            Log.d("operation failed", "operation  756");
                                            showMessageDialog(mContext.getString(R.string.operation_failed), getContext().getDrawable(R.drawable.ic_iconfinder_ic_cancel_48px_352263));
                                        }
                                    }
                                }
                            } else {
                                getRequestCheckChecked();
                            }
                        }
                    }
                }
            }else {
                CommonUtils.showProgressDialog(getContext());
            }
        }
    }

    private boolean isCheckInTime() {
        boolean isCheckInTime = false ;
        String checkInTime = (String) SharePreferenceUtility.getPreferences(mContext, CHECK_IN_TIME, SharePreferenceUtility.PREFTYPE_STRING);
        String checkOutTime = (String) SharePreferenceUtility.getPreferences(mContext, CHECK_OUT_TIME, SharePreferenceUtility.PREFTYPE_STRING);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date checkIn = null, checkOut = null, currTime = null;
        String time = sdf.format(new Date());
        try{
            checkIn = sdf.parse(checkInTime);
            checkOut = sdf.parse(checkOutTime);
            currTime = sdf.parse("18:00:00");
        }catch (Exception e){
            e.printStackTrace();
        }
        /*if (currTime.after(checkIn) && currTime.before(checkOut)) {
            isCheckInTime = true ;
        } else if (currTime.after(checkIn) && currTime.after(checkOut)) {
            isCheckInTime = true ;
        }*/
        if(checkOut.compareTo(checkIn) <= 0) {
            if(currTime.compareTo(checkOut) < 0 || currTime.compareTo(checkIn) >= 0) {
                isCheckInTime = true;
            }
        } else if(currTime.compareTo(checkOut) < 0 && currTime.compareTo(checkIn) >= 0) {
            isCheckInTime = true;
        }

        return isCheckInTime;
    }

    private boolean isCheckInDate() {
        boolean isCorrectDate = false;
        try {
            String checkInDate = (String) SharePreferenceUtility.getPreferences(mContext, CHECK_IN_DATE, SharePreferenceUtility.PREFTYPE_STRING);
            String checkOutDate = (String) SharePreferenceUtility.getPreferences(mContext, CHECK_OUT_DATE, SharePreferenceUtility.PREFTYPE_STRING);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date checkIn = null, checkOut = null, todaysDate = null;
            String today = sdf.format(new Date());
            try {
                checkIn = sdf.parse(checkInDate);
                checkOut = sdf.parse(checkOutDate);
                todaysDate = sdf.parse(today);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((todaysDate.compareTo(checkIn) >= 0) && (todaysDate.compareTo(checkOut) <= 0)) {
                isCorrectDate = true;
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return isCorrectDate;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (alertDialog!=null && !alertDialog.isShowing()) {
            alertDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                }
            },2000);
        }
    }

    private boolean isDeviceNearBy(String mack_address) {
        Log.d("selected mac address", mack_address);
        HashSet<ExtendedBluetoothDevice> hashSet = new HashSet<ExtendedBluetoothDevice>();


        hashSet.addAll(mLeDevices);
        mLeDevices.clear();
        mLeDevices.addAll(hashSet);

        Log.e("get ble address", mLeDevices.toString());
        if (mack_address.isEmpty()) {
            for (int i = 0; i < mLeDevices.size(); i++) {
                if (mLeDevices != null && mack_address.equals(mLeDevices.get(i).getAddress())) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < mLeDevices.size(); i++) {
                if (mLeDevices != null && curKey.getLockMac().equals(mLeDevices.get(i).getAddress())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void getRequestCheckChecked() {
        String guest_id = (String) SharePreferenceUtility.getPreferences(mContext, Constants.AppConst.GUEST_ID, SharePreferenceUtility.PREFTYPE_STRING);
        String order_id = (String) SharePreferenceUtility.getPreferences(mContext, Constants.AppConst.ORDER_ID, SharePreferenceUtility.PREFTYPE_STRING);
        String token = (String) SharePreferenceUtility.getPreferences(mContext, TOKEN, SharePreferenceUtility.PREFTYPE_STRING);

        Call<CheckoutCheckResponse> checkoutCheckResponseCall = services.CHECKOUT_CHECK_RESPONSE_CALL(order_id, guest_id, token);
        checkoutCheckResponseCall.enqueue(new Callback<CheckoutCheckResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<CheckoutCheckResponse> call, Response<CheckoutCheckResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().response.statusCode == 200) {
                        if (response.body().response.isCheckout != null) {
                            if (response.body().response.orderStatus != 5) {
                              /*  if (response.body().response.orderStatus == 0) {
                                }else if (response.body().response.orderStatus == 3) {
                                    showMessageDialog("You have already checked-out", getActivity().getDrawable(R.drawable.ic_iconfinder_ok_2639876));

                                }*/

                                showMessageDialog(response.body().response.message, getActivity().getDrawable(R.drawable.ic_iconfinder_ic_cancel_48px_352263));

                            } else {
                                if (mKey == null) {
                                    startActivity(new Intent(getContext(), AddLockActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                } else {
                                    SharePreferenceUtility.saveStringPreferences(mContext, CHECK_IN, response.body().response.checkInDate);
                                    SharePreferenceUtility.saveStringPreferences(mContext, CHECK_OUT, response.body().response.checkOutDate);
                                    SharePreferenceUtility.saveStringPreferences(mContext, ARRIVE_TIME, response.body().response.checkInTime);
                                    SharePreferenceUtility.saveStringPreferences(mContext, DEPARTURE_TIME, response.body().response.checkOutTime);

                                    if (mKey != null && mKey.getLockMac() != null && mTTLockAPI.isConnected(mKey.getLockMac())) {//If the lock is connected, you can call interface directly
                                        img_lock.setBackgroundResource(R.drawable.ic_unlock_color);
                                        if (mKey.isAdmin()) {
                                            mTTLockAPI.unlockByAdministrator(null, openid, mKey.getLockVersion(), mKey.getAdminPwd(), mKey.getLockKey(), mKey.getLockFlagPos(), System.currentTimeMillis(), mKey.getAesKeyStr(), mKey.getTimezoneRawOffset());
                                        } else {
                                            mTTLockAPI.unlockByUser(null, openid, mKey.getLockVersion(), mKey.getStartDate(), mKey.getEndDate(), mKey.getLockKey(), mKey.getLockFlagPos(), mKey.getAesKeyStr(), mKey.getTimezoneRawOffset());
                                        }
                                    } else {//to connect the lock
                                        if (isDeviceNearBy(mKey.getLockMac())) {
                                            progressView.setVisibility(View.VISIBLE);
                                            progressView.startAnimation();
                                            new Handler().postDelayed(
                                                    new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            progressView.stopAnimation();
                                                            progressView.setVisibility(View.GONE);
                                                            img_lock.setVisibility(View.GONE);
                                                            mIvUnLock.setVisibility(View.VISIBLE);
                                                        }
                                                    }, 5000);

                                            new Handler().postDelayed(
                                                    new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            img_lock.setVisibility(View.VISIBLE);
                                                            mIvUnLock.setVisibility(View.GONE);
                                                        }
                                                    }, 8000);
                                            progressView.setVisibility(View.VISIBLE);
                                            progressView.startAnimation();
                                            mTTLockAPI.connect(mKey.getLockMac());
                                            bleSession.setOperation(Operation.CLICK_UNLOCK);
                                            bleSession.setLockmac(mKey.getLockMac());
                                        } else {
                                            Log.d("operation failed", "operation  887");
                                            showMessageDialog(mContext.getString(R.string.operation_failed), getContext().getDrawable(R.drawable.ic_iconfinder_ic_cancel_48px_352263));
                                        }
                                    }
                                }
                            }
                        } else {
                            showMessageDialog(" Something went wrong. Please try again!!!", getContext().getDrawable(R.drawable.ic_iconfinder_ic_cancel_48px_352263));
                        }
                    } else if (response.body().response.statusCode == 400) {
                        showMessageDialog("Access Denied. Someone is using your credentials!!!", getContext().getDrawable(R.drawable.ic_iconfinder_ic_cancel_48px_352263));
                    }
                } else {
                    DbService.deleteAllKey();
//                    Toast.makeText(mContext, "Logout Successfully", Toast.LENGTH_SHORT).show();
                    MyPreference.putStr(mContext, MyPreference.ACCESS_TOKEN, "");
                    MyPreference.putStr(mContext, MyPreference.OPEN_ID, "");
                    SharePreferenceUtility.saveBooleanPreferences(mContext, Config.IS_ADMIN_LOGIN, false);
                    SharePreferenceUtility.saveObjectPreferences(mContext, KEY_VALUE, null);
                    SharePreferenceUtility.saveObjectPreferences(mContext, USER_KEY_VALUE, null);
                    SharePreferenceUtility.saveBooleanPreferences(mContext, Const.IS_LOGIN, false);
                    Intent intent = new Intent(mContext, SplashScreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
//                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CheckoutCheckResponse> call, Throwable t) {
                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void init() {
        services = new ApiServiceProvider(mContext).apiServices;
        mContext.registerReceiver(mReceiver, getIntentFilter());
    }

    private IntentFilter getIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleConstant.ACTION_BLE_DEVICE);
        intentFilter.addAction(BleConstant.ACTION_BLE_DISCONNECTED);
        return intentFilter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (alertDialog!=null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void sendUnlockTime() {
        String guest_id = (String) SharePreferenceUtility.getPreferences(mContext, Constants.AppConst.GUEST_ID, SharePreferenceUtility.PREFTYPE_STRING);
        String smo_id = (String) SharePreferenceUtility.getPreferences(mContext, Constants.AppConst.USER_ID, SharePreferenceUtility.PREFTYPE_STRING);
        String guest_type = (String) SharePreferenceUtility.getPreferences(getContext(), GUEST_TYPE, SharePreferenceUtility.PREFTYPE_STRING);
        String token = (String) SharePreferenceUtility.getPreferences(mContext, TOKEN, SharePreferenceUtility.PREFTYPE_STRING);


        Call<LockTimeUpdateResponse> checkoutCheckResponseCall = services.LOCK_TIME_UPDATE_RESPONSE_CALL(smo_id, guest_id, guest_type, String.valueOf(System.currentTimeMillis()), token);
        checkoutCheckResponseCall.enqueue(new Callback<LockTimeUpdateResponse>() {
            @Override
            public void onResponse(Call<LockTimeUpdateResponse> call, Response<LockTimeUpdateResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().response.statusCode == 200) {
                        Log.d("Send Unlock", response.body().response.response);
                    }
                }
            }

            @Override
            public void onFailure(Call<LockTimeUpdateResponse> call, Throwable t) {
                Log.d("Send Unlock", t.getMessage());
            }
        });

    }
}