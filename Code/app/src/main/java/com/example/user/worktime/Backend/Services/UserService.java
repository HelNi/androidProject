package com.example.user.worktime.Backend.Services;

import com.example.user.worktime.Classes.User.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by nsh on 11.07.2017.
 */

public interface UserService {
    @GET("api/user/self.json")
    Call<User> getSelf();

    @GET("api/user/{id}.json")
    Call<User> getUser(@Path("id") Integer id);
}
