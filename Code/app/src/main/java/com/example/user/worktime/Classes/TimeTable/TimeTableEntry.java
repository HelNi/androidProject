package com.example.user.worktime.Classes.TimeTable;

import android.support.annotation.Nullable;

import com.example.user.worktime.Classes.User.User;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

/**
 * Created by User on 06.07.2017.
 */

public class TimeTableEntry {
    protected long id;

    protected LocalDateTime start;

    protected LocalDateTime end;

    protected String description;

    protected Activity activity;

    protected User user;

    public TimeTableEntry() {
    }

    public TimeTableEntry(LocalDateTime start, LocalDateTime end, String description, Activity activity, User user) {
        this.start = start;
        this.end = end;
        this.description = description;
        this.activity = activity;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
