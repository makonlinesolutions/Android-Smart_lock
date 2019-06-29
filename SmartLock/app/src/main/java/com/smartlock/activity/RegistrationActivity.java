package com.smartlock.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartlock.R;
import com.smartlock.net.ResponseService;
import com.smartlock.sp.MyPreference;
import com.smartlock.utils.DisplayUtil;

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
        setContentView(R.layout.activity_registration);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btn_login = findViewById(R.id.btn_login);
        mContext = RegistrationActivity.this;

        txt_label = findViewById(R.id.text_label);

        mEtLoginId = findViewById(R.id.edt_mobile_num);
        mEtPassword = findViewById(R.id.edt_password);
        btn_login.setOnClickListener(this);

        ((TextView) findViewById(R.id.text_label)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

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
                String msg = getString(R.string.words_registration_successed);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.has("errcode")) {
                        if (TextUtils.isEmpty(username)){
                            toast("Please enter mobile number");
                        }else if (TextUtils.isEmpty(password)){
                            toast("Please enter password");
                        }else {
                            msg = "Registration failed!";
                            showMessageDialog(msg,"",R.drawable.ic_iconfinder_ic_cancel_48px_352263);
                        }
                    } else {
                        showMessageDialog(msg,jsonObject.getString("username"),R.drawable.ic_iconfinder_ok_2639876);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private void showMessageDialog(final String msg, final String user_id, int resId){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_unlock_dialog, null, false);
        builder.setView(view);
        final Dialog dialog = builder.create();
        TextView textView = view.findViewById(R.id.tv_unlock);
        TextView tv_message = view.findViewById(R.id.tv_message);
        ImageView imageView = view.findViewById(R.id.iv_lock);
        tv_message.setText(msg);
        imageView.setBackgroundResource(resId);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (msg.equalsIgnoreCase(getString(R.string.words_registration_successed))) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                    onResume();
                }else {
                    dialog.dismiss();
                }
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void toast(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
