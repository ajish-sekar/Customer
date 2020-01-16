package com.beingdev.magicprint.prodcutscategory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.beingdev.magicprint.Cart;
import com.beingdev.magicprint.NotificationActivity;
import com.beingdev.magicprint.R;
import com.beingdev.magicprint.api.ApiUtil;
import com.beingdev.magicprint.models.Product;
import com.beingdev.magicprint.networksync.CheckInternetConnection;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    public static String KEY_CATEGORY = "category";

    RecyclerView recyclerView;
    LottieAnimationView tv_no_item;
    StaggeredGridLayoutManager layoutManager;
    ProductsAdapter adapter;
    View container;

    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Toolbar toolbar = findViewById(R.id.products_toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        //Initializing our Recyclerview
        recyclerView = findViewById(R.id.products_recycler_view);
        tv_no_item = findViewById(R.id.products_tv_no_cards);
        container = findViewById(R.id.products_layout);

        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();

        category = intent.getStringExtra(KEY_CATEGORY);

        fetchProducts(category);

    }

    void fetchProducts(String category){

        Call<List<Product>> call = ApiUtil.getService().getProductsinCategory(category);

//        Call<List<Product>> call = ApiUtil.getService().getProducts();


        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(tv_no_item.getVisibility() == View.VISIBLE){
                    tv_no_item.setVisibility(View.GONE);
                }
                if(response.isSuccessful()){
                    ArrayList<Product> products = new ArrayList<>(response.body());
                    adapter = new ProductsAdapter(products,getApplicationContext());
                    recyclerView.setAdapter(adapter);

                }else{
                    Toast.makeText(getApplicationContext(),"Please Try Again",Toast.LENGTH_SHORT).show();
                    Snackbar.make(container,"Error Loading",Snackbar.LENGTH_SHORT)
                            .setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    fetchProducts(category);
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                if(tv_no_item.getVisibility() == View.VISIBLE){
                    tv_no_item.setVisibility(View.GONE);
                }
                Snackbar.make(container,"Error Loading",Snackbar.LENGTH_SHORT)
                        .setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fetchProducts(category);
                            }
                        })
                        .show();
            }
        });
    }

    public void viewCart(View view) {
        startActivity(new Intent(ProductsActivity.this, Cart.class));
        finish();
    }

    public void Notifications(View view) {
        startActivity(new Intent(ProductsActivity.this, NotificationActivity.class));
        finish();
    }
}
