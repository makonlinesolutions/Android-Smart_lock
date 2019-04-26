package com.smartlock.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smartlock.R;
import com.smartlock.net.ResponseService;
import com.smartlock.sp.MyPreference;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mEtLoginId, mEtPassword;
    Button btn_login;
    String a;
    int keyDel;
    private Context mContext;
    String user_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = findViewById(R.id.btn_login);
        mContext = LoginActivity.this;

        mEtLoginId = findViewById(R.id.edt_mobile_num);
        mEtPassword = findViewById(R.id.edt_password);
        btn_login.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("user_id")) {
            user_name = intent.getStringExtra("user_id");
        }
        mEtLoginId.setText(user_name);
        ((Button) findViewById(R.id.btn_sign_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
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
        new AsyncTask<Void, Integer, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return ResponseService.auth(username, password);
            }

            @Override
            protected void onPostExecute(String json) {
                String msg = getString(R.string.words_authorize_successed);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.has("errcode")) {
                        msg = "Invalid client, client_id or client_secret error";
                    } else {
                        String access_token = jsonObject.getString("access_token");
                        String openid = jsonObject.getString("openid");
                        MyPreference.putStr(mContext, MyPreference.ACCESS_TOKEN, access_token);
                        MyPreference.putStr(mContext, MyPreference.OPEN_ID, openid);
                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                        onResume();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }
}
