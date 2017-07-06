package com.example.user.worktime.Classes.TimeTable;

import com.example.user.worktime.User.User;

import org.joda.time.LocalDate;

import java.util.Enumeration;
import java.util.List;

/**
 * Created by User on 06.07.2017.
 */

public class TimeTable {
    private long id;

    private User user;

    private LocalDate date;

    private List<TimeTableEntry> entries;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<TimeTableEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<TimeTableEntry> entries) {
        this.entries = entries;
    }

    public void addEntry(TimeTableEntry entry) {
        this.entries.add(entry);
    }

    public void removeEntry(TimeTableEntry entry) {
        this.entries.remove(entry);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
