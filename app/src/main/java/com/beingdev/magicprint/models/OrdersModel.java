package com.beingdev.magicprint.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrdersModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("items")
    @Expose
    private List<CartModel> items = null;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("paid")
    @Expose
    private Boolean paid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<CartModel> getItems() {
        return items;
    }

    public void setItems(List<CartModel> items) {
        this.items = items;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }
}
