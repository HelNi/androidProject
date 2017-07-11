package com.example.user.worktime.Classes.TimeTable;

/**
 * Created by User on 06.07.2017.
 */

public enum Activity {
    DEFAULT("Basislast"),
    URLAUB("Urlaub"),
    SOFTWAREENTWICKLUNG("Softwareentwicklung"),
    MEETING("Meeting");

    private String activity;

    Activity(String activity) {
        this.activity = activity;
    }

    @Override public String toString() {
        return activity;
    }
}
