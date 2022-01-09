package com.example.madcamp_project2.ui.home;

import com.example.madcamp_project2.ui.Schedule;

import java.util.Comparator;

public class ScheduleComparator implements Comparator<Schedule> {
    @Override
    public int compare(Schedule schedule, Schedule t1) {
        if (Schedule.TimeFormat.isBefore(t1.getStart_time(), schedule.getStart_time())) return 1;
        else if (Schedule.TimeFormat.isBefore(schedule.getStart_time(), t1.getStart_time())) return -1;
        return 0;
    }
}
