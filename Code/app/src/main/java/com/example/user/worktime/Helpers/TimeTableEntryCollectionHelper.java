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
 *
 * Provies various helper function for getting information from a List of Time Table entries.
 */

public class TimeTableEntryCollectionHelper {
    private TimeTableEntryCollectionHelper() {

    }

    /**
     * Get a hashmap of all entries that have a time gap before the previous entry.
     * For example if one fragment ends at 14:00 and the next begins at 14:30, there's a 30 minute gap.
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
     * Get All entries who overlap with their previous entry.
     */
    public static SparseArray<Duration> overlapLengths(List<TimeTableEntry> entries) {
        LocalDateTime lastEnd = null;
        int i = 0;
        SparseArray<Duration> returnValue = new SparseArray<>();

        for (TimeTableEntry entry : entries) {
            if (lastEnd == null) {
                lastEnd = entry.getEnd();
                ++i;
                continue; // Skip the first entry of the day.
            }

            Duration duration = new Duration(entry.getStart().toDateTime(), lastEnd.toDateTime());

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
     * Get a suggestion for the next entries start time (which is the end time of the last entry in the list)
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

    /**
     *
     * @param entries The entries to summarize.
     * @return Sum of the durations of all entries. Overlapping times are counted twice.
     */
    public static Duration sumDuration(List<TimeTableEntry> entries) {
        Duration sum = Duration.ZERO;

        for (TimeTableEntry entry : entries) {
            sum = sum.plus(entry.getDuration());
        }
        return sum;
    }
}
