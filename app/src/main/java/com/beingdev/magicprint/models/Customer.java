package com.beingdev.magicprint.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Customer {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("customer_first_name")
    @Expose
    private String customerFirstName;
    @SerializedName("customer_last_name")
    @Expose
    private String customerLastName;
    @SerializedName("customer_email")
    @Expose
    private Object customerEmail;
    @SerializedName("customer_date_joined")
    @Expose
    private String customerDateJoined;
    @SerializedName("customer_contact")
    @Expose
    private String customerContact;
    @SerializedName("customer_profile_pic")
    @Expose
    private String customerProfilePic;
    @SerializedName("customer_shipping_address")
    @Expose
    private Integer customerShippingAddress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public Object getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(Object customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerDateJoined() {
        return customerDateJoined;
    }

    public void setCustomerDateJoined(String customerDateJoined) {
        this.customerDateJoined = customerDateJoined;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getCustomerProfilePic() {
        return customerProfilePic;
    }

    public void setCustomerProfilePic(String customerProfilePic) {
        this.customerProfilePic = customerProfilePic;
    }

    public Integer getCustomerShippingAddress() {
        return customerShippingAddress;
    }

    public void setCustomerShippingAddress(Integer customerShippingAddress) {
        this.customerShippingAddress = customerShippingAddress;
    }
}
