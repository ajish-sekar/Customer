package com.beingdev.magicprint.orders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.beingdev.magicprint.Cart;
import com.beingdev.magicprint.NotificationActivity;
import com.beingdev.magicprint.R;
import com.beingdev.magicprint.api.ApiUtil;
import com.beingdev.magicprint.models.OrdersModel;
import com.beingdev.magicprint.models.Product;
import com.beingdev.magicprint.networksync.CheckInternetConnection;
import com.beingdev.magicprint.prodcutscategory.ProductsActivity;
import com.beingdev.magicprint.prodcutscategory.ProductsAdapter;
import com.beingdev.magicprint.usersession.UserSession;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LottieAnimationView tv_no_item;
    StaggeredGridLayoutManager layoutManager;
    OrdersAdapter adapter;
    View container;

    private UserSession session;
    private HashMap<String,String> user;
    private String name,email,photo,mobile;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        Toolbar toolbar = findViewById(R.id.orders_toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        getValues();

        //Initializing our Recyclerview
        recyclerView = findViewById(R.id.orders_recycler_view);
        tv_no_item = findViewById(R.id.orders_tv_no_cards);
        container = findViewById(R.id.orders_layout);

        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        fetchOrders();
    }

    private void getValues() {

        //create new session object by passing application context
        session = new UserSession(getApplicationContext());

        //validating session
        session.isLoggedIn();

        //get User details if logged in
        user = session.getUserDetails();

        name = user.get(UserSession.KEY_NAME);
        email = user.get(UserSession.KEY_EMAIL);
        mobile = user.get(UserSession.KEY_MOBiLE);
        photo = user.get(UserSession.KEY_PHOTO);
        userId = 2;
    }

    private void fetchOrders(){
        Call<List<OrdersModel>> call = ApiUtil.getService().getOrders(userId);


        call.enqueue(new Callback<List<OrdersModel>>() {
            @Override
            public void onResponse(Call<List<OrdersModel>> call, Response<List<OrdersModel>> response) {
                if(tv_no_item.getVisibility() == View.VISIBLE){
                    tv_no_item.setVisibility(View.GONE);
                }
                if(response.isSuccessful()){
                    Log.v("Orders","Successful request");
                    ArrayList<OrdersModel> orders = new ArrayList<>(response.body());
                    adapter = new OrdersAdapter(orders,getApplicationContext());
                    recyclerView.setAdapter(adapter);

                }else{
                    Log.v("Orders","Unsuccessful request");
                    Toast.makeText(getApplicationContext(),"Please Try Again",Toast.LENGTH_SHORT).show();
                    Snackbar.make(container,"Error Loading",Snackbar.LENGTH_SHORT)
                            .setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    fetchOrders();
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<List<OrdersModel>> call, Throwable t) {
                Log.e("Network Error",t.getMessage());
                if(tv_no_item.getVisibility() == View.VISIBLE){
                    tv_no_item.setVisibility(View.GONE);
                }
                Snackbar.make(container,"Error Loading",Snackbar.LENGTH_SHORT)
                        .setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fetchOrders();
                            }
                        })
                        .show();
            }
        });
    }

    public void viewCart(View view) {
        startActivity(new Intent(OrdersActivity.this, Cart.class));
        finish();
    }

    public void Notifications(View view) {
        startActivity(new Intent(OrdersActivity.this, NotificationActivity.class));
        finish();
    }
}
