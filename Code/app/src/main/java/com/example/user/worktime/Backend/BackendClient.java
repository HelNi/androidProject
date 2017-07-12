package com.example.user.worktime.Backend;

import android.support.annotation.NonNull;

import com.example.user.worktime.Backend.Services.ActivityService;
import com.example.user.worktime.Backend.Services.AuthService;
import com.example.user.worktime.Backend.Services.TimeTableEntryService;
import com.example.user.worktime.Backend.Services.UserService;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nsh on 11.07.2017.
 */

public class BackendClient {
    public static final String BACKEND_BASE_URL = "https://nsh.webuse.de/worktime/";
    // shared preferences for backend API settings, like the API key.
    // Use with getSharedPreferences in activities.
    public static final String PREFERENCE_NAME = "BACKEND_API";
    public static final String PREFERENCE_API_KEY = "API_KEY";

    private static BackendClient instance = null;

    private Retrofit retrofit;
    private AuthService mAuthService;
    private UserService mUserService;
    private TimeTableEntryService mEntryService;
    private ActivityService mActivityService;

    private BackendClient(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    /**
     * @return The Service which is used for authenticating.
     */
    public AuthService getAuthService() {
        if (mAuthService == null) {
            mAuthService = this.retrofit.create(AuthService.class);
        }

        return mAuthService;
    }

    // NYI
    public ActivityService getActivityService() {
        if (mActivityService == null) {
            mActivityService = this.retrofit.create(ActivityService.class);
        }

        return mActivityService;
    }

    public TimeTableEntryService getTimeTableEntryService() {
        if (mEntryService == null) {
            mEntryService = this.retrofit.create(TimeTableEntryService.class);
        }

        return mEntryService;
    }

    /**
     * @return The service which is responsible for adding users.,
     */
    public UserService getUserService() {
        if (mUserService == null) {
            mUserService = this.retrofit.create(UserService.class);
        }

        return mUserService;
    }

    public static BackendClient getInstance() {
        if (instance == null) {
            Retrofit.Builder builder = new Retrofit.Builder();
            OkHttpClient client = buildClient();

            Retrofit retrofit = builder.baseUrl(BACKEND_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            instance = new BackendClient(retrofit);
        }

        return instance;
    }

    // Add the API token as a query parameter.
    @NonNull
    private static OkHttpClient buildClient() {
        return new OkHttpClient.Builder().addInterceptor(
                new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        String apiToken = BackendApiTokenManager.getApiToken();
                        if (apiToken != null) {
                            Request original = chain.request();
                            HttpUrl originalHttpUrl = original.url();

                            HttpUrl url = originalHttpUrl.newBuilder()
                                    .addQueryParameter("token", apiToken)
                                    .build();

                            // Request customization: add request headers
                            Request.Builder requestBuilder = original.newBuilder()
                                    .url(url);

                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        }

                        return chain.proceed(chain.request());
                    }
                }
        ).build();
    }
}