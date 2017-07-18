package com.example.user.worktime.Helpers;

import com.example.user.worktime.Backend.BackendClient;
import com.example.user.worktime.Classes.TimeTable.Activity;

import java.util.List;

import retrofit2.Call;

/**
 * Created by User on 17.07.2017.
 *
 * This could as well be done manually in an Activity.
 */
@Deprecated
public class ActivityFetcher {
    public static Call<List<Activity>> getActivitiesAsync() {
        // Asynchronously fetch activities. Sub-Views can register callbacks.
        // This makes the request for activites every time because you can't enqueue multiple
        // Callbacks on one Call object for some reason.
        return BackendClient.getInstance().getActivityService().getActivities();
    }
}
