package com.example.madcamp_project2.ui;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TripPlan implements Serializable {
    private static final long serialVersionUID = 1L;
    String title;
    Date start_date;
    Date end_date;
    int duration;
    // users 추가 필요
    Country destination;
    TripState state;
    ArrayList<Schedule>[] schedules;

    public TripPlan() {
        super();
        this.title = "제주도";
        Calendar calendar = Calendar.getInstance();
        // Month에는 4월이면 3이 들어가야 함
        calendar.set(2022, 0, 5);
        this.start_date = calendar.getTime();
        calendar.set(2022, 0, 10);
        this.end_date = calendar.getTime();
        this.destination = new Country(CountryEnum.JEJU);
        this.state = TripState.BEFORE;
        schedules = new ArrayList[5];

        duration = 5;
        for (int i = 0; i < duration; i++) {
            schedules[i] = new ArrayList<>();
        }
    }

    public TripPlan(String title, Date start_date, Date end_date, Country destination, TripState state) {
        this.title = title;
        this.start_date = start_date;
        this.end_date = end_date;
        this.destination = destination;
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Country getDestination() {
        return destination;
    }

    public void setDestination(Country destination) {
        this.destination = destination;
    }

    public TripState getState() {
        return state;
    }

    public void setState(TripState state) {
        this.state = state;
    }

    public ArrayList<Schedule> getSchedule(int day) {
        return schedules[day];
    }
    public ArrayList<Schedule>[] getSchedules() { return schedules; }

    public void setSchedules(ArrayList<Schedule> schedules, int day) {
        this.schedules[day] = schedules;
    }
}
