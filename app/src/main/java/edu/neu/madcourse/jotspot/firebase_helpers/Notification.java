package edu.neu.madcourse.jotspot.firebase_helpers;

import java.util.ArrayList;

public class Notification {
    private ArrayList<String> days;
    // TODO: figure out how to store this (https://docs.oracle.com/javase/8/docs/api/java/time/package-summary.html)
    private String time;

    // Empty (default) constructor needed for calls to DataSnapshot.getValue
    public Notification() {}

    // Constructor with list of days and time
    public Notification(ArrayList<String> days, String time) {
        this.days = days;
        this.time = time;
    }

    public ArrayList<String> getDays() {
        return days;
    }

    public void setDays(ArrayList<String> inDays) {
        this.days = inDays;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String inTime) {
        this.time = inTime;
    }

    // TODO: figure out how to format ArrayList separated by commas
    @Override
    public String toString() {
        return "Day: " + this.days + " Time: " + this.time;
    }
}
