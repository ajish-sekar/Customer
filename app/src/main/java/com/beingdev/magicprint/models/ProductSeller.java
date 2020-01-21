package com.beingdev.magicprint.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductSeller implements Serializable {

    @SerializedName("seller_account")
    @Expose
    private Integer sellerAccount;
    @SerializedName("seller_name")
    @Expose
    private String sellerName;
    @SerializedName("seller_contact")
    @Expose
    private String sellerContact;
    @SerializedName("seller_address")
    @Expose
    private Integer sellerAddress;

    public Integer getSellerAccount() {
        return sellerAccount;
    }

    public void setSellerAccount(Integer sellerAccount) {
        this.sellerAccount = sellerAccount;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerContact() {
        return sellerContact;
    }

    public void setSellerContact(String sellerContact) {
        this.sellerContact = sellerContact;
    }

    public Integer getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(Integer sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

}
