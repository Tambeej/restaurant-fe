package com.tamar.restaurant.models;

import com.google.gson.annotations.SerializedName;
import com.tamar.restaurant.models.user.Kitchen;
import com.tamar.restaurant.models.user.Manager;

public class Restaurant {
    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("manager")
    private Manager manager;

    @SerializedName("kitchen")
    private Kitchen kitchen;

    //Getters and Setters


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Manager getManager() {
        return manager;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
