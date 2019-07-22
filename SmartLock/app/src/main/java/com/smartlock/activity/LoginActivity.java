package com.smartlock.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.smartlock.model.KeyDetails;
import com.smartlock.model.KeyDetailsResponse;
import com.smartlock.model.LoginResponse;
import com.smartlock.net.ResponseService;
import com.smartlock.retrofit.ApiServiceProvider;
import com.smartlock.retrofit.ApiServices;
import com.smartlock.retrofit.RetrofitBase;
import com.smartlock.sp.MyPreference;
import com.smartlock.utils.Const;
import com.smartlock.utils.DisplayUtil;
import com.smartlock.utils.SharePreferenceUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mEtLoginId, mEtPassword;
    Button btn_login;
    TextView txt_label;
    String a;
    int keyDel;
    private Context mContext;
//    String user_name = "";
    ApiServices services ;
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

       validateEntries(username,password);

    }

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
            getRequestPMSLogin(username,password);
        }
    }

    private void getRequestPMSLogin(String username, String password) {
        Call<LoginResponse> loginResponseCall = services.LOGIN_RESPONSE_OBSERVABLE(username, password);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();

                if(loginResponse.response.statusCode == 200) {
                    Toast.makeText(mContext, ""+loginResponse.response.status, Toast.LENGTH_SHORT).show();
                    callTTLogin();
                }else {
                    Toast.makeText(mContext, ""+loginResponse.response.status, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callTTLogin() {
        final String username = mEtLoginId.getText().toString();
        final String password = mEtPassword.getText().toString();
        new AsyncTask<Void, Integer, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return ResponseService.auth("novatest_7276469974", "123456");
            }

            @SuppressLint("NewApi")
            @Override
            protected void onPostExecute(String json) {
                String msg = getString(R.string.words_login_successed);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.has("errmsg")) {
                        if (TextUtils.isEmpty(username)) {
                            toast("Please enter mobile number");
                        } else if (TextUtils.isEmpty(password)) {
                            toast("Please enter password");
                        } else {
                            msg = "Invalid login credentials!\nTry again";
                            showMessageDialog(msg, getDrawable(R.drawable.ic_iconfinder_ic_cancel_48px_352263));
                        }
                        getKeyDetails();
                    } else {
                        SharePreferenceUtility.saveBooleanPreferences(mContext, Const.IS_LOGIN, true);
                        showMessageDialog(msg, getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                        String access_token = jsonObject.getString("access_token");
                        String openid = jsonObject.getString("openid");
                        MyPreference.putStr(mContext, MyPreference.ACCESS_TOKEN, access_token);
                        MyPreference.putStr(mContext, MyPreference.OPEN_ID, openid);
                        getKeyDetails();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private void getKeyDetails() {
        Call<KeyDetailsResponse> keyDetailsResponseCall = services.KEY_DETAILS_OBSERVABLE("1");
        keyDetailsResponseCall.enqueue(new Callback<KeyDetailsResponse>() {
            @Override
            public void onResponse(Call<KeyDetailsResponse> call, Response<KeyDetailsResponse> response) {
                KeyDetailsResponse keyDetailsResponse = response.body();

                if(keyDetailsResponse.response.statusCode == 200) {
                   List<KeyDetails> key_details = keyDetailsResponse.response.response ;
                   if(key_details.size() > 0){
                       Toast.makeText(mContext, ""+keyDetailsResponse.response.status, Toast.LENGTH_SHORT).show();
                   }
                }else if(keyDetailsResponse.response.statusCode == 400){
                    Toast.makeText(mContext, ""+keyDetailsResponse.response.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KeyDetailsResponse> call, Throwable t) {
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

                if (msg.equalsIgnoreCase(getString(R.string.words_login_successed))) {
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
