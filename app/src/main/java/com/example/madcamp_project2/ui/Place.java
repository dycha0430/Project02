package com.example.madcamp_project2.ui;

import java.io.Serializable;

public class Place implements Serializable {
    public Place() {
        name = "제주도";
        address = "test address";
    }

    public Place(String name, String address) {
        this.name = name;
        this.address = address;
    }

    String name;
    String address;

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
}
