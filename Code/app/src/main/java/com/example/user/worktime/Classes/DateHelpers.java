package com.example.user.worktime.Classes;

import android.icu.util.DateInterval;
import android.text.format.DateUtils;
import android.text.format.Formatter;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;

import java.util.Date;

/**
 * Created by User on 06.07.2017.
 */

public class DateHelpers {
    /**
     *
     * @param date
     * @return Number of the day of the specified date
     */
    public static int getDayPage(LocalDate date) {
        return Days.daysBetween(new LocalDate(0l), date).getDays();
    }

    /**
     *
     * @param day
     * @return The Date of the Specified day.
     */
    public static LocalDate dayNumToDate(int day) {
        LocalDate localDate = new LocalDate(0l);
        return localDate.plusDays(day);
    }
}
