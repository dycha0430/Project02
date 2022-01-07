package com.example.madcamp_project2.ui;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Schedule implements Serializable {

    public static class TimeFormat {
        int hour;
        int minute;

        public TimeFormat(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }
    }

    int money;
    String memo;
    TimeFormat start_time;
    TimeFormat end_time;
    Place place;

    public Schedule() {
        this.money = 0;
        this.memo = "Memo";
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currentminute = Calendar.getInstance().get(Calendar.MINUTE);
        this.start_time = new TimeFormat(currentHour, currentminute);
        this.end_time = new TimeFormat(currentHour, currentminute);
        this.place = new Place();
    }

    public Schedule(int money, String memo, TimeFormat start_time, TimeFormat end_time, Place place) {
        this.money = money;
        this.memo = memo;
        this.start_time = start_time;
        this.end_time = end_time;
        this.place = place;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public TimeFormat getStart_time() {
        return start_time;
    }

    public void setStart_time(TimeFormat start_time) {
        this.start_time = start_time;
    }

    public TimeFormat getEnd_time() {
        return end_time;
    }

    public void setEnd_time(TimeFormat end_time) {
        this.end_time = end_time;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
