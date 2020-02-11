package com.thirumathikart.customer.api;

import com.thirumathikart.customer.models.AddressModel;
import com.thirumathikart.customer.models.AddressRequest;
import com.thirumathikart.customer.models.CartModel;
import com.thirumathikart.customer.models.CartPostResponse;
import com.thirumathikart.customer.models.CartRequest;
import com.thirumathikart.customer.models.CheckoutRequest;
import com.thirumathikart.customer.models.CheckoutResponse;
import com.thirumathikart.customer.models.LoginRequest;
import com.thirumathikart.customer.models.LoginResponse;
import com.thirumathikart.customer.models.OrderConfirmModel;
import com.thirumathikart.customer.models.OrderConfirmResponse;
import com.thirumathikart.customer.models.OrdersModel;
import com.thirumathikart.customer.models.Product;
import com.thirumathikart.customer.models.RegisterModel;
import com.thirumathikart.customer.models.RegisterResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @PUT("/cart/{cart_id}/")
    Call<CartPostResponse> updateCart(@Path("cart_id") int cartId, @Body CartRequest cart);

    @DELETE("/cart/{cart_id}/")
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

    @POST("/customers/register/")
    Call<RegisterResponse> register(@Body RegisterModel user);

    @POST("/cart/confirm-order/")
    Call<OrderConfirmResponse> confirmOrder(@Body OrderConfirmModel confirmModel);

}
