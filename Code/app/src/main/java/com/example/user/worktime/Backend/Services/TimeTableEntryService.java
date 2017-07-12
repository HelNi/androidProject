package com.example.user.worktime.Backend.Services;

import com.example.user.worktime.Classes.TimeTable.TimeTableEntry;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by nsh on 12.07.2017.
 */

public interface TimeTableEntryService {
    @GET("")
    Call<TimeTableEntry> getEntry(@Path("id") long id);
}
