package com.example.quakereportupdated;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Earthquake {
    private double magnitude;
    private String place;
    private long date;
    private String url;

    public Earthquake(double magnitude, String place, long date, String url){
        this.magnitude = magnitude;
        this.place = place;
        this.date = date;
        this.url = url;
    }


    public double getMagnitude(){
        return this.magnitude;
    }

    public String getPlace(){
        return this.place;
    }

    public long getDate(){
        return this.date;
    }

    public String getUrl(){
        return this.url;
    }

}
