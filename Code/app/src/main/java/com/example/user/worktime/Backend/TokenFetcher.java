package com.example.user.worktime.Backend;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by nsh on 11.07.2017.
 */

public class TokenFetcher {
    private static SharedPreferences mPreferences;
    private static Context mCtx;

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
        mCtx = ctx;
        mSharedPreferences = mCtx.getSharedPreferences(BackendClient.PREFERENCE_NAME, 0);
    }

    private TokenFetcher() {

    }
}
