package com.beingdev.magicprint.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("username")
    @Expose
    private String number;

    @SerializedName("password")
    @Expose
    private String password;

    public LoginRequest(String number, String password){
        this.number = number;
        this.password = password;
    }

    public String getNumber() {
        return number;
    }

    public String getPassword() {
        return password;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
