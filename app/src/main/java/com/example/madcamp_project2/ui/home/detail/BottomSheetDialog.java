package com.example.madcamp_project2.ui.home.detail;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.Place;
import com.example.madcamp_project2.ui.Schedule;
import com.example.madcamp_project2.ui.TripPlan;
import com.example.madcamp_project2.ui.home.HomeFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nl.joery.timerangepicker.TimeRangePicker;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    TimePicker timePicker;
    TimeRangePicker timeRangePicker;
    TripPlan tripPlan;
    Button startTimeSelect, endTimeSelect, registerBtn;
    Context context;
    String title;
    String address;
    TextView addressTextView, titleTextView;
    EditText moneyEditText, memoEditText;
    int day;

    public BottomSheetDialog(Context context, TripPlan tripPlan, String title, String address, int day) {
        this.context = context;
        this.tripPlan = tripPlan;
        this.title = title;
        this.address = address;
        this.day = day;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog_layout, container, false);

        Schedule schedule = new Schedule();
        startTimeSelect = view.findViewById(R.id.startTimeSelect);
        endTimeSelect = view.findViewById(R.id.endTimeSelect);
        titleTextView = view.findViewById(R.id.titleTextView);
        addressTextView = view.findViewById(R.id.addressTextView);
        moneyEditText = view.findViewById(R.id.money);
        registerBtn = view.findViewById(R.id.registerBtn);
        memoEditText = view.findViewById(R.id.memo);

        startTimeSelect.setText(schedule.getStart_time().getHour() + " : " + schedule.getStart_time().getMinute());
        endTimeSelect.setText(schedule.getEnd_time().getHour() + " : " + schedule.getEnd_time().getMinute());
        addressTextView.setText(address);
        titleTextView.setText(title);

        DecimalFormat df = new DecimalFormat("###,###,####");
        moneyEditText.addTextChangedListener(new NumberTextWatcher(moneyEditText));

        startTimeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mCalendar = Calendar.getInstance();
                int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = mCalendar.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();

                        int end_hour = schedule.getEnd_time().getHour();
                        int end_minute = schedule.getEnd_time().getMinute();

                        if (end_hour < hour || (end_hour == hour && end_minute < minute)) {
                            Toast.makeText(context, "시작 시간이 종료 시간보다 늦습니다. 다시 선택해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        startTimeSelect.setText(hour + " : " + minute);
                        schedule.setStart_time(new Schedule.TimeFormat(hour, minute));
                    }
                }, hour, minute, DateFormat.is24HourFormat(getContext()));

                timePickerDialog.show();
            }
        });

        endTimeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mCalendar = Calendar.getInstance();
                int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = mCalendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();

                        int start_hour = schedule.getStart_time().getHour();
                        int start_minute = schedule.getStart_time().getMinute();

                        if (start_hour > hour || (start_hour == hour && start_minute > minute)) {
                            Toast.makeText(context, "종료 시간이 시작 시간보다 빠릅니다. 다시 선택해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        endTimeSelect.setText(hour + " : " + minute);
                        schedule.setEnd_time(new Schedule.TimeFormat(hour, minute));
                    }
                }, hour, minute, DateFormat.is24HourFormat(getContext()));

                timePickerDialog.show();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                schedule.setMemo(memoEditText.getText().toString());
                String moneyText = moneyEditText.getText().toString();

                int money = 0;
                if (moneyText != null && !moneyText.matches("")) {
                    money = Integer.parseInt(moneyText.replaceAll(",", ""));
                }
                schedule.setMoney(money);
                schedule.setPlace(new Place(title, address));

                // TODO DB에도 추가 필요
                tripPlan.getSchedule(day).add(schedule);

                dismiss();
            }
        });

        return view;
    }
}
