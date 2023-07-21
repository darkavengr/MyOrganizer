package com.example.myorganizer;


public class DataModel {
    static int LIST_NAME = 0;
    static int LIST_DATE = 1;
    static int LIST_START_TIME = 2;
    static int LIST_END_TIME = 3;
    static int LIST_LOCATION = 4;

    String name;
    String date;
    String starttime;
    String endtime;
    String location;
    String version;
    int id_;
    int image;

    public DataModel(String name, String date,String starttime,String endtime,String location) {
        this.name = name;
        this.date=date;
        this.starttime=starttime;
        this.endtime=endtime;
        this.location=location;

    }


    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return starttime;
    }

    public String getEndTime() {
        return endtime;
    }

    public String getLocation() {
        return location;
    }

}