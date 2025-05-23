package com.tamar.restaurant.models;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class Order {
    @SerializedName("id")
    private Long id;

    @SerializedName("client")
    private Client client;

    @SerializedName("table")
    private RestaurantTable table;

    @SerializedName("order_date")
    private String orderDate;


    //Getters and Setters


    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public RestaurantTable getTable() {
        return table;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setTable(RestaurantTable table) {
        this.table = table;
    }
}
