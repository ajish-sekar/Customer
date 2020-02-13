package com.thirumathikart.customer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PasswordResetRequest {
    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("password")
    @Expose
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
