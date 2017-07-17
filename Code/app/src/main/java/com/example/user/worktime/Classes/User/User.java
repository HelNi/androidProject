package com.example.user.worktime.Classes.User;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.LocalDate;

import java.io.Serializable;

/**
 * Created by User on 06.07.2017.
 */

public class User implements Serializable {
    private long id;
    private String salutation;
    private String title;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    private String email;
    @SerializedName("username")
    private String userName;

    //private LocalDate lastLogin;


    public User(long id, String salutation, String title, String firstName, String lastName, String email, String userName) {
        this.id = id;
        this.salutation = salutation;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

/*    public LocalDate getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }*/

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @param weekDay For which day to check
     * @return The duration the employee needs to work for the specified day.
     *         Currently hardcoded, but could be fetched from the Backend.
     */
    public Duration getWorkingTimeForWeekDay(int weekDay) {
        if (weekDay < DateTimeConstants.MONDAY || weekDay > DateTimeConstants.SUNDAY) {
            throw new IllegalArgumentException("Weeday must be in range 1 - 7 , is " + weekDay);
        }

        switch (weekDay) {
            case DateTimeConstants.SATURDAY:
            case DateTimeConstants.SUNDAY:
                return Duration.ZERO;
            default:
                return new Duration(8 * DateTimeConstants.MILLIS_PER_HOUR); // 8 hours.
        }
    }
}
