package com.example.madcamp_project2.ui;

import java.io.Serializable;
import java.util.Date;

public class Schedule implements Serializable {
    int money;
    String memo;
    Date start_time;
    Date end_time;
    Place place;

    public Schedule() {
        this.money = 0;
        this.memo = "Memo";
        this.start_time = new Date();
        this.end_time = new Date();
        this.place = new Place();
    }

    public Schedule(int money, String memo, Date start_time, Date end_time, Place place) {
        this.money = money;
        this.memo = memo;
        this.start_time = start_time;
        this.end_time = end_time;
        this.place = place;
    }
}
