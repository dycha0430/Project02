package com.example.madcamp_project2.ui.home.addtrip.Travel;

import com.example.madcamp_project2.ui.Place;
import com.example.madcamp_project2.ui.Schedule;

public class NewSchedule {
    private int travel_id;
    private int day;
    private int money;
    private String memo;
    private int start_hour;
    private int start_minute;
    private int end_hour;
    private int end_minute;
    private String place_name;
    private String place_address;
    private int schedule_id;

    public NewSchedule(Schedule schedule, int travel_id, int day) {
        this.travel_id = travel_id;
        this.day = day;
        this.money = Integer.parseInt(schedule.getMoney().replace(",", ""));
        this.memo = schedule.getMemo();
        this.start_hour = schedule.getStart_time().getHour();
        this.start_minute = schedule.getStart_time().getMinute();
        this.end_hour = schedule.getEnd_time().getHour();
        this.end_minute = schedule.getEnd_time().getMinute();
        this.place_name = schedule.getPlace().getName();
        this.place_address = schedule.getPlace().getAddress();
        this.schedule_id = schedule.getSchedule_id();
    }

    public int getSchedule_id() {
        return schedule_id;
    }
}
