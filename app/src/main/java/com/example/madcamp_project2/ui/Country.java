package com.example.madcamp_project2.ui;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Country implements Serializable {
    String name;
    // Icon Image

    public Country(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

