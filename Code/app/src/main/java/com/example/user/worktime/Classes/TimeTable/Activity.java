package com.example.user.worktime.Classes.TimeTable;

/**
 * Created by User on 06.07.2017.
 */

public class Activity {
    private long id;
    // TODO: Some kind of hierarchy
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
