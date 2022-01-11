package com.example.madcamp_project2.ui.home.addtrip.Travel;

import com.example.madcamp_project2.ui.User;
import java.util.ArrayList;

public class TravelRequestSend {
    private String from_user_email;
    private ArrayList<String> to_user_email;
    private int travel_id;

    public TravelRequestSend(String from_user_email, ArrayList<User> selectedFriends, int travel_id) {
        this.from_user_email = from_user_email;
        this.to_user_email = new ArrayList<>();
        this.travel_id = travel_id;

        for(User friend : selectedFriends) {
            this.to_user_email.add(friend.getEmail());
        }
    }
}
