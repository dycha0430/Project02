package com.example.madcamp_project2.ui.home.addtrip.Travel;

public class TravelRequest {
    private String from_user_email;
    private String to_user_email;
    private int travel_id;
    private String status;

    public TravelRequest(String from_user_email, String to_user_email, int travel_id, String status) {
        this.from_user_email = from_user_email;
        this.to_user_email = to_user_email;
        this.travel_id = travel_id;
        this.status = status;
    }
}
