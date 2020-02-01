package com.thirumathikart.customer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckoutRequest {
    @SerializedName("customer_id")
    @Expose
    private int customerId;

    @SerializedName("address_id")
    @Expose
    private int addressId;

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getAddressId() {
        return addressId;
    }

    public int getCustomerId() {
        return customerId;
    }
}
