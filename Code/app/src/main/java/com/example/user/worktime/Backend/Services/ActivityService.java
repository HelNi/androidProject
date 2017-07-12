package com.example.user.worktime.Backend.Services;

import com.example.user.worktime.Classes.TimeTable.Activity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by nsh on 12.07.2017.
 */

public interface ActivityService {
    @GET("api/activities.json")
    Call<List<Activity>> getActivities();
}
