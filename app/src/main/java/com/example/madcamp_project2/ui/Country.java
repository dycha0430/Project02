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

    public Country(String str) {
        switch(str){
            case "서울": countryEnum = CountryEnum.SEOUL;
                break;
            case "인천": countryEnum = CountryEnum.INCHEON;
                break;
            case "부산": countryEnum = CountryEnum.BUSAN;
                break;
            case "제주": countryEnum = CountryEnum.JEJU;
                break;
            case "경기": countryEnum = CountryEnum.GYEONGGI;
                break;
            case "포항": countryEnum = CountryEnum.POHANG;
                break;
            case "강릉": countryEnum = CountryEnum.GANGNEUNG;
                break;
            case "속초": countryEnum = CountryEnum.SOKCHO;
                break;
            case "대구": countryEnum = CountryEnum.DAEGU;
                break;
            case "경주": countryEnum = CountryEnum.GYEONGJU;
                break;
            case "여수": countryEnum = CountryEnum.YEOSU;
                break;
            case "전주": countryEnum = CountryEnum.JEONJU;
                break;
            case "춘천": countryEnum = CountryEnum.CHUNCHEON;
                break;
            case "대전": countryEnum = CountryEnum.DAEJEON;
                break;
            default: countryEnum = null;
                break;
        }
    }
}

