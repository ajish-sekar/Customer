package com.thirumathikart.customer.orders;

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
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.thirumathikart.customer.Cart;
import com.thirumathikart.customer.NotificationActivity;
import com.thirumathikart.customer.R;
import com.thirumathikart.customer.api.ApiUtil;
import com.thirumathikart.customer.models.OrdersModel;
import com.thirumathikart.customer.networksync.CheckInternetConnection;
import com.thirumathikart.customer.usersession.UserSession;
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
    TextView noOrders;

    private UserSession session;
    private HashMap<String,String> user;
    private String name,email,photo,mobile,token;
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
        noOrders = findViewById(R.id.no_orders_tv);

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
        String id = user.get(UserSession.KEY_ID);
        if(id!=null && id.length()!=0){
            userId = Integer.parseInt(id);
        }

        token = user.get(UserSession.KEY_TOKEN);
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
                    if(orders.size()==0){
                        recyclerView.setVisibility(View.GONE);
                        noOrders.setVisibility(View.VISIBLE);
                    }else {
                        recyclerView.setVisibility(View.VISIBLE);
                        noOrders.setVisibility(View.GONE);
                    }

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
