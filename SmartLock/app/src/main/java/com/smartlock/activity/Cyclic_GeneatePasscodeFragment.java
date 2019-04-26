package com.smartlock.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.smartlock.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Cyclic_GeneatePasscodeFragment extends Fragment {
    TextView tv_start_time,tv_end_time,tv_mode;
    String start_date_time,end_date_time,mode;
    NumberPicker day_picker;
    LinearLayout bottom_sheet;
    ImageView close_img;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_cyclic_generatedpasscode, container, false);
        tv_start_time=view.findViewById(R.id.tv_start_time);
        tv_end_time=view.findViewById(R.id.tv_end_time);
        tv_mode=view.findViewById(R.id.tv_mode);
        day_picker=view.findViewById(R.id.day_picker);
        bottom_sheet=view.findViewById(R.id.bottom_sheet);
        close_img=view.findViewById(R.id.img_close);

        final  String[] days=new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        day_picker.setMaxValue(6);
        day_picker.setMinValue(0);
        day_picker.setDisplayedValues(days);


        final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
        Calendar calendar=Calendar.getInstance();

        String current_time_date= String.format("%02d",calendar.get(Calendar.HOUR_OF_DAY)) +":" +String.format("%02d",calendar.get(Calendar.MINUTE));
        tv_start_time.setText(current_time_date);


        tv_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new SingleDateAndTimePickerDialog.Builder(getActivity())
                        .bottomSheet()
                        .curved()
                        .displayHours(true)
                        .displayMinutes(true)
                        .displayAmPm(false)
                        .displayDays(false)
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
                        .displayHours(true)
                        .displayMinutes(true)
                        .displayAmPm(false)
                        .displayDays(false)
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


        tv_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_sheet.setVisibility(View.VISIBLE);
            }
        });

        close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_sheet.setVisibility(View.GONE);
            }
        });

        day_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                tv_mode.setText(days[day_picker.getValue()].toString());
            }
        });
        return view;
    }
}
