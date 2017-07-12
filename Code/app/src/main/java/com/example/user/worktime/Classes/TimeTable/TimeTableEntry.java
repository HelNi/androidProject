package com.example.user.worktime.Classes.TimeTable;

import android.support.annotation.Nullable;

import org.joda.time.LocalTime;

/**
 * Created by User on 06.07.2017.
 */

public class TimeTableEntry {
    private long id;

    private LocalTime start;

    private LocalTime end;

    private String description;

    private Category category;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getActivity() {
        return category;
    }

    public void setActivity(@Nullable Category category) {
        this.category = category;
    }
}
