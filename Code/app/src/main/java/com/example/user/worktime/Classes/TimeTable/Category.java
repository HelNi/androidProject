package com.example.user.worktime.Classes.TimeTable;

/**
 * Created by User on 06.07.2017.
 */

public enum Category {
    DEFAULT("Basislast"),
    URLAUB("Urlaub"),
    SOFTWAREENTWICKLUNG("Softwareentwicklung"),
    MEETING("Meeting");

    private String catgory;

    Category(String catgory) {
        this.catgory = catgory;
    }

    @Override public String toString() {
        return catgory;
    }
}
