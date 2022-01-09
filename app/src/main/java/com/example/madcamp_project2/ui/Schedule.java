package com.example.madcamp_project2.ui;

import com.example.madcamp_project2.ui.home.addtrip.Travel.NewSchedule;

import java.io.Serializable;
import java.util.Comparator;

public class Schedule implements Serializable {

    public static class TimeFormat {
        int hour;
        int minute;

        public TimeFormat(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        public static boolean isBefore(TimeFormat a, TimeFormat b) {
            if (a.hour < b.hour) return true;
            else if (a.hour == b.hour && a.minute < b.minute) return true;

            return false;
        }

        public static boolean isSame(TimeFormat a, TimeFormat b) {
            return a.hour == b.hour && a.minute == b.minute;
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
    int schedule_id;

    public Schedule() {
        this.money = "";
        this.memo = "Memo";
        this.start_time = new TimeFormat(0, 0);
        this.end_time = new TimeFormat(12, 0);
        this.place = new Place();
        this.schedule_id = -1;
    }

    public Schedule(String money, String memo, TimeFormat start_time, TimeFormat end_time, Place place) {
        this.money = money;
        this.memo = memo;
        this.start_time = start_time;
        this.end_time = end_time;
        this.place = place;
        this.schedule_id = -1;
    }

    public Schedule(NewSchedule newSchedule) {
        this.money = toCurrencyFormat(newSchedule.getMoney());
        this.memo = newSchedule.getMemo();
        this.start_time = new TimeFormat(newSchedule.getStart_hour(), newSchedule.getStart_minute());
        this.end_time = new TimeFormat(newSchedule.getEnd_hour(), newSchedule.getEnd_minute());
        this.place = new Place(newSchedule.getPlace_name(), newSchedule.getPlace_address());
        this.schedule_id = newSchedule.getSchedule_id();
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

    public void setSchedule_id(int id) { this.schedule_id = id; }

    public int getSchedule_id() { return this.schedule_id; }

    public String toCurrencyFormat(String money) {
        String money_formatted = "";
        for(int i = 0; i < money.length(); i++){
            if((money.length() - i - 1) % 3 == 0 && i != money.length() - 1){
                money_formatted += Character.toString(money.charAt(i)) + ",";
            }else{
                money_formatted += Character.toString(money.charAt(i));
            }
        }
        return money_formatted;
    }
}
