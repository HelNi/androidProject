package com.example.user.worktime.Classes.User;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by User on 06.07.2017.
 */

public interface UserRepositoryInterface {
    /**
     * Fetches a User by their ID
     * @param id Unique ID of the User
     * @return The user or null if it doesn't exist.
     */
    @Nullable User getUser(int id);

    /**
     * Fetches users by their IDs
     * @param ids List of user IDs to get.
     * @return The user or null if it doesn't exist.
     */
    @Nullable List<User> getUsers(@NonNull List<Integer> ids);
}
