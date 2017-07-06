package com.example.user.worktime.Classes.TimeTable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

/**
 * Created by User on 06.07.2017.
 */

public interface TimeTableRepositoryInterface {
    /**
     * Gets a Time Table by its ID
     *
     * @param id Unique ID of a time table that may or may not exist.
     * @return The time table or null if it doesn't exist.
     */
    @Nullable TimeTable getTimeTable(int id);

    /**
     * Get A time table for the specified day.
     * @param day For which day to get the time table.
     * @return The time table or null if it doesn't exist.
     */
    @Nullable TimeTable getTimeTableForDay(int day);

    /**
     * Get a Time Table for the specified date.
     * @param date For which date to get the time table.
     * @return The time table or null if it doesn't exist.
     */
    @Nullable TimeTable getTimeTableForDate(@NonNull LocalDate date);

    /**
     *
     * @param timeTable The Time Table to persist.
     */
    void persistTimeTable(@NonNull TimeTable timeTable);
}
