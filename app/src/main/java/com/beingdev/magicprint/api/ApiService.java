package com.beingdev.magicprint.api;

import com.beingdev.magicprint.models.AddressModel;
import com.beingdev.magicprint.models.AddressRequest;
import com.beingdev.magicprint.models.CartModel;
import com.beingdev.magicprint.models.CartPostResponse;
import com.beingdev.magicprint.models.CartRequest;
import com.beingdev.magicprint.models.CheckoutRequest;
import com.beingdev.magicprint.models.CheckoutResponse;
import com.beingdev.magicprint.models.LoginRequest;
import com.beingdev.magicprint.models.LoginResponse;
import com.beingdev.magicprint.models.OrdersModel;
import com.beingdev.magicprint.models.Product;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/products/{category}")
    Call<List<Product>> getProductsinCategory(@Path("category") String category);

    @GET("/products")
    Call<List<Product>> getProducts();

    @GET("/products")
    Call<List<Product>> searchProducts(@Query("search") String searchQuery);


    @POST("/cart/")
    Call<CartPostResponse> addToCart(@Body CartRequest cart);

    @GET("/cart")
    Call<List<CartModel>> getCart(@Query("customer_id") int customerId);

    @PUT("/cart/{cart_id}")
    Call<CartPostResponse> updateCart(@Body CartRequest cart);

    @DELETE("/cart/{cart_id}")
    Call<ResponseBody> deleteCart(@Path("cart_id") int cartId);

    @GET("/orders")
    Call<List<OrdersModel>> getOrders(@Query("customer_id") int customerId);

    @GET("/customers/address")
    Call<List<AddressModel>> getAddress(@Query("customer_id") int customerId);

    @POST("/customers/address/")
    Call<AddressModel> addAddress(@Body AddressRequest address);

    @POST("/customers/login/")
    Call<LoginResponse> login(@Body LoginRequest user);

    @POST("/cart/checkout/")
    Call<CheckoutResponse> checkout(@Body CheckoutRequest request);
}
