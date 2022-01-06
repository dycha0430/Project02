package com.example.madcamp_project2.ui;

import java.io.Serializable;

public class Place implements Serializable {
    public Place() {
        name = "제주도";
        address = "test address";
        latitude = 0;
        longitude = 0;
    }

    public Place(String name, String address, float latitude, float longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    String name;
    String address;
    float latitude;
    float longitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
