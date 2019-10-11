package com.nova_smartlock.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.nova_smartlock.R;
import com.nova_smartlock.adapter.RecordListAdapter;
import com.nova_smartlock.model.Key;
import com.nova_smartlock.model.RecordListItems;
import com.nova_smartlock.model.RecordListResponse;
import com.nova_smartlock.net.ResponseService;
import com.nova_smartlock.utils.DisplayUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nova_smartlock.activity.NearbyLockActivity.curKey;

public class RecordsActivity extends AppCompatActivity {
    Toolbar toolbar;
    CircleImageView profile_pic;
    RecyclerView recyclerView;
    RecordListAdapter recordListAdapter;
    private List<RecordListItems> arrRecordListItems;
    private RecordListResponse recordListResponse;
    private Key mKey;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        profile_pic = findViewById(R.id.img_user);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recylerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mKey = curKey;
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }


    @Override
    protected void onResume() {
        super.onResume();
        String myDate = "2019/01/01 01:10:45";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long start_time = date.getTime();
        long end_time = System.currentTimeMillis();
        getRequestRecordList(mKey.getLockId(), start_time, end_time, "1", "100");
    }

    private void getRequestRecordList(final int lockId, final long startTime, final long endTime, final String pageNumber, final String pageSize) {

        new AsyncTask<Void, String, String>() {

            @SuppressLint("NewApi")
            @Override
            protected String doInBackground(Void... params) {
                //you can synchronizes all key datas when lastUpdateDate is 0
                String json = ResponseService.getRecordLog(lockId, startTime, endTime, pageNumber, pageSize);
                try {
                    jsonObject = new JSONObject(json);
                    if (jsonObject.has("errcode")) {
                        DisplayUtil.showMessageDialog(RecordsActivity.this, "Couldn't get records!", getDrawable(R.drawable.ic_iconfinder_143_attention_183267));
                        //Toast.makeText(RecordsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        return json;
                    } else {
                        return json;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    arrRecordListItems = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject tmp_data = jsonArray.getJSONObject(i);
                        RecordListItems recordListItems = new RecordListItems(tmp_data.getInt("lockId"),
                                tmp_data.getInt("serverDate"),
                                tmp_data.getInt("recordType"),
                                tmp_data.getInt("success"),
                                tmp_data.getString("keyboardPwd"),
                                tmp_data.getInt("lockDate"),
                                tmp_data.getString("username"));
                        arrRecordListItems.add(recordListItems);
                    }
                  /*  recordListResponse = new RecordListResponse(jsonObject.getInt("total"),
                            jsonObject.getInt("pages"),
                            jsonObject.getInt("pageNo"),
                            jsonObject.getInt("pageSize"),
                            arrRecordListItems);*/

                    RecordListAdapter recordListAdapter = new RecordListAdapter(RecordsActivity.this, arrRecordListItems);
                      recyclerView.setAdapter(recordListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
