package com.example.madcamp_project2.ui.home.addtrip.Travel;

import android.provider.CalendarContract;
import android.util.Log;

import com.example.madcamp_project2.ui.Country;
import com.example.madcamp_project2.ui.friends.Friend.Friend;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class GetTravel {
    private int travel_id;
    private String title;
    private String place_name;
    private int start_year;
    private int start_month;
    private int start_day;
    private int end_year;
    private int end_month;
    private int end_day;
    private ArrayList<NewSchedule> schedule_list;
    private ArrayList<Friend> participant_list;

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

        this.schedule_list = new ArrayList<>();
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
        GregorianCalendar calendar = new GregorianCalendar(start_year, start_month - 1, start_day);
        return calendar.getTime();
    }

    public Date getEnd_date() {
        GregorianCalendar calendar = new GregorianCalendar(end_year, end_month - 1, end_day);
        return calendar.getTime();
    }

    public ArrayList<NewSchedule> getSchedule_list() {
        return schedule_list;
    }
}
