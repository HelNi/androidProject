package com.example.user.worktime.Classes;

import android.icu.util.DateInterval;
import android.support.annotation.NonNull;
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
}
