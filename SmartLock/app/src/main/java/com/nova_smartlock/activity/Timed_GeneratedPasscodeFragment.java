package com.nova_smartlock.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.nova_smartlock.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Timed_GeneratedPasscodeFragment extends Fragment {
    TextView tv_start_time,tv_end_time;
    String start_date_time,end_date_time;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_timed_generatedpasscode, container, false);

        tv_start_time=view.findViewById(R.id.tv_start_time);
        tv_end_time=view.findViewById(R.id.tv_end_time);
        final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy:MM:dd  HH:mm");
        Calendar calendar=Calendar.getInstance();

        String current_time_date= calendar.get(Calendar.YEAR)+ ":" + String.format("%02d",(calendar.get(Calendar.MONTH) + 1)) + ":"  +String.format("%02d",calendar.get(Calendar.DATE)) + "  "+ String.format("%02d",calendar.get(Calendar.HOUR_OF_DAY)) +":" +String.format("%02d",calendar.get(Calendar.MINUTE));
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
                                end_date_time = simpleDateFormat.format(date);
                                tv_end_time.setText(end_date_time);
                            }
                        })
                        .display();
            }
        });
        return view;
    }
}
