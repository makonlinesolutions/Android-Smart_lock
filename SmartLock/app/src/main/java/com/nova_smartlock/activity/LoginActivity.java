package com.nova_smartlock.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.nova_smartlock.BuildConfig;
import com.nova_smartlock.R;
import com.nova_smartlock.db.DatabaseHelper;
import com.nova_smartlock.db.LockDetails;
import com.nova_smartlock.model.KeyDetails;
import com.nova_smartlock.model.KeyDetailsResponse;
import com.nova_smartlock.model.LoginResponse;
import com.nova_smartlock.net.ResponseService;
import com.nova_smartlock.retrofit.ApiServiceProvider;
import com.nova_smartlock.retrofit.ApiServices;
import com.nova_smartlock.sp.MyPreference;
import com.nova_smartlock.utils.Const;
import com.nova_smartlock.utils.DisplayUtil;
import com.nova_smartlock.utils.NetworkUtils;
import com.nova_smartlock.utils.SharePreferenceUtility;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.nova_smartlock.constant.Config.IS_ADMIN_LOGIN;
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
import static com.nova_smartlock.utils.Constants.AppConst.GUEST_ID;
import static com.nova_smartlock.utils.Constants.AppConst.GUEST_TYPE;
import static com.nova_smartlock.utils.Constants.AppConst.KIDS;
import static com.nova_smartlock.utils.Constants.AppConst.ORDER_ID;
import static com.nova_smartlock.utils.Constants.AppConst.ORDER_ON;
import static com.nova_smartlock.utils.Constants.AppConst.ORDER_STATUS;
import static com.nova_smartlock.utils.Constants.AppConst.ORDER_TYPE;
import static com.nova_smartlock.utils.Constants.AppConst.ROOM_NO;
import static com.nova_smartlock.utils.Constants.AppConst.ROOM_SHORT;
import static com.nova_smartlock.utils.Constants.AppConst.ROOM_TYPE;
import static com.nova_smartlock.utils.Constants.AppConst.TOKEN;
import static com.nova_smartlock.utils.Constants.AppConst.USER_ID;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mEtLoginId, mEtPassword;
    FloatingActionButton btn_login;
    TextView txt_label;
    String a;
    int keyDel;
    private Context mContext;
    private ApiServices services;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        mContext = LoginActivity.this;
        mEtLoginId = findViewById(R.id.edt_mobile_num);
        mEtPassword = findViewById(R.id.edt_password);
        btn_login.setOnClickListener(this);

        txt_label = findViewById(R.id.text_label);
        txt_label.setText("GUEST");

        ((TextView)findViewById(R.id.tv_version)).setText("Version Name: "  + BuildConfig.VERSION_NAME);

        ((TextView) findViewById(R.id.tv_register)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        services = new ApiServiceProvider(getApplicationContext()).apiServices;
        alertDialog = new SpotsDialog.Builder().setContext(mContext).setMessage("Loading").build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String access_token = MyPreference.getStr(mContext, MyPreference.ACCESS_TOKEN);
        String openid = MyPreference.getStr(mContext, MyPreference.OPEN_ID);
        ((TextView) findViewById(R.id.auth_access_token)).setText(access_token);
        ((TextView) findViewById(R.id.auth_openid)).setText(openid);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        final String username = mEtLoginId.getText().toString();
        final String password = mEtPassword.getText().toString();
        validateEntries(username, password);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void validateEntries(String username, String password) {
        boolean IS_ENTRY = true;
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(mContext, "Please enter user name", Toast.LENGTH_SHORT).show();
            IS_ENTRY = false;
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(mContext, "Please enter password", Toast.LENGTH_SHORT).show();
            IS_ENTRY = false;
            return;
        }
        if (IS_ENTRY) {
            if (NetworkUtils.isNetworkConnected(mContext)) {
                getRequestPMSLogin(username, password);
            }else {
                DisplayUtil.showMessageDialog(mContext, "Please check Mobile network connection", getDrawable(R.drawable.ic_no_internet));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getRequestPMSLogin(String username, String password) {
        alertDialog.show();
        Call<LoginResponse> loginResponseCall = services.LOGIN_RESPONSE_OBSERVABLE(username, password);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if (loginResponse != null) {
                    if (loginResponse.response.statusCode == 200) {
                        SharePreferenceUtility.saveStringPreferences(mContext, USER_ID, String.valueOf(loginResponse.response.smoId));
                        SharePreferenceUtility.saveStringPreferences(mContext, ORDER_ID, String.valueOf(loginResponse.response.orderId));
                        SharePreferenceUtility.saveStringPreferences(mContext, GUEST_ID, String.valueOf(loginResponse.response.guestId));
                        SharePreferenceUtility.saveStringPreferences(mContext, GUEST_TYPE, String.valueOf(loginResponse.response.guest_type));
                        SharePreferenceUtility.saveStringPreferences(mContext, CHECK_IN_DATE, loginResponse.response.checkInDate);
                        SharePreferenceUtility.saveStringPreferences(mContext, CHECK_OUT_DATE, loginResponse.response.checkOutDate);
                        SharePreferenceUtility.saveStringPreferences(mContext, CHECK_IN_TIME, loginResponse.response.checkInTime);
                        SharePreferenceUtility.saveStringPreferences(mContext, CHECK_OUT_TIME, loginResponse.response.checkOutTime);
                        SharePreferenceUtility.saveStringPreferences(mContext, ORDER_TYPE, loginResponse.response.orderType);
                        SharePreferenceUtility.saveIntPreferences(mContext, ORDER_STATUS, loginResponse.response.orderStatus);
                        SharePreferenceUtility.saveStringPreferences(mContext, TOKEN, "Bearer " + String.valueOf(loginResponse.response.token));

                        callTTLogin();
                    } else {
                        alertDialog.dismiss();
                        showMessageDialog(loginResponse.response.message, getDrawable(R.drawable.ic_iconfinder_ic_cancel_48px_352263));
                    }
                } else {
                    alertDialog.dismiss();
                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                alertDialog.dismiss();
                Toast.makeText(mContext, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callTTLogin() {
        final String username = (String) SharePreferenceUtility.getPreferences(mContext, Const.ADMIN_LOGIN, SharePreferenceUtility.PREFTYPE_STRING) ;
                //mEtLoginId.getText().toString();
        final String password = (String) SharePreferenceUtility.getPreferences(mContext, Const.ADMIN_PASSWORD, SharePreferenceUtility.PREFTYPE_STRING);
                //mEtPassword.getText().toString();
        Log.i("uname",username+"-"+password);
        new AsyncTask<Void, Integer, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return ResponseService.auth(username, password);
            }
            @SuppressLint("NewApi")
            @Override
            protected void onPostExecute(String json) {
                String msg = getString(R.string.words_login_successed);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.has("errmsg")) {
                        alertDialog.dismiss();
                       /* if (TextUtils.isEmpty(username)) {

                            toast("Please enter mobile number");
                        } else if (TextUtils.isEmpty(password)) {
                            toast("Please enter password");
                        } else {
                        }*/
                        msg = "Incorrect Username or Password! Please Try again";
                        showMessageDialog(msg, getDrawable(R.drawable.ic_iconfinder_ic_cancel_48px_352263));
                    } else {
                        String access_token = jsonObject.getString("access_token");
                        String openid = jsonObject.getString("openid");
                        MyPreference.putStr(mContext, MyPreference.ACCESS_TOKEN, access_token);
                        MyPreference.putStr(mContext, MyPreference.OPEN_ID, openid);
                        getKeyDetails();
                    }
                } catch (JSONException e) {
                    alertDialog.dismiss();
                    e.printStackTrace();
                }
                //Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private void getKeyDetails() {
        String user_id = (String) SharePreferenceUtility.getPreferences(mContext, USER_ID, SharePreferenceUtility.PREFTYPE_STRING);
        String token = (String) SharePreferenceUtility.getPreferences(mContext, TOKEN, SharePreferenceUtility.PREFTYPE_STRING);
        Call<KeyDetailsResponse> keyDetailsResponseCall = services.KEY_DETAILS_OBSERVABLE(user_id, token);
        keyDetailsResponseCall.enqueue(new Callback<KeyDetailsResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<KeyDetailsResponse> call, Response<KeyDetailsResponse> response) {
                KeyDetailsResponse keyDetailsResponse = response.body();
                if (keyDetailsResponse != null) {
                    if (keyDetailsResponse.response.statusCode == 200) {
                        List<KeyDetails> key_details = keyDetailsResponse.response.response;
                        if(keyDetailsResponse.response.orderDetails != null){
                            SharePreferenceUtility.saveStringPreferences(LoginActivity.this, ROOM_NO, keyDetailsResponse.response.orderDetails.roomNo);
                            SharePreferenceUtility.saveStringPreferences(LoginActivity.this, ROOM_SHORT, keyDetailsResponse.response.orderDetails.shortCode);
                            SharePreferenceUtility.saveStringPreferences(LoginActivity.this, CHECK_IN, keyDetailsResponse.response.orderDetails.checkIn);
                            SharePreferenceUtility.saveStringPreferences(LoginActivity.this, CHECK_OUT, keyDetailsResponse.response.orderDetails.checkOut);
                            SharePreferenceUtility.saveIntPreferences(LoginActivity.this, ADULTS, keyDetailsResponse.response.orderDetails.adults);
                            SharePreferenceUtility.saveIntPreferences(LoginActivity.this, KIDS, keyDetailsResponse.response.orderDetails.kids);
                            SharePreferenceUtility.saveStringPreferences(LoginActivity.this, ORDER_ON, keyDetailsResponse.response.orderDetails.orderedOn);
                            SharePreferenceUtility.saveStringPreferences(LoginActivity.this, ARRIVE_TIME, keyDetailsResponse.response.orderDetails.arriveTime);
                            SharePreferenceUtility.saveStringPreferences(LoginActivity.this, DEPARTURE_TIME, keyDetailsResponse.response.orderDetails.departureTime);
                            SharePreferenceUtility.saveStringPreferences(LoginActivity.this, GROUP_NAME, keyDetailsResponse.response.orderDetails.groupName);
                            SharePreferenceUtility.saveStringPreferences(LoginActivity.this, GROUP_CODE, keyDetailsResponse.response.orderDetails.groupCode);
                            SharePreferenceUtility.saveStringPreferences(LoginActivity.this, ROOM_TYPE, keyDetailsResponse.response.orderDetails.title);
                        }
                        if (key_details.size() > 0) {
                            DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
                            databaseHelper.deleteAllData();
                            for (int j = 0; j < key_details.size(); j++) {
                                databaseHelper.insertLock(key_details.get(j));
                            }
                            List<LockDetails> tmp_data = databaseHelper.getAllLock();
                            if (tmp_data.size() > 0) {
                                alertDialog.dismiss();
                                Toast.makeText(mContext, "Access Validated! - Login successful!", Toast.LENGTH_SHORT).show();
                                SharePreferenceUtility.saveBooleanPreferences(mContext, Const.IS_LOGIN, true);
                                SharePreferenceUtility.saveBooleanPreferences(mContext, IS_ADMIN_LOGIN, false);
                                Intent intent = new Intent(mContext, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                                showMessageDialog("Access Validated! - Login successful!", getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                            } else {
                                alertDialog.dismiss();
                                showMessageDialog("Oops, No Any Lock assign!!!", getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                            }
//                        Toast.makeText(mContext, "" + keyDetailsResponse.response.status, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (alertDialog!=null && alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                        showMessageDialog(getResources().getString(R.string.contact_admin), getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                    }
                } else {
                    if (alertDialog!=null && alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                    showMessageDialog("No data found, Please contact to administrator!!!", getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                }
            }
            @Override
            public void onFailure(Call<KeyDetailsResponse> call, Throwable t) {
                alertDialog.dismiss();
                Toast.makeText(mContext, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog!=null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.this.finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private void showMessageDialog(final String msg, Drawable drawable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_unlock_dialog, null, false);
        builder.setView(view);
        final Dialog dialog = builder.create();
        TextView textView = view.findViewById(R.id.tv_unlock);
        TextView tv_message = view.findViewById(R.id.tv_message);
        ImageView imageView = view.findViewById(R.id.iv_lock);
        tv_message.setText(msg);
        imageView.setBackground(drawable);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (msg.equals("Access Validated! - Login successful!")) {
                    SharePreferenceUtility.saveBooleanPreferences(mContext, IS_ADMIN_LOGIN, false);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    onResume();
                } else {
                    dialog.dismiss();
                }
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void toast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
