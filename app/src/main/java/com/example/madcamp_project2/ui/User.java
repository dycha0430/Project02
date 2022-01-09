package com.example.madcamp_project2.ui;

public class User {
    int id; //type 이 맞나?
    String name;
    String email;
    User[] friends;
    TripPlan myTrips;
    String profile;

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

    public User[] getFriends() {
        return friends;
    }

    public void setFriends(User[] friends) {
        this.friends = friends;
    }

    public TripPlan getMyTrips() {
        return myTrips;
    }

    public void setMyTrips(TripPlan myTrips) {
        this.myTrips = myTrips;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
