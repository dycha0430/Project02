package com.example.madcamp_project2.ui.home.detail;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
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

import com.example.madcamp_project2.LoginActivity;
import com.example.madcamp_project2.MainActivity;
import com.example.madcamp_project2.MyAPI;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.Place;
import com.example.madcamp_project2.ui.Schedule;
import com.example.madcamp_project2.ui.TripPlan;
import com.example.madcamp_project2.ui.home.addtrip.Travel.NewSchedule;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;

import nl.joery.timerangepicker.TimeRangePicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    Schedule editSchedule = null;

    public BottomSheetDialog(Context context, Schedule schedule, int day, TripPlan tripPlan) {
        this.context = context;
        this.editSchedule = schedule;
        this.day = day;
        this.tripPlan = tripPlan;
    }
    public BottomSheetDialog(Context context, TripPlan tripPlan, String title, String address, int day) {
        this.context = context;
        this.tripPlan = tripPlan;
        this.title = title;
        this.address = address;
        this.day = day;
        editSchedule = null;
    }

    String getTimeString(int hour, int minute) {
        String text = "";

        if (hour < 10) text += "0" + hour;
        else text += hour + "";

        text += " : ";
        if (minute < 19) text += "0" + minute;
        else text += minute;

        return text;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog_layout, container, false);

        Schedule schedule = new Schedule();

        if (editSchedule != null) {
            schedule.setMoney(editSchedule.getMoney());
            schedule.setMemo(editSchedule.getMemo());
            schedule.setStart_time(editSchedule.getStart_time());
            schedule.setEnd_time(editSchedule.getEnd_time());
            schedule.setPlace(editSchedule.getPlace());
            schedule.setSchedule_id(editSchedule.getSchedule_id());
        }

        startTimeSelect = view.findViewById(R.id.startTimeSelect);
        endTimeSelect = view.findViewById(R.id.endTimeSelect);
        titleTextView = view.findViewById(R.id.titleTextView);
        addressTextView = view.findViewById(R.id.addressTextView);
        moneyEditText = view.findViewById(R.id.money);
        registerBtn = view.findViewById(R.id.registerBtn);
        memoEditText = view.findViewById(R.id.memo);

        String timeString = getTimeString(schedule.getStart_time().getHour(), schedule.getStart_time().getMinute());
        startTimeSelect.setText(timeString);
        timeString = getTimeString(schedule.getEnd_time().getHour(), schedule.getEnd_time().getMinute());
        endTimeSelect.setText(timeString);

        if (editSchedule != null) {
            addressTextView.setText(schedule.getPlace().getAddress());
            titleTextView.setText(schedule.getPlace().getName());
        } else {
            addressTextView.setText(address);
            titleTextView.setText(title);
        }

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
                            Toast.makeText(getContext(), "시작 시간이 종료 시간보다 늦습니다. 다시 선택해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String timeString = getTimeString(hour, minute);
                        startTimeSelect.setText(timeString);
                        schedule.setStart_time(new Schedule.TimeFormat(hour, minute));
                    }
                }, hour, minute, DateFormat.is24HourFormat(getContext()));

                timePickerDialog.updateTime(schedule.getStart_time().getHour(), schedule.getStart_time().getMinute());
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
                            Toast.makeText(getContext(), "종료 시간이 시작 시간보다 빠릅니다. 다시 선택해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String timeString = getTimeString(hour, minute);
                        endTimeSelect.setText(timeString);
                        schedule.setEnd_time(new Schedule.TimeFormat(hour, minute));
                    }
                }, hour, minute, DateFormat.is24HourFormat(getContext()));

                timePickerDialog.updateTime(schedule.getEnd_time().getHour(), schedule.getEnd_time().getMinute());
                timePickerDialog.show();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Schedule.TimeFormat start = schedule.getStart_time();
                Schedule.TimeFormat end = schedule.getEnd_time();

                for (Schedule s : tripPlan.getSchedule(day)) {
                    if (s.equals(editSchedule)) continue;
                    Schedule.TimeFormat s_start = s.getStart_time();
                    Schedule.TimeFormat s_end = s.getEnd_time();

                    if (((Schedule.TimeFormat.isBefore(s_start, start) || Schedule.TimeFormat.isSame(s_start, start)) && Schedule.TimeFormat.isBefore(start, s_end))
                            || (Schedule.TimeFormat.isBefore(s_start, end) && (Schedule.TimeFormat.isBefore(end, s_end) || Schedule.TimeFormat.isSame(end, s_end)))
                            || (Schedule.TimeFormat.isBefore(start, s_start) && Schedule.TimeFormat.isBefore(s_end, end))) {
                        Toast.makeText(context, "다른 일정과 시간이 겹칩니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                schedule.setMemo(memoEditText.getText().toString());
                String moneyText = moneyEditText.getText().toString();
                if (moneyText.equals("")) moneyText = "0";

                // money String에서 int 가져오는 코드
//                int money = 0;
//                if (moneyText != null && !moneyText.matches("")) {
//                    money = Integer.parseInt(moneyText.replaceAll(",", ""));
//                }

                schedule.setMoney(moneyText);
                schedule.setPlace(new Place(title, address));

                // TODO DB에도 추가 필요
                String token = "";

                String file_path = MainActivity.get_filepath();
                JSONParser parser = new JSONParser();

                try {
                    FileReader reader = new FileReader(file_path+"/userinfo.json");
                    Object obj = parser.parse(reader);
                    JSONObject jsonObject = (JSONObject) obj;
                    reader.close();

                    token = jsonObject.get("token").toString();
                }
                catch (IOException | ParseException e) {
                    e.printStackTrace();
                }

                Schedule schedule_to_post;

                if (editSchedule == null) {
                    tripPlan.getSchedule(day).add(schedule);
                    schedule_to_post = schedule;
                } else {
                    editSchedule.setMoney(schedule.getMoney());
                    editSchedule.setMemo(schedule.getMemo());
                    editSchedule.setStart_time(schedule.getStart_time());
                    editSchedule.setEnd_time(schedule.getEnd_time());
                    editSchedule.setSchedule_id(schedule.getSchedule_id());
                    schedule_to_post = editSchedule;
                }

                MyAPI myapi = LoginActivity.get_MyAPI();
                NewSchedule newSchedule = new NewSchedule(schedule_to_post, tripPlan.getTravel_id(), day);
                Call<NewSchedule> post_schedule = myapi.post_schedule("Bearer " + token, newSchedule);

                post_schedule.enqueue(new Callback<NewSchedule>() {
                    @Override
                    public void onResponse(Call<NewSchedule> call, Response<NewSchedule> response) {
                        if(response.isSuccessful()) {
                            NewSchedule responseSchedule = response.body();
                            Log.d("POST NEW SCHEDULE", "SUCCESS");
                            Log.d("POST NEW SCHEDULE", String.valueOf(responseSchedule.getSchedule_id()));
                            schedule_to_post.setSchedule_id(responseSchedule.getSchedule_id());
                        }
                        else {
                            Log.d("POST NEW SCHEDULE", "FAILED");
                        }
                    }

                    @Override
                    public void onFailure(Call<NewSchedule> call, Throwable t) {
                        Log.d("POST NEW SCHEDULE", "FAILED");
                    }
                });

                ViewPagerAdapter.scheduleAdapters[day].setSchedules(tripPlan.getSchedule(day));
                ViewPagerAdapter.scheduleAdapters[day].notifyDataSetChanged();

                dismiss();
            }
        });

        return view;
    }
}
