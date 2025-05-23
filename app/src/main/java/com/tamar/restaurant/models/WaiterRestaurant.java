package com.tamar.restaurant.models;

import com.google.gson.annotations.SerializedName;
import com.tamar.restaurant.models.user.Waiter;

public class WaiterRestaurant {
    @SerializedName("id")
    private Long id;

    @SerializedName("waiter")
    private Waiter waiter;

    @SerializedName("restaurant")
    private Restaurant restaurant;

    //Getters and Setters

    public Long getId() {
        return id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }
}
