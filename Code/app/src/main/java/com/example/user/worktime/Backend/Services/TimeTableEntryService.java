package com.example.user.worktime.Backend.Services;

import com.example.user.worktime.Classes.TimeTable.TimeTableEntry;

import org.joda.time.LocalDateTime;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by nsh on 12.07.2017.
 */

public interface TimeTableEntryService {
    /**
     * Get a single entry
     * @param id which entry to get
     * @return 200 and the entry on success
     */
    @GET("api/entries/{id}.json")
    Call<TimeTableEntry> getEntry(@Path("id") long id);

    @GET("api/entries/between.json")
    Call<List<TimeTableEntry>> getEntriesBetweenDates(@Query("start") LocalDateTime start, @Query("end") LocalDateTime end);

    /**
     * Updates an entry.
     *
     * @param id    Which entry to update
     * @param entry the new values
     * @return 200 on success
     */
    @PUT("api/entries/{id}.json")
    Call<Void> updateEntry(@Path("id") long id, @Body TimeTableEntry entry);

    /**
     * Delete a
     *
     * @param id Which entry to delete
     * @return 204 on success
     */
    @DELETE("api/entries/{id}.json")
    Call<Void> deleteEntry(@Path("id") long id);

    /**
     * Create a new entry
     *
     * @return 201 on success
     */
    @DELETE("api/entries.json")
    Call<Void> createEntry(@Body TimeTableEntry entry);
}
