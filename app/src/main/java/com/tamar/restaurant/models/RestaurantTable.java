package com.tamar.restaurant.models;

import com.google.gson.annotations.SerializedName;

public class RestaurantTable {
    @SerializedName("id")
    private long id;

    @SerializedName("restaurant")
    private Restaurant restaurant;

    @SerializedName("tablenumber")
    private int tableNumber;

    //Getters and Setters


    public long getId() {
        return id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }
}
