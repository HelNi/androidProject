package com.example.user.worktime.Helpers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.example.user.worktime.Classes.TimeTable.TimeTableEntry;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import java.util.List;

/**
 * Created by User on 14.07.2017.
 */

public class TimeTableEntryCollectionHelper {
    private TimeTableEntryCollectionHelper() {

    }

    /**
     * @return Hashmap, the keys are the position (0 is right after the first element), the second the
     * duration of the gap.
     */
    public static SparseArray<Duration> gapLengths(List<TimeTableEntry> entries) {
        LocalDateTime lastEnd = null;
        int i = 0;
        SparseArray<Duration> returnValue = new SparseArray<>();

        for (TimeTableEntry entry : entries) {
            if (lastEnd == null) {
                lastEnd = entry.getEnd();
                ++i;
                continue; // Skip the first entry of the day.
            }

            Duration duration = new Duration(lastEnd.toDateTime(), entry.getStart().toDateTime());

            long minuteDifference = duration.getStandardMinutes();
            if (minuteDifference > 0) {
                returnValue.append(i, duration);
            }
            ++i;
            lastEnd = entry.getEnd();
        }
        return returnValue;
    }

    /**
     * @param entries A sorted (by start time) list of entries.
     * @return basically the end of the last datetime in the list Or null if the list is empty.
     */
    public static
    @NonNull
    LocalTime suggestNextStartTime(@NonNull List<TimeTableEntry> entries) {
        if (entries.isEmpty()) {
            return LocalTime.parse("08:00");
        }

        return entries.get(entries.size() - 1).getEnd().toLocalTime();
    }
}
