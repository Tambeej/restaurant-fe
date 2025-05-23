package com.tamar.restaurant.models;

import com.google.gson.annotations.SerializedName;
import com.tamar.restaurant.models.user.Waiter;

public class WaiterAssignment {
    @SerializedName("id")
    private Long id;

    @SerializedName("table")
    private RestaurantTable table;

    @SerializedName("waiter")
    private Waiter waiter;

    //Getters and Setters


    public Long getId() {
        return id;
    }

    public RestaurantTable getTable() {
        return table;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTable(RestaurantTable table) {
        this.table = table;
    }

    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }
}
