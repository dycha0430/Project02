package com.example.madcamp_project2.ui;

import java.io.Serializable;

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

    String money;
    String memo;
    TimeFormat start_time;
    TimeFormat end_time;
    Place place;

    public Schedule() {
        this.money = "";
        this.memo = "Memo";
        this.start_time = new TimeFormat(0, 0);
        this.end_time = new TimeFormat(12, 0);
        this.place = new Place();
    }

    public Schedule(String money, String memo, TimeFormat start_time, TimeFormat end_time, Place place) {
        this.money = money;
        this.memo = memo;
        this.start_time = start_time;
        this.end_time = end_time;
        this.place = place;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
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
