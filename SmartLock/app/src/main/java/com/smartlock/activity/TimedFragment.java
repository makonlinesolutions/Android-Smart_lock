package com.smartlock.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.smartlock.R;
import com.smartlock.model.Key;
import com.smartlock.net.ResponseService;
import com.smartlock.sp.MyPreference;
import com.smartlock.utils.DisplayUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.smartlock.activity.NearbyLockActivity.curKey;

public class TimedFragment extends Fragment {
    private TextView tv_start_time, tv_end_time;
    private String start_date_time, end_date_time;
    private long start_time_millisecond;
    private long end_time_millisecond;
    private EditText mEtAccount;
    private EditText mEtName;
    private Switch mSUnlock, mSAdmin;
    private Button mBtSend;
    private Key mKey;
    private int openid;
    private Context mContext;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timed, container, false);

        tv_start_time = view.findViewById(R.id.tv_start_time);
        tv_end_time = view.findViewById(R.id.tv_end_time);
        mSAdmin = view.findViewById(R.id.switch_admin);
        mSUnlock = view.findViewById(R.id.switch_unlock);
        mBtSend = view.findViewById(R.id.btn_send);
        mEtAccount = view.findViewById(R.id.edt_account);
        mContext = getContext();
        openid = MyPreference.getOpenid(getActivity(), MyPreference.OPEN_ID);
        if (curKey != null) {
            mKey = curKey;
        } else {
            startActivity(new Intent(getActivity(), NearbyLockActivity.class));
        }


        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd  HH:mm");
        Calendar calendar = Calendar.getInstance();

        String current_time_date = calendar.get(Calendar.YEAR) + ":" + String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + ":" + String.format("%02d", calendar.get(Calendar.DATE)) + "  " + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE));
        tv_start_time.setText(current_time_date);


        tv_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new SingleDateAndTimePickerDialog.Builder(getActivity())
                        .bottomSheet()
                        .curved()
                        .displayDays(false)
                        .displayMonth(true)
                        .displayYears(true)
                        .displayHours(true)
                        .displayMinutes(true)
                        .displayMonthNumbers(true)
                        .displayAmPm(false)
                        .displayDaysOfMonth(true)
                        .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                            @Override
                            public void onDisplayed(SingleDateAndTimePicker picker) {

                            }
                        })
                        .listener(new SingleDateAndTimePickerDialog.Listener() {
                            @Override
                            public void onDateSelected(Date date) {
                                start_time_millisecond = date.getTime();
                                start_date_time = simpleDateFormat.format(date);
                                tv_start_time.setText(start_date_time);
                            }
                        })
                        .display();
            }
        });


        tv_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SingleDateAndTimePickerDialog.Builder(getActivity())
                        .bottomSheet()
                        .curved()
                        .displayDays(false)
                        .displayMonth(true)
                        .displayYears(true)
                        .displayHours(true)
                        .displayMinutes(true)
                        .displayMonthNumbers(true)
                        .displayAmPm(false)
                        .displayDaysOfMonth(true)
                        .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                            @Override
                            public void onDisplayed(SingleDateAndTimePicker picker) {

                            }
                        })
                        .listener(new SingleDateAndTimePickerDialog.Listener() {
                            @Override
                            public void onDateSelected(Date date) {
                                end_time_millisecond = date.getTime();
                                end_date_time = simpleDateFormat.format(date);
                                tv_end_time.setText(end_date_time);
                            }
                        })
                        .display();
            }
        });

        mBtSend.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                final String user_name = mEtAccount.getText().toString().trim();

                if (!TextUtils.isEmpty(user_name)) {

                    new AsyncTask<Void, Integer, String>() {

                        @Override
                        protected String doInBackground(Void... params) {
                            return ResponseService.sendEKey(mKey, user_name, start_time_millisecond, end_time_millisecond);
                        }

                        @SuppressLint("NewApi")
                        @Override
                        protected void onPostExecute(String json) {
                            String msg = getContext().getString(R.string.words_authorize_successed);
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                if (jsonObject.has("errcode")) {
                                    msg = "Authentication failed!";
                                    if (jsonObject.getInt("errcode") == 0) {
                                        DisplayUtil.showMessageDialog(getContext(), msg, getActivity().getDrawable(R.drawable.ic_iconfinder_143_attention_183267)); //ToDo change mesage
                                        //Toast.makeText(mContext, "Now you can the credentials", Toast.LENGTH_SHORT).show();
                                    }else {
                                        DisplayUtil.showMessageDialog(getContext(), msg, getActivity().getDrawable(R.drawable.ic_iconfinder_143_attention_183267)); //ToDo change mesage
                                        //Toast.makeText(mContext, "Now you can the credentials", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    String access_token = jsonObject.getString("access_token");
                                    String openid = jsonObject.getString("openid");
                                    MyPreference.putStr(mContext, MyPreference.ACCESS_TOKEN, access_token);
                                    MyPreference.putStr(mContext, MyPreference.OPEN_ID, openid);
                                    /*Intent intent = new Intent(mContext, MainActivity.class);
                                    startActivity(intent);
                                    onResume();*/
                                    DisplayUtil.showMessageDialog(getContext(), "E-Key sent successfully", getActivity().getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                                    //Toast.makeText(mContext, "E-Key sent successfully", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute();
                } else {
                    DisplayUtil.showMessageDialog(getContext(), "Please enter user name", getActivity().getDrawable(R.drawable.ic_iconfinder_143_attention_183267));
                    //Toast.makeText(mContext, "Please enter user name", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }
}
