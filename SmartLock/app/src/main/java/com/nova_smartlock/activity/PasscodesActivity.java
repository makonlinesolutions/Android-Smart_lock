package com.nova_smartlock.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.nova_smartlock.R;
import com.nova_smartlock.adapter.PasscodeListAdapter;
import com.nova_smartlock.dao.DbService;
import com.nova_smartlock.model.Key;
import com.nova_smartlock.model.PasscodeListItem;
import com.nova_smartlock.model.PasscodeListResponse;
import com.nova_smartlock.net.ResponseService;
import com.nova_smartlock.utils.DisplayUtil;
import com.nova_smartlock.utils.SharePreferenceUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.nova_smartlock.utils.Const.KEY_VALUE;

public class PasscodesActivity extends AppCompatActivity {

    private Context mContext;
    private RecyclerView mRvPasscode;
    private List<Key> arrKey;
    private Key mKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcodes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mContext = PasscodesActivity.this;
        mRvPasscode = findViewById(R.id.rv_passcode);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRvPasscode.setLayoutManager(linearLayoutManager);
        arrKey = DbService.getKeyListKey();
        mKey = (Key) SharePreferenceUtility.getPreferences(mContext, KEY_VALUE, SharePreferenceUtility.PREFTYPE_OBJECT);

        getRequestPasscode();
    }

    private void getRequestPasscode() {

        new AsyncTask<Void, Integer, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return ResponseService.keyboardPwdList(mKey.getLockId(), 1, 1000);
            }

            @SuppressLint("NewApi")
            @Override
            protected void onPostExecute(String json) {
                String msg = "Operation failed";
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    Log.e("passcode response",json);
                    if (jsonObject.has("errcode")) {
                        msg = "Operation failed!";
                        if (jsonObject.getInt("errcode") == 0) {
                            DisplayUtil.showMessageDialog(mContext, msg, getDrawable(R.drawable.ic_iconfinder_143_attention_183267)); //ToDo change mesage
                        } else {
                            DisplayUtil.showMessageDialog(mContext, msg, getDrawable(R.drawable.ic_iconfinder_143_attention_183267)); //ToDo change mesage
                        }
                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        int page_number = jsonObject.getInt("pageNo");
                        int pageSize = jsonObject.getInt("pageSize");
                        int pages = jsonObject.getInt("pages");
                        int total = jsonObject.getInt("total");
                        List<PasscodeListItem> arListItems = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            PasscodeListItem passcodeListItem = new PasscodeListItem();
                            JSONObject tmp_data = jsonArray.getJSONObject(i);
                            passcodeListItem.setLockId(tmp_data.getInt("lockId"));
                            passcodeListItem.setKeyboardPwdVersion(tmp_data.getInt("keyboardPwdVersion"));
                            passcodeListItem.setEndDate(tmp_data.getLong("endDate"));
                            passcodeListItem.setSendDate(tmp_data.getLong("sendDate"));
                            passcodeListItem.setKeyboardPwdId(tmp_data.getInt("keyboardPwdId"));
                            passcodeListItem.setKeyboardPwd(tmp_data.getString("keyboardPwd"));
                            passcodeListItem.setKeyboardPwdType(tmp_data.getInt("keyboardPwdType"));
                            passcodeListItem.setStartDate(tmp_data.getLong("startDate"));
                            passcodeListItem.setStartDate(tmp_data.getLong("startDate"));
                            passcodeListItem.setReceiverUsername(tmp_data.getString("receiverUsername"));
                            passcodeListItem.setStatus(tmp_data.getInt("status"));
                            arListItems.add(passcodeListItem);
                        }
                        PasscodeListResponse passcodeListResponse = new PasscodeListResponse(arListItems, page_number, pageSize, pages, total);
                        PasscodeListAdapter passcodeListAdapter = new PasscodeListAdapter(mContext, passcodeListResponse.mPasscodeListItem);
                        mRvPasscode.setAdapter(passcodeListAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
