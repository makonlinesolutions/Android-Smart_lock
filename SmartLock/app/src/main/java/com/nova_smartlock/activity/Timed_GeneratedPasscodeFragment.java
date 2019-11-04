package com.nova_smartlock.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.nova_smartlock.R;
import com.nova_smartlock.dao.DbService;
import com.nova_smartlock.model.Key;
import com.nova_smartlock.net.ResponseService;
import com.nova_smartlock.utils.DisplayUtil;
import com.nova_smartlock.utils.NetworkUtils;
import com.nova_smartlock.utils.SharePreferenceUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.nova_smartlock.utils.Const.KEY_VALUE;

public class Timed_GeneratedPasscodeFragment extends Fragment {
    private TextView tv_start_time, tv_end_time;
    private String start_date_time, end_date_time;
    private Context mContext;
    private Button mBtGeneratePasscode;
    private List<Key> arrKey;
    private Key mKey;
    private long start_time_milisecond = 0;
    private long end_time_milisecond = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_timed_generatedpasscode, container, false);

        mContext = getContext();
        mBtGeneratePasscode = view.findViewById(R.id.btn_generate_passcode_time);
        tv_start_time=view.findViewById(R.id.tv_start_time);
        tv_end_time=view.findViewById(R.id.tv_end_time);
        arrKey = DbService.getKeyListKey();
        mKey = (Key) SharePreferenceUtility.getPreferences(getContext(), KEY_VALUE, SharePreferenceUtility.PREFTYPE_OBJECT);
        final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy:MM:dd  HH:mm");
        Calendar calendar=Calendar.getInstance();

        String current_time_date= calendar.get(Calendar.YEAR)+ ":" + String.format("%02d",(calendar.get(Calendar.MONTH) + 1)) + ":"  +String.format("%02d",calendar.get(Calendar.DATE)) + "  "+ String.format("%02d",calendar.get(Calendar.HOUR_OF_DAY)) +":" +String.format("%02d",calendar.get(Calendar.MINUTE));
//        tv_start_time.setText(current_time_date);


        tv_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SingleDateAndTimePickerDialog.Builder(getActivity())
                        .bottomSheet()
                        .curved()
                        .mustBeOnFuture()
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
                                start_time_milisecond = date.getTime();
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
                        .mustBeOnFuture()
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
                                end_time_milisecond = date.getTime();
                                end_date_time = simpleDateFormat.format(date);
                                tv_end_time.setText(end_date_time);
                            }
                        })
                        .display();
            }
        });


        mBtGeneratePasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkConnected(mContext)) {
                    getRequestGeneratePasscode();
                } else {
                    DisplayUtil.showMessageDialog(mContext, "Please check mobile network connection", getActivity().getResources().getDrawable(R.drawable.ic_no_internet));
                }
            }
        });
        return view;
    }


    private void getRequestGeneratePasscode() {

        if (start_time_milisecond == 0) {
            DisplayUtil.showMessageDialog(getContext(), "Please select start date and time", getActivity().getResources().getDrawable(R.drawable.ic_iconfinder_143_attention_183267)); //ToDo change mesage
        } else if (end_time_milisecond == 0) {
            DisplayUtil.showMessageDialog(getContext(), "Please select end date and time", getActivity().getResources().getDrawable(R.drawable.ic_iconfinder_143_attention_183267)); //ToDo change mesage

        } else {
            new AsyncTask<Void, Integer, String>() {

                @Override
                protected String doInBackground(Void... params) {
                    return ResponseService.getKeyboardPwdPermanent(mKey.getLockId(), 4, 3, start_time_milisecond, end_time_milisecond);
                }

                @SuppressLint("NewApi")
                @Override
                protected void onPostExecute(String json) {
                    try {
                        String msg = getContext().getString(R.string.words_authorize_successed);
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.has("errcode")) {
                            msg = "Operation failed!";
                            if (jsonObject.getInt("errcode") == 0) {
                                DisplayUtil.showMessageDialog(getContext(), msg, getActivity().getDrawable(R.drawable.ic_iconfinder_143_attention_183267)); //ToDo change mesage
                                //Toast.makeText(mContext, "Now you can the credentials", Toast.LENGTH_SHORT).show();
                            } else {
                                DisplayUtil.showMessageDialog(getContext(), msg, getActivity().getDrawable(R.drawable.ic_iconfinder_143_attention_183267)); //ToDo change mesage
                                //Toast.makeText(mContext, "Now you can the credentials", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String keyboardPwd = jsonObject.getString("keyboardPwd");
                            String keyboardPwdId = jsonObject.getString("keyboardPwdId");

                            DisplayUtil.showMessageDialog(getContext(), "Passcode generated successfully. Your Passcode is: " + keyboardPwd, getActivity().getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                            //Toast.makeText(mContext, "E-Key sent successfully", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        }
    }
}
