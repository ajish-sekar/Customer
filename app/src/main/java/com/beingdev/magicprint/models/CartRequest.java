package com.beingdev.magicprint.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartRequest {
    @SerializedName("quantity")
    @Expose
    int quantity;

    @SerializedName("customer")
    @Expose
    int customer;

    @SerializedName("product")
    @Expose
    int product;

    public CartRequest(int quantity,int customer, int product){
        this.quantity = quantity;
        this.customer = customer;
        this.product = product;
    }
}
