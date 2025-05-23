package com.tamar.restaurant.models;

import com.google.gson.annotations.SerializedName;

public class Dish {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private double price;

    @SerializedName("category")
    private DishCategory category;

    @SerializedName("restaurant")
    private Restaurant restaurant;

    //Getters and Setters


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public DishCategory getCategory() {
        return category;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategory(DishCategory category) {
        this.category = category;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
