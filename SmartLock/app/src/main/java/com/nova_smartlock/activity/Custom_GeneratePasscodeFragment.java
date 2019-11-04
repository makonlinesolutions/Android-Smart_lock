package com.nova_smartlock.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.processbutton.FlatButton;
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

public class Custom_GeneratePasscodeFragment extends Fragment {
    private TextView tv_start_time, tv_end_time;
    private String start_date_time, end_date_time;
    private Button mBtGeneratePasscode;
    private View mParentView;
    private List<Key> arrKey;
    private Key mKey;
    private Context mContext;
    private long start_time_milisecond = 0;
    private long end_time_milisecond = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.custom_generatepasscode_fragment, container, false);
        mBtGeneratePasscode = mParentView.findViewById(R.id.btn_generate_passcode_custom);
        mContext = getContext();
        arrKey = DbService.getKeyListKey();
        mKey = (Key) SharePreferenceUtility.getPreferences(getContext(), KEY_VALUE, SharePreferenceUtility.PREFTYPE_OBJECT);
        mBtGeneratePasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start_time_milisecond == 0) {
                    DisplayUtil.showMessageDialog(getContext(), "Please select start date and time", getActivity().getDrawable(R.drawable.ic_iconfinder_143_attention_183267)); //ToDo change mesage
                } else if (end_time_milisecond == 0) {
                    DisplayUtil.showMessageDialog(getContext(), "Please select end date and time", getActivity().getDrawable(R.drawable.ic_iconfinder_143_attention_183267)); //ToDo change mesage
                } else {
                    openDialogForChangeKeyName();
                }
            }
        });
        tv_start_time = mParentView.findViewById(R.id.tv_start_time);
        tv_end_time = mParentView.findViewById(R.id.tv_end_time);
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
        return mParentView;
    }


    private void getRequestGeneratePasscode(final String passcode) {
        new AsyncTask<Void, Integer, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return ResponseService.getKeyboardPwdCustom(mKey.getLockId(), start_time_milisecond, end_time_milisecond, passcode, "1");
            }

            @SuppressLint("NewApi")
            @Override
            protected void onPostExecute(String json) {
                String msg = getContext().getString(R.string.words_authorize_successed);
                try {
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
//                            String keyboardPwd = jsonObject.getString("keyboardPwdId");
//                            String keyboardPwdId = jsonObject.getString("keyboardPwdId");

                        DisplayUtil.showMessageDialog(getContext(), "Passcode generated successfully. Your Passcode is: " + passcode, getActivity().getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                        //Toast.makeText(mContext, "E-Key sent successfully", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }
/*

    private void createCustomPasscode(long startDate,long endDate,String passcode){
//        ensureBluetoothIsEnabled();
//        showConnectLockToast();
        if(TextUtils.isEmpty(passcode)){
            Toast.makeText(mContext, "passcode is required", Toast.LENGTH_SHORT).show();
            return;
        }

        TTLockClient.getDefault().createCustomPasscode(passcode, startDate, endDate, mKey.getLockKey(), mKey.getLockMac(), new CreateCustomPasscodeCallback() {
            @Override
            public void onCreateCustomPasscodeSuccess(String passcode) {
                Toast.makeText(mContext, " passcode is created : " + passcode + " you can try it on lock now", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "passcode is created.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(LockError error) {
                Toast.makeText(mContext, error.getCommand(), Toast.LENGTH_SHORT).show();
//                makeErrorToast(error);
            }
        });
    }*/


    private void openDialogForChangeKeyName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dia_view = layoutInflater.inflate(R.layout.custom_edit_text_view, null);
        builder.setView(dia_view);
        final EditText edt_lock_name = dia_view.findViewById(R.id.edt_lockName);
        FlatButton button = dia_view.findViewById(R.id.fb_ChangeName);
//        final String name = "ADJ";
        edt_lock_name.setHint("Enter Passcode ");
        edt_lock_name.setInputType(InputType.TYPE_CLASS_NUMBER);

        final Dialog dialog = builder.create();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passcode = edt_lock_name.getText().toString().trim();
                if (TextUtils.isEmpty(passcode)) {
                    DisplayUtil.showMessageDialog(getContext(), "Please enter the passcode", getActivity().getDrawable(R.drawable.ic_iconfinder_143_attention_183267));
                } else {
                    if (NetworkUtils.isNetworkConnected(mContext)) {
                        getRequestGeneratePasscode(passcode);
//                        getRequestGeneratePasscode(passcode);
                        dialog.dismiss();
                    } else {
                        DisplayUtil.showMessageDialog(mContext, "Please check mobile network connection", getActivity().getDrawable(R.drawable.ic_no_internet));
                    }
                }
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }
}
