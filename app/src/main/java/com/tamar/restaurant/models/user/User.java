package com.tamar.restaurant.models.user;

import com.google.gson.annotations.SerializedName;
import com.tamar.restaurant.models.Role;

public class User {

    @SerializedName("id")
    private Long id;

    @SerializedName("email")
    private String email;

    @SerializedName("user_name")
    private String userName;

    @SerializedName("role")
    private Role role;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
