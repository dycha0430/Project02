package com.example.madcamp_project2.ui;

import java.util.ArrayList;

public class User {
    static int global_id = 0;
    int id; //type 이 맞나?
    String name;
    String email;
    ArrayList<User> friends;
    ArrayList<User> pending_requests;
    ArrayList<TripPlan> myTrips;
    String profile;

    public User(String name, String email, String profile) {
        this.id = global_id++;
        this.name = name;
        this.email = email;
        this.profile = profile;
        friends = new ArrayList<>();
        myTrips = new ArrayList<>();
    }

    public User() {
        this.id = global_id++;
        this.name = "Test name";
        this.email = "Test email";
        this.profile = "";
        friends = new ArrayList<>();
        myTrips = new ArrayList<>();
    }

    public void setUser(User user) {
        this.id = user.id;
        this.name = user.name;
        this.email = user.email;
        this.profile = user.profile;
        this.friends = user.friends;
        this.myTrips = user.myTrips;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public ArrayList<TripPlan> getMyTrips() {
        return myTrips;
    }

    public void setMyTrips(ArrayList<TripPlan> myTrips) {
        this.myTrips = myTrips;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
