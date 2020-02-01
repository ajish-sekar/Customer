package com.thirumathikart.customer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckoutResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("order")
    @Expose
    private OrdersModel order;
    @SerializedName("paytm")
    @Expose
    private PaytmModel paytm;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrdersModel getOrder() {
        return order;
    }

    public void setOrder(OrdersModel order) {
        this.order = order;
    }

    public PaytmModel getPaytm() {
        return paytm;
    }

    public void setPaytm(PaytmModel paytm) {
        this.paytm = paytm;
    }

}
