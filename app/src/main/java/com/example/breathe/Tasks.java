package com.example.breathe;

import java.io.Serializable;

public class Tasks implements Serializable {
    private String title;
    private String date;
    private String time;
    private String description;
    private String location;


    public Tasks(String title,String date,String time, String description, String location) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.description = description;
        this.location = location;

    }

    public Tasks(){

    }




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
