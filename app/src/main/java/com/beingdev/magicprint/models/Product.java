package com.beingdev.magicprint.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("product_title")
    @Expose
    private String productTitle;
    @SerializedName("product_category")
    @Expose
    private String productCategory;
    @SerializedName("product_price")
    @Expose
    private Float productPrice;
    @SerializedName("product_description")
    @Expose
    private String productDescription;
    @SerializedName("product_stock")
    @Expose
    private Integer productStock;
    @SerializedName("product_photo")
    @Expose
    private String productPhoto;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("available")
    @Expose
    private Boolean available;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("seller")
    @Expose
    private ProductSeller seller;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public Float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Float productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Integer getProductStock() {
        return productStock;
    }

    public void setProductStock(Integer productStock) {
        this.productStock = productStock;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
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

    public ProductSeller getSeller() {
        return seller;
    }

    public void setSeller(ProductSeller seller) {
        this.seller = seller;
    }

}
