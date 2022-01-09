package com.example.madcamp_project2.ui.home.addtrip.Travel;

import android.provider.CalendarContract;
import android.util.Log;

import com.example.madcamp_project2.ui.Country;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class GetTravel {
    int travel_id;
    String title;
    String place_name;
    int start_year;
    int start_month;
    int start_day;
    int end_year;
    int end_month;
    int end_day;

    public GetTravel(int id, String title, Date start_date, Date end_date, Country destination) {
        this.travel_id = id;
        this.title = title;
        this.place_name = destination.getName();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start_date);
        this.start_day = calendar.get(Calendar.DAY_OF_MONTH);
        this.start_month = calendar.get(Calendar.MONTH) + 1;
        this.start_year = calendar.get(Calendar.YEAR);

        calendar.setTime(end_date);
        this.end_day = calendar.get(Calendar.DAY_OF_MONTH);
        this.end_month = calendar.get(Calendar.MONTH) + 1;
        this.end_year = calendar.get(Calendar.YEAR);
    }

    public int getTravel_id() {
        return travel_id;
    }

    public String getTitle() {
        return title;
    }

    public String getPlace_name() {
        return place_name;
    }

    public Date getStart_date() {
        GregorianCalendar calendar = new GregorianCalendar(start_year, start_month, start_day);
        return calendar.getTime();
    }

    public Date getEnd_date() {
        GregorianCalendar calendar = new GregorianCalendar(end_year, end_month, end_day);
        return calendar.getTime();
    }
}
