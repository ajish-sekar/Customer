package com.beingdev.magicprint.api;

import com.beingdev.magicprint.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/products/{category}")
    Call<List<Product>> getProductsinCategory(@Path("category") String category);

    @GET("/products")
    Call<List<Product>> getProducts();
}
