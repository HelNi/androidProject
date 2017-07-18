package com.example.user.worktime.Helpers;

import android.icu.util.DateInterval;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.text.format.Formatter;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Weeks;

import java.util.Date;

/**
 * Created by User on 06.07.2017.
 *
 * This class provides helper method for the view pagers to calculate the right page to display or the dates to display for a page.
 */

public class DateHelpers {
    /**
     *
     * @param date For which date to get the day numer
     * @return Number of the day of the specified date after 1.1.1970
     */
    public static int getDayPage(@NonNull LocalDate date) {
        return Days.daysBetween(new LocalDate(0L), date).getDays();
    }

    /**
     *
     * @param day Day number after 1.1.1970
     * @return The Date of the Specified day.
     */
    public @NonNull static LocalDate dayNumToDate(int day) {
        LocalDate localDate = new LocalDate(0L);
        return localDate.plusDays(day);
    }

    /**
     *
     * @param date For which date to get the day numer
     * @return Number of the day of the specified date after 1.1.1970
     */
    public static int getWeekPage(@NonNull LocalDate date) {
        return Weeks.weeksBetween(new LocalDate(1970, 1, 5), date).getWeeks();
    }

    /**
     *
     * @param week Day number after 1.1.1970
     * @return The Date of the Specified day.
     */
    public @NonNull static LocalDate weekToDate(int week) {
        LocalDate localDate = new LocalDate(1970, 1, 5); // 5.1.1970 - Because 1.1. is a monday.
        return localDate.plusWeeks(week);
    }
}
