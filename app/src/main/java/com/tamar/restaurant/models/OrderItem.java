package com.tamar.restaurant.models;

import com.google.gson.annotations.SerializedName;

public class OrderItem {
    @SerializedName("id")
    private Long id;

    @SerializedName("order")
    private Order order;

    @SerializedName("dish")
    private Dish dish;

    @SerializedName("status")
    private OrderStatus status = OrderStatus.ORDERED;

    //Getters and Setters


    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Dish getDish() {
        return dish;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
