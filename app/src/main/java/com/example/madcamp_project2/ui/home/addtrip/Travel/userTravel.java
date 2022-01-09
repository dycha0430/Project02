package com.example.madcamp_project2.ui.home.addtrip.Travel;

import java.util.ArrayList;

public class userTravel {
    private String email;
    private ArrayList<GetTravel> travel_list;

    public userTravel(String email) {
        this.email = email;
        travel_list = new ArrayList<>();
    }

    public ArrayList<GetTravel> getTravel_list() {
        return travel_list;
    }
}
