package com.smartlock.activity;

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
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartlock.R;
import com.smartlock.db.DatabaseHelper;
import com.smartlock.db.LockDetails;
import com.smartlock.model.KeyDetails;
import com.smartlock.model.KeyDetailsResponse;
import com.smartlock.model.LoginResponse;
import com.smartlock.net.ResponseService;
import com.smartlock.retrofit.ApiServiceProvider;
import com.smartlock.retrofit.ApiServices;
import com.smartlock.sp.MyPreference;
import com.smartlock.utils.Const;
import com.smartlock.utils.Constants;
import com.smartlock.utils.NetworkUtils;
import com.smartlock.utils.SharePreferenceUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.smartlock.constant.Config.IS_ADMIN_LOGIN;
import static com.smartlock.utils.Constants.AppConst.GUEST_ID;
import static com.smartlock.utils.Constants.AppConst.ORDER_ID;
import static com.smartlock.utils.Constants.AppConst.TOKEN;
import static com.smartlock.utils.Constants.AppConst.USER_ID;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mEtLoginId, mEtPassword;
    Button btn_login;
    TextView txt_label;
    String a;
    int keyDel;
    private Context mContext;
    //    String user_name = "";
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
        txt_label.setText("LOGIN");

      /*  Intent intent = getIntent();
        if (intent != null && intent.hasExtra("user_id")) {
            user_name = intent.getStringExtra("user_id");
        }
        mEtLoginId.setText(user_name);*/
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
                Fragment_home.getInstance().showMessageDialog("Please check internet connection",getDrawable(R.drawable.ic_no_internet));
            }
        }
    }

    private void getRequestPMSLogin(String username, String password) {
        alertDialog.show();
        Call<LoginResponse> loginResponseCall = services.LOGIN_RESPONSE_OBSERVABLE(username, password);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();

                if (loginResponse != null) {
                    if (loginResponse.response.statusCode == 200) {
                        SharePreferenceUtility.saveStringPreferences(mContext, USER_ID, String.valueOf(loginResponse.response.smoId));
                        SharePreferenceUtility.saveStringPreferences(mContext, ORDER_ID, String.valueOf(loginResponse.response.orderId));
                        SharePreferenceUtility.saveStringPreferences(mContext, GUEST_ID, String.valueOf(loginResponse.response.guestId));
                        SharePreferenceUtility.saveStringPreferences(mContext, TOKEN, "Bearer " + String.valueOf(loginResponse.response.token));
                        callTTLogin();
                    } else {
                        alertDialog.dismiss();
                        showMessageDialog(loginResponse.response.message, getDrawable(R.drawable.ic_iconfinder_ic_cancel_48px_352263));
                    };
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
        final String username = mEtLoginId.getText().toString();
        final String password = mEtPassword.getText().toString();
        new AsyncTask<Void, Integer, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return ResponseService.auth(Constants.AppConst.NOVA_LOCK_ADMIN_USER_ID, Constants.AppConst.NOVA_LOCK_ADMIN_USER_PASSWORD);
            }

            @SuppressLint("NewApi")
            @Override
            protected void onPostExecute(String json) {
                String msg = getString(R.string.words_login_successed);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.has("errmsg")) {
                        alertDialog.dismiss();
                        if (TextUtils.isEmpty(username)) {
                            toast("Please enter mobile number");
                        } else if (TextUtils.isEmpty(password)) {
                            toast("Please enter password");
                        } else {
                            msg = "Invalid login credentials!\nTry again";
                            showMessageDialog(msg, getDrawable(R.drawable.ic_iconfinder_ic_cancel_48px_352263));
                        }
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
                        if (key_details.size() > 0) {

                            DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
                            databaseHelper.deleteAllData();

                            for (int j = 0; j < key_details.size(); j++) {
                                databaseHelper.insertLock(key_details.get(j));
                            }
                            List<LockDetails> tmp_data = databaseHelper.getAllLock();
                            if (tmp_data.size() > 0) {
                                alertDialog.dismiss();
                                SharePreferenceUtility.saveBooleanPreferences(mContext, Const.IS_LOGIN, true);
                                SharePreferenceUtility.saveBooleanPreferences(mContext, IS_ADMIN_LOGIN, false);
                                Intent intent = new Intent(mContext, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


//                                showMessageDialog("Login Successfully", getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                            } else {
                                alertDialog.dismiss();
                                showMessageDialog("Oops, No Any Lock assign!!!", getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                            }
//                        Toast.makeText(mContext, "" + keyDetailsResponse.response.status, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        alertDialog.dismiss();
                        showMessageDialog("No data found, Please contact to administrator!!!", getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                    }
                } else {
                    alertDialog.dismiss();
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
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are You Sure You Want To Exit?")
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

                if (msg.equals("Login Successfully")) {
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
