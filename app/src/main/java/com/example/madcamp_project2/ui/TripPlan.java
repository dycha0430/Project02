package com.example.madcamp_project2.ui;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.madcamp_project2.ui.home.addtrip.Travel.GetTravel;
import com.example.madcamp_project2.ui.home.addtrip.Travel.NewSchedule;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TripPlan implements Serializable {
    private static final long serialVersionUID = 1L;
    String title;
    Date start_date;
    Date end_date;
    ArrayList<User> participants;
    Country destination;
    ArrayList<Schedule>[] schedules;
    private int travel_id;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public TripPlan() {
        super();
        this.travel_id = -1;
        this.title = "제주도";
        Calendar calendar = Calendar.getInstance();
        // Month에는 4월이면 3이 들어가야 함
        calendar.set(2022, 0, 5);
        this.start_date = calendar.getTime();
        calendar.set(2022, 0, 10);
        this.end_date = calendar.getTime();
        this.destination = new Country(CountryEnum.JEJU);

        int duration = getDuration();
        schedules = new ArrayList[5];
        for (int i = 0; i < duration; i++) {
            schedules[i] = new ArrayList<>();
        }

        this.participants = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public TripPlan(int id, String title, Date start_date, Date end_date, Country destination) {
        this.travel_id = id;
        this.title = title;
        this.start_date = start_date;
        this.end_date = end_date;
        this.destination = destination;

        int duration = getDuration();
        schedules = new ArrayList[duration];
        for (int i = 0; i < duration; i++) {
            schedules[i] = new ArrayList<>();
        }
        this.participants = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public TripPlan(GetTravel getTravel) {
        this.travel_id = getTravel.getTravel_id();
        this.title = getTravel.getTitle();
        this.start_date = getTravel.getStart_date();
        this.end_date = getTravel.getEnd_date();
        this.destination = new Country(getTravel.getPlace_name());
        this.participants = new ArrayList<>();

        // TODO: set schedules
        int duration = getDuration();
        schedules = new ArrayList[getDuration()];
        for (int i = 0; i < duration; i++) {
            schedules[i] = new ArrayList<>();
        }

        ArrayList<NewSchedule> schedule_list = getTravel.getSchedule_list();
        for (NewSchedule schedule : schedule_list) {
            schedules[schedule.getDay()].add(new Schedule(schedule));
        }
     }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getDuration() {
        LocalDate localStartDate = start_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localEndDate = end_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period period = Period.between(localStartDate, localEndDate);

        return period.getDays() + 1;
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

    public ArrayList<User> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<User> participants) {
//        for (User participant : participants) {
//            this.participants.add(participant);
//        }
        this.participants = participants;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public TripState getState() {
        long today = System.currentTimeMillis();
        LocalDate localStartDate = start_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localEndDate = end_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localTodayDate = LocalDate.now();

        TripState state;
        if (localTodayDate.isBefore(localStartDate)) {
            state = TripState.BEFORE;
        } else if (localEndDate.isBefore(localTodayDate)) {
            state = TripState.AFTER;
        } else {
            state = TripState.ING;
        }
        return state;
    }

    public ArrayList<Schedule> getSchedule(int day) {
        return schedules[day];
    }
    public ArrayList<Schedule>[] getSchedules() { return schedules; }

    public void setSchedules(ArrayList<Schedule> schedules, int day) {
        this.schedules[day] = schedules;
    }

    public void setTravel_id(int travel_id) {
        this.travel_id = travel_id;
    }
    public int getTravel_id() {
        return travel_id;
    }
}
