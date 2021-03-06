package com.example.user.worktime.Backend;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by nsh on 11.07.2017.
 *
 * This class stores and retrieves the Backend-API token persistently.
 * It is necessary to use this class because shared preferences for some reason don't update other shared preferences in the same app when an entry changes, gets added or deleted.
 */

public class BackendApiTokenManager {
    private static SharedPreferences mPreferences;

    private static SharedPreferences mSharedPreferences;

    /**
     * @return The API token is one is set.  This token is saved between app starts.
     */
    @Nullable public static String getApiToken() {
        return mSharedPreferences.getString(BackendClient.PREFERENCE_API_KEY, null);
    }

    public static Boolean setApiToken(@NonNull String token) {
        return mSharedPreferences.edit().putString(BackendClient.PREFERENCE_API_KEY, token).commit();
    }

    public static Boolean unsetApiToken() {
        return mSharedPreferences.edit().remove(BackendClient.PREFERENCE_API_KEY).commit();
    }

    public static void init(@NonNull Context ctx) {
        mSharedPreferences = ctx.getSharedPreferences(BackendClient.PREFERENCE_NAME, 0);
    }

    private BackendApiTokenManager() {

    }
}
