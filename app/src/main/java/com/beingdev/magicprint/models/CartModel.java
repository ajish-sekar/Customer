package com.beingdev.magicprint.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("product")
    @Expose
    private CartProduct product;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public CartProduct getProduct() {
        return product;
    }

    public void setProduct(CartProduct product) {
        this.product = product;
    }
}
