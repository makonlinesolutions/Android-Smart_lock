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

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mEtLoginId, mEtPassword;
    Button btn_login;
    TextView txt_label;

    String a;
    int keyDel;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        mContext = RegistrationActivity.this;

        txt_label = findViewById(R.id.text_label);
        txt_label.setText("REGISTRATION");

        mEtLoginId = findViewById(R.id.edt_mobile_num);
        mEtPassword = findViewById(R.id.edt_password);
        btn_login.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        String access_token = MyPreference.getStr(mContext, MyPreference.ACCESS_TOKEN);
//        String openid = MyPreference.getStr(mContext, MyPreference.OPEN_ID);
        ((TextView) findViewById(R.id.auth_access_token)).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.auth_openid)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.btn_sign_up)).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        final String username = mEtLoginId.getText().toString();
        final String password = mEtPassword.getText().toString();
        new AsyncTask<Void, Integer, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return ResponseService.sign_up(username, password);
            }

            @Override
            protected void onPostExecute(String json) {
                String msg = getString(R.string.words_authorize_successed);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.has("errcode")) {
                        msg = "Something went wrong";
                    } else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.putExtra("user_id",jsonObject.getString("username"));
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
