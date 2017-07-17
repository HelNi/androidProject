package com.example.user.worktime.Classes.TimeTable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nsh on 12.07.2017.
 *
 */

public class Activity implements Serializable {
    protected long id;

    protected String name;

    @SerializedName("category_name")
    protected String categoryName;

    protected Boolean deprecated;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    @Override
    public String toString() {
        return categoryName + " / " + name;
    }
}
