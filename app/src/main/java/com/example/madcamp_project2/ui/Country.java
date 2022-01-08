package com.example.madcamp_project2.ui;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Country implements Serializable {
    CountryEnum countryEnum;
    // Icon Image

    public Country(CountryEnum countryEnum) {
        this.countryEnum = countryEnum;
    }

    public CountryEnum getCountryEnum() {
        return countryEnum;
    }

    public void setCountryEnum(CountryEnum countryEnum) {
        this.countryEnum = countryEnum;
    }

    public String getName() {
        return countryEnum.toString();
    }
}

