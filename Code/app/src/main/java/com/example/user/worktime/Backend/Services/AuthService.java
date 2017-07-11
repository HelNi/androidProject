package com.example.user.worktime.Backend.Services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by nsh on 11.07.2017.
 */

public interface AuthService {
    @GET("api/get_token.json")
    Call<String> getAuthToken(@Query("username") String userName,
                              @Query("password") String password);
}
