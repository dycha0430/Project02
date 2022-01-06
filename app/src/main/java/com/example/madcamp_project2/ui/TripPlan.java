package com.example.madcamp_project2.ui;

import java.io.Serializable;
import java.util.ArrayList;

public class TripPlan implements Serializable {
    private static final long serialVersionUID = 1L;
    String title;
    String start_date;
    String end_date;
    // users 추가 필요
    Place destination;
    TripState state;
    ArrayList<Schedule> schedules;

    public TripPlan() {
        super();
        this.title = "제주도";
        this.start_date = "2022-01-01";
        this.end_date = "2022-01-03";
        this.destination = new Place();
        this.state = TripState.BEFORE;
    }

    public TripPlan(String title, String start_date, String end_date, Place destination, TripState state, ArrayList<Schedule> schedules) {
        this.title = title;
        this.start_date = start_date;
        this.end_date = end_date;
        this.destination = destination;
        this.state = state;
        this.schedules = schedules;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Place getDestination() {
        return destination;
    }

    public void setDestination(Place destination) {
        this.destination = destination;
    }

    public TripState getState() {
        return state;
    }

    public void setState(TripState state) {
        this.state = state;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(ArrayList<Schedule> schedules) {
        this.schedules = schedules;
    }
}
